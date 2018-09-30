package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetJumper;
import uy.com.agm.gamefour.game.GameWorld;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Jumper extends AbstractGameObject {
    private static final String TAG = Jumper.class.getName();

    private GameWorld gameWorld;
    private TextureRegion jumperStand;
    private Animation jumperIdleAnimation;
    private Animation jumperJumpAnimation;
    private float jumperStateTime;

    public Jumper(GameWorld gameWorld, float x, float y) {
        this.gameWorld = gameWorld;

        AssetJumper assetJumper = Assets.getInstance().getSprites().getJumper();
        jumperStand = assetJumper.getJumperStand();
        jumperIdleAnimation = assetJumper.getJumperIdleAnimation();
        jumperJumpAnimation = assetJumper.getJumperJumpAnimation();

        // Sets initial values for location, width and height and initial frame as jumperStand.
        setBounds(0, gameWorld.getGameCamera().position().y, jumperStand.getRegionWidth() * AssetJumper.SCALE, jumperStand.getRegionHeight() * AssetJumper.SCALE);
        setRegion(jumperStand);

        jumperStateTime = 0;
    }

    public void onSuccessfulJump() {
        gameWorld.addLevel();

        // todo ACA LO MUEVO A PREPO..ESTO NO ESTARIA
        float x = gameWorld.getPlatforms().getPlatform(1).getX();
        float y = gameWorld.getPlatforms().getPlatform(1).getY();
        setPosition(x, y);
    }

    public Vector2 position() {
        // TODO ESTO RETORNA LA POSICION DE BOX2D
        return new Vector2(getX(),getY());
    }


    @Override
    public void update(float deltaTime) {
//        jumperStateTime += deltaTime;
//        if (jumperStateTime > 3f) {
//            gameWorld.getCamera().position.x = getX();
//            jumperStateTime = 0;
//        }

//        float velocity = -3.5f;
//        gameWorld.getGameCamera().position().x = gameWorld.getGameCamera().position().x + velocity * deltaTime;

//        gameWorld.getCamera().position.y = gameWorld.getCamera().position.y + velocity * deltaTime;
        //setPosition(getX() + velocity * deltaTime, getY());
        setRegion((TextureRegion) jumperJumpAnimation.getKeyFrame(jumperStateTime, true));
        jumperStateTime += deltaTime;

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
