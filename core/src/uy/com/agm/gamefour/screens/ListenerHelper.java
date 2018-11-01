package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.tools.AudioManager;

/**
 * Created by AGMCORP on 10/14/2018.
 */

public class ListenerHelper {
    private static final String TAG = ListenerHelper.class.getName();

    public static InputListener screenNavigationListener(final ScreenEnum screenEnum, final ScreenTransitionEnum screenTransitionEnum, final Object... params) {
        return
                new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        ScreenManager.getInstance().showScreen(screenEnum, screenTransitionEnum, params);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                };
    }

    public static InputListener runnableListener(final Runnable runnable) {
        return
                new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getClick());
                        runnable.run();
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                };
    }
}
