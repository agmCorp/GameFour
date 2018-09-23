package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class AbstractScreen implements Screen {
    private static final String TAG = AbstractScreen.class.getName();

    protected GameFour game;

    public AbstractScreen(GameFour game) {
        this.game = game;

        // Sets whether the BACK button on Android should be caught.
        // This will prevent the app from being paused. Will have no effect on the desktop/html.
        Gdx.input.setCatchBackKey(true);
    }

    public static void clearScreen() {
        // Clear the screen with black
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /** Called by {@link uy.com.agm.gamefour.game.DirectedGame} when this screen becomes the current screen. */
    public abstract void show();

    public abstract void render(float deltaTime);

    public abstract void resize(int width, int height);

    public abstract void pause();

    /** Called by {@link uy.com.agm.gamefour.game.DirectedGame} when this screen must be paused before a transition. */
    public abstract void stop();

    public abstract void resume();

    /** Called by {@link uy.com.agm.gamefour.game.DirectedGame} when this screen is no longer the current screen. */
    public abstract void hide();

    public abstract void dispose();

    public abstract InputProcessor getInputProcessor();

    public abstract void applyViewport();
}
