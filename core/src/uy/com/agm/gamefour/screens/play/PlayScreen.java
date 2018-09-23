package uy.com.agm.gamefour.screens.play;

import com.badlogic.gdx.InputProcessor;

import uy.com.agm.gamefour.game.GameController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.WorldController;
import uy.com.agm.gamefour.game.WorldRenderer;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class PlayScreen extends PlayAbstractScreen {
    private static final String TAG = PlayScreen.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    public PlayScreen(GameFour game) {
        super(game);

        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController, game.getGameBatch(), game.getGameShapeRenderer(), game.getBox2DDebugRenderer());
    }

    public void shakeMe(float duration) {
        worldController.getGameWorld().getGameCamera().shake(duration);
    }

    public void shakeMe(float amplitude, float duration) {
        worldController.getGameWorld().getGameCamera().shake(amplitude, duration);
    }

    @Override
    public void show() {
        // Nothing to do here.
    }

    @Override
    public void render(float deltaTime) {
        if (isPlayScreenStateRunning()) {
            worldController.update(deltaTime);
        }
        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        worldController.getGameWorld().getGameCamera().resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
        // TODO INVOCAR A PANTALLA LINDA DE PAUSA
    }

    @Override
    public void hide() {
        worldController.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController.getInputProcessor(new GameController(this));
    }

    @Override
    public void applyViewport() {
        worldController.getGameWorld().getGameCamera().applyViewport();
    }
}
