package uy.com.agm.gamefour.screens.gui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by AGMCORP on 12/10/2018.
 */

public class PowerBar extends ProgressBar {
    private static final String TAG = PowerBar.class.getName();

    private static final int WIDTH = 250;
    private static final int HEIGHT = 20;
    private static final float MIN = 0.0f;
    private static final float MAX = 100.0f;
    private static final float STEP = 4.0f;

    public PowerBar() {
        super(MIN, MAX, STEP, false, new ProgressBarStyle());

        ProgressBar.ProgressBarStyle progressBarStyle = getStyle();
        progressBarStyle.background = getColoredDrawable(WIDTH, HEIGHT, Color.RED);
        progressBarStyle.knob = getColoredDrawable(0, HEIGHT, Color.GREEN);
        progressBarStyle.knobBefore = getColoredDrawable(WIDTH, HEIGHT, Color.YELLOW);

        setHeight(HEIGHT);
    }

    private Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
    }

    public void decrease() {
        setValue(getValue() - STEP);
    }

    public void increase() {
        setValue(getValue() + STEP);
    }

    @Override
    public float getPrefWidth() {
        // WA: Impossible to set 'width' on an horizontal ProgressBar using size, resize or setWidth
        return (float) WIDTH;
    }

    public boolean isFull() {
        return getValue() >= MAX;
    }

    public boolean isEmpty() {
        return getValue() <= MIN;
    }

    public void reset() {
        setValue(MIN);
    }
}
