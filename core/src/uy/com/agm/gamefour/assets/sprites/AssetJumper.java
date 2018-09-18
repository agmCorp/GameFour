package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetJumper {
    private static final String TAG = AssetJumper.class.getName();
    // todo
    public AssetJumper(TextureAtlas atlas) {

    }
/*
    // Constants (meters = pixels * resizeFactor / PPM)
    public static final float WIDTH_METERS = 127.0f * 0.6f / PlayScreen.PPM;
    public static final float HEIGHT_METERS = 127.0f * 0.6f / PlayScreen.PPM;

    private TextureRegion heroStandUp;
    private TextureRegion heroStandDown;
    private Animation heroDeathAnimation;
    private Animation heroWalkUpAnimation;
    private Animation heroWalkDownAnimation;
    private Animation heroWalkLeftRightAnimation;

    public AssetJumper(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        heroStandUp = atlas.findRegion("heroUp", 1);
        heroStandDown = atlas.findRegion("heroDown", 1);

        // Animation
        regions = atlas.findRegions("heroUp");
        heroWalkUpAnimation = new Animation(0.5f / 37.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroDown");
        heroWalkDownAnimation = new Animation(0.5f / 37.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroUp");
        heroWalkLeftRightAnimation = new Animation(0.5f / 37.0f, regions, Animation.PlayMode.LOOP); // We use the same animation as heroWalkUpAnimation
        regions.clear();

        // Animation
        regions = atlas.findRegions("heroDead");
        heroDeathAnimation = new Animation(0.5f / 25.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getHeroStandUp() {
        return heroStandUp;
    }

    public TextureRegion getHeroStandDown() {
        return heroStandDown;
    }

    public Animation getHeroDeathAnimation() {
        return heroDeathAnimation;
    }

    public Animation getHeroWalkUpAnimation() {
        return heroWalkUpAnimation;
    }

    public Animation getHeroWalkDownAnimation() {
        return heroWalkDownAnimation;
    }

    public Animation getHeroWalkLeftRightAnimation() {
        return heroWalkLeftRightAnimation;
    }
    */
}
