package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetPlatformA {
    private static final String TAG = AssetPlatformA.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion platformAStand;
    private Animation platformAAnimation;

    public AssetPlatformA(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        platformAStand = atlas.findRegion("platformA", 1);

        // Animation
        regions = atlas.findRegions("platformA");
        platformAAnimation = new Animation(0.3f / 3.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getPlatformAStand() {
        return platformAStand;
    }

    public Animation getPlatformAAnimation() {
        return platformAAnimation;
    }

    public float getWidth() {
        return ( platformAStand.getRegionWidth() / GameCamera.PPM ) * SCALE;
    }

    public float getHeight() {
        return ( platformAStand.getRegionHeight() / GameCamera.PPM ) * SCALE;
    }

}
