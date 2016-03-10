package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;
import java.io.FileNotFoundException;
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
    float xPosition;
    float yPosition;

    LinkedList<Cells> unWalkable = new LinkedList<Cells>();
    String[][] weights = null;
    public Grid() {

    }


    public void loadGrid() {
        Scanner sc = null;
        Scanner wc = null;
        Scanner scann = null;
        try {
            sc = new Scanner(new File("input.txt"));
            wc = new Scanner(new File("input.txt"));
            scann = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }


        while (sc.hasNextLine()) {
            height++;
            sc.nextLine();
        }
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


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cell[i][j] = new Cells("white.jpg");
                cell[i][j].setSize(Gdx.graphics.getWidth() / (width + Gdx.graphics.getWidth() * 0.00125f), Gdx.graphics.getHeight() / height);
                cell[i][j].setPosition(xPosition, yPosition);
                xPosition = xPosition + (Gdx.graphics.getWidth() / width);
                cell[i][j].setWeight(weights[i][j]);
                if (cell[i][j].getWeight().equals("F")) {
                    unWalkable.add(cell[i][j]);
                }
                cell[i][j].setX(j);
                cell[i][j].setY(i);
            }
            yPosition = yPosition + (Gdx.graphics.getHeight() / height) + Gdx.graphics.getHeight() * 0.0125f;
            xPosition = 0;
        }
        this.setNeighbors();
        start = cell[0][0];
        end = cell[3][9];
        end.cell.setColor(Color.GOLD);
        this.Astar();
    }

    public LinkedList<Cells> getCells(){
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


    public void Astar() {

        LinkedList<Cells> path = new LinkedList<Cells>();
        LinkedList<Cells> open = new LinkedList<Cells>();
        start.setWeight("0");
        start.cell.setColor(Color.BLACK);
        calculateG(start);
        calculateH(start);
        open.push(start);

        LinkedList<Cells> closed = new LinkedList<Cells>();
        end.setGValue(0);

        while (!open.isEmpty()) {
            this.sortLists(open);
            this.sortLists(closed);
            current = open.pop();
            current.cell.setColor(Color.LIGHT_GRAY);
            closed.push(current);

            //check if best case
            if (current.equals(end)){
                if (current.getParent() != null){
                    Cells p = current;
                        path.push(p);
                        this.sortLists(path);
                }else{
                    path.push(current);
                }

                break;
            }
            calculateG(current);
            calculateH(current);

            for (Cells n : current.getNeighbor()){
                if (!open.contains(n) && !closed.contains(n) && !unWalkable.contains(n)){
                    calculateG(n);
                    calculateH(n);
                    n.setParent(current);
                    if (current.equals(start)){
                        current.setGValue(0);
                    }
                    //n.cell.setColor(Color.CORAL);
                    open.push(n);
                    this.sortLists(open);
                }
            }
        }
        end.cell.setColor(Color.GOLD);
        for (Cells c : unWalkable) {
            c.cell.setColor(Color.FIREBRICK);
        }

        Cells finalPath = path.pop();
        while (!finalPath.equals(start)){
            finalPath = finalPath.getParent();
            finalPath.cell.setColor(Color.CYAN);
        }
        start.cell.setColor(Color.BLACK);

    }
    public void sortLists(LinkedList<Cells> list){
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

