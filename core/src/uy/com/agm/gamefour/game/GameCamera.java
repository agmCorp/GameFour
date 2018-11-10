package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.tools.Shaker;

/**
 * Created by AGMCORP on 9/23/2018.
 */

public class GameCamera {
    private static final String TAG = GameCamera.class.getName();

    public static final float PPM = 100; // Scale (Pixels Per Meter)

    // Game camera and viewport
    private OrthographicCamera gameCamera;
    private Viewport gameViewPort;

    // Screen shaker
    private Shaker shaker;

    public GameCamera() {
        // Creates a ExtendViewport to maintain virtual aspect ratio despite screen size
        // We use the convention 100 pixels = 1 meter to work with meters and therefore meters per seconds in velocity and so on.
        float width = GameFour.APPLICATION_WIDTH / PPM;
        float height = GameFour.APPLICATION_HEIGHT / PPM;
        gameCamera = new OrthographicCamera();
        gameViewPort = new ExtendViewport(width, height, gameCamera);

        /** WA: Only after update getWorldWidth() and getWorldHeight in ExtendViewport
         * are defined (see {@link uy.com.agm.gamefour.screens.gui.GUIAbstractScreen})
         * This is the same as this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) */
        gameViewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        // Screen shaker
        shaker = new Shaker(gameCamera);
    }

    public void update(float deltaTime) {
        // Update the game camera with correct coordinates after changes
        gameCamera.update();

        // Update the shaker
        shaker.update(deltaTime);
    }

    public void resize(int width, int height) {
        gameViewPort.update(width, height, false);
    }

    public float getWorldWidth() {
        return gameViewPort.getWorldWidth();
    }

    public float getWorldHeight() {
        return gameViewPort.getWorldHeight();
    }

    public Vector3 position() {
        return gameCamera.position;
    }

    // Returns the combined projection and view matrix
    public Matrix4 getCombined() {
        return gameCamera.combined;
    }

    public void applyViewport() {
        gameViewPort.apply();
    }

    public void shake(float duration, boolean interrupt) {
        shaker.shake(duration, interrupt);
    }

    public void shake(float amplitude, float duration, boolean interrupt) {
        shaker.shake(amplitude, duration, interrupt);
    }
}
