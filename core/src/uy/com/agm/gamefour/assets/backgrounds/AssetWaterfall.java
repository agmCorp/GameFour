package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetWaterfall {
    private static final String TAG = AssetWaterfall.class.getName();

    private TextureRegion layer1;
    private TextureRegion layer2;
    private TextureRegion layer3;
    private TextureRegion layer4;
    private TextureRegion layer5;

    public AssetWaterfall(TextureAtlas atlas) {
        layer1 = atlas.findRegion("waterfall1");
        layer2 = atlas.findRegion("waterfall2");
        layer3 = atlas.findRegion("waterfall3");
        layer4 = atlas.findRegion("waterfall4");
        layer5 = atlas.findRegion("waterfall5");
    }

    public TextureRegion getLayer1() {
        return layer1;
    }

    public TextureRegion getLayer2() {
        return layer2;
    }

    public TextureRegion getLayer3() {
        return layer3;
    }

    public TextureRegion getLayer4() {
        return layer4;
    }

    public TextureRegion getLayer5() {
        return layer5;
    }
}
