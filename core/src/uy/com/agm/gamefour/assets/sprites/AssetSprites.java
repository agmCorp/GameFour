package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by AGMCORP on 18/9/2018.
 */


public class AssetSprites {
    private static final String TAG = AssetSprites.class.getName();

    private AssetJumper jumper;
    private AssetEnemy enemy;
    private AssetPlatformA platformA;
    private AssetPlatformB platformB;
    private AssetPlatformC platformC;
    private AssetPlatformD platformD;
    private AssetPlatformE platformE;
    private AssetPlatformF platformF;

    public AssetSprites(TextureAtlas atlasSprites) {
        jumper = new AssetJumper(atlasSprites);
        enemy = new AssetEnemy(atlasSprites);
        platformA = new AssetPlatformA(atlasSprites);
        platformB = new AssetPlatformB(atlasSprites);
        platformC = new AssetPlatformC(atlasSprites);
        platformD = new AssetPlatformD(atlasSprites);
        platformE = new AssetPlatformE(atlasSprites);
        platformF = new AssetPlatformF(atlasSprites);
    }

    public AssetJumper getJumper() {
        return jumper;
    }

    public AssetEnemy getEnemy() {
        return enemy;
    }

    public AssetPlatformA getPlatformA() {
        return platformA;
    }

    public AssetPlatformB getPlatformB() {
        return platformB;
    }

    public AssetPlatformC getPlatformC() {
        return platformC;
    }

    public AssetPlatformD getPlatformD() {
        return platformD;
    }

    public AssetPlatformE getPlatformE() {
        return platformE;
    }

    public AssetPlatformF getPlatformF() {
        return platformF;
    }
}
