package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by AGMCORP on 30/9/2018.
 */


public class AssetBackgrounds {
    private static final String TAG = AssetBackgrounds.class.getName();

    private AssetDesert desert;

    public AssetBackgrounds(TextureAtlas atlasBackgrounds) {
        desert = new AssetDesert(atlasBackgrounds);
    }

    public AssetDesert getDesert() {
        return desert;
    }
}
