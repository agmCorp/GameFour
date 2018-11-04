package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetPlatformF implements IAssetPlatform {
    private static final String TAG = AssetPlatformF.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion platformFStand;
    private Animation platformFAnimation;

    public AssetPlatformF(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        platformFStand = atlas.findRegion("platformF", 1);

        // Animation
        regions = atlas.findRegions("platformF");
        platformFAnimation = new Animation(1.4f / 14.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    @Override
    public TextureRegion getPlatformStand() {
        return platformFStand;
    }

    @Override
    public Animation getPlatformAnimation() {
        return platformFAnimation;
    }

    @Override
    public float getWidth() {
        return (platformFStand.getRegionWidth() / GameCamera.PPM) * SCALE;
    }

    @Override
    public float getHeight() {
        return (platformFStand.getRegionHeight() / GameCamera.PPM) * SCALE;
    }
}
