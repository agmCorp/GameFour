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
    private TextureRegion signIn;
    private TextureRegion signInPressed;
    private TextureRegion signInDisabled;
    private TextureRegion showLeaderboards;
    private TextureRegion showLeaderboardsPressed;
    private TextureRegion showLeaderboardsDisabled;
    private TextureRegion rateGame;
    private TextureRegion rateGamePressed;
    private TextureRegion reload;
    private TextureRegion reloadPressed;
    private TextureRegion bigReload;
    private TextureRegion bigReloadPressed;
    private TextureRegion home;
    private TextureRegion homePressed;
    private TextureRegion bigHome;
    private TextureRegion bigHomePressed;
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
        signIn = atlasGUI.findRegion("signIn");
        signInPressed = atlasGUI.findRegion("signInPressed");
        signInDisabled = atlasGUI.findRegion("signInDisabled");
        showLeaderboards = atlasGUI.findRegion("showLeaderboards");
        showLeaderboardsPressed = atlasGUI.findRegion("showLeaderboardsPressed");
        showLeaderboardsDisabled = atlasGUI.findRegion("showLeaderboardsDisabled");
        rateGame = atlasGUI.findRegion("rateGame");
        rateGamePressed = atlasGUI.findRegion("rateGamePressed");
        reload = atlasGUI.findRegion("reload");
        reloadPressed = atlasGUI.findRegion("reloadPressed");
        bigReload = atlasGUI.findRegion("bigReload");
        bigReloadPressed = atlasGUI.findRegion("bigReloadPressed");
        home = atlasGUI.findRegion("home");
        homePressed = atlasGUI.findRegion("homePressed");
        bigHome = atlasGUI.findRegion("bigHome");
        bigHomePressed = atlasGUI.findRegion("bigHomePressed");
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

    public TextureRegion getSignIn() {
        return signIn;
    }

    public TextureRegion getSignInPressed() {
        return signInPressed;
    }

    public TextureRegion getSignInDisabled() {
        return signInDisabled;
    }

    public TextureRegion getShowLeaderboards() {
        return showLeaderboards;
    }

    public TextureRegion getShowLeaderboardsPressed() {
        return showLeaderboardsPressed;
    }

    public TextureRegion getShowLeaderboardsDisabled() {
        return showLeaderboardsDisabled;
    }

    public TextureRegion getRateGame() {
        return rateGame;
    }

    public TextureRegion getRateGamePressed() {
        return rateGamePressed;
    }

    public TextureRegion getReload() {
        return reload;
    }

    public TextureRegion getReloadPressed() {
        return reloadPressed;
    }

    public TextureRegion getBigReload() {
        return bigReload;
    }

    public TextureRegion getBigReloadPressed() {
        return bigReloadPressed;
    }

    public TextureRegion getHome() {
        return home;
    }

    public TextureRegion getHomePressed() {
        return homePressed;
    }

    public TextureRegion getBigHome() {
        return bigHome;
    }

    public TextureRegion getBigHomePressed() {
        return bigHomePressed;
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
