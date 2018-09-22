package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by AGMCORP on 18/9/2018.
 */


public class AssetSprites {
    private static final String TAG = AssetSprites.class.getName();

    private AssetJumper jumper;

    public AssetSprites(TextureAtlas atlasSprites) {
        jumper = new AssetJumper(atlasSprites);
    }

    public AssetJumper getJumper() {
        return jumper;
    }
}
