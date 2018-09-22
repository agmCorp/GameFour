package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.tools.Shaker;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    // Temporary GC friendly vector
    private Vector2 tmp;

    // Game camera and viewport
    private OrthographicCamera gameWorldCamera;
    private Viewport gameWorldViewPort;

    // Screen shaker
    private Shaker shaker;

    // Sprites
    private Jumper jumper;

    public GameWorld() {
        // Temporary GC friendly vector
        tmp = new Vector2();

        // Creates a ExtendViewport to maintain virtual aspect ratio despite screen size
        // We use the convention 100 pixels = 1 meter to work with meters and therefore meters per seconds in velocity and so on.
        float width = GameFour.APPLICATION_WIDTH / GameFour.PPM;
        float height = GameFour.APPLICATION_HEIGHT / GameFour.PPM;
        gameWorldCamera = new OrthographicCamera();
        gameWorldViewPort = new ExtendViewport(width, height, gameWorldCamera);

        // Screen shaker
        shaker = new Shaker(gameWorldCamera);

        // Creates Jumper in the game world
        jumper = new Jumper(this, 0, height / 2);

        // todo
        Gdx.app.debug(TAG, "*** width e height gameWorldViewPort en GAMEWORLD constructor **: " + gameWorldViewPort.getWorldWidth() + " " + gameWorldViewPort.getWorldHeight());
    }

    public void resize(int width, int height) {
        gameWorldViewPort.update(width, height, true);

        // todo
        Gdx.app.debug(TAG, "*** width e height gameWorldViewPort en GAMEWORLD resize **: " + gameWorldViewPort.getWorldWidth() + " " + gameWorldViewPort.getWorldHeight());
    }

    public OrthographicCamera getCamera() {
        return gameWorldCamera;
    }

    public Viewport getViewPort() {
        return gameWorldViewPort;
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);

        // Always at the end
        updateCamera(deltaTime);
    }

    private void updateCamera(float deltaTime) {
        // Update the game camera with correct coordinates after changes
        gameWorldCamera.update();

        // Update the shaker
        shaker.update(deltaTime);
    }

    public Shaker getShaker() {
        return shaker;
    }

    public void render(SpriteBatch batch) {
        // This order is important.
        // This determines if a sprite has to be drawn in front or behind another sprite.
        jumper.render(batch);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        jumper.renderDebug(shapeRenderer);
    }
}

