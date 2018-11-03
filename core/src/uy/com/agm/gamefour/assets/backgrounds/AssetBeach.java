package uy.com.agm.gamefour.assets.backgrounds;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.sprites.ParallaxSB;


/**
 * Created by AGMCORP on 30/9/2018.
 */

public class AssetBeach {
    private static final String TAG = AssetBeach.class.getName();

    private static final float LAYER1_VEL = -3.0f;
    private static final float LAYER2_VEL = -0.5f;
    private static final float LAYER3_VEL = -1.0f;
    private static final float LAYER4_VEL = -0.5f;
    private static final float LAYER5_VEL = -0.1f;

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

    public void build(ParallaxSB parallaxSB) {
        parallaxSB.addFarawayLayer(layer7); // background
        parallaxSB.addFarawayLayer(layer6); // sun

        parallaxSB.addDynamicLayer(layer5, 2, true, LAYER5_VEL); // sea
        parallaxSB.addDynamicLayer(layer4, 2, true, LAYER4_VEL); // clouds 1
        parallaxSB.addDynamicLayer(layer3, 2, true, LAYER3_VEL); // clouds 2

        Array<TextureRegion> colTextureRegion = new Array<TextureRegion>();
        colTextureRegion.add(layer2A);
        colTextureRegion.add(layer2B);
        parallaxSB.addDynamicLayer(colTextureRegion, true, LAYER2_VEL); // island, boat

        colTextureRegion.clear();
        colTextureRegion.add(layer1A);
        colTextureRegion.add(layer1B);
        parallaxSB.addDynamicLayer(colTextureRegion, true, LAYER1_VEL); // sand
    }
}
