package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public abstract class AbstractGameObject extends Sprite {

    public void renderDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }

    public abstract void update(float deltaTime);
    public abstract void render(SpriteBatch spriteBatch);
}
