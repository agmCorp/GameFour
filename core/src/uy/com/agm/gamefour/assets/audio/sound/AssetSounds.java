package uy.com.agm.gamefour.assets.audio.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.assets.Assets;

/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetSounds {
    private static final String TAG = AssetSounds.class.getName();

    private Array<Sound> jump;
    private Sound hit;
    private Sound bodyImpact;
    private Sound click;
    private Sound newAchievement;
    private Sound bloodSplash;
    private Sound voice;

    public AssetSounds(AssetManager am) {
        jump = new Array<Sound>();
        jump.add(am.get(Assets.FX_FILE_JUMP_A, Sound.class));
        jump.add(am.get(Assets.FX_FILE_JUMP_B, Sound.class));
        jump.add(am.get(Assets.FX_FILE_JUMP_C, Sound.class));
        jump.add(am.get(Assets.FX_FILE_JUMP_D, Sound.class));
        jump.add(am.get(Assets.FX_FILE_JUMP_E, Sound.class));
        hit = am.get(Assets.FX_FILE_HIT, Sound.class);
        bodyImpact = am.get(Assets.FX_FILE_BODY_IMPACT, Sound.class);
        click = am.get(Assets.FX_FILE_CLICK, Sound.class);
        newAchievement = am.get(Assets.FX_FILE_NEW_ACHIEVEMENT, Sound.class);
        bloodSplash = am.get(Assets.FX_FILE_BLOOD_SPLASH, Sound.class);
        voice = am.get(Assets.FX_FILE_VOICE, Sound.class);
    }

    public Sound getJump() {
        return jump.get(MathUtils.random(0, jump.size - 1));
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

    public Sound getNewAchievement() {
        return newAchievement;
    }

    public Sound getBloodSplash() {
        return bloodSplash;
    }

    public Sound getVoice() {
        return voice;
    }
}