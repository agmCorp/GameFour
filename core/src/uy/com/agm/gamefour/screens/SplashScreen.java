package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.util.ScreenEnum;
import uy.com.agm.gamefour.screens.util.ScreenManager;
import uy.com.agm.gamefour.screens.util.ScreenTransitionEnum;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class SplashScreen extends GUIAbstractScreen {
    private static final String TAG = SplashScreen.class.getName();

    private static final String TEXTURE_ATLAS_SPLASH_SCREEN = "atlas/loading/loading.atlas";
    private static final float LOGO_OFFSET_Y = 100.0f;
    private static final float START_X = 35.0f;
    private static final float END_X = 430.0f;
    private static final float PIVOT = 450.0f;
    private static final float LOADING_BACKGROUND_HEIGHT = 55.0f;
    private static final float MIN_SPLASH_TIME = 3.0f;
    private static final float ALPHA = 0.1f;

    private AssetManager assetManager;
    private float splashTime;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingFrameBg;
    private Image loadingBar;

    private float startX, endX;
    private float percent;

    private boolean stop;

    public SplashScreen(GameFour game) {
        super(game);

        this.assetManager = new AssetManager();
        splashTime = 0;
        stop = false;
    }

    @Override
    public void show() {
        // Tell the manager to load assets for the loading screen
        assetManager.load(TEXTURE_ATLAS_SPLASH_SCREEN, TextureAtlas.class);

        // Wait until they are finished loading
        assetManager.finishLoading();

        // Get our texture atlas from the manager
        TextureAtlas atlas = assetManager.get(TEXTURE_ATLAS_SPLASH_SCREEN, TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("logo"));
        loadingFrame = new Image(atlas.findRegion("loadingFrame"));
        loadingBarHidden = new Image(atlas.findRegion("loadingBarHidden"));
        screenBg = new Image(atlas.findRegion("screenBg"));
        loadingFrameBg = new Image(atlas.findRegion("loadingFrameBg"));
        loadingBar = new Image(atlas.findRegion("loadingBar2"));

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingFrameBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        // Load the rest of assets asynchronously
        Assets.getInstance().init(assetManager);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        // Make the background fill the screen
        screenBg.setSize(stage.getWidth(), stage.getHeight());

        // Place the logo in the middle of the screen and LOGO_OFFSET_Y px up
        logo.setX((stage.getWidth() - logo.getWidth()) / 2);
        logo.setY((stage.getHeight() - logo.getHeight()) / 2 + LOGO_OFFSET_Y);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame
        loadingBar.setX(loadingFrame.getX());
        loadingBar.setY(loadingFrame.getY());

        // Place the image that will hide the bar on top of the bar
        loadingBarHidden.setX(startX);
        loadingBarHidden.setY(loadingBar.getY());

        // The start position and how far to move the hidden loading bar
        startX = START_X;
        endX = END_X;
        percent = 0;

        // The rest of the hidden bar
        loadingFrameBg.setSize(PIVOT, LOADING_BACKGROUND_HEIGHT);
        loadingFrameBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
        loadingFrameBg.setY(loadingBarHidden.getY());
    }

    @Override
    public void render(float deltaTime) {
        clearScreen();
        if (!stop) {
            updateLogic(deltaTime);
        }
        renderLogic();
    }

    private void updateLogic(float deltaTime) {
        splashTime += deltaTime;
        if (assetManager.update() && splashTime >= MIN_SPLASH_TIME) { // Load some, will return true if done loading
            Assets.getInstance().finishLoading();
            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_LEFT_LINEAR);
        } else {
            // Interpolate the percentage to make it more smooth
            percent = Interpolation.linear.apply(percent, assetManager.getProgress(), ALPHA);

            // Update positions (and size) to match the percentage
            loadingBarHidden.setX(startX + endX * percent);
            loadingFrameBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
            loadingFrameBg.setWidth(PIVOT - PIVOT * percent);
            loadingFrameBg.invalidate();

            // Show the loading screen
            stage.act();
        }
    }

    private void renderLogic() {
        stage.draw();
    }

    @Override
    public void pause() {
    }

    @Override
    public void stop() {
        stop = true;
    }

    @Override
    public void resume() {
        stop = false;
    }

    @Override
    public void hide() {
        super.hide();

        // Dispose the loading assets as we no longer need them
        assetManager.unload(TEXTURE_ATLAS_SPLASH_SCREEN);
    }
}
