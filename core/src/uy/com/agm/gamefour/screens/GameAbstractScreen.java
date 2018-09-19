package uy.com.agm.gamefour.screens;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GameAbstractScreen  extends AbstractScreen {
    private static final String TAG = GameAbstractScreen.class.getName();

    public GameAbstractScreen(GameFour game) {
        super(game);
    }

    @Override
    public void dispose () {
        hide();
    }
}
