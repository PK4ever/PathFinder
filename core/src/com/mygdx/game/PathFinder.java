package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.io.FileNotFoundException;

public class PathFinder extends ApplicationAdapter {
    SpriteBatch batch;
    Grid grid = new Grid();
    Cells start = null;
    Cells end = null;
    Vector3 touchPoint;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;
    int clicks =0;
    BitmapFont font;
    @Override
    public void create() {
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.translate(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        camera.update();

        touchPoint=new Vector3();
        batch = new SpriteBatch();
        try {
            grid.loadGrid();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        grid.drawGrid(batch);
        batch.end();

        batch.begin();
        font.setColor(Color.BLACK);
        for (Cells all : grid.getCells()) {
            if (!all.equals(start) && !all.equals(end) && !grid.unWalkable.contains(all)) {
                font.draw(batch, all.getId(), all.cell.getX() + Gdx.graphics.getWidth() * 0.019230769f, all.cell.getY() + Gdx.graphics.getHeight() * 0.032051282f);
            }else if(all.equals(start)){
                font.draw(batch,"START", all.cell.getX() + Gdx.graphics.getWidth() * 0.019230769f - 15, all.cell.getY() + Gdx.graphics.getHeight() * 0.032051282f);
            }else if(all.equals(end)){
                font.draw(batch,"END", all.cell.getX() + Gdx.graphics.getWidth() * 0.019230769f - 12, all.cell.getY() + Gdx.graphics.getHeight() * 0.032051282f);
            }

        }
        batch.end();

        boolean done =false;
        if(Gdx.input.justTouched())
        {
            for (Cells a : grid.getCells()) {
                if (!grid.Teleporters.contains(a) && !grid.unWalkable.contains(a) && !grid.path.contains(a) && !grid.open.contains(a)) {
                    a.cell.setColor(Color.WHITE);
                }
            }
            grid.visited.clear();
            grid.open.clear();
            grid.path.clear();
            grid.closed.clear();
            //touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));

            for (Cells a : grid.getCells()) {
                if (a.cell.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
                    if (clicks == 0) {
                        start = a;
                        start.cell.setColor(Color.SLATE);
                    }
                    if (clicks == 1){
                        end =a;
                        end.cell.setColor(Color.GOLD);
                        grid.Astar(start,end);
                        done = true;
                    }
                    clicks++;
                }
            }
        }
        if (done){
            clicks =0;
        }

        camera.update();


    }

}
