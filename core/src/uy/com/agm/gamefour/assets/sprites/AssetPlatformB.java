package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetPlatformB implements IAssetPlatform {
    private static final String TAG = AssetPlatformB.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion platformBStand;
    private Animation platformBAnimation;

    public AssetPlatformB(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        platformBStand = atlas.findRegion("platformB", 1);

        // Animation
        regions = atlas.findRegions("platformB");
        platformBAnimation = new Animation(1.2f / 12.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    @Override
    public TextureRegion getPlatformStand() {
        return platformBStand;
    }

    @Override
    public Animation getPlatformAnimation() {
        return platformBAnimation;
    }

    @Override
    public float getWidth() {
        return (platformBStand.getRegionWidth() / GameCamera.PPM) * SCALE;
    }

    @Override
    public float getHeight() {
        return (platformBStand.getRegionHeight() / GameCamera.PPM) * SCALE;
    }

}
