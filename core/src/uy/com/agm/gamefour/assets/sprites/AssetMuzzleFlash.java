package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetMuzzleFlash implements IAssetSprite {
    private static final String TAG = AssetMuzzleFlash.class.getName();

    private static final float SCALE = 1.0f;

    private TextureRegion muzzleFlashStand;
    private TextureRegion muzzleFlashShoot;
    private TextureRegion muzzleFlashImpact;

    public AssetMuzzleFlash(TextureAtlas atlas) {
        muzzleFlashStand = atlas.findRegion("muzzleFlashShoot");
        muzzleFlashShoot = atlas.findRegion("muzzleFlashShoot");
        muzzleFlashImpact = atlas.findRegion("muzzleFlashImpact");
    }

    public TextureRegion getMuzzleFlashStand() {
        return muzzleFlashStand;
    }

    public TextureRegion getMuzzleFlashShoot() {
        return muzzleFlashShoot;
    }

    public TextureRegion getMuzzleFlashImpact() {
        return muzzleFlashImpact;
    }

    public float getWidth() {
        return (muzzleFlashStand.getRegionWidth() / GameCamera.PPM) * SCALE;
    }

    public float getHeight() {
        return (muzzleFlashStand.getRegionHeight() / GameCamera.PPM) * SCALE;
    }
}
