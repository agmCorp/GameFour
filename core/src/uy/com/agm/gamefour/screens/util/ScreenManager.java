package uy.com.agm.gamefour.screens.util;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class ScreenManager {
    private static final String TAG = ScreenManager.class.getName();

    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to the game
    private GameFour game;

    // Singleton: prevent instantiation from other classes
    private ScreenManager() {
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Initialization with the game class
    public void initialize(GameFour game) {
        this.game = game;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum screenEnum, ScreenTransitionEnum screenTransitionEnum, Object... params) {
        // Show new screen
        if (screenTransitionEnum != null) {
            game.setScreen(screenEnum.getScreen(game, params), screenTransitionEnum.getScreenTransition());
        } else {
            game.setScreen(screenEnum.getScreen(game, params));
        }
    }
}