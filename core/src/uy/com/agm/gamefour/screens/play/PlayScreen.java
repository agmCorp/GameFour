package uy.com.agm.gamefour.screens.play;

import com.admob.IAdsController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import uy.com.agm.gamefour.game.GameController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.WorldController;
import uy.com.agm.gamefour.game.WorldRenderer;
import uy.com.agm.gamefour.screens.gui.Hud;
import uy.com.agm.gamefour.screens.gui.InfoScreen;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class PlayScreen extends PlayAbstractScreen {
    private static final String TAG = PlayScreen.class.getName();

    private static float SHAKE_DURATION = 2.0f;

    private Hud hud;
    private InfoScreen infoScreen;
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private boolean endGame;

    public PlayScreen(GameFour game) {
        super(game);

        hud = new Hud(game);
        infoScreen = new InfoScreen(game);

        worldController = new WorldController(this);
        GameWorld gameWorld = worldController.getGameWorld();
        worldRenderer = new WorldRenderer(gameWorld, game.getGameBatch(), game.getGameShapeRenderer(), game.getBox2DDebugRenderer());
        endGame = false;
        showBannerAd();
    }

    @Override
    public void show() {
        hud.build();
        infoScreen.build();
    }

    @Override
    public void render(float deltaTime) {
        if (isPlayScreenStateRunning()) {
            hud.update(deltaTime);
            infoScreen.update(deltaTime);
            worldController.update(deltaTime);
        }
        worldRenderer.render();
        hud.render();
        infoScreen.render();

        // Analyze game results
        if (playScreenState == PlayScreenState.RUNNING) {
            gameResults(deltaTime);
        }
    }

    private void gameResults(float deltaTime) {
        if (worldController.isGameOver() && !endGame) {
            showInterstitialAd();

            worldController.getGameWorld().getGameCamera().shake(SHAKE_DURATION);
            endGame = true;
            infoScreen.showGameOver();
        }
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        infoScreen.resize(width, height);
        worldController.getGameWorld().getGameCamera().resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
        // TODO INVOCAR A PANTALLA LINDA DE PAUSA
    }

    @Override
    public void hide() {
        hud.dispose();
        infoScreen.dispose();
        worldController.dispose();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return worldController.getInputProcessor(new GameController(worldController.getGameWorld(), this));
    }

    @Override
    public void applyViewport() {
        hud.applyViewport();
        infoScreen.applyViewport();
        worldController.getGameWorld().getGameCamera().applyViewport();
    }

    public Hud getHud() {
        return hud;
    }

    public InfoScreen getInfoScreen() {
        return infoScreen;
    }

    public void showBannerAd() {
        IAdsController adsController = game.getAdsController();
        if (adsController.isWifiConnected()) {
            adsController.showBannerAd();
        } else {
            Gdx.app.debug(TAG, "**** Not connected to the internet");
        }
    }

    public void showInterstitialAd() {
        IAdsController adsController = game.getAdsController();
        if (adsController.isWifiConnected()) {
            adsController.showInterstitialAd(new Runnable() {
                @Override
                public void run() {
                   // if (!isPlayScreenStateRunning()) { // el juego esta en pausa?
                   //     dimScreen.setGameStateRunning();// todo aca no se, depende cuando haga la pantalla de pausa
                   // }
                }
            });
        } else {
            Gdx.app.debug(TAG, "**** Not connected to the internet");
        }
    }
}
