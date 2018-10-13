package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.gui.widget.PowerBar;

/**
 * Created by AGM on 10/12/2018.
 */

public class PowerBarScreen extends GUIAbstractScreen {
    private static final String TAG = PowerBarScreen.class.getName();

    private static final float PAD = 50.0f;
    private static final float SWING_DELAY = 0.0165f;

    private PowerBar powerBar;
    private boolean swing;
    private float swingTime;

    public PowerBarScreen(GameFour game) {
        super(game);
        powerBar = new PowerBar();
        swing = false;
        swingTime = 0;
    }

    @Override
    public void show() {
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.bottom();
        table.setFillParent(true);
        table.add(powerBar);
        table.padBottom(PAD);
        stage.addActor(table);
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
