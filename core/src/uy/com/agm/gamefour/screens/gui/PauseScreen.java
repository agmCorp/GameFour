package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.gui.AssetGUI;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameSettings;
import uy.com.agm.gamefour.playservices.DummyPlayServices;
import uy.com.agm.gamefour.screens.ListenerHelper;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.tools.AudioManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGM on 11/1/2018.
 */

public class PauseScreen extends GUIOverlayAbstractScreen {
    private static final String TAG = PauseScreen.class.getName();

    private static final float TITLE_OFFSET_Y = 200.0f;
    private static final float BUTTONS_OFFSET_Y = 120.0f;
    private static final float BUTTONS_ANIM_DURATION = 1.0f;
    private static final float BUTTONS_MOVE_BY_AMOUNT = 110.0f;
    private static final float DIM_ALPHA = 0.8f;

    private PlayScreen playScreen;
    private Assets assets;
    private AssetGUI assetGUI;
    private I18NBundle i18NGameThreeBundle;
    private GameSettings prefs;
    private Image screenPauseBg;
    private Label pauseLabel;
    private ImageButton play;
    private ImageButton home;
    private ImageButton audio;
    private ImageButton rateGame;
    private ImageButton reload;

    public PauseScreen(GameFour game, PlayScreen playScreen) {
        super(game);

        this.playScreen = playScreen;
        assets = Assets.getInstance();
        assetGUI = assets.getGUI();
        i18NGameThreeBundle = assets.getI18NGameFour().getI18NGameFourBundle();
        prefs = GameSettings.getInstance();
    }

    @Override
    public void build() {
        // Background
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, DIM_ALPHA);
        pixmap.fill();
        TextureRegion dim = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();
        screenPauseBg = new Image(dim);
        stage.addActor(screenPauseBg);

        // Title
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = assets.getFonts().getBig();
        pauseLabel = new Label(i18NGameThreeBundle.format("pauseScreen.title"), labelStyleBig);
        stage.addActor(pauseLabel);

        // Buttons
        defineButtons();
        stage.addActor(home);
        stage.addActor(audio);
        stage.addActor(rateGame);
        stage.addActor(reload);
        stage.addActor(play);

        // Initially hidden
        setVisible(false);
    }

    private void defineButtons() {
        play = new ImageButton(new TextureRegionDrawable(assetGUI.getPlay()),
                new TextureRegionDrawable(assetGUI.getPlayPressed()));

        home = new ImageButton(new TextureRegionDrawable(assetGUI.getHome()),
                new TextureRegionDrawable(assetGUI.getHomePressed()));

        audio = new ImageButton(new TextureRegionDrawable(assetGUI.getAudio()),
                new TextureRegionDrawable(assetGUI.getAudioPressed()),
                new TextureRegionDrawable(assetGUI.getAudioChecked()));
        audio.setChecked(!prefs.isAudio());

        rateGame = new ImageButton(new TextureRegionDrawable(assetGUI.getRateGame()),
                new TextureRegionDrawable(assetGUI.getRateGamePressed()));

        reload = new ImageButton(new TextureRegionDrawable(assetGUI.getReload()),
                new TextureRegionDrawable(assetGUI.getReloadPressed()));

        // Events
        play.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                playScreen.setGameStateRunning();
            }
        }));
        home.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN));
        audio.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                prefs.setAudio(!audio.isChecked());
                prefs.save();
                AudioManager.getInstance().onSettingsUpdated();
            }
        }));
        rateGame.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                rateGame();
            }
        }));
        reload.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.COLOR_FADE_WHITE, false));
    }

    private void setVisible(boolean visible) {
        screenPauseBg.setVisible(visible);
        pauseLabel.setVisible(visible);
        play.setVisible(visible);
        home.setVisible(visible);
        audio.setVisible(visible);
        rateGame.setVisible(visible);
        reload.setVisible(visible);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        float w = stage.getWidth(); // Same as stage.getViewport().getWorldWidth()
        float h = stage.getHeight();

        // Resize background
        screenPauseBg.setSize(w, h);

        // Place the title
        pauseLabel.setX((w - pauseLabel.getWidth()) / 2);
        pauseLabel.setY((h - pauseLabel.getHeight()) / 2 + TITLE_OFFSET_Y);

        // Place buttons
        placeButtons(w, h);

        if (isPauseScreenVisible()) {
            // Buttons Animations
            setButtonsAnimation();
        }
    }

    private void placeButtons(float width, float height) {
        play.setX((width - play.getWidth()) / 2);
        play.setY((height - play.getHeight()) / 2 - BUTTONS_OFFSET_Y);
        float x = play.getX() + play.getWidth() / 2 - audio.getWidth() / 2;
        float y = play.getY() + play.getHeight() / 2 - audio.getHeight() / 2;
        home.setPosition(x, y);
        audio.setPosition(x, y);
        rateGame.setPosition(x, y);
        reload.setPosition(x, y);
    }

    public boolean isPauseScreenVisible() {
        return screenPauseBg.isVisible();
    }

    private void setButtonsAnimation() {
        // Disable events
        play.setTouchable(Touchable.disabled);
        home.setTouchable(Touchable.disabled);
        audio.setTouchable(Touchable.disabled);
        rateGame.setTouchable(Touchable.disabled);
        reload.setTouchable(Touchable.disabled);

        // Only available on Android version
        rateGame.setVisible(!(playServices instanceof DummyPlayServices));

        // Set actions
        play.clearActions();
        home.clearActions();
        audio.clearActions();
        rateGame.clearActions();

        play.addAction(sequence(moveBy(0, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION, Interpolation.bounceOut),
                run(new Runnable() {
                    public void run () {
                        // Enable events
                        play.setTouchable(Touchable.enabled);
                        home.setTouchable(Touchable.enabled);
                        audio.setTouchable(Touchable.enabled);
                        rateGame.setTouchable(Touchable.enabled);
                        reload.setTouchable(Touchable.enabled);
                    }
                })));
        home.addAction(moveBy(BUTTONS_MOVE_BY_AMOUNT, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION, Interpolation.bounceOut));
        audio.addAction(moveBy(-BUTTONS_MOVE_BY_AMOUNT, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION, Interpolation.bounceOut));
        rateGame.addAction(moveBy(0, BUTTONS_MOVE_BY_AMOUNT * 2, BUTTONS_ANIM_DURATION, Interpolation.bounceOut));
    }

    @Override
    public void update(float deltaTime) {
        stage.act();
    }

    @Override
    public void render() {
        stage.draw();
    }

    public void showPauseScreen() {
        if (!isPauseScreenVisible()) {
            playScreen.doPause();
            setVisible(true);
            startStageAnimation(false, new Runnable() {
                @Override
                public void run() {
                    setButtonsAnimation();
                }
            });

            // Only PauseScreen responds to events
            Gdx.input.setInputProcessor(stage);
        }
    }

    public void hidePauseScreen() {
        if (isPauseScreenVisible()) {
            startStageAnimation(true, new Runnable() {
                @Override
                public void run() {
                    setVisible(false);
                    placeButtons(stage.getWidth(), stage.getHeight());
                    playScreen.resume();

                    // Enable input for PlayScreen
                    Gdx.input.setInputProcessor(playScreen.getInputProcessor());
                }
            });
        }
    }

    private void rateGame() {
        playServices.rateGame();
    }
}
