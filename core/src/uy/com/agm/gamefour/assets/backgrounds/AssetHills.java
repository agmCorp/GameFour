package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uy.com.agm.gamefour.sprites.ParallaxSB;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetHills {
    private static final String TAG = AssetHills.class.getName();

    private static final float LAYER1_VEL = -4.0f;
    private static final float LAYER2_VEL = -2.0f;
    private static final float LAYER3_VEL = -1.3f;
    private static final float LAYER4_VEL = -0.9f;
    private static final float LAYER5_VEL = -0.7f;
    private static final float LAYER6_VEL = -0.4f;
    private static final float LAYER7_VEL = -0.1f;
    private static final float LAYER8_VEL = -1.0f;
    private static final float LAYER9_VEL = -0.8f;
    private static final float LAYER10_VEL = -0.5f;

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

    public void build(ParallaxSB parallaxSB) {
        parallaxSB.addFarawayLayer(layer11); // background

        parallaxSB.addDynamicLayer(layer10, 2, true, LAYER10_VEL); // clouds 1
        parallaxSB.addDynamicLayer(layer9, 2, true, LAYER9_VEL);   // clouds 2
        parallaxSB.addDynamicLayer(layer8, 2, true, LAYER8_VEL);   // clouds 3
        parallaxSB.addDynamicLayer(layer7, 2, true, LAYER7_VEL);   // distant hills
        parallaxSB.addDynamicLayer(layer6, 2, true, LAYER6_VEL);
        parallaxSB.addDynamicLayer(layer5, 2, true, LAYER5_VEL);
        parallaxSB.addDynamicLayer(layer4, 2, true, LAYER4_VEL);
        parallaxSB.addDynamicLayer(layer3, 2, true, LAYER3_VEL);
        parallaxSB.addDynamicLayer(layer2, 2, true, LAYER2_VEL);
        parallaxSB.addDynamicLayer(layer1, 2, true, LAYER1_VEL);
    }
}
