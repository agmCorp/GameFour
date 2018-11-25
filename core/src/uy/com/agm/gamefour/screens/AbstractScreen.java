package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import uy.com.agm.gamefour.admob.IAdsController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.playservices.IPlayServices;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class AbstractScreen implements Screen {
    private static final String TAG = AbstractScreen.class.getName();

    protected GameFour game;
    IAdsController adsController;
    IPlayServices playServices;

    public AbstractScreen(GameFour game) {
        this.game = game;
        adsController =  game.getAdsController();
        playServices = game.getPlayServices();

        // Sets whether the BACK button on Android should be caught.
        // This will prevent the app from being paused. Will have no effect on the desktop/html.
        Gdx.input.setCatchBackKey(true);
    }

    public static void clearScr() {
        // Clear the screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    protected void showBannerAd() {
        if (adsController.isWifiConnected()) {
            adsController.showBannerAd();
        } else {
            Gdx.app.debug(TAG, "**** Not connected to the internet");
        }
    }

    protected void hideBannerAd() {
        adsController.hideBannerAd();
    }

    protected void showInterstitialAd() {
        if (adsController.isWifiConnected()) {
            adsController.showInterstitialAd(null); // We don't need to execute anything after ad
        } else {
            Gdx.app.debug(TAG, "**** Not connected to the internet");
        }
    }

    // todo
    protected void signIn() {
        if (playServices.isWifiConnected() && !playServices.isSignedIn()) {
            playServices.signIn();
        }
    }

    protected void showLeaderboards() {
        playServices.submitScore(99);
        if (playServices.isSignedIn()) {
            Gdx.app.debug(TAG, "INVOCO A SHOW()");
            playServices.showLeaderboards();
        } else {
            Gdx.app.debug(TAG, "NO INVOCO A SHOW()");
        }
    }

    public void submitScore(int highscore) {
        if (playServices.isSignedIn()) {
            playServices.submitScore(highscore);
        }
    }

    protected void rateGame() {
        playServices.rateGame();
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
