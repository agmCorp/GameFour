package uy.com.agm.gamefour.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class FadeTransition implements IScreenTransition {
    private static final String TAG = FadeTransition.class.getName();

    private static final FadeTransition instance = new FadeTransition();
    private float duration;

    public static FadeTransition init(float duration) {
        instance.duration = duration;
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
        alpha = Interpolation.fade.apply(alpha);

        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(currScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, currScreen.getWidth(), currScreen.getHeight(), false, true);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(nextScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, nextScreen.getWidth(), nextScreen.getHeight(), false, true);
        batch.end();
    }
}
