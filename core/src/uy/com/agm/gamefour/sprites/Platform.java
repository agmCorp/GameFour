package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetPlatformA;

/**
 * Created by AGMCORP on 26/9/2018.
 */

public class Platform extends AbstractGameObject {
    private static final String TAG = Platform.class.getName();

    private TextureRegion platformStand;
    private Animation platformAnimation;
    private float platformStateTime;
    private Vector2 velocity;


    public Platform(float x, float y) {
        AssetPlatformA assetPlatformA = Assets.getInstance().getSprites().getPlatformA();
        platformStand = assetPlatformA.getPlatformAStand();
        platformAnimation = assetPlatformA.getPlatformAAnimation();

        // Sets initial values for location, width and height and initial frame as platformStand.
        setBounds(x, y, platformStand.getRegionWidth() * AssetPlatformA.SCALE, platformStand.getRegionHeight() * AssetPlatformA.SCALE);
        setRegion(platformStand);

        platformStateTime = 0;
        velocity = new Vector2(0, 0);
    }


    public void setVelocity(float vx, float vy) {
        velocity.set(vx, vy);
    }

    public void reposition(float x) {
        setPosition(x, getY());
    }

    @Override
    public void update(float deltaTime) {
        // Set velocity because It could have been changed (see reverseVelocity)..blabla
        //b2body.setLinearVelocity(velocity);

        setPosition(getX() + velocity.x * deltaTime, getY() + velocity.y * deltaTime);
        setRegion(platformStand);
        platformStateTime += deltaTime;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
