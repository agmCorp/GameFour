package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by AGMCORP on 18/9/2018.
 */


public class AssetSprites {
    private static final String TAG = AssetSprites.class.getName();

    private AssetJumper jumper;
    private AssetPlatformA platformA;

    public AssetSprites(TextureAtlas atlasSprites) {
        jumper = new AssetJumper(atlasSprites);
        platformA = new AssetPlatformA(atlasSprites);
    }

    public AssetJumper getJumper() {
        return jumper;
    }

    public AssetPlatformA getPlatformA() {
        return platformA;
    }
}
