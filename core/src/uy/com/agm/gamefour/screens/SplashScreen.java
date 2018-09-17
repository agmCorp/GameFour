package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class SplashScreen extends GUIAbstractScreen {
    private Texture img;

    public SplashScreen(GameFour game) {
        super(game);

        img = new Texture("badlogic.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = game.getGuiBatch();
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
