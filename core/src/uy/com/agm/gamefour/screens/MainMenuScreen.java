package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.util.ScreenEnum;
import uy.com.agm.gamefour.screens.util.ScreenManager;
import uy.com.agm.gamefour.screens.util.ScreenTransitionEnum;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class MainMenuScreen extends GUIAbstractScreen {
    public MainMenuScreen(GameFour game) {
        super(game);
    }

    @Override
    public void show() {
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        Label big = new Label("Texto grande", labelStyleBig);
        Label normal = new Label("Texto normal", labelStyleNormal);
        Label small = new Label("Texto chico", labelStyleSmall);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setFillParent(true);
        table.add(big).row();
        table.add(normal).row();
        table.add(small).row();
        stage.addActor(table);

        table.addListener(new InputListener() {
                              @Override
                              public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                  ScreenManager.getInstance().showScreen(ScreenEnum.CREDITS, ScreenTransitionEnum.COLOR_FADE_WHITE);
                              }

                              @Override
                              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                  return true;
                              }
                          }
        );
    }

    @Override
    public void render(float deltaTime) {
        clearScreen();
        stage.act();
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void resume() {

    }

}
