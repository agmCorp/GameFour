package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by AGMCORP on 30/9/2018.
 */


public class AssetBackgrounds {
    private static final String TAG = AssetBackgrounds.class.getName();

    public static final int MAX_BACKGROUNDS = 11;

    private AssetDesert desert;
    private AssetForest forest;
    private AssetBeach beach;
    private AssetHills hills;
    private AssetWaterfall waterfall;
    private AssetCastle castle;
    private AssetZombie zombie;
    private AssetSpider spider;
    private AssetGhost ghost;
    private AssetNight night;
    private AssetCandy candy;

    public AssetBackgrounds(TextureAtlas atlasBackgrounds) {
        desert = new AssetDesert(atlasBackgrounds);
        forest = new AssetForest(atlasBackgrounds);
        beach = new AssetBeach(atlasBackgrounds);
        hills = new AssetHills(atlasBackgrounds);
        waterfall = new AssetWaterfall(atlasBackgrounds);
        castle = new AssetCastle(atlasBackgrounds);
        zombie = new AssetZombie(atlasBackgrounds);
        spider = new AssetSpider(atlasBackgrounds);
        ghost = new AssetGhost(atlasBackgrounds);
        night = new AssetNight(atlasBackgrounds);
        candy = new AssetCandy(atlasBackgrounds);
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

    public AssetZombie getZombie() {
        return zombie;
    }

    public AssetSpider getSpider() {
        return spider;
    }

    public AssetGhost getGhost() {
        return ghost;
    }

    public AssetNight getNight() {
        return night;
    }

    public AssetCandy getCandy() {
        return candy;
    }
}
