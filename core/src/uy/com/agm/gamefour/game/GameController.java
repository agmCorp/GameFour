package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamefour.screens.gui.Hud;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameController implements GestureDetector.GestureListener, InputProcessor {
    private static final String TAG = GameController.class.getName();

    private GameWorld gameWorld;
    private PlayScreen playScreen;
    private Jumper jumper;
    private Hud hud;

    public GameController(GameWorld gameWorld, PlayScreen playScreen) {
        this.gameWorld = gameWorld;
        this.playScreen = playScreen;
        jumper = gameWorld.getJumper();
        hud = playScreen.getHud();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        startJump();
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        shoot();
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
        cheatMode();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                startJump();
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                if (jumper.isJumping()) {
                    shoot();
                } else {
                    performJump();
                }
                break;
            case Input.Keys.P:
                cheatMode();
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        performJump();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private void startJump() {
        if (playScreen.isPlayScreenStateRunning()) {
            if (jumper.isIdle()) {
                hud.startSwing();
            }
        }
    }

    private void performJump() {
        if (playScreen.isPlayScreenStateRunning()) {
            if (jumper.isIdle() && hud.isSwinging()) {
                hud.stopSwing();
                jumper.jump(hud.getPowerBarValue());
            }
        }
    }

    private void cheatMode() {
        if (playScreen.isPlayScreenStateRunning()) {
            if (DebugConstants.POWER_JUMP_ENABLED && !jumper.isDead()) {
                hud.stopSwing();
                jumper.powerJump();
            }
        }
    }

    private void shoot() {
        if (playScreen.isPlayScreenStateRunning()) {
            if (jumper.isJumping()) {
                jumper.shoot();
            }
        }
    }
}

