package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class MainMenuScreen extends GUIAbstractScreen {
    public MainMenuScreen(GameFour game) {
        super(game);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        // Clear the screen with black
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
