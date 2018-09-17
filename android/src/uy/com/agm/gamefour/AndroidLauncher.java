package uy.com.agm.gamefour;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import uy.com.agm.gamefour.game.GameFour;

public class AndroidLauncher extends AndroidApplication {
	private static final String TAG = AndroidLauncher.class.getName();

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// We want to conserve battery
		config.useAccelerometer = false;
		config.useCompass = false;

		// Keep the screen on and hide status bar
		config.useWakelock = true;
		config.hideStatusBar = true;

		initialize(new GameFour(), config);
	}
}
