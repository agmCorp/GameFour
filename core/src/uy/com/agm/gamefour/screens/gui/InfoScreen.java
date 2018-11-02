package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.gui.AssetGUI;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameSettings;
import uy.com.agm.gamefour.screens.ListenerHelper;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;
import uy.com.agm.gamefour.screens.play.PlayScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGMCORP on 10/14/2018.
 */

public class InfoScreen extends GUIOverlayAbstractScreen {
    private static final String TAG = InfoScreen.class.getName();

    private static final float ANIMATION_DURATION = 1.0f;
    private static final float BUTTON_WIDTH = 85.0f;

    I18NBundle i18NGameThreeBundle;
    private Table mainTable;
    private Label scoreLabel;
    private Label highScoreLabel;

    public InfoScreen(GameFour game) {
        super(game);
    }

    @Override
    public void build() {
        i18NGameThreeBundle = Assets.getInstance().getI18NGameFour().getI18NGameFourBundle();

        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        Label gameOverLabel = new Label(i18NGameThreeBundle.format("infoScreen.gameOver"), labelStyleBig);
        scoreLabel = new Label("SCORE", labelStyleNormal);
        highScoreLabel = new Label("HIGH_SCORE", labelStyleSmall);

        mainTable = new Table();
        mainTable.setDebug(DebugConstants.DEBUG_LINES);
        mainTable.center();
        mainTable.setFillParent(true);
        mainTable.add(gameOverLabel).row();
        mainTable.add(getButtonsTable()).row();
        mainTable.add(scoreLabel).row();
        mainTable.add(highScoreLabel);
        mainTable.setVisible(false);
        stage.addActor(mainTable);


        // // TODO: 11/2/2018
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(Assets.getInstance().getGUI().getHome()),
                new TextureRegionDrawable(Assets.getInstance().getGUI().getHomePressed()));
        pauseButton.setY(500);
        pauseButton.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                ((PlayScreen) game.getCurrentScreen()).setGameStatePaused();
            }
        }));

        stage.addActor(pauseButton);

    }

    private Table getButtonsTable() {
        AssetGUI assetGUI = Assets.getInstance().getGUI();

        ImageButton reload = new ImageButton(new TextureRegionDrawable(assetGUI.getReload()),
                new TextureRegionDrawable(assetGUI.getReloadPressed()));

        ImageButton home = new ImageButton(new TextureRegionDrawable(assetGUI.getHome()),
                new TextureRegionDrawable(assetGUI.getHomePressed()));

        reload.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.COLOR_FADE_WHITE));
        home.addListener(ListenerHelper.screenNavigationListener(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_DOWN));

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.add(reload).width(BUTTON_WIDTH);
        table.add(home).width(BUTTON_WIDTH);
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

    private void setStageAnimation() {
        float h = stage.getHeight();
        final Group group = stage.getRoot();
        group.setY(h);
        group.setTouchable(Touchable.disabled);
        group.clearActions();
        group.addAction(sequence(moveBy(0, -h, ANIMATION_DURATION, Interpolation.bounceOut), run(new Runnable() {
            public void run () {
                group.setTouchable(Touchable.enabled);
            }
        })));
    }

    public void showGameOver() {
        GameSettings prefs = GameSettings.getInstance();
        int currentScore = ((PlayScreen) game.getCurrentScreen()).getHud().getScore();
        int bestScore = prefs.getHighScore();
        if (currentScore > bestScore) {
            bestScore = currentScore;
            prefs.setHighScore(bestScore);
            prefs.save();
        }
        showScores(currentScore, bestScore);
        setStageAnimation();
        mainTable.setVisible(true);
    }

    private void showScores(int score, int highScore) {
        scoreLabel.setText(i18NGameThreeBundle.format("infoScreen.score", score));
        highScoreLabel.setText(i18NGameThreeBundle.format("infoScreen.highScore", highScore));
    }
}
