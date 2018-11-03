package uy.com.agm.gamefour.assets.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGM on 11/3/2018.
 */

public interface IAssetPlatform extends IAssetSprite {
    TextureRegion getPlatformStand();
    Animation getPlatformAnimation();
}
