package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GUIAbstractScreen extends AbstractScreen {
    private static final String TAG = AbstractScreen.class.getName();

    private boolean stop;
    protected OrthographicCamera guiCamera;
    protected Viewport viewport;
    protected Stage stage;

    public GUIAbstractScreen(GameFour game) {
        super(game);
        stop = false;
        guiCamera = new OrthographicCamera();
        viewport = new ExtendViewport(GameFour.APPLICATION_WIDTH, GameFour.APPLICATION_HEIGHT, guiCamera);
        stage = new Stage(viewport, game.getGuiBatch());
    }

    @Override
    public Viewport getViewport() {
        return stage.getViewport();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        clearScreen();
        if (!stop) {
            updateLogic(deltaTime);
        }
        renderLogic();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public void pause() {
        stop();
    }

    @Override
    public void resume() {
        stop = false;
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose () {
        hide();
    }

    protected abstract void updateLogic(float deltaTime);
    protected abstract void renderLogic();
}
