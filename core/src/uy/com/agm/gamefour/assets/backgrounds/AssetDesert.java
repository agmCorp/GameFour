package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetDesert {
    private static final String TAG = AssetDesert.class.getName();

    private TextureRegion layer1;
    private TextureRegion layer2;
    private TextureRegion layer3;
    private TextureRegion layer4;
    private TextureRegion layer5;
    private TextureRegion layer6;
    private TextureRegion layer7;
    private TextureRegion layer8;
    private TextureRegion layer9;
    private TextureRegion layer10;

    public AssetDesert(TextureAtlas atlas) {
        layer1 = atlas.findRegion("desert1", 1);
        layer2 = atlas.findRegion("desert2", 1);
        layer3 = atlas.findRegion("desert3", 1);
        layer4 = atlas.findRegion("desert4", 1);
        layer5 = atlas.findRegion("desert5", 1);
        layer6 = atlas.findRegion("desert6", 1);
        layer7 = atlas.findRegion("desert7", 1);
        layer8 = atlas.findRegion("desert8", 1);
        layer9 = atlas.findRegion("desert9", 1);
        layer10 = atlas.findRegion("desert10", 1);
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

    public TextureRegion getLayer7() {
        return layer7;
    }

    public TextureRegion getLayer8() {
        return layer8;
    }

    public TextureRegion getLayer9() {
        return layer9;
    }

    public TextureRegion getLayer10() {
        return layer10;
    }
}
