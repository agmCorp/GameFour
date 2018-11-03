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
    private TextureRegion exit;
    private TextureRegion exitPressed;
    private TextureRegion reload;
    private TextureRegion reloadPressed;
    private TextureRegion home;
    private TextureRegion homePressed;
    private TextureRegion pause;
    private TextureRegion pausePressed;
    private TextureRegion menuBg;
    private TextureRegion littleCloud;
    private TextureRegion trail;
    private TextureRegion creditsBg;
    private AssetRocket rocket;

    public AssetGUI(TextureAtlas atlasGUI) {
        audio = atlasGUI.findRegion("audio");
        audioPressed = atlasGUI.findRegion("audioPressed");
        audioChecked = atlasGUI.findRegion("audioChecked");
        play = atlasGUI.findRegion("play");
        playPressed = atlasGUI.findRegion("playPressed");
        info = atlasGUI.findRegion("info");
        infoPressed = atlasGUI.findRegion("infoPressed");
        exit = atlasGUI.findRegion("exit");
        exitPressed = atlasGUI.findRegion("exitPressed");
        reload = atlasGUI.findRegion("reload");
        reloadPressed = atlasGUI.findRegion("reloadPressed");
        home = atlasGUI.findRegion("home");
        homePressed = atlasGUI.findRegion("homePressed");
        pause = atlasGUI.findRegion("pause");
        pausePressed = atlasGUI.findRegion("pausePressed");
        menuBg = atlasGUI.findRegion("menuBg");
        littleCloud = atlasGUI.findRegion("littleCloud");
        trail = atlasGUI.findRegion("trail");
        creditsBg = atlasGUI.findRegion("creditsBg");
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

    public TextureRegion getExit() {
        return exit;
    }

    public TextureRegion getExitPressed() {
        return exitPressed;
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

    public TextureRegion getPause() {
        return pause;
    }

    public TextureRegion getPausePressed() {
        return pausePressed;
    }

    public TextureRegion getMenuBg() {
        return menuBg;
    }

    public TextureRegion getLittleCloud() {
        return littleCloud;
    }

    public TextureRegion getTrail() {
        return trail;
    }

    public TextureRegion getCreditsBg() {
        return creditsBg;
    }

    public AssetRocket getRocket() {
        return rocket;
    }
}
