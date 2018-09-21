package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.WorldController;
import uy.com.agm.gamefour.game.WorldRenderer;
import uy.com.agm.gamefour.game.tools.GameWorld;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class PlayScreen2 extends GameAbstractScreen {
    private static final String TAG = PlayScreen2.class.getName();

    private GameWorld gameWorld;
    private WorldController worldController;
    private WorldRenderer worldRenderer;

    public PlayScreen2(GameFour game) {
        super(game);

        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController, game.getGameBatch(), game.getGameShapeRenderer());
        gameWorld = new GameWorld(worldController, worldRenderer);
    }

    @Override
    public void show() {
        // Nothing to do here.
    }

    @Override
    public void render(float deltaTime) {
        if (isGameScreenStateRunning()) {
            worldController.update(deltaTime);
        }

        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
        // TODO INVOCAR A PANTALLA LINDA DE PAUSA
    }

    @Override
    public void hide() {
        worldController.dispose();
        // esto sería el dispose de esta pantalla, capaz llama al renderer.dispose y wordcontroller.dispose
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController.getInputProcessor();
    }

    @Override
    public Viewport getViewport() {
        return worldRenderer.getGameViewport();
    }

    @Override
    public OrthographicCamera getCamera() {
        return worldRenderer.getGameCamera();
    }
}
