package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by AGM on 10/15/2018.
 */

public class GameSettings {
    private static final String TAG = GameSettings.class.getName();

    private static final String SETTINGS = "powerJumpSettings";
    private static final String HIGH_SCORE = "highScore";
    private static final String BACKGROUND_ID = "backgroundId";

    // Singleton: unique instance
    private static GameSettings instance;

    private Preferences prefs;
    private int highScore;
    private int backgroundId;

    // Singleton: prevent instantiation from other classes
    private GameSettings() {
        prefs = Gdx.app.getPreferences(SETTINGS);
    }

    // Singleton: retrieve instance
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public void load() {
        highScore = prefs.getInteger(HIGH_SCORE, 0);
        backgroundId = prefs.getInteger(BACKGROUND_ID, 1);
    }

    public void save() {
        prefs.putInteger(HIGH_SCORE, highScore);
        prefs.putInteger(BACKGROUND_ID, backgroundId);
        prefs.flush();
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }
}
