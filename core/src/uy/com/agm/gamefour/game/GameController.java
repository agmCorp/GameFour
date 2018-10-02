package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamefour.screens.play.PlayScreen;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameController implements GestureDetector.GestureListener, InputProcessor {
    private static final String TAG = GameController.class.getName();

    private GameWorld gameWorld;
    private PlayScreen playScreen;

    public GameController(GameWorld gameWorld, PlayScreen playScreen) {
        this.gameWorld = gameWorld;
        this.playScreen = playScreen;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (playScreen.isPlayScreenStateRunning()) {
            Gdx.app.debug(TAG, "**** Touch down");
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
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
    }

    @Override
    public boolean keyDown(int keycode) {
        if (playScreen.isPlayScreenStateRunning()) {
            switch (keycode) {
                case Input.Keys.SPACE:
                    Gdx.app.debug(TAG, "**** Space down");
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (playScreen.isPlayScreenStateRunning()) {
            switch (keycode) {
                case Input.Keys.SPACE:
                    Gdx.app.debug(TAG, "**** Space up");
                    break;
            }
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
        if (playScreen.isPlayScreenStateRunning()) {
            Gdx.app.debug(TAG, "**** Touch up");
            gameWorld.getJumper().jump();
        }
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
}

