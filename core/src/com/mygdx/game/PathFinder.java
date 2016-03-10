package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PathFinder extends ApplicationAdapter {
    OrthographicCamera camera;
    SpriteBatch batch;
    Grid grid = new Grid();
    @Override
    public void create() {
        camera = new OrthographicCamera(400,400);
        camera.translate(200f,200f);
        batch = new SpriteBatch();
        grid.loadGrid();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        grid.drawGrid(batch);

        batch.end();


        ///grid.Astar();

    }

}
