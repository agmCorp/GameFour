package uy.com.agm.gamefour.screens.play;

import com.badlogic.gdx.InputProcessor;

import uy.com.agm.gamefour.game.GameController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameSettings;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.WorldController;
import uy.com.agm.gamefour.game.WorldRenderer;
import uy.com.agm.gamefour.screens.gui.Hud;
import uy.com.agm.gamefour.screens.gui.InfoScreen;
import uy.com.agm.gamefour.screens.gui.PauseScreen;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class PlayScreen extends PlayAbstractScreen {
    private static final String TAG = PlayScreen.class.getName();

    private static float SHAKE_DURATION = 2.0f;

    private Hud hud;
    private InfoScreen infoScreen;
    private PauseScreen pauseScreen;
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private GameSettings prefs;
    private boolean endGame;

    public PlayScreen(GameFour game) {
        super(game);

        hud = new Hud(game);
        infoScreen = new InfoScreen(game);
        pauseScreen = new PauseScreen(game);

        worldController = new WorldController(this);
        GameWorld gameWorld = worldController.getGameWorld();
        worldRenderer = new WorldRenderer(gameWorld, game.getGameBatch(), game.getGameShapeRenderer(), game.getBox2DDebugRenderer());
        prefs = GameSettings.getInstance();
        endGame = false;
    }

    @Override
    public void show() {
        hud.build();
        infoScreen.build();
        pauseScreen.build();
    }

    @Override
    public void render(float deltaTime) {
        pauseScreen.update(deltaTime);
        if (isPlayScreenStateRunning()) {
            hud.update(deltaTime);
            infoScreen.update(deltaTime);
            worldController.update(deltaTime);
        }

        worldRenderer.render();
        hud.render();
        infoScreen.render();
        pauseScreen.render();

        // Analyze game results
        if (playScreenState == PlayScreenState.RUNNING) {
            gameResults(deltaTime);
        }
    }

    private void gameResults(float deltaTime) {
        if (worldController.isGameOver() && !endGame) {
            // Advertisement
            if (hud.isScoreAboveAverage()) {
                prefs.decreaseCountdownAd();
                if (prefs.isCountdownAdFinish()) {
                    prefs.resetCountdownAd();
                    showInterstitialAd();
                }
            }

            // Game over
            GameWorld gameWorld = worldController.getGameWorld();
            gameWorld.getGameCamera().shake(SHAKE_DURATION);
            gameWorld.getJumper().onDead();
            infoScreen.showGameOver();
            endGame = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        infoScreen.resize(width, height);
        pauseScreen.resize(width, height);
        worldController.getGameWorld().getGameCamera().resize(width, height);
    }

    @Override
    public void pause() {
        hideBannerAd();
        super.pause();
    }

    @Override
    public void resume() {
        if (!pauseScreen.isPauseScreenVisible()) {
            showBannerAd();
            super.resume();
        }
    }

    @Override
    public void hide() {
        hud.dispose();
        infoScreen.dispose();
        pauseScreen.dispose();
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
        pauseScreen.applyViewport();
        worldController.getGameWorld().getGameCamera().applyViewport();
    }

    public void setGameStatePaused() {
        pauseScreen.showPauseScreen();
        infoScreen.disableEvents();
    }

    public void setGameStateRunning() {
        pauseScreen.hidePauseScreen();
    }

    public Hud getHud() {
        return hud;
    }

    public InfoScreen getInfoScreen() {
        return infoScreen;
    }

    public PauseScreen getPauseScreen() {
        return pauseScreen;
    }
}
