package uy.com.agm.gamefour.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.tools.GameWorld;
import uy.com.agm.gamefour.screens.AbstractScreen;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class WorldRenderer {
    // Basic WorldRenderer variables
    private OrthographicCamera gameCamera;
    private Viewport gameViewPort;

    // Box2d variables
    private Box2DDebugRenderer box2DDebugRenderer;

    private World box2dWorld;
    private GameWorld gameWorld;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    public WorldRenderer(World box2dWorld, GameWorld gameWorld, SpriteBatch batch, ShapeRenderer shapeRenderer) {
        this.box2dWorld = box2dWorld;
        this.gameWorld = gameWorld;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;

        // Creates a ExtendViewport to maintain virtual aspect ratio despite screen size
        // We use the convention 100 pixels = 1 meter to work with meters and therefore meters per seconds in velocity and so on.
        gameCamera = new OrthographicCamera();
        gameViewPort = new ExtendViewport(GameFour.APPLICATION_WIDTH / GameFour.PPM, GameFour.APPLICATION_HEIGHT / GameFour.PPM, gameCamera);

        // Places the gameCamera in the middle of the screen
        gameCamera.position.set(gameViewPort.getWorldWidth() / 2, gameViewPort.getWorldHeight() / 2, 0);

        // Allows debug lines of the Box2D world.
        if (DebugConstants.DEBUG_LINES) {
            box2DDebugRenderer = new Box2DDebugRenderer();
        }
    }

    public void render() {
        AbstractScreen.clearScreen();

        // Render Box2DDebugLines
        if (DebugConstants.DEBUG_LINES) {
            box2DDebugRenderer.render(box2dWorld, gameCamera.combined);
        }

        // Sets the batch to now draw what the gameCamera sees.
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();

        // This order is important
        // This determines if a sprite has to be drawn in front or behind another sprite
        gameWorld.render(batch);

        batch.end();

        // Debug
        if (DebugConstants.DEBUG_LINES) {
            // Sets the shapeRenderer to now draw what the gameCamera sees.
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            gameWorld.renderDebug(shapeRenderer);

            shapeRenderer.end();
        }
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public Viewport getGameViewPort() {
        return gameViewPort;
    }

    public void resize(int width, int height) {
        gameViewPort.update(width, height);
    }
}
