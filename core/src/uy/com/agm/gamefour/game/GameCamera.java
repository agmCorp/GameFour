package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.tools.Shaker;

/**
 * Created by AGM on 9/23/2018.
 */

public class GameCamera {
    private static final String TAG = GameCamera.class.getName();

    // Game camera and viewport
    private OrthographicCamera gameCamera;
    private Viewport gameViewPort;

    // Screen shaker
    private Shaker shaker;

    public GameCamera() {
        // Creates a ExtendViewport to maintain virtual aspect ratio despite screen size
        // We use the convention 100 pixels = 1 meter to work with meters and therefore meters per seconds in velocity and so on.
        float width = GameFour.APPLICATION_WIDTH / GameFour.PPM;
        float height = GameFour.APPLICATION_HEIGHT / GameFour.PPM;
        gameCamera = new OrthographicCamera();
        gameViewPort = new ExtendViewport(width, height, gameCamera);

        /** WA: Only after update getWorldWidth() and getWorldHeight in ExtendViewport
         * are defined (see {@link uy.com.agm.gamefour.screens.gui.GUIAbstractScreen}) */
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
        gameViewPort.update(width, height, true);
    }

    public float getFrustumWidth() {
        return gameViewPort.getWorldWidth();
    }

    public float getFrustumHeight() {
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

    public void shake(float duration) {
        shaker.shake(duration);
    }

    public void shake(float amplitude, float duration) {
        shaker.shake(amplitude, duration);
    }
}
