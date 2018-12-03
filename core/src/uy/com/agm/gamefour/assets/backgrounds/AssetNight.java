package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uy.com.agm.gamefour.sprites.ParallaxSB;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetNight {
    private static final String TAG = AssetNight.class.getName();

    private static final float LAYER1_VEL = -5.0f;
    private static final float LAYER2_VEL = -2.0f;
    private static final float LAYER3_VEL = -1.0f;
    private static final float LAYER4_VEL = -0.5f;
    private static final float LAYER5_VEL = -0.2f;

    private TextureRegion layer1;
    private TextureRegion layer2;
    private TextureRegion layer3;
    private TextureRegion layer4;
    private TextureRegion layer5;
    private TextureRegion layer6;
    private TextureRegion layer7;

    public AssetNight(TextureAtlas atlas) {
        layer1 = atlas.findRegion("night1");
        layer2 = atlas.findRegion("night2");
        layer3 = atlas.findRegion("night3");
        layer4 = atlas.findRegion("night4");
        layer5 = atlas.findRegion("night5");
        layer6 = atlas.findRegion("night6");
        layer7 = atlas.findRegion("night7");
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

    public void build(ParallaxSB parallaxSB) {
        parallaxSB.addFarawayLayer(layer7); // moon
        parallaxSB.addFarawayLayer(layer6);  // stars

        parallaxSB.addDynamicLayer(layer5, 2, true, LAYER5_VEL);
        parallaxSB.addDynamicLayer(layer4, 2, true, LAYER4_VEL);
        parallaxSB.addDynamicLayer(layer3, 2, true, LAYER3_VEL);
        parallaxSB.addDynamicLayer(layer2, 2, true, LAYER2_VEL);
        parallaxSB.addDynamicLayer(layer1, 2, true, LAYER1_VEL);
    }
}

