package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetBeach {
    private static final String TAG = AssetBeach.class.getName();

    private TextureRegion layer1A;
    private TextureRegion layer1B;
    private TextureRegion layer2A;
    private TextureRegion layer2B;
    private TextureRegion layer3;
    private TextureRegion layer4;
    private TextureRegion layer5;
    private TextureRegion layer6;
    private TextureRegion layer7;

    public AssetBeach(TextureAtlas atlas) {
        layer1A = atlas.findRegion("beach1A");
        layer1B = atlas.findRegion("beach1B");
        layer2A = atlas.findRegion("beach2A");
        layer2B = atlas.findRegion("beach2B");
        layer3 = atlas.findRegion("beach3");
        layer4 = atlas.findRegion("beach4");
        layer5 = atlas.findRegion("beach5");
        layer6 = atlas.findRegion("beach6");
        layer7 = atlas.findRegion("beach7");
    }

    public TextureRegion getLayer1A() {
        return layer1A;
    }

    public TextureRegion getLayer1B() {
        return layer1B;
    }

    public TextureRegion getLayer2A() {
        return layer2A;
    }

    public TextureRegion getLayer2B() {
        return layer2B;
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

    public TextureRegion getLayer7() {
        return layer7;
    }
}
