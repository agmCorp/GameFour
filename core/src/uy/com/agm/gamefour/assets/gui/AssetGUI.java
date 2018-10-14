package uy.com.agm.gamefour.assets.gui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGMCORP on 18/9/2018.
 */


public class AssetGUI {
    private static final String TAG = AssetGUI.class.getName();

    private TextureRegion reload;
    private TextureRegion reloadPressed;
    private TextureRegion home;
    private TextureRegion homePressed;

    public AssetGUI(TextureAtlas atlasGUI) {
        reload = atlasGUI.findRegion("reload");
        reloadPressed = atlasGUI.findRegion("reloadPressed");
        home = atlasGUI.findRegion("home");
        homePressed = atlasGUI.findRegion("homePressed");
    }

    public TextureRegion getReload() {
        return reload;
    }

    public TextureRegion getReloadPressed() {
        return reloadPressed;
    }

    public TextureRegion getHome() {
        return home;
    }

    public TextureRegion getHomePressed() {
        return homePressed;
    }
}
