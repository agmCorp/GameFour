package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import uy.com.agm.gamefour.screens.util.ScreenEnum;
import uy.com.agm.gamefour.screens.util.ScreenManager;
import uy.com.agm.gamefour.screens.util.ScreenTransitionEnum;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class GameFour extends DirectedGame {
    private static final String TAG = GameFour.class.getName();

    private SpriteBatch guiBatch;
    private SpriteBatch gameBatch;
    private ShapeRenderer gameShapeRenderer;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Constructs a new SpriteBatch
        guiBatch = new SpriteBatch();

        // Constructs a new SpriteBatch
        gameBatch = new SpriteBatch();

        // Constructs a new ShapeRenderer for debugging
        gameShapeRenderer = new ShapeRenderer();

        // Sets a splash screen
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.SPLASH, ScreenTransitionEnum.ROTATING, this);
    }

    public SpriteBatch getGuiBatch() {
        return guiBatch;
    }

    public SpriteBatch getGameBatch() {
        return gameBatch;
    }

    public ShapeRenderer getGameShapeRenderer() {
        return gameShapeRenderer;
    }

    @Override
    public void dispose() {
        super.dispose();
        gameBatch.dispose();
        gameShapeRenderer.dispose();
    }
}
