package uy.com.agm.gamefour.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.AbstractScreen;

public class DesktopLauncher {
    private static final String TAG = DesktopLauncher.class.getName();

    private static final String TITLE = "Game Four";

	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = TITLE;
        config.width = AbstractScreen.APPLICATION_WIDTH;
        config.height = AbstractScreen.APPLICATION_HEIGHT;
        new LwjglApplication(new GameFour(), config);
	}
}
