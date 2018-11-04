package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetPlatformE implements IAssetPlatform {
    private static final String TAG = AssetPlatformE.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion platformEStand;
    private Animation platformEAnimation;

    public AssetPlatformE(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        platformEStand = atlas.findRegion("platformE", 1);

        // Animation
        regions = atlas.findRegions("platformE");
        platformEAnimation = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    @Override
    public TextureRegion getPlatformStand() {
        return platformEStand;
    }

    @Override
    public Animation getPlatformAnimation() {
        return platformEAnimation;
    }

    @Override
    public float getWidth() {
        return (platformEStand.getRegionWidth() / GameCamera.PPM) * SCALE;
    }

    @Override
    public float getHeight() {
        return (platformEStand.getRegionHeight() / GameCamera.PPM) * SCALE;
    }
}
