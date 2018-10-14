package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetHills {
    private static final String TAG = AssetHills.class.getName();

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
    private TextureRegion layer11;

    public AssetHills(TextureAtlas atlas) {
        layer1 = atlas.findRegion("hills1");
        layer2 = atlas.findRegion("hills2");
        layer3 = atlas.findRegion("hills3");
        layer4 = atlas.findRegion("hills4");
        layer5 = atlas.findRegion("hills5");
        layer6 = atlas.findRegion("hills6");
        layer7 = atlas.findRegion("hills7");
        layer8 = atlas.findRegion("hills8");
        layer9 = atlas.findRegion("hills9");
        layer10 = atlas.findRegion("hills10");
        layer11 = atlas.findRegion("hills11");
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

    public TextureRegion getLayer11() {
        return layer11;
    }
}
