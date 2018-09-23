package uy.com.agm.gamefour.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import uy.com.agm.gamefour.screens.AbstractScreen;
import uy.com.agm.gamefour.screens.transitions.ScreenTransition;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class DirectedGame implements ApplicationListener {
    private static final String TAG = DirectedGame.class.getName();

    private boolean init;
    private AbstractScreen currentScreen;
    private AbstractScreen nextScreen;
    private FrameBuffer currentFbo;
    private FrameBuffer nextFbo;
    private SpriteBatch batch;
    private float transitionTime;
    private ScreenTransition screenTransition;

    public void setScreen(AbstractScreen screen) {
        setScreen(screen, null);
    }

    public void setScreen(AbstractScreen screen, ScreenTransition screenTransition) {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        if (!init) {
            currentFbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
            nextFbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
            batch = new SpriteBatch();
            init = true;
        }

        // Start new transition
        nextScreen = screen;

        // Activate next screen
        nextScreen.show();
        nextScreen.resize(width, height);

        // Pause both screens
        if (currentScreen != null) {
            currentScreen.stop();
        }
        nextScreen.stop();

        // Disable input
        Gdx.input.setInputProcessor(null);

        this.screenTransition = screenTransition;
        transitionTime = 0;
    }

    @Override
    public void render() {
        // Get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
        if (nextScreen == null) {
            // No ongoing transition
            if (currentScreen != null) {
                currentScreen.render(deltaTime);
            }
        } else {
            // Ongoing transition
            float duration = 0;
            if (screenTransition != null) {
                duration = screenTransition.getDuration();
            }
            transitionTime = Math.min(transitionTime + deltaTime, duration);
            if (screenTransition == null || transitionTime >= duration) {
                // No transition effect set or transition has just finished
                if (currentScreen != null) {
                    currentScreen.hide();
                }
                nextScreen.resume();
                // Enable input for next screen
                Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
                // Switch screens
                currentScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            } else {
                // Render screens to FBOs
                currentFbo.begin();
                if (currentScreen != null) {
                    currentScreen.render(deltaTime);
                }
                currentFbo.end();
                // WA: Applies the viewport to the camera and sets the glViewport
                if (currentScreen != null) {
                    currentScreen.applyViewport();
                }

                nextFbo.begin();
                nextScreen.render(deltaTime);
                nextFbo.end();
                // WA: Applies the viewport to the camera and sets the glViewport
                nextScreen.applyViewport();

                // Render transition effect to screen
                float alpha = transitionTime / duration;
                screenTransition.render(batch, currentFbo.getColorBufferTexture(), nextFbo.getColorBufferTexture(), alpha);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
        if (nextScreen != null) {
            nextScreen.resize(width, height);
        }
    }

    @Override
    public void pause() {
        if (currentScreen != null) {
            currentScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (currentScreen != null) {
            currentScreen.resume();
        }
    }

    @Override
    public void dispose() {
        if (currentScreen != null) {
            currentScreen.hide();
        }
        if (nextScreen != null) {
            nextScreen.hide();
        }
        if (init) {
            currentFbo.dispose();
            currentScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }

    // Returns the currently active screen */
    public Screen getCurrentScreen () {
        return currentScreen;
    }
}

