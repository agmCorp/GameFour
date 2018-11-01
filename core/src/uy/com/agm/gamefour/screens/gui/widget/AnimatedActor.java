package uy.com.agm.gamefour.screens.gui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AnimatedActor extends Actor {
    private static final String TAG = AnimatedActor.class.getName();

    private Animation animation;
    private TextureRegion textureRegion;
    private float stateTime;
    private Color tint;

    public AnimatedActor(Animation animation) {
        init(animation, 1.0f, Color.WHITE);
    }

    public AnimatedActor(Animation animation, float scale) {
        init(animation, scale, Color.WHITE);
    }

    public AnimatedActor(Animation animation, float scale, Color tint) {
        init(animation, scale, tint);
    }

    private void init(Animation animation, float scale, Color tint) {
        this.animation = animation;
        textureRegion = (TextureRegion) animation.getKeyFrame(0);
        setSize(textureRegion.getRegionWidth() * scale, textureRegion.getRegionHeight() * scale);
        stateTime = 0;
        this.tint = tint;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        textureRegion = (TextureRegion) animation.getKeyFrame(stateTime);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(tint);
        batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());
    }
}
