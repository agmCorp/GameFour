package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.AbstractScreen;
import uy.com.agm.gamefour.tools.AudioManager;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GUIAbstractScreen extends AbstractScreen {
    private static final String TAG = GUIAbstractScreen.class.getName();

    // GUI state
    private enum GUIScreenState {
        PAUSED, RUNNING, STOPPED
    }
    private GUIScreenState guiScreenState;

    protected OrthographicCamera guiCamera;
    protected Viewport guiViewport;
    protected Stage stage;

    public GUIAbstractScreen(GameFour game) {
        super(game);
        guiScreenState = GUIScreenState.RUNNING;
        guiCamera = new OrthographicCamera();
        guiViewport = new ExtendViewport(GameFour.APPLICATION_WIDTH, GameFour.APPLICATION_HEIGHT, guiCamera);

        /** Internally calls guiViewport.update() (see {@link uy.com.agm.gamefour.game.GameWorld} and
         * this.resize(int width, int height)) */
        stage = new Stage(guiViewport, game.getGuiBatch());
    }

    @Override
    public void applyViewport() {
        stage.getViewport().apply();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        // Update logic
        update(deltaTime);

        // Render logic
        render();
    }

    private void update(float deltaTime) {
        if (guiScreenState == GUIScreenState.RUNNING) {
            handleInput(deltaTime);
            updateLogic(deltaTime);
        }
    }

    private void handleInput(float deltaTime) {
        // BACK button (Android) or ESCAPE key (desktop/html)
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            goBack();
        }
    }

    private void render() {
        clearScr();
        renderLogic();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void stop() {
        guiScreenState = GUIScreenState.STOPPED;
    }

    @Override
    public void pause() {
        AudioManager.getInstance().pauseMusic();
        guiScreenState = GUIScreenState.PAUSED;
    }

    @Override
    public void resume() {
        AudioManager.getInstance().resumeMusic();
        guiScreenState = GUIScreenState.RUNNING;
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
    protected abstract void goBack();
}
