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

    private static float SHAKE_DURATION = 3.0f;

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    public PlayScreen(GameFour game) {
        super(game);

        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController.getGameWorld(), game.getGameBatch(), game.getGameShapeRenderer(), game.getBox2DDebugRenderer());
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

        // Analyze game results
        if (playScreenState == PlayScreenState.RUNNING) {
            gameResults(deltaTime);
        }
    }

    private void gameResults(float deltaTime) {
        if (worldController.isGameOver()) {
            worldController.getGameWorld().getGameCamera().shake(SHAKE_DURATION);
            // TODO ACA MUESTRO PANTALLA SUPERPUESTA DE GAMEOVER?
        }
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
        return worldController.getInputProcessor(new GameController(worldController.getGameWorld(), this));
    }

    @Override
    public void applyViewport() {
        worldController.getGameWorld().getGameCamera().applyViewport();
    }
}
