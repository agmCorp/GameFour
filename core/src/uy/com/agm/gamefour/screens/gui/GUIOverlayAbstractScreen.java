package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GUIOverlayAbstractScreen implements Disposable {
    private static final String TAG = GUIOverlayAbstractScreen.class.getName();

    protected GameFour game;
    protected OrthographicCamera guiOverlayCamera;
    protected Viewport guiOverlayViewport;
    protected Stage stage;

    public GUIOverlayAbstractScreen(GameFour game) {
        this.game = game;
        guiOverlayCamera = new OrthographicCamera();
        guiOverlayViewport = new ExtendViewport(GameFour.APPLICATION_WIDTH, GameFour.APPLICATION_HEIGHT, guiOverlayCamera);

        /** Internally calls guiOverlayViewport.update() (see {@link uy.com.agm.gamefour.game.GameWorld} and
         * this.resize(int width, int height)) */
        stage = new Stage(guiOverlayViewport, game.getGuiBatch());
    }

    public void applyViewport() {
        stage.getViewport().apply();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    public abstract void build();
    public abstract void update(float deltaTime);
    public abstract void render();
}
