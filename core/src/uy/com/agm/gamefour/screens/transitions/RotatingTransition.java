package uy.com.agm.gamefour.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class RotatingTransition implements ScreenTransition {
    private static final String TAG = RotatingTransition.class.getName();

    private static final RotatingTransition instance = new RotatingTransition();
    private float duration;
    private Interpolation interpolation;
    private float angle;
    private TransitionScaling scaling;

    public static RotatingTransition init(float duration, Interpolation interpolation, float angle, TransitionScaling scaling) {
        instance.duration = duration;
        instance.interpolation = interpolation;
        instance.angle = angle;
        instance.scaling = scaling;
        return instance;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currentScreenTexture, Texture nextScreenTexture, float percent) {
        float width = currentScreenTexture.getWidth();
        float height = currentScreenTexture.getHeight();
        float x = 0;
        float y = 0;

        float scaleFactor;

        switch (scaling) {
            case IN:
                scaleFactor = percent;
                break;
            case OUT:
                scaleFactor = 1.0f - percent;
                break;
            case NONE:
            default:
                scaleFactor = 1.0f;
                break;
        }

        float rotation = 1;
        if (interpolation != null) {
            rotation = interpolation.apply(percent);
        }

        batch.begin();
        batch.draw(currentScreenTexture, 0, 0, width / 2, height / 2, width, height, 1, 1, 0, 0, 0, (int) width, (int) height, false,
                true);
        batch.draw(nextScreenTexture, 0, 0, width / 2, height / 2, nextScreenTexture.getWidth(), nextScreenTexture.getHeight(),
                scaleFactor, scaleFactor, rotation * angle, 0, 0, nextScreenTexture.getWidth(), nextScreenTexture.getHeight(), false,
                true);
        batch.end();

    }

    public enum TransitionScaling {
        NONE, IN, OUT
    }
}
