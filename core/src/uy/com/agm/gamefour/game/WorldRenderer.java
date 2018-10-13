package uy.com.agm.gamefour.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import uy.com.agm.gamefour.screens.AbstractScreen;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class WorldRenderer {
    private static final String TAG = WorldRenderer.class.getName();

    private GameWorld gameWorld;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer box2DDebugRenderer;

    public WorldRenderer(GameWorld gameWorld, SpriteBatch batch, ShapeRenderer shapeRenderer, Box2DDebugRenderer box2DDebugRenderer) {
        this.gameWorld = gameWorld;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.box2DDebugRenderer = box2DDebugRenderer;
    }

    public void render() {
        AbstractScreen.clearScr();

        // Gets the combined projection and view matrix of the game camera
        Matrix4 combined = gameWorld.getGameCamera().getCombined();

        // Sets the batch to now draw what the game camera sees.
        batch.setProjectionMatrix(combined);
        batch.begin();

        // Render the game world
        gameWorld.render(batch);

        batch.end();

        // Render bounding boxes
        if (shapeRenderer != null) {
            // Sets the shapeRenderer to now draw what the game camera sees.
            shapeRenderer.setProjectionMatrix(combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            // Render the game world (debug)
            gameWorld.renderSpriteDebug(shapeRenderer);

            shapeRenderer.end();
        }

        // Render Box2DDebugLines
        if (box2DDebugRenderer != null) {
            gameWorld.renderBox2DDebug(box2DDebugRenderer);
        }
    }
}
