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
import uy.com.agm.gamefour.assets.backgrounds.AssetBackgrounds;
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
    public static final String FX_FILE_JUMP_A = "audio/sounds/jumpA.ogg";
    public static final String FX_FILE_JUMP_B = "audio/sounds/jumpB.ogg";
    public static final String FX_FILE_JUMP_C = "audio/sounds/jumpC.ogg";
    public static final String FX_FILE_JUMP_D = "audio/sounds/jumpD.ogg";
    public static final String FX_FILE_JUMP_E = "audio/sounds/jumpE.ogg";
    public static final String FX_FILE_HIT = "audio/sounds/hit.ogg";
    public static final String FX_FILE_BODY_IMPACT = "audio/sounds/bodyImpact.ogg";
    public static final String FX_FILE_CLICK = "audio/sounds/click.ogg";
    public static final String FX_FILE_NEW_ACHIEVEMENT = "audio/sounds/newAchievement.ogg";
    public static final String FX_FILE_BLOOD_SPLASH = "audio/sounds/bloodSplash.ogg";
    public static final String FX_FILE_VOICE = "audio/sounds/voice.ogg";

    // Music
    public static final String MUSIC_FILE_MAIN_MENU = "audio/music/songMainMenu.ogg";
    public static final String MUSIC_FILE_CREDITS = "audio/music/songCredits.ogg";
    public static final String MUSIC_FILE_GAME = "audio/music/songGame.ogg";

    // Texture atlas
    private static final String TEXTURE_ATLAS_BACKGROUNDS = "atlas/backgrounds/backgrounds.atlas";
    private static final String TEXTURE_ATLAS_SPRITES = "atlas/sprites/sprites.atlas";
    private static final String TEXTURE_ATLAS_GUI = "atlas/gui/gui.atlas";

    private static Assets instance;
    private AssetManager assetManager;
    private AssetI18NGameFour i18NGameFour;
    private AssetBackgrounds backgrounds;
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

        TextureAtlas atlasBackgrounds = assetManager.get(TEXTURE_ATLAS_BACKGROUNDS);
        TextureAtlas atlasSprites = assetManager.get(TEXTURE_ATLAS_SPRITES);
        TextureAtlas atlasGUI = assetManager.get(TEXTURE_ATLAS_GUI);

        // Enables linear texture filtering for pixel smoothing
        for (Texture texture : atlasSprites.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        for (Texture texture : atlasGUI.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        i18NGameFour = new AssetI18NGameFour(assetManager);
        backgrounds = new AssetBackgrounds(atlasBackgrounds);
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
        assetManager.load(TEXTURE_ATLAS_BACKGROUNDS, TextureAtlas.class);
        assetManager.load(TEXTURE_ATLAS_SPRITES, TextureAtlas.class);
        assetManager.load(TEXTURE_ATLAS_GUI, TextureAtlas.class);
    }

    private void loadSounds() {
        assetManager.load(FX_FILE_JUMP_A, Sound.class);
        assetManager.load(FX_FILE_JUMP_B, Sound.class);
        assetManager.load(FX_FILE_JUMP_C, Sound.class);
        assetManager.load(FX_FILE_JUMP_D, Sound.class);
        assetManager.load(FX_FILE_JUMP_E, Sound.class);
        assetManager.load(FX_FILE_HIT, Sound.class);
        assetManager.load(FX_FILE_BODY_IMPACT, Sound.class);
        assetManager.load(FX_FILE_CLICK, Sound.class);
        assetManager.load(FX_FILE_NEW_ACHIEVEMENT, Sound.class);
        assetManager.load(FX_FILE_BLOOD_SPLASH, Sound.class);
        assetManager.load(FX_FILE_VOICE, Sound.class);
    }

    private void loadMusic() {
        assetManager.load(MUSIC_FILE_MAIN_MENU, Music.class);
        assetManager.load(MUSIC_FILE_CREDITS, Music.class);
        assetManager.load(MUSIC_FILE_GAME, Music.class);
    }

    public AssetI18NGameFour getI18NGameFour() {
        return i18NGameFour;
    }

    public AssetBackgrounds getBackgrounds() {
        return backgrounds;
    }

    public AssetSprites getSprites() {
        return sprites;
    }

    public AssetGUI getGUI() {
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
