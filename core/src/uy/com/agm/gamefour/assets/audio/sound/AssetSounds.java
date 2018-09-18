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

    public AssetSounds(AssetManager am) {
        jump = am.get(Assets.FX_FILE_JUMP, Sound.class);
    }

    public static String getTAG() {
        return TAG;
    }
}