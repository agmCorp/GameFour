package uy.com.agm.gamefour.screens.gui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.fonts.AssetFonts;
import uy.com.agm.gamefour.assets.gui.AssetGUI;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.screens.ListenerHelper;
import uy.com.agm.gamefour.screens.ScreenEnum;
import uy.com.agm.gamefour.screens.ScreenManager;
import uy.com.agm.gamefour.screens.ScreenTransitionEnum;
import uy.com.agm.gamefour.screens.gui.widget.TypingLabelWorkaround;
import uy.com.agm.gamefour.tools.AudioManager;

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

    private static final float PAD = 50.0f;
    private static final float TITLE_ANIM_FADE_DURATION = 1.0f;
    private static final float TITLE_ANIM_MOVE_DURATION = 0.5f;
    private static final float TITLE_MOVE_BY_AMOUNT = 15.0f;

    private Assets assets;
    private AssetGUI assetGUI;
    private I18NBundle i18NGameThreeBundle;
    private Label.LabelStyle labelStyleBig;
    private Label.LabelStyle labelStyleCredits;
    private Label title;
    private Image creditsBg;

    public CreditsScreen(GameFour game) {
        super(game);

        assets = Assets.getInstance();
        assetGUI = assets.getGUI();
        i18NGameThreeBundle = assets.getI18NGameFour().getI18NGameFourBundle();

        // Styles
        AssetFonts assetFonts = assets.getFonts();
        labelStyleBig = new Label.LabelStyle();
        labelStyleBig.font = assetFonts.getBig();

        labelStyleCredits = new Label.LabelStyle();
        labelStyleCredits.font = assetFonts.getCredits();

        // Play credits music
        AudioManager.getInstance().playMusic(assets.getMusic().getSongCredits());
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
    public void show() {
        // Background
        creditsBg = new Image(assetGUI.getCreditsBg());
        stage.addActor(creditsBg);

        // Title
        title = new Label(i18NGameThreeBundle.format("creditsScreen.title"), labelStyleBig);

        // Message
        TypingLabelWorkaround msgLabel = new TypingLabelWorkaround(i18NGameThreeBundle.format("creditsScreen.msg"), labelStyleCredits);
        msgLabel.setAlignment(Align.center);
        msgLabel.setWrap(true);

        // Main table
        Table table = new Table();
        table.setDebug(DebugConstants.DEBUG_LINES);
        table.center();
        table.setFillParent(true);
        table.add(title).padBottom(PAD).row();
        table.add(msgLabel).width(stage.getWidth()).fill();
        stage.addActor(table);

        // Events
        stage.addListener(ListenerHelper.runnableListener(new Runnable() {
            @Override
            public void run() {
                goBack();
            }
        }));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        float w = stage.getWidth(); // Same as stage.getViewport().getWorldWidth()
        float h = stage.getHeight();

        // Place the menu background in the middle of the screen
        creditsBg.setX((w - creditsBg.getWidth()) / 2);
        creditsBg.setY((h - creditsBg.getHeight()) / 2);

        // Title animation
        SequenceAction sequenceOne = sequence(fadeIn(TITLE_ANIM_FADE_DURATION), fadeOut(TITLE_ANIM_FADE_DURATION));
        SequenceAction sequenceTwo = sequence(moveBy(TITLE_MOVE_BY_AMOUNT, -TITLE_MOVE_BY_AMOUNT, TITLE_ANIM_MOVE_DURATION, Interpolation.smooth),
                moveBy(-TITLE_MOVE_BY_AMOUNT, TITLE_MOVE_BY_AMOUNT, TITLE_ANIM_MOVE_DURATION, Interpolation.smooth));
        title.addAction(parallel(forever(sequenceOne), forever(sequenceTwo)));
    }
}
