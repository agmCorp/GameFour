package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenManager;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class MainMenuScreen extends GUIAbstractScreen {
    private static final String TAG = MainMenuScreen.class.getName();

    public MainMenuScreen(GameFour game) {
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
        // Nothing to do here
    }

    @Override
    public void show() {
        Label.LabelStyle labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = Assets.getInstance().getFonts().getDefaultBig();

        Label.LabelStyle labelStyleNormal = new Label.LabelStyle();
        labelStyleNormal.font = Assets.getInstance().getFonts().getDefaultNormal();

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = Assets.getInstance().getFonts().getDefaultSmall();

        Label big = new Label("PowerJump", labelStyleBig);
        Label normal = new Label("Game Assets", labelStyleNormal);
        Label small = new Label("Texto chico", labelStyleSmall);

        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture("mock.png"))));
        table.setFillParent(true);
//        table.add(big).row();
//        table.add(normal).row();
//        table.add(small).row();
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
}
