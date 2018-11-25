package uy.com.agm.gamefour.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uy.com.agm.gamefour.admob.DummyAdsController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.playservices.DummyPlayServices;

public class DesktopLauncher {
    private static final String TAG = DesktopLauncher.class.getName();

	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = GameFour.TITLE;
        config.width = GameFour.APPLICATION_WIDTH;
        config.height = GameFour.APPLICATION_HEIGHT;
        new LwjglApplication(new GameFour(new DummyAdsController(), new DummyPlayServices()), config);
	}
}
