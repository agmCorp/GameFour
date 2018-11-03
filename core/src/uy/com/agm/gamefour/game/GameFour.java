package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.rafaskoberg.gdx.typinglabel.TypingConfig;

import uy.com.agm.gamefour.admob.IAdsController;
import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenManager;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class GameFour extends DirectedGame {
    private static final String TAG = GameFour.class.getName();

    public static final int APPLICATION_WIDTH = 480;
    public static final int APPLICATION_HEIGHT = 800;
    public static final String TITLE = "Game Four";

    private SpriteBatch guiBatch;
    private SpriteBatch gameBatch;
    private ShapeRenderer gameShapeRenderer;
    private Box2DDebugRenderer box2DDebugRenderer;
    private IAdsController adsController;

    public GameFour(IAdsController adsController){
        this.adsController = adsController;
    }

    @Override
    public void create() {
        // Debug
        if (DebugConstants.DEBUG_MODE) {
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
        } else {
            Gdx.app.setLogLevel(Application.LOG_INFO);
            Gdx.app.log(TAG, "**** Debug messages not enabled (set DEBUG_MODE = true to enable them) ****");
        }

        // Load preferences and settings
        GameSettings.getInstance().load();

        // Set TypingConfig new line character interval multiplier
        TypingConfig.INTERVAL_MULTIPLIERS_BY_CHAR.put('\n', 0);

        // Constructs a new guiBatch
        guiBatch = new SpriteBatch();

        // Constructs a new gameBatch
        gameBatch = new SpriteBatch();

        // Constructs a new gameShapeRenderer for debugging purposes
        if (DebugConstants.DEBUG_LINES) {
            gameShapeRenderer = new ShapeRenderer();
        } else {
            gameShapeRenderer = null;
        }

        // Constructs a new box2DDebugRenderer for debugging purposes
        if (DebugConstants.DEBUG_BOX2D) {
            box2DDebugRenderer = new Box2DDebugRenderer();
        } else {
            box2DDebugRenderer = null;
        }

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

    public Box2DDebugRenderer getBox2DDebugRenderer() {
        return box2DDebugRenderer;
    }

    public IAdsController getAdsController() {
        return adsController;
    }

    @Override
    public void dispose() {
        super.dispose();
        gameBatch.dispose();
        if (DebugConstants.DEBUG_LINES) {
            gameShapeRenderer.dispose();
        }
        if (DebugConstants.DEBUG_BOX2D) {
            box2DDebugRenderer.dispose();
        }
        Assets.getInstance().dispose();
        gameBatch = null;
        gameShapeRenderer = null;
        box2DDebugRenderer = null;
    }
}
