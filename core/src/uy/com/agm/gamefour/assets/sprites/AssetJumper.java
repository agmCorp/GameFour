package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetJumper {
    private static final String TAG = AssetJumper.class.getName();

    public static final float SCALE = 0.7f;

    private TextureRegion jumperStand;
    private Animation jumperJumpAnimation;
    private Animation jumperIdleAnimation;

    public  AssetJumper(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        jumperStand = atlas.findRegion("jumperIdle", 1);

        // Animation
        regions = atlas.findRegions("jumperJump");
        jumperJumpAnimation = new Animation(0.5f / 24.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();

        // Animation
        regions = atlas.findRegions("jumperIdle");
        jumperIdleAnimation = new Animation(2.5f / 9.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getJumperStand() {
        return jumperStand;
    }

    public Animation getJumperJumpAnimation() {
        return jumperJumpAnimation;
    }

    public Animation getJumperIdleAnimation() {
        return jumperIdleAnimation;
    }
}
