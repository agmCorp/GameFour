package uy.com.agm.gamefour.screens.play;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.WorldController;
import uy.com.agm.gamefour.game.WorldRenderer;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class PlayScreen2 extends PlayAbstractScreen {
    private static final String TAG = PlayScreen2.class.getName();

    private GameWorld gameWorld;
    private WorldController worldController;
    private WorldRenderer worldRenderer;

    public PlayScreen2(GameFour game) {
        super(game);

        gameWorld = new GameWorld();
        worldController = new WorldController(gameWorld);
        worldRenderer = new WorldRenderer(worldController, game.getGameBatch(), game.getGameShapeRenderer(), game.getBox2DDebugRenderer());
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
        gameWorld.resize(width, height);
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
    public Viewport getViewport() {
        return gameWorld.getViewPort();
    }
}
