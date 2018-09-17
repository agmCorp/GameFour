package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class AbstractScreen implements Screen {
    private static final String TAG = AbstractScreen.class.getName();

    // Application width
    public static final int APPLICATION_WIDTH = 480;

    // Application height
    public static final int APPLICATION_HEIGHT = 800;

    protected GameFour game;

    public AbstractScreen(GameFour game) {
        this.game = game;
    }

    public abstract void show();

    public abstract void render(float deltaTime);

    public abstract void resize(int width, int height);

    public abstract void pause();

    public abstract void resume();

    public abstract void hide();

    public abstract void dispose();

    public abstract InputProcessor getInputProcessor();

    public abstract Viewport getViewport();
}
