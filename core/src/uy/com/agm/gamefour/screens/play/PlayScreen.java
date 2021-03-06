package uy.com.agm.gamefour.screens.play;

import com.badlogic.gdx.InputProcessor;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.GameController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameSettings;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.WorldController;
import uy.com.agm.gamefour.game.WorldRenderer;
import uy.com.agm.gamefour.screens.gui.Hud;
import uy.com.agm.gamefour.screens.gui.InfoScreen;
import uy.com.agm.gamefour.screens.gui.PauseScreen;
import uy.com.agm.gamefour.tools.AudioManager;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class PlayScreen extends PlayAbstractScreen {
    private static final String TAG = PlayScreen.class.getName();

    private static final float SHAKE_DURATION = 2.0f;

    private Hud hud;
    private InfoScreen infoScreen;
    private PauseScreen pauseScreen;
    private WorldController worldController;
    private GameWorld gameWorld;
    private WorldRenderer worldRenderer;
    private GameSettings prefs;
    private boolean endGame;
    private boolean levelCompleted;

    public PlayScreen(GameFour game) {
        super(game);

        hud = new Hud(game);
        infoScreen = new InfoScreen(game, this);
        pauseScreen = new PauseScreen(game, this);

        worldController = new WorldController(this);
        gameWorld = worldController.getGameWorld();
        worldRenderer = new WorldRenderer(gameWorld, game.getGameBatch(), game.getGameShapeRenderer(), game.getBox2DDebugRenderer());
        prefs = GameSettings.getInstance();
        endGame = false;
        levelCompleted = false;

        // Play level music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongGame());
    }

    @Override
    public void show() {
        hud.build();
        infoScreen.build();
        pauseScreen.build();
    }

    @Override
    public void render(float deltaTime) {
        // Update logic
        pauseScreen.update(deltaTime);
        if (isPlayScreenStateRunning()) {
            hud.update(deltaTime);
            infoScreen.update(deltaTime);
            worldController.update(deltaTime);
        }

        // Render logic
        worldRenderer.render();
        hud.render();
        infoScreen.render();
        pauseScreen.render();

        // Analyze game results
        if (playScreenState == PlayScreenState.RUNNING) {
            gameResults();
        }
    }

    private void gameResults() {
        if (!endGame) {
            // We evaluate mutual exclusion conditions.
            // A boolean value is used to avoid nested if/else sentences.
            boolean finish = false;

            // Show game controllers help
            finish = !finish && prefs.mustShowHelp();
            if (finish) {
                infoScreen.showHelp();
                prefs.setShowHelp(false);
            }

            finish = !finish && levelCompleted;
            if (finish) {
                gameWorld.addLevel();
                levelCompleted = false;
            }

            finish = !finish && worldController.isGameOver();
            if (finish) {
                // Advertisement
                if (hud.isScoreAboveAverage()) {
                    prefs.decreaseCountdownAd();
                    if (prefs.isCountdownAdFinish()) {
                        prefs.resetCountdownAd();
                        showInterstitialAd();
                    }
                }

                // Game over
                gameWorld.getGameCamera().shake(SHAKE_DURATION, true);
                gameWorld.getJumper().onDead();
                infoScreen.showGameOver();
                endGame = true;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        infoScreen.resize(width, height);
        pauseScreen.resize(width, height);
        gameWorld.getGameCamera().resize(width, height);
    }

    @Override
    public void pause() {
        AudioManager.getInstance().pauseMusic();
        doPause();
    }

    public void doPause() {
        hideBannerAd();
        super.pause();
    }

    public void setGameStatePaused() {
        pauseScreen.showPauseScreen();
    }

    public void setGameStateRunning() {
        pauseScreen.hidePauseScreen();
    }

    @Override
    public void resume() {
        AudioManager.getInstance().resumeMusic();
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
        return worldController.getInputProcessor(new GameController(gameWorld, this));
    }

    @Override
    public void applyViewport() {
        hud.applyViewport();
        infoScreen.applyViewport();
        pauseScreen.applyViewport();
        gameWorld.getGameCamera().applyViewport();
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

    public void setLevelIsCompleted() {
        levelCompleted = true;
    }
}
