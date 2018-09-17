package uy.com.agm.gamefour.screens.transitions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class ColorFadeTransition implements ScreenTransition {
    private static final String TAG = ColorFadeTransition.class.getName();

    private static final ColorFadeTransition instance = new ColorFadeTransition();
    private float duration;
    private Color color;
    private Interpolation interpolation;
    private Texture texture;

    public static ColorFadeTransition init(float duration, Color color, Interpolation interpolation) {
        instance.duration = duration;
        instance.color = new Color(Color.WHITE);
        instance.interpolation = interpolation;

        instance.texture = new Texture(1, 1, Format.RGBA8888);
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, 1, 1);
        instance.texture.draw(pixmap, 0, 0);

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

        if (interpolation != null) {
            percent = interpolation.apply(percent);
        }

        batch.begin();

        float fade = percent * 2;
        if (fade > 1.0f) {
            fade = 1.0f - (percent * 2 - 1.0f);
            color.a = 1.0f - fade;
            batch.setColor(color);

            batch.draw(nextScreenTexture, 0, 0, width / 2, height / 2, nextScreenTexture.getWidth(), nextScreenTexture.getHeight(),
                    1, 1, 0, 0, 0, nextScreenTexture.getWidth(), nextScreenTexture.getHeight(), false, true);
        } else {
            color.a = 1.0f - fade;
            batch.setColor(color);

            batch.draw(currentScreenTexture, 0, 0, width / 2, height / 2, width, height, 1, 1, 0, 0, 0, (int) width, (int) height,
                    false, true);
        }

        color.a = fade;

        batch.setColor(color);
        batch.draw(texture, 0, 0, width, height);
        batch.end();
        batch.setColor(Color.WHITE);
    }
}
