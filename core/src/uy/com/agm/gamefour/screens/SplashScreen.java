package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.widget.AnimatedActor;

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

    private AssetManager assetManager;
    private float splashTime;
    private boolean finishLoading;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private AnimatedActor loadingBar;

    public SplashScreen(GameFour game) {
        super(game);

        this.assetManager = new AssetManager();
        splashTime = 0;
        finishLoading = false;
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
        loadingBg = new Image(atlas.findRegion("loadingFrameBg"));

        // Add the loading bar animation
        Animation anim = new Animation(0.05f, atlas.findRegions("loadingBarAnim"), Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new AnimatedActor(anim);

        // Or if you only need a static bar, you can use loadingBar1 or loadingBar2
        // loadingBar = new Image(atlas.findRegion("loadingBar2"));

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        // Load the rest of assets asynchronously
        Assets.getInstance().init(assetManager);
    }

    @Override
    public void resize(int width, int height) {
        // Set our screen to always be XXX x VIEWPORT_GUI_HEIGHT in size
        width = VIEWPORT_GUI_WIDTH;
        height = VIEWPORT_GUI_HEIGHT;

        super.resize(width, height);

        // Make the background fill the screen
        screenBg.setSize(width, height);

        // Place the logo in the middle of the screen and LOGO_OFFSET_Y px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY((height - logo.getHeight()) / 2 + LOGO_OFFSET_Y);
        Gdx.app.debug(TAG, "********" + height + " " + width + " " + logo.getWidth() + " " + logo.getHeight());

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
        loadingBg.setSize(PIVOT, LOADING_BACKGROUND_HEIGHT);
        loadingBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
        loadingBg.setY(loadingBarHidden.getY());
    }

    @Override
    public void render(float deltaTime) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        splashTime += deltaTime;
//        if (assetManager.update() && splashTime >= MIN_SPLASH_TIME && !finishLoading) { // Load some, will return true if done loading
//            Assets.getInstance().finishLoading();
//            finishLoading = true;
//            ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.SLIDE_LEFT_LINEAR);
//        } else {
//            // Interpolate the percentage to make it more smooth
//            percent = Interpolation.linear.apply(percent, assetManager.getProgress(), 0.1f);
//
//            // Update positions (and size) to match the percentage
//            loadingBarHidden.setX(startX + endX * percent);
//            loadingBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
//            loadingBg.setWidth(PIVOT - PIVOT * percent);
//            loadingBg.invalidate();
//
//            // Show the loading screen
//            stage.act();
//            stage.draw();
//        }

        if (assetManager.update()) { // Load some, will return true if done loading
            if (Gdx.input.isTouched()) { // If the screen is touched after the game is done loading, go to the main menu screen
                Gdx.app.exit();
            }
        }

            // Interpolate the percentage to make it more smooth
            percent = Interpolation.linear.apply(percent, assetManager.getProgress(), 0.1f);

            // Update positions (and size) to match the percentage
            loadingBarHidden.setX(startX + endX * percent);
            loadingBg.setX(loadingBarHidden.getX() + loadingBarHidden.getWidth());
            loadingBg.setWidth(PIVOT - PIVOT * percent);
            loadingBg.invalidate();

            // Show the loading screen
            stage.act();
            stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();

        // Dispose the loading assets as we no longer need them
        assetManager.unload(TEXTURE_ATLAS_SPLASH_SCREEN);
    }
}
