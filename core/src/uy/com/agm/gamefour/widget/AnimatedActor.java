package uy.com.agm.gamefour.widget;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AnimatedActor extends Actor {
    private Animation animation;
    private TextureRegion textureRegion;
    private float stateTime;

    public AnimatedActor(Animation animation) {
        this.animation = animation;
        textureRegion = (TextureRegion) animation.getKeyFrame(0);
    }

    @Override
    public void act(float delta) {
        stateTime += delta;
        textureRegion = (TextureRegion) animation.getKeyFrame(stateTime);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY());
    }

    public void setAnimation(Animation animation) {
        stateTime = 0;
        this.animation = animation;
    }
}
