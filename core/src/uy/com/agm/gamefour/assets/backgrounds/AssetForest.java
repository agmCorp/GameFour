package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetForest {
    private static final String TAG = AssetForest.class.getName();

    private TextureRegion layer1;
    private TextureRegion layer2;
    private TextureRegion layer3;
    private TextureRegion layer4;
    private TextureRegion layer5;
    private TextureRegion layer6;

    public AssetForest(TextureAtlas atlas) {
        layer1 = atlas.findRegion("forest1");
        layer2 = atlas.findRegion("forest2");
        layer3 = atlas.findRegion("forest3");
        layer4 = atlas.findRegion("forest4");
        layer5 = atlas.findRegion("forest5");
        layer6 = atlas.findRegion("forest6");
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

    public TextureRegion getLayer6() {
        return layer6;
    }
}
