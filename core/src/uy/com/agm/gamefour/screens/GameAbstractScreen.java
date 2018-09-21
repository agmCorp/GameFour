package uy.com.agm.gamefour.screens;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GameAbstractScreen extends AbstractScreen {
    private static final String TAG = GameAbstractScreen.class.getName();

    // Game state
    protected enum GameScreenState {
        PAUSED, RUNNING, STOPPED
    }
    protected GameScreenState gameScreenState;


    public GameAbstractScreen(GameFour game) {
        super(game);
        gameScreenState = GameScreenState.RUNNING;
    }

    public boolean isGameScreenStateRunning() {
        return gameScreenState == GameScreenState.RUNNING;
    }

    @Override
    public void stop() {
        gameScreenState = GameScreenState.STOPPED;
    }

    @Override
    public void pause() {
        gameScreenState = GameScreenState.PAUSED;
    }

    @Override
    public void resume() {
        gameScreenState = GameScreenState.RUNNING;
    }

    @Override
    public void dispose () {
        hide();
    }
}
