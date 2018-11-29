package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.fonts.AssetFonts;
import uy.com.agm.gamefour.assets.gui.AssetGUI;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameSettings;
import uy.com.agm.gamefour.screens.ListenerHelper;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.tools.AudioManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGMCORP on 10/14/2018.
 */

public class InfoScreen extends GUIOverlayAbstractScreen {
    private static final String TAG = InfoScreen.class.getName();

    private static final float ANIMATION_DURATION = 1.0f;
    private static final float BUTTON_WIDTH = 180.0f;
    private static final int MAX_TITLE_KEYS = 15;

    private Assets assets;
    private I18NBundle i18NGameThreeBundle;
    private AssetFonts assetFonts;
    private AssetGUI assetGUI;
    private Array<String> titleKeys;
    private Table gameOverTable;
    private Table gameControllersHelpTable;
    private ImageButton pause;
    private Label gameOverLabel;
    private Label scoreLabel;
    private Label highScoreLabel;

    public InfoScreen(GameFour game) {
        super(game);

        assets = Assets.getInstance();
        i18NGameThreeBundle = assets.getI18NGameFour().getI18NGameFourBundle();
        assetFonts = assets.getFonts();
        assetGUI = assets.getGUI();
        titleKeys = new Array<String>();
        for(int i = 0; i < MAX_TITLE_KEYS; i++) {
            titleKeys.add(i18NGameThreeBundle.format("infoScreen.title" + i));
        }
    }

    @Override
    public void build() {
        gameOverTable = getGameOverTable();
        stage.addActor(gameOverTable);

        gameControllersHelpTable = getGameControllersHelpTable();
        stage.addActor(gameControllersHelpTable);

        // Pause button
        pause = new ImageButton(new TextureRegionDrawable(assetGUI.getPause()),
                new TextureRegionDrawable(assetGUI.getPausePressed()));
        pause.setPosition(0, stage.getHeight() - pause.getHeight());
        pause.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                ((PlayScreen) game.getCurrentScreen()).setGameStatePaused();
            }
        }));
        stage.addActor(pause);
    }

    private Table getGameOverTable() {
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = assetFonts.getBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = assetFonts.getNormal();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = assetFonts.getSmall();

        gameOverLabel = new Label("GAME OVER", labelStyleBig);
        scoreLabel = new Label("SCORE", labelStyleNormal);
        highScoreLabel = new Label("HIGH_SCORE", labelStyleSmall);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setFillParent(true); // todo
        table.add(gameOverLabel).row();
        table.add(getGameOverButtonsTable()).row();
        table.add(scoreLabel).row();
        table.add(highScoreLabel);
        table.setVisible(false);

        return table;
    }

    private Table getGameOverButtonsTable() {
        AssetGUI assetGUI = Assets.getInstance().getGUI();

        ImageButton reload = new ImageButton(new TextureRegionDrawable(assetGUI.getBigReload()),
                new TextureRegionDrawable(assetGUI.getBigReloadPressed()));

        ImageButton home = new ImageButton(new TextureRegionDrawable(assetGUI.getBigHome()),
                new TextureRegionDrawable(assetGUI.getBigHomePressed()));

        reload.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.COLOR_FADE_WHITE, false));
        home.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN));

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.add(reload).width(BUTTON_WIDTH);
        table.add(home).width(BUTTON_WIDTH);
        return table;
    }

    private Table getGameControllersHelpTable() {
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setFillParent(true); // todo
        table.add(new Image(new Texture(Gdx.files.internal("basura/MOCKUP.PNG")))).row();
        table.add(getGameControllersHelpButtonsTable());
        table.setVisible(false);

        return table;
    }

    private Table getGameControllersHelpButtonsTable() {
        AssetGUI assetGUI = Assets.getInstance().getGUI();

        ImageButton reload = new ImageButton(new TextureRegionDrawable(assetGUI.getBigReload()),
                new TextureRegionDrawable(assetGUI.getBigReloadPressed()));
        reload.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                setStageAnimation(false);
            }
        }));
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.add(reload).width(BUTTON_WIDTH);
        return table;
    }

    @Override
    public void update(float deltaTime) {
        Group group = stage.getRoot();
        if (!group.isTouchable()) {
            group.setTouchable(Touchable.enabled);
        }
        stage.act();
    }

    @Override
    public void render() {
        stage.draw();
    }

    public void disableEvents() {
        Group group = stage.getRoot();
        if (group.isTouchable()) {
            group.setTouchable(Touchable.disabled);
        }
    }

    private void setStageAnimation(boolean down) {
        float h = stage.getHeight();
        final Group group = stage.getRoot();
        group.setY(h);
        group.setTouchable(Touchable.disabled);
        group.clearActions();
        group.addAction(sequence(moveBy(0, down ? -h : h, ANIMATION_DURATION, Interpolation.bounceOut), run(new Runnable() {
            public void run () {
                group.setTouchable(Touchable.enabled);
            }
        })));
    }

    public void showGameOver() {
        GameSettings prefs = GameSettings.getInstance();
        Hud hud = ((PlayScreen) game.getCurrentScreen()).getHud();
        int currentScore = hud.getScore();
        int bestScore = prefs.getHighScore();
        if (currentScore > bestScore) {
            bestScore = currentScore;
            prefs.setHighScore(bestScore);
            prefs.save();

            // Leaderboards
            playServices.submitScore(bestScore);

            // Audio effect
            AudioManager.getInstance().playSound(assets.getSounds().getNewAchievement());
        }

        if (hud.isScoreAboveAverage()) {
            gameOverLabel.setText(titleKeys.get(MathUtils.random(0, titleKeys.size - 1)));
        } else {
            gameOverLabel.setText(i18NGameThreeBundle.format("infoScreen.titleDefault", currentScore));
        }
        scoreLabel.setText(i18NGameThreeBundle.format("infoScreen.score", currentScore));
        highScoreLabel.setText(i18NGameThreeBundle.format("infoScreen.highScore", bestScore));
        gameOverTable.setVisible(true);
        gameControllersHelpTable.setVisible(false);
        pause.setVisible(false);
        setStageAnimation(true);
    }

    public void showGameControllersHelp() {
        gameOverTable.setVisible(false);
        gameControllersHelpTable.setVisible(true);
        pause.setVisible(false);
        setStageAnimation(true);
    }
}
