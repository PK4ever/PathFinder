package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by paulk4ever on 3/1/16.
 */
public class Grid {

    int width = 0;
    int height = 0;
    Cells[][] cell = null;
    Cells start;
    Cells current;
    Cells end;

    LinkedList<Cells> unWalkable = new LinkedList<Cells>();
    LinkedList<Cells> Teleporters = new LinkedList<Cells>();
    LinkedList<Cells> path = new LinkedList<Cells>();
    LinkedList<Cells> open = new LinkedList<Cells>();
    LinkedList<Cells> closed = new LinkedList<Cells>();
    LinkedList<Cells> visited = new LinkedList<Cells>();
    String[][] weights = null;

    public Grid() {

    }


    public void loadGrid() throws FileNotFoundException {
        Scanner wc = null;
        Scanner scann = null;

        LineNumberReader lineReader = new LineNumberReader(new FileReader(String.valueOf(Gdx.files.local("input.txt"))));
        try {
            lineReader.skip(Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalNumberOfLines = lineReader.getLineNumber();

        try {
            lineReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        height = totalNumberOfLines;


        wc = new Scanner(new File(String.valueOf(Gdx.files.local(("input.txt")))));
        scann = new Scanner(new File(String.valueOf(Gdx.files.local(("input.txt")))));

        while (wc.hasNext()) {
            if (!wc.next().equals("")) {
                width++;
            }
        }

        width = width / height;
        cell = new Cells[height][width];
        weights = new String[height][width];
        while (scann.hasNext()) {
            for (int i = height - 1; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    weights[i][j] = scann.next();
                }
            }
        }

        if (width <= 10000 && height <= 10000) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    cell[i][j] = new Cells("white.jpg");
                    cell[i][j].setSize(Gdx.graphics.getWidth()*0.064102564f, Gdx.graphics.getHeight()*0.064102564f);
                    cell[i][j].setPosition(j * Gdx.graphics.getWidth()*0.066666667f, i * Gdx.graphics.getHeight()*0.066666667f);
                    cell[i][j].setWeight(weights[i][j]);
                    cell[i][j].setId(cell[i][j].getWeight());
                    if (cell[i][j].getWeight().equalsIgnoreCase("F")) {
                        unWalkable.add(cell[i][j]);
                        cell[i][j].setWeight("0");
                        cell[i][j].cell.setColor(Color.FIREBRICK);
                    }
                    if (cell[i][j].getWeight().startsWith("T")) {
                        Teleporters.add(cell[i][j]);
                        cell[i][j].setWeight("0");
                        cell[i][j].cell.setColor(Color.ROYAL);
                    }
                    cell[i][j].setX(j);
                    cell[i][j].setY(i);

                }

            }
        }else{
            System.out.println("the map is too big. it has to be less than 10000 X 10000");
            return;
        }
        this.setNeighbors();

    }


    public LinkedList<Cells> getCells() {
        LinkedList<Cells> allNodes = new LinkedList<Cells>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                allNodes.push(cell[i][j]);
            }

        }
        return allNodes;
    }

    public void setNeighbors() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j > 0) {

                    cell[i][j].setNeighbour(cell[i][j - 1]);
                    cell[i][j - 1].setNeighbour(cell[i][j]);
                }

                if (i > 0) {
                    cell[i - 1][j].setNeighbour(cell[i][j]);
                    cell[i][j].setNeighbour(cell[i - 1][j]);
                }

            }
        }

    }

    public void drawGrid(SpriteBatch batch) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cell[i][j].draw(batch);
            }
        }
    }

    public void calculateG(Cells current) {
        int G = Integer.parseInt(current.getWeight());
        current.setGValue(G);
    }

    public void calculateH(Cells current) {
        int H = (Math.abs(current.get_X() - end.get_X())) + (Math.abs(current.get_Y() - end.get_Y()));
        current.setHValue(H);
    }


    public void Astar(Cells s , Cells end) {
        this.start = s;
        this.end = end;
        this.end.setWeight("0");
        this.start.setWeight("0");
        calculateG(this.start);
        calculateH(this.start);
        start.setCost();
        open.push(this.start);
        end.setGValue(0);

        while (!open.isEmpty()) {
            this.start.cell.setColor(Color.SLATE);
            this.sortLists(open);
            this.sortLists(closed);
            current = open.pop();
            //current.cell.setColor(Color.LIGHT_GRAY);
            closed.push(current);

            //check if best case
            if (current.equals(end)) {
                if (current.getParent() != null) {
                    Cells p = current;
                    path.push(p);
                    this.sortLists(path);
                } else {
                    path.push(current);
                }

                break;
            }
            visited.push(current);

            calculateG(current);
            calculateH(current);
            current.setCost();

            if (Teleporters.contains(current)) {
                for (Cells tn : current.neighbor) {
                    if (!open.contains(tn) && !closed.contains(tn) && !unWalkable.contains(tn)) {
                        calculateG(tn);
                        calculateH(tn);
                        tn.setParent(current);
                        tn.setCost();
                        open.push(tn);
                        this.sortLists(open);

                        for (Cells t : Teleporters) {
                            if (t.getId().equals(current.getId())) {
                                if (!open.contains(t) && !closed.contains(t) && !unWalkable.contains(t)) {
                                    calculateG(t);
                                    calculateH(t);
                                    t.setCost();
                                    t.setCost();
                                    if (!t.equals(current)) {
                                        for (Cells teleportN : t.getNeighbor()) {
                                            if(!teleportN.getId().equals(t.getId())) {
                                                calculateG(teleportN);
                                                calculateH(teleportN);
                                                teleportN.setParent(current);
                                                teleportN.setCost();
                                                open.push(teleportN);
                                                this.sortLists(open);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {

                for (Cells n : current.getNeighbor()) {
                    if (!open.contains(n) && !closed.contains(n) && !unWalkable.contains(n)) {
                        calculateG(n);
                        calculateH(n);
                        n.setParent(current);
                        n.setCost();
                        if (current.equals(this.start)) {
                            current.setGValue(0);
                        }
                        open.push(n);
                        this.sortLists(open);
                    }
                }
            }

        }
        this.showPath();
        this.reset();

    }

    public void showPath() {
        if (!path.isEmpty()) {
            Cells finalPath = path.pop();
            while (!finalPath.equals(this.start)) {
                finalPath = finalPath.getParent();
                path.push(finalPath);

            }
            for (Cells v:visited){
                if (!v.equals(start)) {
                    v.cell.setColor(Color.LIGHT_GRAY);
                }
            }

            for (Cells p :path){
                if (!p.equals(start)) {
                    p.cell.setColor(Color.CYAN);
                }
            }

        }
    }

    public void reset() {
        end.cell.setColor(Color.GOLD);
        for (Cells t : Teleporters) {
            //t.cell.setColor(Color.GREEN);
            t.cell.setColor(Color.ROYAL);
        }
        for (Cells c : unWalkable) {
            c.cell.setColor(Color.FIREBRICK);
        }
    }

    public void sortLists(LinkedList<Cells> list) {
        Collections.sort(list, new Comparator<Cells>() {
            @Override
            public int compare(Cells o1, Cells o2) {
                if (o1.getCost() < o2.getCost()) {
                    return -1;
                }
                if (o1.getCost() > o2.getCost()) {
                    return 1;
                }
                return 0;
            }
        });
    }

}

