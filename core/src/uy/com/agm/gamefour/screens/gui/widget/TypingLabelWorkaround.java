package uy.com.agm.gamefour.screens.gui.widget;

import com.rafaskoberg.gdx.typinglabel.TypingLabel;

/**
 * Created by AGMCORP on 11/2/2018.
 */

/* Workaround: Sometimes, when I start the game for the first time (and in some devices) typing labels are showed unreadable.
 * I've found a small workaround for this. Basically, at the end of act method in TypingLabel class, I check if that
 * method is called for the first time, if yes force a restart() of the label, this seems to fix the issue but I hope
 * that the developer could fix it in a better way.
 */
public class TypingLabelWorkaround extends TypingLabel {
    private boolean firstTime;

    public TypingLabelWorkaround(CharSequence text, LabelStyle style) {
        super(text, style);
        firstTime = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(firstTime) {
            restart();
            firstTime = false;
        }
    }
}
