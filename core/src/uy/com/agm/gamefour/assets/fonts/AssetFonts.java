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
    private static final float FONT_SMALL = 0.3f;
    private static final float FONT_NORMAL = 0.5f;
    private static final float FONT_BIG = 0.9f;

    private BitmapFont defaultSmall;
    private BitmapFont defaultNormal;
    private BitmapFont defaultBig;

    public AssetFonts() {
        // Creates three fonts using a personal bitmap font
        defaultSmall = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        defaultNormal = new BitmapFont(Gdx.files.internal(FONT_FILE), false);
        defaultBig = new BitmapFont(Gdx.files.internal(FONT_FILE), false);

        // Sets font sizes
        defaultSmall.getData().setScale(FONT_SMALL);
        defaultNormal.getData().setScale(FONT_NORMAL);
        defaultBig.getData().setScale(FONT_BIG);

        // Enables linear texture filtering for smooth fonts
        defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public BitmapFont getDefaultSmall() {
        return defaultSmall;
    }

    public BitmapFont getDefaultNormal() {
        return defaultNormal;
    }

    public BitmapFont getDefaultBig() {
        return defaultBig;
    }

    @Override
    public void dispose() {
        defaultSmall.dispose();
        defaultNormal.dispose();
        defaultBig.dispose();
    }
}

