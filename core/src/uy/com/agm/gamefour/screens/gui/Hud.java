package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.fonts.AssetFonts;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.gui.widget.PowerBar;

/**
 * Created by AGMCORP on 10/12/2018.
 */

public class Hud extends GUIOverlayAbstractScreen {
    private static final String TAG = Hud.class.getName();

    private static final float PAD_TOP = 50.0f;
    private static final float PAD_BOTTOM = 80.0f;
    private static final float SWING_DELAY = 0.02f;
    private static final int POWER_BAR_WIDTH = 250;
    private static final int POWER_BAR_HEIGHT = 15;
    private static final int AVERAGE_SCORE = 8;

    private I18NBundle i18NGameThreeBundle;
    private AssetFonts assetFonts;
    private PowerBar powerBar;
    private boolean swing;
    private float swingTime;
    private int score;
    private Label scoreLabel;
    private int fps;
    private Label fpsLabel;
    private Table mainTable;

    public Hud(GameFour game) {
        super(game);

        i18NGameThreeBundle = Assets.getInstance().getI18NGameFour().getI18NGameFourBundle();
        assetFonts = Assets.getInstance().getFonts();
        powerBar = new PowerBar(POWER_BAR_WIDTH, POWER_BAR_HEIGHT);
        swing = false;
        swingTime = 0;
        score = 0;
        fps = 0;
    }

    @Override
    public void build() {
        mainTable = new Table();
        mainTable.setDebug(DebugConstants.DEBUG_LINES);
        mainTable.center();
        mainTable.setFillParent(true);
        mainTable.add(getTopTable()).height(stage.getHeight() / 2).row();
        mainTable.add(getBottomTable()).height(stage.getHeight() / 2);
        stage.addActor(mainTable);
    }

    private Table getBottomTable() {
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.bottom();
        if (DebugConstants.SHOW_FPS) {
            table.add(getFPSTable()).row();
        }
        table.add(powerBar).size(POWER_BAR_WIDTH, POWER_BAR_HEIGHT);
        table.padBottom(PAD_BOTTOM);
        return table;
    }

    private Table getTopTable() {
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = assetFonts.getBig();
        scoreLabel = new Label(String.valueOf(score), labelStyleBig);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.top();
        table.add(scoreLabel);
        table.padTop(PAD_TOP);
        return table;
    }

    private Table getFPSTable() {
        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = assetFonts.getSmall();
        Label fpsTitle = new Label(i18NGameThreeBundle.format("hud.FPS"), labelStyleSmall);
        fpsLabel = new Label(String.valueOf(fps), labelStyleSmall);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.add(fpsTitle).row();
        table.add(fpsLabel);
        return table;
    }

    private void updateFPS() {
        if (DebugConstants.SHOW_FPS) {
            fps = Gdx.graphics.getFramesPerSecond();
            fpsLabel.setText(String.valueOf(fps));
        }
    }

    @Override
    public void update(float deltaTime) {
        if (swing || !powerBar.isEmpty()) {
            swingTime += deltaTime;
            if (swingTime > SWING_DELAY) {
                swingTime = 0;
                if (swing) {
                    if (powerBar.isFull()) {
                        powerBar.reset();
                    } else {
                        powerBar.increase();
                    }
                } else {
                    if (!powerBar.isEmpty()) {
                        powerBar.decrease();
                    }
                }
            }
        }
        stage.act();
        updateFPS();
    }

    public void addScore(int value) {
        score += value;
        scoreLabel.setText(String.valueOf(score));
    }

    public int getScore() {
        return score;
    }

    public boolean isScoreAboveAverage() {
        return score > AVERAGE_SCORE;
    }

    @Override
    public void render() {
        stage.draw();
    }

    public void startSwing() {
        swing = true;
        swingTime = 0;
    }

    public void stopSwing() {
        swing = false;
    }

    public boolean isSwinging() {
        return swing;
    }

    public float getPowerBarValue() {
        return powerBar.getValue();
    }

    public void setVisible(boolean visible) {
        mainTable.setVisible(visible);
    }
}
