package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public abstract class AbstractGameObject extends Sprite {
    public abstract void update(float deltaTime);

    public abstract void renderDebug(ShapeRenderer shapeRenderer);
}
