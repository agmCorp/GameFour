package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by AGMCORP on 10/15/2018.
 */

public class GameSettings {
    private static final String TAG = GameSettings.class.getName();

    private static final int DEF_COUNT_AD = 3;
    private static final String SETTINGS = "powerJumpSettings";
    private static final String HIGH_SCORE = "highScore";
    private static final String BACKGROUND_ID = "backgroundId";
    private static final String AUDIO = "audio";

    // Singleton: unique instance
    private static GameSettings instance;

    private int countdownAd; // No need to persist it
    private Preferences prefs;
    private int highScore;
    private int backgroundId;
    private boolean audio;

    // Singleton: prevent instantiation from other classes
    private GameSettings() {
        countdownAd = DEF_COUNT_AD;
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
        audio = prefs.getBoolean(AUDIO, true);
    }

    public void save() {
        prefs.putInteger(HIGH_SCORE, highScore);
        prefs.putInteger(BACKGROUND_ID, backgroundId);
        prefs.putBoolean(AUDIO, audio);
        prefs.flush();
    }

    public void decreaseCountdownAd() {
        countdownAd = countdownAd > 0 ? countdownAd - 1 : 0;
    }

    public void resetCountdownAd() {
        countdownAd = DEF_COUNT_AD;
    }

    public boolean isCountdownAdFinish() {
        return countdownAd <= 0;
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

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }
}
