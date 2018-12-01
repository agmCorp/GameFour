package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.playservices.IPlayServices;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public abstract class GUIOverlayAbstractScreen implements Disposable {
    private static final String TAG = GUIOverlayAbstractScreen.class.getName();

    private static final float STAGE_ANIM_DURATION = 1.0f;

    protected GameFour game;
    protected IPlayServices playServices;
    protected OrthographicCamera guiOverlayCamera;
    protected Viewport guiOverlayViewport;
    protected Stage stage;

    public GUIOverlayAbstractScreen(GameFour game) {
        this.game = game;
        this.playServices = game.getPlayServices();
        guiOverlayCamera = new OrthographicCamera();
        guiOverlayViewport = new ExtendViewport(GameFour.APPLICATION_WIDTH, GameFour.APPLICATION_HEIGHT, guiOverlayCamera);

        /** Internally calls guiOverlayViewport.update() (see {@link uy.com.agm.gamefour.game.GameWorld} and
         * this.resize(int width, int height)) */
        stage = new Stage(guiOverlayViewport, game.getGuiBatch());
    }

    public void applyViewport() {
        stage.getViewport().apply();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    protected void startStageAnimation(boolean moveUp, final Runnable callBackOnFinish) {
        float h = stage.getHeight();
        final Group group = stage.getRoot();
        group.setY(moveUp ? 0 : h);
        group.setTouchable(Touchable.disabled);
        group.clearActions();
        group.addAction(sequence(moveBy(0, moveUp ? h : -h, STAGE_ANIM_DURATION, Interpolation.bounceOut), run(new Runnable() {
            public void run () {
                group.setTouchable(Touchable.enabled);
                if (callBackOnFinish != null) {
                    callBackOnFinish.run();
                }
            }
        })));
    }

    public abstract void build();
    public abstract void update(float deltaTime);
    public abstract void render();
}
