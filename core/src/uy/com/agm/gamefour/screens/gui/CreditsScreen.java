package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.AbstractScreen;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenManager;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class CreditsScreen extends GUIAbstractScreen {
    private static final String TAG = CreditsScreen.class.getName();

    public CreditsScreen(GameFour game) {
        super(game);
    }

    @Override
    protected void updateLogic(float deltaTime) {
        stage.act();
    }

    @Override
    protected void renderLogic() {
        stage.draw();
    }

    @Override
    protected void goBack() {
        ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU, ScreenTransitionEnum.ROTATING_BOUNCE);
    }

    @Override
    protected void clearScreen() {
        AbstractScreen.clearScr();
    }

    @Override
    public void show() {
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        Label big = new Label("CREDITOS", labelStyleBig);
        // Actions
        SequenceAction sequenceOne = sequence(fadeIn(1.0f), fadeOut(1.0f));
        SequenceAction sequenceTwo = sequence(moveBy(15.0f, -15.0f, 0.5f, Interpolation.smooth),
                moveBy(-15.0f, 15.0f, 0.5f, Interpolation.smooth));
        big.addAction(parallel(forever(sequenceOne), forever(sequenceTwo)));

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setFillParent(true);
        table.add(big).row();
        stage.addActor(table);

        table.addListener(new InputListener() {
                              @Override
                              public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                  ScreenManager.getInstance().showScreen(ScreenEnum.PLAY_GAME, ScreenTransitionEnum.SLICE_UP_DOWN_10);
                              }

                              @Override
                              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                  return true;
                              }
                          }
        );
    }
}
