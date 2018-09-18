package uy.com.agm.gamefour.assets.gui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGMCORP on 18/9/2018.
 */


public class AssetGUI {
    private static final String TAG = AssetGUI.class.getName();

    private TextureRegion play;
    private TextureRegion playPressed;

    public AssetGUI(TextureAtlas atlasGUI) {
        play = atlasGUI.findRegion("play");
        playPressed = atlasGUI.findRegion("playPressed");
    }

    public TextureRegion getPlay() {
        return play;
    }

    public TextureRegion getPlayPressed() {
        return playPressed;
    }
}
