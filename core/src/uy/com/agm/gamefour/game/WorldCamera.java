package uy.com.agm.gamefour.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class WorldCamera {
    // Basic WorldCamera variables
    private OrthographicCamera worldCamera;
    private Viewport worldViewPort;

    public WorldCamera() {
        // Creates a ExtendViewport to maintain virtual aspect ratio despite screen size
        // We use the convention 100 pixels = 1 meter to work with meters and therefore meters per seconds in velocity and so on.
        worldCamera = new OrthographicCamera();
        worldViewPort = new ExtendViewport(GameFour.APPLICATION_WIDTH / GameFour.PPM, GameFour.APPLICATION_HEIGHT / GameFour.PPM, worldCamera);

        // Places the worldCamera in the middle of the screen
        worldCamera.position.set(worldViewPort.getWorldWidth() / 2, worldViewPort.getWorldHeight() / 2, 0);
    }

    public OrthographicCamera getWorldCamera() {
        return worldCamera;
    }

    public Viewport getWorldViewPort() {
        return worldViewPort;
    }

    public void resize(int width, int height) {
        worldViewPort.update(width, height);
    }
}
