package uy.com.agm.gamefour.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import uy.com.agm.gamefour.screens.AbstractScreen;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class WorldRenderer {
    private static final String TAG = WorldRenderer.class.getName();

    private WorldController worldController;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer box2DDebugRenderer;

    public WorldRenderer(WorldController worldController, SpriteBatch batch, ShapeRenderer shapeRenderer, Box2DDebugRenderer box2DDebugRenderer) {
        this.worldController = worldController;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.box2DDebugRenderer = box2DDebugRenderer;
    }

    public void render() {
        AbstractScreen.clearScreen();

        // Gets the game world and camera
        GameWorld gameWorld = worldController.getGameWorld();
        OrthographicCamera gameWorldCamera = gameWorld.getCamera();

        // Sets the batch to now draw what the world camera sees.
        batch.setProjectionMatrix(gameWorldCamera.combined);
        batch.begin();

        // Render the game world
        gameWorld.render(batch);

        batch.end();

        // Render bounding boxes
        if (shapeRenderer != null) {
            // Sets the shapeRenderer to now draw what the gameCamera sees.
            shapeRenderer.setProjectionMatrix(gameWorldCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            // Render the game world (debug)
            gameWorld.renderDebug(shapeRenderer);

            shapeRenderer.end();
        }

        // Render Box2DDebugLines
        if (box2DDebugRenderer != null) {
            box2DDebugRenderer.render(worldController.getBox2DWorld(), gameWorldCamera.combined);
        }
    }
}
