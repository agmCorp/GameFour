package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetPlatformC implements IAssetPlatform {
    private static final String TAG = AssetPlatformC.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion platformCStand;
    private Animation platformCAnimation;

    public AssetPlatformC(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        platformCStand = atlas.findRegion("platformC", 1);

        // Animation
        regions = atlas.findRegions("platformC");
        platformCAnimation = new Animation(0.5f / 5.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    @Override
    public TextureRegion getPlatformStand() {
        return platformCStand;
    }

    @Override
    public Animation getPlatformAnimation() {
        return platformCAnimation;
    }

    @Override
    public float getWidth() {
        return ( platformCStand.getRegionWidth() / GameCamera.PPM ) * SCALE;
    }

    @Override
    public float getHeight() {
        return ( platformCStand.getRegionHeight() / GameCamera.PPM ) * SCALE;
    }

}
