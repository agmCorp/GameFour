package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetExplosion implements IAssetSprite {
    private static final String TAG = AssetExplosion.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion explosionStand;
    private Animation explosionAnimation;

    public AssetExplosion(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        explosionStand = atlas.findRegion("explosion", 1);

        // Animation
        regions = atlas.findRegions("explosion");
        explosionAnimation = new Animation(0.5f / 21.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getExplosionStand() {
        return explosionStand;
    }

    public Animation getExplosionAnimation() {
        return explosionAnimation;
    }

    public float getWidth() {
        return (explosionStand.getRegionWidth() / GameCamera.PPM) * SCALE;
    }

    public float getHeight() {
        return (explosionStand.getRegionHeight() / GameCamera.PPM) * SCALE;
    }
}
