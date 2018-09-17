package uy.com.agm.gamefour.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by AGMCORP on 17/9/2018.
 */
public class SlideTransition implements ScreenTransition {
    private static final String TAG = SlideTransition.class.getName();

    // Constants
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;

    private static final SlideTransition instance = new SlideTransition();
    private float duration;
    private int direction;
    private boolean slideOut;
    private Interpolation easing;

    public static SlideTransition init(float duration, int direction, boolean slideOut, Interpolation easing) {
        instance.duration = duration;
        instance.direction = direction;
        instance.slideOut = slideOut;
        instance.easing = easing;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {
        float w = currScreen.getWidth();
        float h = currScreen.getHeight();
        float x = 0;
        float y = 0;
        if (easing != null) {
            alpha = easing.apply(alpha);
        }

        // Calculate position offset
        switch (direction) {
            case LEFT:
                x = -w * alpha;
                if (!slideOut) {
                    x += w;
                }
                break;
            case RIGHT:
                x = w * alpha;
                if (!slideOut) {
                    x -= w;
                }
                break;
            case UP:
                y = h * alpha;
                if (!slideOut) {
                    y -= h;
                }
                break;
            case DOWN:
                y = -h * alpha;
                if (!slideOut) {
                    y += h;
                }
                break;
        }

        // Drawing order depends on slide type ('in' or 'out')
        Texture texBottom = slideOut ? nextScreen : currScreen;
        Texture texTop = slideOut ? currScreen : nextScreen;

        // Finally, draw both screens
        batch.begin();
        batch.draw(texBottom, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, currScreen.getWidth(), currScreen.getHeight(), false, true);
        batch.draw(texTop, x, y, 0, 0, w, h, 1, 1, 0, 0, 0, nextScreen.getWidth(), nextScreen.getHeight(), false, true);
        batch.end();
    }
}
