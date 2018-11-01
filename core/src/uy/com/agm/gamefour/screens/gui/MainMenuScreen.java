package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.gui.AssetGUI;
import uy.com.agm.gamefour.assets.sprites.AssetSprites;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameSettings;
import uy.com.agm.gamefour.screens.ListenerHelper;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;
import uy.com.agm.gamefour.screens.gui.widget.AnimatedActor;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class MainMenuScreen extends GUIAbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    private static final float PAD = 30.0f;
    private static final float BUTTON_WIDTH = 85.0f;
    private static final float PLAY_BUTTON_SIZE = 155.0f;
    private static final float JUMPER_SCALE = 1.3f;
    private static final float ROCKET_BACKGROUND_SCALE = 0.2f;
    private static final float ROCKET_BG_OFFSET_X = 200.0f;
    private static final float CLOUD_OFFSET_X = 40.0f;
    private static final float CLOUD_OFFSET_Y = 20.0f;
    private static final float JUMPER_OFFSET_Y = 150.0f;
    private static final float ROCKET_FG_OFFSET_Y = 110;

    private Assets assets;
    private AssetGUI assetGUI;
    private AssetSprites assetSprites;
    private I18NBundle i18NGameThreeBundle;
    private GameSettings prefs;
    private Image menuBackground;
    private Image littleCloud;
    private Image trail;
    private AnimatedActor jumper;
    private AnimatedActor rocketBackground;
    private AnimatedActor rocketForeground;

    public MainMenuScreen(GameFour game) {
        super(game);
        assets = Assets.getInstance();
        assetGUI = assets.getGUI();
        assetSprites = assets.getSprites();
        i18NGameThreeBundle = assets.getI18NGameFour().getI18NGameFourBundle();
        prefs = GameSettings.getInstance();
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
        // Background
        menuBackground = new Image(assetGUI.getMenuBackground());
        stage.addActor(menuBackground);

        // Distant rocket
        rocketBackground = new AnimatedActor(assetGUI.getRocket().getRocketAnimation(), ROCKET_BACKGROUND_SCALE);
        stage.addActor(rocketBackground);

        // Cloud
        littleCloud = new Image(assetGUI.getLittleCloud());
        stage.addActor(littleCloud);

        // Trail
        trail = new Image(assetGUI.getTrail());
        stage.addActor(trail);

        // Main character
        jumper = new AnimatedActor(assetSprites.getJumper().getJumperJumpAnimation(), JUMPER_SCALE, Color.SALMON);
        stage.addActor(jumper);

        // Near rocket
        rocketForeground = new AnimatedActor(assetGUI.getRocket().getRocketAnimation());
        stage.addActor(rocketForeground);

        // Main table (title and buttons)
        stage.addActor(getMainTable());
    }

    private Table getMainTable() {
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setFillParent(true);
        table.add(getTopTable()).height(GameFour.APPLICATION_HEIGHT / 2).row();
        table.add(getBottomTable()).height(GameFour.APPLICATION_HEIGHT / 2);
        return table;
    }

    private Table getTopTable() {
        Label.LabelStyle labelStyleGameTitle = new Label.LabelStyle();
        labelStyleGameTitle.font = assets.getFonts().getDefaultGameTitle();
        Label gameTitle = new Label(i18NGameThreeBundle.format("mainMenuScreen.gameTitle"), labelStyleGameTitle);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.top();
        table.add(gameTitle);
        table.padTop(PAD);
        return table;
    }

    private Table getBottomTable() {
        final ImageButton audio = new ImageButton(new TextureRegionDrawable(assetGUI.getAudio()),
                new TextureRegionDrawable(assetGUI.getAudioPressed()),
                new TextureRegionDrawable(assetGUI.getAudioChecked()));
        audio.setChecked(!prefs.isAudio());

        ImageButton play = new ImageButton(new TextureRegionDrawable(assetGUI.getPlay()),
                new TextureRegionDrawable(assetGUI.getPlayPressed()));

        ImageButton info = new ImageButton(new TextureRegionDrawable(assetGUI.getInfo()),
                new TextureRegionDrawable(assetGUI.getInfoPressed()));

        // Events
        audio.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                prefs.setAudio(!audio.isChecked());
                prefs.save();
            }
        }));
        play.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.COLOR_FADE_WHITE));
        info.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.CREDITS, ScreenTransitionEnum.SLICE_UP_DOWN_10));

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.bottom();
        table.add(audio).width(BUTTON_WIDTH);
        table.add(play).size(PLAY_BUTTON_SIZE);
        table.add(info).width(BUTTON_WIDTH);
        table.padBottom(PAD);
        return table;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        float w = stage.getWidth(); // Same as stage.getViewport().getWorldWidth()
        float h = stage.getHeight();

        // Place the menuBackground in the middle of the screen
        menuBackground.setX((w - menuBackground.getWidth()) / 2);
        menuBackground.setY((h - menuBackground.getHeight()) / 2);

        // Place a distant rocket
        rocketBackground.setX((w - rocketBackground.getWidth()) / 2 - ROCKET_BG_OFFSET_X);
        rocketBackground.setY((h - rocketBackground.getHeight()) / 2);

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
    }
}
