package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetPlatformD implements IAssetPlatform {
    private static final String TAG = AssetPlatformD.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion platformDStand;
    private Animation platformDAnimation;

    public AssetPlatformD(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        platformDStand = atlas.findRegion("platformD", 1);

        // Animation
        regions = atlas.findRegions("platformD");
        platformDAnimation = new Animation(0.3f / 3.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    @Override
    public TextureRegion getPlatformStand() {
        return platformDStand;
    }

    @Override
    public Animation getPlatformAnimation() {
        return platformDAnimation;
    }

    @Override
    public float getWidth() {
        return ( platformDStand.getRegionWidth() / GameCamera.PPM ) * SCALE;
    }

    @Override
    public float getHeight() {
        return ( platformDStand.getRegionHeight() / GameCamera.PPM ) * SCALE;
    }

}
