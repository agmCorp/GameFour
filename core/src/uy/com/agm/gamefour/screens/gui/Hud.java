package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.screens.gui.widget.PowerBar;

/**
 * Created by AGM on 10/12/2018.
 */

public class Hud extends GUIAbstractScreen {
    private static final String TAG = Hud.class.getName();

    private static final float PAD = 50.0f;
    private static final float SWING_DELAY = 0.02f;

    private GameWorld gameWorld;
    private PowerBar powerBar;
    private boolean swing;
    private float swingTime;
    private int score;
    private Label scoreLabel;

    public Hud(GameFour game, GameWorld gameWorld) {
        super(game);
        this.gameWorld = gameWorld;
        powerBar = new PowerBar();
        swing = false;
        swingTime = 0;
        score = 0;
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setFillParent(true);
        table.add(getScoreTable()).height(GameFour.APPLICATION_HEIGHT / 2).row();
        table.add(getPowerBarTable()).height(GameFour.APPLICATION_HEIGHT / 2);
        stage.addActor(table);
    }

    private Table getPowerBarTable() {
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.bottom();
        table.add(powerBar);
        table.padBottom(PAD);
        return table;
    }

    private Table getScoreTable() {
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();
        scoreLabel = new Label(String.valueOf(score), labelStyleBig);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.top();
        table.add(scoreLabel);
        table.padTop(PAD);
        return table;
    }

    @Override
    protected void updateLogic(float deltaTime) {
        swingTime += deltaTime;
        if (swingTime > SWING_DELAY) {
            swingTime = 0;
            if (swing) {
                if (powerBar.isFull()) {
                    powerBar.reset();
                } else {
                    powerBar.increase();
                }
            }
        }
        updateScore(gameWorld.getLevel() - 1);
        stage.act();
    }

    private void updateScore(int value) {
        score = value;
        scoreLabel.setText(String.valueOf(score));
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
    protected void clearScreen() {
        // Nothing to do here
    }

    public void update(float deltaTime) {
        updateLogic(deltaTime);
    }

    public void render() {
        renderLogic();
    }

    public void setSwing(boolean swing) {
        this.swing = swing;
        swingTime = 0;
    }

    public float getValue() {
        return powerBar.getValue();
    }
}
