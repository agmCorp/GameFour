package uy.com.agm.gamefour.game;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class DebugConstants {
    // Master variable: turns debug mode on or off
    public static final boolean TURN_ON_DEBUG = true;

    // Sets the log level to debug
    public static final boolean DEBUG_MODE = true && TURN_ON_DEBUG;

    // Debug lines around sprites and scene2d tables
    public static final boolean DEBUG_LINES = true && TURN_ON_DEBUG;

    // Debug lines around box2d bodies
    public static final boolean DEBUG_BOX2D = true && TURN_ON_DEBUG;

    // Shows/hides background image
    public static final boolean HIDE_BACKGROUND = false && TURN_ON_DEBUG;

    // Shows/hides FPS counter
    public static final boolean SHOW_FPS = true && TURN_ON_DEBUG;
}
