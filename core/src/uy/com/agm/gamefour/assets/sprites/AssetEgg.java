package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetEgg implements IAssetSprite {
    private static final String TAG = AssetEgg.class.getName();

    private static final float SCALE = 0.5f;

    private TextureRegion eggStand;
    Array<TextureAtlas.AtlasRegion> eggs;

    public AssetEgg(TextureAtlas atlas) {
        eggs = atlas.findRegions("egg");
        eggStand = eggs.get(0);
    }

    public TextureRegion getRandomEgg() {
        return eggs.get(MathUtils.random(0, eggs.size - 1));
    }

    public float getWidth() {
        return (eggStand.getRegionWidth() / GameCamera.PPM) * SCALE;
    }

    public float getHeight() {
        return (eggStand.getRegionHeight() / GameCamera.PPM) * SCALE;
    }
}
