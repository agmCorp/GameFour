package uy.com.agm.gamefour.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by AGMCORP on 17/9/2018.
 */
public interface IScreenTransition {
    float getDuration();
    void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}
