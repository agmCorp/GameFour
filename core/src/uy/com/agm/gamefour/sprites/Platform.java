package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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

    private static final float SCALE = 0.4f;
    private static final float MAX_VELOCITY = 1.5f;
    private static final float MIN_VELOCITY = 0.5f;
    private static final float UPPER_LIMIT = 7.0f;
    private static final float BOTTOM_LIMIT = 1.0f;

    private GameWorld gameWorld;
    private TextureRegion platformStand;
    private Animation platformAnimation;
    private float stateTime;
    private Body body;
    private enum State {
        STATIC, UP, DOWN
    }
    private State currentState;
    private float velocity;

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

        currentState = State.STATIC;
        velocity = 0.0f;
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
        polygonShape.setAsBox(getBodyWidth() / 2, getBodyHeight() / 2);
        fixtureDef.filter.categoryBits = WorldContactListener.PLATFORM_BIT; // Depicts what this fixture is
        fixtureDef.filter.maskBits = WorldContactListener.JUMPER_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void startMovement() {
        if (currentState == State.STATIC) {
            currentState = MathUtils.randomBoolean() ? State.UP : State.DOWN;
            velocity = MathUtils.random(MIN_VELOCITY, MAX_VELOCITY);
        }
    }

    public void stopMovement() {
        currentState = State.STATIC;
    }

    public void reposition(float x, float y) {
        body.setTransform(x + getWidth() / 2, y + getHeight() / 2, body.getAngle());
        setPosition(x, y);
        currentState = State.STATIC;
    }

    public float getBodyWidth() {
        return getWidth() * SCALE; // The width of the body is arbitrarily smaller than the width of the image
    }

    public float getBodyHeight() {
        return getHeight() * SCALE; // The height of the body is arbitrarily smaller than the height of the image
    }

    @Override
    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    @Override
    public void update(float deltaTime) {
        switch (currentState) {
            case UP:
                stateMovingUp(deltaTime);
                break;
            case DOWN:
                stateMovingDown(deltaTime);
                break;
            case STATIC:
                stateStatic(deltaTime);
                break;
            default:
                break;
        }
    }

    private void stateMovingUp(float deltaTime) {
        // Set new velocity
        body.setLinearVelocity(0, velocity);
        updateSprite(deltaTime);

        if (getY() + getHeight() >= UPPER_LIMIT) {
            currentState = State.DOWN;
        }
    }

    private void stateMovingDown(float deltaTime) {
        // Set new velocity
        body.setLinearVelocity(0, -velocity);
        updateSprite(deltaTime);
        if (getY() <= BOTTOM_LIMIT) {
            currentState = State.UP;
        }
    }

    private void stateStatic(float deltaTime) {
        // Set new velocity
        body.setLinearVelocity(0, 0);
        updateSprite(deltaTime);
    }

    private void updateSprite(float deltaTime) {
        // Update this Sprite to correspond with the position of the Box2D body
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) platformAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
