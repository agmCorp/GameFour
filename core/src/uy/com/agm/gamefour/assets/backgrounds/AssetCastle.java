package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uy.com.agm.gamefour.sprites.ParallaxSB;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetCastle {
    private static final String TAG = AssetCastle.class.getName();

    private static final float LAYER1_VEL = -5.0f;
    private static final float LAYER2_VEL = -2.0f;
    private static final float LAYER3_VEL = -1.0f;
    private static final float LAYER4_VEL = -0.5f;
    private static final float LAYER5_VEL = -0.2f;
    private static final float LAYER6_VEL = -1.0f;
    private static final float LAYER7_VEL = -0.5f;

    private TextureRegion layer1;
    private TextureRegion layer2;
    private TextureRegion layer3;
    private TextureRegion layer4;
    private TextureRegion layer5;
    private TextureRegion layer6;
    private TextureRegion layer7;
    private TextureRegion layer8;

    public AssetCastle(TextureAtlas atlas) {
        layer1 = atlas.findRegion("castle1");
        layer2 = atlas.findRegion("castle2");
        layer3 = atlas.findRegion("castle3");
        layer4 = atlas.findRegion("castle4");
        layer5 = atlas.findRegion("castle5");
        layer6 = atlas.findRegion("castle6");
        layer7 = atlas.findRegion("castle7");
        layer8 = atlas.findRegion("castle8");
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

    public void build(ParallaxSB parallaxSB) {
        parallaxSB.addFarawayLayer(layer8); // background

        parallaxSB.addDynamicLayer(layer7, 2, true, LAYER7_VEL); // clouds 1
        parallaxSB.addDynamicLayer(layer6, 2, true, LAYER6_VEL); // clouds 2
        parallaxSB.addDynamicLayer(layer5, 2, true, LAYER5_VEL); // distant mountains
        parallaxSB.addDynamicLayer(layer4, 2, true, LAYER4_VEL);
        parallaxSB.addDynamicLayer(layer3, 2, true, LAYER3_VEL);
        parallaxSB.addDynamicLayer(layer2, 2, true, LAYER2_VEL);
        parallaxSB.addDynamicLayer(layer1, 2, true, LAYER1_VEL);
    }
}

