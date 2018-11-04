package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetEnemy implements IAssetSprite {
    private static final String TAG = AssetEnemy.class.getName();

    private static final float SCALE = 0.5f;

    private TextureRegion enemyStand;
    private TextureRegion enemySplat;
    private Animation enemyAnimation;

    public AssetEnemy(TextureAtlas atlas) {
        Array<TextureAtlas.AtlasRegion> regions;

        enemyStand = atlas.findRegion("enemy", 1);
        enemySplat = atlas.findRegion("enemySplat");

        // Animation
        regions = atlas.findRegions("enemy");
        enemyAnimation = new Animation(0.5f / 15.0f, regions, Animation.PlayMode.LOOP);
        regions.clear();
    }

    public TextureRegion getEnemyStand() {
        return enemyStand;
    }

    public TextureRegion getEnemySplat() {
        return enemySplat;
    }

    public Animation getEnemyAnimation() {
        return enemyAnimation;
    }

    public float getWidth() {
        return (enemyStand.getRegionWidth() / GameCamera.PPM) * SCALE;
    }

    public float getHeight() {
        return (enemyStand.getRegionHeight() / GameCamera.PPM) * SCALE;
    }
}
