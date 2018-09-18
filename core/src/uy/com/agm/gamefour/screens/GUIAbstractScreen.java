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

    // GUI Width
    protected static final int VIEWPORT_GUI_WIDTH = APPLICATION_WIDTH;

    // GUI Height
    protected static final int VIEWPORT_GUI_HEIGHT = APPLICATION_HEIGHT;

    protected Viewport viewport;
    protected Stage stage;

    public GUIAbstractScreen(GameFour game) {
        super(game);
        viewport = new ExtendViewport(VIEWPORT_GUI_WIDTH, VIEWPORT_GUI_HEIGHT, new OrthographicCamera());
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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
