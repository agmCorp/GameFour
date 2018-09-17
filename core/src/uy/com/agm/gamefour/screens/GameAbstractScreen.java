package uy.com.agm.gamefour.screens;

import uy.com.agm.gamefour.game.GameFour;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GameAbstractScreen  extends AbstractScreen {
    private static final String TAG = GameAbstractScreen.class.getName();

    // Scale (Pixels Per Meter)
    protected static final float PPM = 100;

    // Visible game world width (meters)
    protected static final float VIEWPORT_GAME_WIDTH = APPLICATION_WIDTH / PPM;

    // Visible game world height (meters)
    protected static final float VIEWPORT_GAME_HEIGHT = APPLICATION_HEIGHT / PPM;

    public GameAbstractScreen(GameFour game) {
        super(game);
    }
}
