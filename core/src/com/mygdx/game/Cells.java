package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;

/**
 * Created by paulk4ever on 3/1/16.
 */
public class Cells {
    Sprite cell;
    int FValue;
    int GValue;
    int HValue;
    int X, Y;
    Cells parent;
    String weight;
    LinkedList<Cells> neighbor = new LinkedList<Cells>();
    public Cells(String name) {
        cell = new Sprite(new Texture(name));

    }


    public void setSize(float x, float y) {
        cell.setSize(x, y);
    }

    public void setPosition(float x, float y) {
        cell.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        cell.draw(batch);
    }

    public void setNeighbour(Cells s) {
        neighbor.add(s);
    }

    public int getCost() {
        if (this.getParent() != null){
            return this.getParent().getCost() + this.getGValue();
        }
        return this.getGValue();
    }

    public void setX(int x) {
        this.X = x;
    }

    public void setY(int y) {
        this.Y = y;
    }

    public int get_X() {
        return X;
    }

    public int get_Y() {
        return Y;
    }

    public LinkedList<Cells> getNeighbor() {
        return neighbor;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getFValue() {
        FValue = GValue + HValue;
        return FValue;
    }

    public int getGValue() {
        return GValue;
    }


    public void setGValue(int GValue) {
        this.GValue = GValue;
    }

    public Sprite getCell() {
        return cell;
    }

    public float getHValue() {
        return HValue;
    }

    public void setHValue(int HValue) {
        this.HValue = HValue;
    }
    public void setParent(Cells parent){
        this.parent = parent;
    }
    public Cells getParent(){
        return this.parent;
    }

}
