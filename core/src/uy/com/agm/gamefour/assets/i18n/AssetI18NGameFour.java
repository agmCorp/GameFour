package uy.com.agm.gamefour.assets.i18n;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetI18NGameFour {
    private static final String TAG = AssetI18NGameFour.class.getName();

    private I18NBundle i18NGameFourBundle;

    public AssetI18NGameFour(AssetManager assetManager) {
        // todo i18NGameFourBundle = assetManager.get("i18n/I18NGameFourBundle", I18NBundle.class);
    }

    public I18NBundle getI18NGameFourBundle() {
        return i18NGameFourBundle;
    }
}

