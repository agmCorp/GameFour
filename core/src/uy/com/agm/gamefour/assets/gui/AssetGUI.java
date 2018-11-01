package uy.com.agm.gamefour.assets.gui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGMCORP on 18/9/2018.
 */


public class AssetGUI {
    private static final String TAG = AssetGUI.class.getName();

    private TextureRegion audio;
    private TextureRegion audioPressed;
    private TextureRegion audioChecked;
    private TextureRegion play;
    private TextureRegion playPressed;
    private TextureRegion info;
    private TextureRegion infoPressed;
    private TextureRegion reload;
    private TextureRegion reloadPressed;
    private TextureRegion home;
    private TextureRegion homePressed;
    private TextureRegion menuBackground;
    private TextureRegion littleCloud;
    private TextureRegion trail;
    private AssetRocket rocket;

    public AssetGUI(TextureAtlas atlasGUI) {
        audio = atlasGUI.findRegion("audio");
        audioPressed = atlasGUI.findRegion("audioPressed");
        audioChecked = atlasGUI.findRegion("audioChecked");
        play = atlasGUI.findRegion("play");
        playPressed = atlasGUI.findRegion("playPressed");
        info = atlasGUI.findRegion("info");
        infoPressed = atlasGUI.findRegion("infoPressed");
        reload = atlasGUI.findRegion("reload");
        reloadPressed = atlasGUI.findRegion("reloadPressed");
        home = atlasGUI.findRegion("home");
        homePressed = atlasGUI.findRegion("homePressed");
        menuBackground = atlasGUI.findRegion("menuBackground");
        littleCloud = atlasGUI.findRegion("littleCloud");
        trail = atlasGUI.findRegion("trail");
        rocket = new AssetRocket(atlasGUI);
    }

    public TextureRegion getAudio() {
        return audio;
    }

    public TextureRegion getAudioPressed() {
        return audioPressed;
    }

    public TextureRegion getAudioChecked() {
        return audioChecked;
    }

    public TextureRegion getPlay() {
        return play;
    }

    public TextureRegion getPlayPressed() {
        return playPressed;
    }

    public TextureRegion getInfo() {
        return info;
    }

    public TextureRegion getInfoPressed() {
        return infoPressed;
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

    public TextureRegion getMenuBackground() {
        return menuBackground;
    }

    public TextureRegion getLittleCloud() {
        return littleCloud;
    }

    public TextureRegion getTrail() {
        return trail;
    }

    public AssetRocket getRocket() {
        return rocket;
    }
}
