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
    private WorldCamera worldCamera;
    private WorldController worldController;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer box2DDebugRenderer;

    public WorldRenderer(WorldCamera worldCamera, WorldController worldController, SpriteBatch batch, ShapeRenderer shapeRenderer, Box2DDebugRenderer box2DDebugRenderer) {
        this.worldCamera = worldCamera;
        this.worldController = worldController;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.box2DDebugRenderer = box2DDebugRenderer;
    }

    public void render() {
        AbstractScreen.clearScreen();

        // Gets the world camera
        OrthographicCamera wc = worldCamera.getWorldCamera();

        // Sets the batch to now draw what the world camera sees.
        batch.setProjectionMatrix(wc.combined);
        batch.begin();

        // This order is important
        // This determines if a sprite has to be drawn in front or behind another sprite
        renderJumper(batch);

        batch.end();

        // Render bounding boxes
        if (shapeRenderer != null) {
            // Sets the shapeRenderer to now draw what the gameCamera sees.
            shapeRenderer.setProjectionMatrix(wc.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            renderDebugJumper(shapeRenderer);

            shapeRenderer.end();
        }

        // Render Box2DDebugLines
        if (box2DDebugRenderer != null) {
            box2DDebugRenderer.render(worldController.getBox2DWorld(), worldCamera.getWorldCamera().combined);
        }
    }

    private void renderJumper(SpriteBatch batch) {
        //jumper.draw(batch);
    }

    private void renderDebugJumper(ShapeRenderer shapeRenderer) {
        //jumper.renderDebug(shapeRenderer);
    }
}
