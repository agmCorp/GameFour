package uy.com.agm.gamefour.assets.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;


/**
 * Created by AGMCORP on 18/9/2018.
 */

public class AssetFonts implements Disposable {
    private static final String TAG = AssetFonts.class.getName();

    private static final String FONT_FILE = "fonts/fonts.fnt";
    private static final float FONT_SMALL = 0.4f;
    private static final float FONT_NORMAL = 0.6f;
    private static final float FONT_BIG = 1.0f;
    private static final float FONT_GAME_TITLE = 0.85f;
    private static final float FONT_CREDITS = 0.3f;

    private BitmapFont small;
    private BitmapFont normal;
    private BitmapFont big;
    private BitmapFont gameTitle;
    private BitmapFont credits;

    public AssetFonts() {
        // Creates three fonts using a personal bitmap font
        small = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        normal = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        big = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        gameTitle = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        credits = new BitmapFont(Gdx.files.internal(FONT_FILE), false);

        // Sets font sizes
        small.getData().setScale(FONT_SMALL);
        normal.getData().setScale(FONT_NORMAL);
        big.getData().setScale(FONT_BIG);
        gameTitle.getData().setScale(FONT_GAME_TITLE);
        credits.getData().setScale(FONT_CREDITS);

        // Enables linear texture filtering for smooth fonts
        small.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        normal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        big.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameTitle.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        credits.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public BitmapFont getSmall() {
        return small;
    }

    public BitmapFont getNormal() {
        return normal;
    }

    public BitmapFont getBig() {
        return big;
    }

    public BitmapFont getGameTitle() {
        return gameTitle;
    }

    public BitmapFont getCredits() {
        return credits;
    }

    @Override
    public void dispose() {
        small.dispose();
        normal.dispose();
        big.dispose();
        gameTitle.dispose();
        credits.dispose();
    }
}

