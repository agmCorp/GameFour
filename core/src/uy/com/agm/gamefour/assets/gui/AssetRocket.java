package uy.com.agm.gamefour.assets.gui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;

/**
 * Created by AGMCORP on 11/1/2018.
 */

public class AssetRocket {
    private static final String TAG = AssetRocket.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion rocketStand;
    private Animation rocketAnimation;

    public  AssetRocket(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        rocketStand = atlas.findRegion("rocket", 1);

        // Animation
        regions = atlas.findRegions("rocket");
        rocketAnimation = new Animation(2.0f / 51.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getRocketStand() {
        return rocketStand;
    }

    public Animation getRocketAnimation() {
        return rocketAnimation;
    }

    public float getWidth() {
        return ( rocketStand.getRegionWidth() / GameCamera.PPM ) * SCALE;
    }

    public float getHeight() {
        return ( rocketStand.getRegionHeight() / GameCamera.PPM ) * SCALE;
    }
}
