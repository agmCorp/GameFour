package uy.com.agm.gamefour.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.audio.music.AssetMusic;
import uy.com.agm.gamefour.assets.audio.sound.AssetSounds;
import uy.com.agm.gamefour.assets.fonts.AssetFonts;
import uy.com.agm.gamefour.assets.gui.AssetGUI;
import uy.com.agm.gamefour.assets.i18n.AssetI18NGameFour;
import uy.com.agm.gamefour.assets.sprites.AssetSprites;

/**
 * Created by AGMCORP on 18/9/2018.
 */

public class Assets implements Disposable, AssetErrorListener {
    private static final String TAG = Assets.class.getName();

    // Sound FXs
    public static final String FX_FILE_JUMP = "audio/sounds/jump.ogg";

    // Music
    public static final String MUSIC_FILE_MAIN_MENU = "audio/music/mainMenu.ogg";
    public static final String MUSIC_FILE_GAME = "audio/music/game.ogg";

    // Texture atlas
    private static final String TEXTURE_ATLAS_SPRITES = "atlas/sprites/dynamicObjects.atlas"; // todo
    private static final String TEXTURE_ATLAS_GUI = "atlas/gui/scene2d.atlas"; // todo

    private static Assets instance;
    private AssetManager assetManager;
    private AssetI18NGameFour i18NGameFour;
    private AssetSprites sprites;
    private AssetGUI gui;
    private AssetFonts fonts;
    private AssetSounds sounds;
    private AssetMusic music;

    // Singleton: prevent instantiation from other classes
    private Assets() {
    }

    // Singleton: retrieve instance
    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;

        // Sets asset manager error handler
        assetManager.setErrorListener(this);

        // Loads i18n
        loadI18NGameFour();

        // Loads texture atlas
        loadTextureAtlas();

        // Loads all sounds
        loadSounds();

        // Loads all music
        loadMusic();
    }

    public void finishLoading() {
        Gdx.app.debug(TAG, "***************************");
        Gdx.app.debug(TAG, "***** Number of assets loaded: " + assetManager.getAssetNames().size);
        for (String assetName : assetManager.getAssetNames()) {
            Gdx.app.debug(TAG, "***** Asset: " + assetName);
        }
        Gdx.app.debug(TAG, "***************************");

        // Enables linear texture filtering for pixel smoothing
        TextureAtlas atlasSprites = assetManager.get(TEXTURE_ATLAS_SPRITES);
        for (Texture texture : atlasSprites.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        TextureAtlas atlasGUI = assetManager.get(TEXTURE_ATLAS_GUI);
        for (Texture texture : atlasGUI.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        i18NGameFour = new AssetI18NGameFour(assetManager);
        sprites = new AssetSprites(atlasSprites);
        gui = new AssetGUI(atlasGUI);
        fonts = new AssetFonts();
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }

    private void loadI18NGameFour() {
        assetManager.load("i18n/I18NGameFourBundle", I18NBundle.class);
    }

    private void loadTextureAtlas() {
        assetManager.load(TEXTURE_ATLAS_SPRITES, TextureAtlas.class);
        assetManager.load(TEXTURE_ATLAS_GUI, TextureAtlas.class);
    }

    private void loadSounds() {
        assetManager.load(FX_FILE_JUMP, Sound.class);
    }

    private void loadMusic() {
        assetManager.load(MUSIC_FILE_MAIN_MENU, Music.class);
        assetManager.load(MUSIC_FILE_GAME, Music.class);
    }

    public AssetI18NGameFour getI18NGameFour() {
        return i18NGameFour;
    }

    public AssetSprites getSprites() {
        return sprites;
    }

    public AssetGUI getGui() {
        return gui;
    }

    public AssetFonts getFonts() {
        return fonts;
    }

    public AssetSounds getSounds() {
        return sounds;
    }

    public AssetMusic getMusic() {
        return music;
    }

    @Override
    public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
        Gdx.app.error(TAG, "Error loading asset: '" + assetDescriptor.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.dispose();
        assetManager = null;
        fonts = null;
    }
}
