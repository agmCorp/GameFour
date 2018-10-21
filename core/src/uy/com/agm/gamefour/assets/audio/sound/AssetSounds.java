package uy.com.agm.gamefour.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import uy.com.agm.gamefour.assets.Assets;

/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetSounds {
    private static final String TAG = AssetSounds.class.getName();

    private Sound jump;
    private Sound hit;
    private Sound bodyImpact;
    private Sound click;

    public AssetSounds(AssetManager am) {
        jump = am.get(Assets.FX_FILE_JUMP, Sound.class);
        hit = am.get(Assets.FX_FILE_HIT, Sound.class);
        bodyImpact = am.get(Assets.FX_FILE_BODY_IMPACT, Sound.class);
        click = am.get(Assets.FX_FILE_CLICK, Sound.class);
    }

    public Sound getJump() {
        return jump;
    }

    public Sound getHit() {
        return hit;
    }

    public Sound getBodyImpact() {
        return bodyImpact;
    }

    public Sound getClick() {
        return click;
    }
}