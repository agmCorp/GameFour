package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import uy.com.agm.gamefour.assets.Assets;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Jumper extends AbstractGameObject {
    private TextureRegion jumper;

    public Jumper(float x, float y) {
        jumper = Assets.getInstance().getSprites().getJumper().getJumper();
        setBounds(x, y, 0.762f, 0.762f);
    }

    @Override
    public void update(float deltaTime) {
        float velocity = 2;

        setPosition(getX() + velocity * deltaTime, getY());
        setRegion(jumper);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }

    @Override
    public void renderDebug(ShapeRenderer shapeRenderer) {
        //shapeRenderer.rect(getBoundingRectangle().x, getBoundingRectangle().y, getBoundingRectangle().width, getBoundingRectangle().height);
    }
}
