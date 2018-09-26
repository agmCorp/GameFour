package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamefour.assets.Assets;

/**
 * Created by AGMCORP on 26/9/2018.
 */

public class Platform extends AbstractGameObject {
    private static final String TAG = Platform.class.getName();

    private TextureRegion platformStand;
    private float stateTime;
    private Vector2 velocity;


    public Platform(float x, float y) {
        // Sets initial values for location, width and height and initial frame as platformStand.
        platformStand = Assets.getInstance().getSprites().getJumper().getJumper();
        setBounds(x, y, 0.762f, 0.762f);
        setRegion(platformStand);
        stateTime = 0;

        velocity = new Vector2(0,0);
    }



    @Override
    public void update(float deltaTime) {
        // Set velocity because It could have been changed (see reverseVelocity)..blabla
        //b2body.setLinearVelocity(velocity);

        setPosition(getX() + velocity.x * deltaTime, getY() + velocity.y * deltaTime);
        setRegion(platformStand);
        stateTime += deltaTime;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
