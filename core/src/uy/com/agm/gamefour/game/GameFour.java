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

    public static final int APPLICATION_WIDTH = 480;
    public static final int APPLICATION_HEIGHT = 800;
    public static final float PPM = 100; // Scale (Pixels Per Meter)
    public static final String TITLE = "Game Four";

    private SpriteBatch guiBatch;
    private SpriteBatch gameBatch;
    private ShapeRenderer gameShapeRenderer;

    @Override
    public void create() {
        // Debug
        if (DebugConstants.DEBUG_MODE) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_INFO);
            Gdx.app.log(TAG, "**** Debug messages not enabled (set DEBUG_MODE = true to enable them) ****");
        }

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
