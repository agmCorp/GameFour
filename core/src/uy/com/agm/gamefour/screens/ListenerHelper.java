package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.concurrent.Callable;

/**
 * Created by AGM on 10/14/2018.
 */

public class ListenerHelper {
    private static final String TAG = ListenerHelper.class.getName();

    public static InputListener screenNavigationListener(final ScreenEnum screenEnum, final ScreenTransitionEnum screenTransitionEnum, final Object... params) {
        return
                new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        // todo Audio FX poner click

                        ScreenManager.getInstance().showScreen(screenEnum, screenTransitionEnum, params);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                };
    }

    public static InputListener functionCallableListener(final Callable<Void> function) {
        return
                new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        // todo Audio FX poner click

                        try {
                            function.call();
                        } catch (Exception e) {
                            Gdx.app.error(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                };
    }
}
