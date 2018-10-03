package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetPlatformA;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;

/**
 * Created by AGMCORP on 26/9/2018.
 */

public class Platform extends AbstractDynamicObject {
    private static final String TAG = Platform.class.getName();

    private GameWorld gameWorld;
    private TextureRegion platformStand;
    private Animation platformAnimation;
    private float stateTime;
    private Body body;
    private Vector2 velocity;


    public Platform(GameWorld gameWorld, float x, float y) {
        this.gameWorld = gameWorld;

        AssetPlatformA assetPlatformA = Assets.getInstance().getSprites().getPlatformA();
        platformStand = assetPlatformA.getPlatformAStand();
        platformAnimation = assetPlatformA.getPlatformAAnimation();

        // Sets initial values for location, width and height and initial frame as platformStand.
        setBounds(x, y, assetPlatformA.getWidth(), assetPlatformA.getHeight());
        setRegion(platformStand);
        stateTime = 0;

        // Box2d
        definePlatform();

        velocity = new Vector2(0, 0);
    }

    private void definePlatform() {
        // Creates main body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2); // In b2box the origin is at the center of the body
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = gameWorld.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setGravityScale(0); // No gravity

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(getWidth() / 4, getHeight() / 4); // The surface to land on is arbitrarily smaller than the size of the image
        fixtureDef.filter.categoryBits = WorldContactListener.PLATFORM_BIT; // Depicts what this fixture is
        fixtureDef.filter.maskBits = WorldContactListener.JUMPER_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void setVelocity(float vx, float vy) {
        velocity.set(vx, vy);
    }

    public void reposition(float x, float y) {
        body.setTransform(x + getWidth() / 2, y + getHeight() / 2, body.getAngle());
        setPosition(x, y);
    }

    @Override
    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    @Override
    public void update(float deltaTime) {
        // Set new velocity
        body.setLinearVelocity(velocity);

        // Update this Sprite to correspond with the position of the Box2D body
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) platformAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;

//        // todo observacion: yo creo que el movimiento en general de todas las plataformas tiene que ser orquestado por platforms y no
        // dentro de cada plataforma...no se.
//        if (getY() + getHeight() > gameWorld.getGameCamera().position().y + gameWorld.getGameCamera().getWorldHeight() / 2
//                || getY() < gameWorld.getGameCamera().position().y - gameWorld.getGameCamera().getWorldHeight() / 2) {
//            velocity.scl(-1); // oscila, usar estados
//        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
