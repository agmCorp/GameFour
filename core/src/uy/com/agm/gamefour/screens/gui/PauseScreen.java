package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
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
import uy.com.agm.gamefour.screens.ListenerHelper;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGM on 11/1/2018.
 */

public class PauseScreen extends GUIOverlayAbstractScreen {
    private static final String TAG = PauseScreen.class.getName();

    private static final float TITLE_OFFSET_Y = 200.0f;
    private static final float BUTTONS_OFFSET_Y = 100.0f;
    private static final float BUTTONS_ANIM_DURATION = 1.0f;
    private static final float BUTTONS_MOVE_BY_AMOUNT = 110.0f;
    private static final float STAGE_ANIM_DURATION = 1.0f;
    private static final float DIM_ALPHA = 0.7f;

    private Assets assets;
    private AssetGUI assetGUI;
    private I18NBundle i18NGameThreeBundle;
    private GameSettings prefs;
    private Image screenPauseBg;
    private Label pauseLabel;
    private ImageButton play;
    private ImageButton home;
    private ImageButton audio;
    private ImageButton reload;
    private boolean isPauseScreenVisible;

    public PauseScreen(GameFour game) {
        super(game);
        assets = Assets.getInstance();
        assetGUI = assets.getGUI();
        i18NGameThreeBundle = assets.getI18NGameFour().getI18NGameFourBundle();
        prefs = GameSettings.getInstance();
        isPauseScreenVisible = false;
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
        labelStyleBig.font = assets.getFonts().getDefaultBig();
        pauseLabel = new Label(i18NGameThreeBundle.format("pauseScreen.title"), labelStyleBig);
        stage.addActor(pauseLabel);

        // Buttons
        defineButtons();
        stage.addActor(play);
        stage.addActor(home);
        stage.addActor(audio);
        stage.addActor(reload);

        // Initially hidden
        hidePauseScreen();
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

        reload = new ImageButton(new TextureRegionDrawable(assetGUI.getReload()),
                new TextureRegionDrawable(assetGUI.getReloadPressed()));

        // Events
        play.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                hidePauseScreen();
            }
        }));
        home.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN));
        audio.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                prefs.setAudio(!audio.isChecked());
                prefs.save();
            }
        }));
        reload.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.COLOR_FADE_WHITE));
    }

    private void hidePauseScreen() {
        screenPauseBg.setVisible(false);
        pauseLabel.setVisible(false);
        play.setVisible(false);
        home.setVisible(false);
        audio.setVisible(false);
        reload.setVisible(false);
        isPauseScreenVisible = false;
    }

    public void showPauseScreen() {
        setStageAnimation();
        screenPauseBg.setVisible(true);
        pauseLabel.setVisible(true);
        play.setVisible(true);
        home.setVisible(true);
        audio.setVisible(true);
        reload.setVisible(true);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        float w = stage.getWidth(); // Same as stage.getViewport().getWorldWidth()
        float h = stage.getHeight();

        // Background
        screenPauseBg.setSize(w, h);

        // Place the title
        pauseLabel.setX((w - pauseLabel.getWidth()) / 2);
        pauseLabel.setY((h - pauseLabel.getHeight()) / 2 + TITLE_OFFSET_Y);

        // Place buttons
        play.setX((w - play.getWidth()) / 2);
        play.setY((h - play.getHeight()) / 2 - BUTTONS_OFFSET_Y);
        float x = play.getX() + play.getWidth() / 2 - audio.getWidth() / 2;
        float y = play.getY() + play.getHeight() / 2 - audio.getHeight() / 2;
        home.setPosition(x, y);
        audio.setPosition(x, y);
        reload.setPosition(x, y);

        // Disable events
        play.setTouchable(Touchable.disabled);
        home.setTouchable(Touchable.disabled);
        audio.setTouchable(Touchable.disabled);
        reload.setTouchable(Touchable.disabled);

        if (isPauseScreenVisible()) {
            setButtonsAnimation();
        }
    }

    private boolean isPauseScreenVisible() {
        return isPauseScreenVisible;
    }

    @Override
    public void update(float deltaTime) {
        stage.act();
    }

    @Override
    public void render() {
        stage.draw();
    }

    private void setStageAnimation() {
        final Group group = stage.getRoot();
        group.setY(GameFour.APPLICATION_HEIGHT);
        group.setTouchable(Touchable.disabled);
        group.addAction(sequence(moveBy(0, -GameFour.APPLICATION_HEIGHT, STAGE_ANIM_DURATION, Interpolation.bounceOut), run(new Runnable() {
            public void run () {
                setButtonsAnimation();
                group.setTouchable(Touchable.enabled);
            }
        })));
    }

    private void setButtonsAnimation() {
        // Set actions
        play.clearActions();
        home.clearActions();
        audio.clearActions();

        play.addAction(sequence(moveBy(0, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION, Interpolation.smooth),
                run(new Runnable() {
                    public void run () {
                        // Enable events
                        play.setTouchable(Touchable.enabled);
                        home.setTouchable(Touchable.enabled);
                        audio.setTouchable(Touchable.enabled);
                        reload.setTouchable(Touchable.enabled);

                        isPauseScreenVisible = true;
                    }
                })));
        home.addAction(moveBy(BUTTONS_MOVE_BY_AMOUNT, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION));
        audio.addAction(moveBy(-BUTTONS_MOVE_BY_AMOUNT, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION, Interpolation.smooth));
    }
}
