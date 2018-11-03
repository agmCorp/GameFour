package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.gui.AssetGUI;
import uy.com.agm.gamefour.assets.sprites.AssetSprites;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameSettings;
import uy.com.agm.gamefour.screens.ListenerHelper;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;
import uy.com.agm.gamefour.screens.gui.widget.AnimatedActor;
import uy.com.agm.gamefour.tools.AudioManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class MainMenuScreen extends GUIAbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    private static final float JUMPER_SCALE = 1.3f;
    private static final float ROCKET_BACKGROUND_SCALE = 0.2f;
    private static final float TITLE_OFFSET_Y = 700.0f;
    private static final float ROCKET_BG_OFFSET_X = 200.0f;
    private static final float ROCKET_BG_OFFSET_Y = 50.0f;
    private static final float CLOUD_OFFSET_X = 40.0f;
    private static final float CLOUD_OFFSET_Y = 20.0f;
    private static final float JUMPER_OFFSET_Y = 200.0f;
    private static final float ROCKET_FG_OFFSET_Y = 40.0f;
    private static final float BUTTONS_OFFSET_Y = 350.0f;
    private static final float BUTTONS_ANIM_DURATION = 1.0f;
    private static final float BUTTONS_MOVE_BY_AMOUNT = 110.0f;

    private Assets assets;
    private AssetGUI assetGUI;
    private AssetSprites assetSprites;
    private I18NBundle i18NGameThreeBundle;
    private GameSettings prefs;
    private Image menuBackground;
    private Label gameTitle;
    private Image littleCloud;
    private Image trail;
    private AnimatedActor jumper;
    private AnimatedActor rocketBackground;
    private AnimatedActor rocketForeground;
    private ImageButton play;
    private ImageButton info;
    private ImageButton audio;
    private ImageButton exit;

    public MainMenuScreen(GameFour game) {
        super(game);

        assets = Assets.getInstance();
        assetGUI = assets.getGUI();
        assetSprites = assets.getSprites();
        i18NGameThreeBundle = assets.getI18NGameFour().getI18NGameFourBundle();
        prefs = GameSettings.getInstance();

        // Play menu music
        AudioManager.getInstance().playMusic(Assets.getInstance().getMusic().getSongMainMenu());
    }

    @Override
    protected void updateLogic(float deltaTime) {
        stage.act();
    }

    @Override
    protected void renderLogic() {
        stage.draw();
    }

    @Override
    protected void goBack() {
        // Nothing to do here
    }

    @Override
    public void show() {
        hideBannerAd();

        // Background
        menuBackground = new Image(assetGUI.getMenuBackground());
        stage.addActor(menuBackground);

        // Title
        Label.LabelStyle labelStyleGameTitle = new Label.LabelStyle();
        labelStyleGameTitle.font = assets.getFonts().getGameTitle();
        gameTitle = new Label(i18NGameThreeBundle.format("mainMenuScreen.gameTitle"), labelStyleGameTitle);
        stage.addActor(gameTitle);

        // Distant rocket
        rocketBackground = new AnimatedActor(assetGUI.getRocket().getRocketAnimation(), true, ROCKET_BACKGROUND_SCALE);
        stage.addActor(rocketBackground);

        // Cloud
        littleCloud = new Image(assetGUI.getLittleCloud());
        stage.addActor(littleCloud);

        // Trail
        trail = new Image(assetGUI.getTrail());
        stage.addActor(trail);

        // Main character
        jumper = new AnimatedActor(assetSprites.getJumper().getJumperJumpAnimation(), false, JUMPER_SCALE, Color.SALMON);
        stage.addActor(jumper);

        // Near rocket
        rocketForeground = new AnimatedActor(assetGUI.getRocket().getRocketAnimation(), true);
        stage.addActor(rocketForeground);

        // Buttons
        defineButtons();
        stage.addActor(audio);
        stage.addActor(info);
        stage.addActor(exit);
        stage.addActor(play);
    }

    private void defineButtons() {
        play = new ImageButton(new TextureRegionDrawable(assetGUI.getPlay()),
                new TextureRegionDrawable(assetGUI.getPlayPressed()));

        info = new ImageButton(new TextureRegionDrawable(assetGUI.getInfo()),
                new TextureRegionDrawable(assetGUI.getInfoPressed()));

        audio = new ImageButton(new TextureRegionDrawable(assetGUI.getAudio()),
                new TextureRegionDrawable(assetGUI.getAudioPressed()),
                new TextureRegionDrawable(assetGUI.getAudioChecked()));
        audio.setChecked(!prefs.isAudio());

        exit = new ImageButton(new TextureRegionDrawable(assetGUI.getExit()),
                new TextureRegionDrawable(assetGUI.getExitPressed()));

        // Events
        play.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.COLOR_FADE_WHITE));
        info.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.CREDITS, ScreenTransitionEnum.SLICE_UP_DOWN_10));
        audio.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                prefs.setAudio(!audio.isChecked());
                prefs.save();
                AudioManager.getInstance().onSettingsUpdated();
            }
        }));
        exit.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                Gdx.app.exit();
            }
        }));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        float w = stage.getWidth(); // Same as stage.getViewport().getWorldWidth()
        float h = stage.getHeight();

        // Place the menuBackground in the middle of the screen
        menuBackground.setX((w - menuBackground.getWidth()) / 2);
        menuBackground.setY((h - menuBackground.getHeight()) / 2);

        // Place the title
        gameTitle.setPosition((w - gameTitle.getWidth()) / 2, TITLE_OFFSET_Y);

        // Place a distant rocket
        rocketBackground.setX((w - rocketBackground.getWidth()) / 2 - ROCKET_BG_OFFSET_X);
        rocketBackground.setY((h - rocketBackground.getHeight()) / 2 + ROCKET_BG_OFFSET_Y);

        // Place a little cloud on the rocket
        littleCloud.setX(rocketBackground.getX() + CLOUD_OFFSET_X);
        littleCloud.setY(rocketBackground.getY() + CLOUD_OFFSET_Y);

        // Place a trail
        trail.setX(littleCloud.getX());
        trail.setY(littleCloud.getY());

        // Place the main character
        jumper.setX((w - jumper.getWidth()) / 2);
        jumper.setY((h - jumper.getHeight()) / 2 + JUMPER_OFFSET_Y);

        // Place a rocket in the foreground
        rocketForeground.setX((w - rocketForeground.getWidth()) / 2);
        rocketForeground.setY((h - rocketForeground.getHeight()) / 2 - ROCKET_FG_OFFSET_Y);

        // Place buttons
        play.setX((w - play.getWidth()) / 2);
        play.setY((h - play.getHeight()) / 2 - BUTTONS_OFFSET_Y);
        float x = play.getX() + play.getWidth() / 2 - audio.getWidth() / 2;
        float y = play.getY() + play.getHeight() / 2 - audio.getHeight() / 2;
        info.setPosition(x, y);
        audio.setPosition(x, y);
        exit.setPosition(x, y);

        // Buttons Animations
        setButtonsAnimation();
    }

    private void setButtonsAnimation() {
        // Disable events
        play.setTouchable(Touchable.disabled);
        info.setTouchable(Touchable.disabled);
        audio.setTouchable(Touchable.disabled);
        exit.setTouchable(Touchable.disabled);

        // Set actions
        play.clearActions();
        info.clearActions();
        audio.clearActions();

        play.addAction(sequence(moveBy(0, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION, Interpolation.smooth),
                run(new Runnable() {
                    public void run () {
                        // Enable events
                        play.setTouchable(Touchable.enabled);
                        info.setTouchable(Touchable.enabled);
                        audio.setTouchable(Touchable.enabled);
                        exit.setTouchable(Touchable.enabled);
                    }
                })));
        info.addAction(moveBy(BUTTONS_MOVE_BY_AMOUNT, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION));
        audio.addAction(moveBy(-BUTTONS_MOVE_BY_AMOUNT, BUTTONS_MOVE_BY_AMOUNT, BUTTONS_ANIM_DURATION, Interpolation.smooth));
    }
}
