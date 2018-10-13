package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by AGMCORP on 30/9/2018.
 */


public class AssetBackgrounds {
    private static final String TAG = AssetBackgrounds.class.getName();

    public static final int MAX_BACKGROUNDS = 2;

    private AssetDesert desert;
    private AssetForest forest;

    public AssetBackgrounds(TextureAtlas atlasBackgrounds) {
        desert = new AssetDesert(atlasBackgrounds);
        forest = new AssetForest(atlasBackgrounds);
    }

    public AssetDesert getDesert() {
        return desert;
    }

    public AssetForest getForest() {
        return forest;
    }
}
