package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GUIAbstractScreen extends AbstractScreen {
    private static final String TAG = AbstractScreen.class.getName();

    // GUI state
    private enum GUIScreenState {
        PAUSED, RUNNING, STOPPED
    }
    private GUIScreenState guiScreenState;

    protected OrthographicCamera guiCamera;
    protected Viewport viewport;
    protected Stage stage;

    public GUIAbstractScreen(GameFour game) {
        super(game);
        guiScreenState = GUIScreenState.RUNNING;
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
    public OrthographicCamera getCamera() {
        return guiCamera;
    }

    @Override
    public void render(float deltaTime) {
        // Update logic
        update(deltaTime);

        // Render logic
        render();
    }

    private void update(float deltaTime) {
        if (guiScreenState == guiScreenState.RUNNING) {
            handleInput(deltaTime);
            updateLogic(deltaTime);
        }
    }

    private void handleInput(float deltaTime) {
        // Back button Android
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            goBack();
        }
    }

    private void render() {
        clearScreen();
        renderLogic();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        Gdx.app.error(TAG, "************ ERROR - ANTES");
        Gdx.app.debug(TAG, "************ DEBUG - ANTES");
        Gdx.app.log(TAG, "************ LOG - ANTES");
        // WA html version
        if (stage.getRoot() instanceof Table) {
            Table table = ((Table) stage.getRoot());
            // table.validate();
            Gdx.app.error(TAG, "************ ERROR - HOLA");
            Gdx.app.debug(TAG, "************ DEBUG - HOLA");
            Gdx.app.log(TAG, "************ LOG - HOLA");
        }
    }

    @Override
    public void stop() {
        guiScreenState = guiScreenState.STOPPED;
    }

    @Override
    public void pause() {
        guiScreenState = guiScreenState.PAUSED;
    }

    @Override
    public void resume() {
        guiScreenState = guiScreenState.RUNNING;
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
