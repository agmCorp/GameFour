package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by AGMCORP on 30/9/2018.
 */


public class AssetBackgrounds {
    private static final String TAG = AssetBackgrounds.class.getName();

    public static final int MAX_BACKGROUNDS = 6;

    private AssetDesert desert;
    private AssetForest forest;
    private AssetBeach beach;
    private AssetHills hills;
    private AssetWaterfall waterfall;
    private AssetCastle castle;

    public AssetBackgrounds(TextureAtlas atlasBackgrounds) {
        desert = new AssetDesert(atlasBackgrounds);
        forest = new AssetForest(atlasBackgrounds);
        beach = new AssetBeach(atlasBackgrounds);
        hills = new AssetHills(atlasBackgrounds);
        waterfall = new AssetWaterfall(atlasBackgrounds);
        castle = new AssetCastle(atlasBackgrounds);
    }

    public AssetDesert getDesert() {
        return desert;
    }

    public AssetForest getForest() {
        return forest;
    }

    public AssetBeach getBeach() {
        return beach;
    }

    public AssetHills getHills() {
        return hills;
    }

    public AssetWaterfall getWaterfall() {
        return waterfall;
    }

    public AssetCastle getCastle() {
        return castle;
    }
}
