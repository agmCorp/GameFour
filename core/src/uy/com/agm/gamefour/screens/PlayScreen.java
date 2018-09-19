package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class PlayScreen extends GameAbstractScreen {
    public PlayScreen(GameFour game) {
        super(game);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        clearScreen();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }

    @Override
    public Viewport getViewport() {
        return null;
    }
}
