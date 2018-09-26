package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.game.GameWorld;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Jumper extends AbstractGameObject {
    private static final String TAG = Jumper.class.getName();

    private GameWorld gameWorld;
    private TextureRegion jumperStand;
    private float stateTime;

    public Jumper(GameWorld gameWorld, float x, float y) {
        this.gameWorld = gameWorld;

        // Sets initial values for location, width and height and initial frame as jumperStand.
        jumperStand = Assets.getInstance().getSprites().getJumper().getJumper();
        setBounds(0, 0, 0.762f, 0.762f);
        setRegion(jumperStand);

        stateTime = 0;
    }

    public void onSuccessfulJump() {
        gameWorld.addLevel();
    }

    public Vector2 position() {
        // TODO ESTO RETORNA LA POSICION DE BOX2D
        return new Vector2(0,0);
    }


    @Override
    public void update(float deltaTime) {
//        stateTime += deltaTime;
//        if (stateTime > 3f) {
//            gameWorld.getCamera().position.x = getX();
//            stateTime = 0;
//        }

//        float velocity = -3.5f;
//        gameWorld.getGameCamera().position().x = gameWorld.getGameCamera().position().x + velocity * deltaTime;

//        gameWorld.getCamera().position.y = gameWorld.getCamera().position.y + velocity * deltaTime;
        //setPosition(getX() + velocity * deltaTime, getY());
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
