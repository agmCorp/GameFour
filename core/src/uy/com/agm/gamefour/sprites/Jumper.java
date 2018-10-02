package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetJumper;
import uy.com.agm.gamefour.game.GameCamera;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Jumper extends AbstractGameObject {
    private static final String TAG = Jumper.class.getName();

    public static final float CIRCLE_SHAPE_RADIUS_METERS = 30.0f / GameCamera.PPM;
    private static final float SENSOR_OFFSET_METERS = 0.1f;

    private enum State {
        IDLE, JUMPING, DEAD, DISPOSE
    }
    private GameWorld gameWorld;
    private TextureRegion jumperStand;
    private Animation jumperIdleAnimation;
    private Animation jumperJumpAnimation;
    private float stateTime;
    private Body body;
    private State currentState;

    public Jumper(GameWorld gameWorld, float x, float y) {
        this.gameWorld = gameWorld;

        AssetJumper assetJumper = Assets.getInstance().getSprites().getJumper();
        jumperStand = assetJumper.getJumperStand();
        jumperIdleAnimation = assetJumper.getJumperIdleAnimation();
        jumperJumpAnimation = assetJumper.getJumperJumpAnimation();

        // Sets initial values for location, width and height and initial frame as jumperStand.
        setBounds(x, y, assetJumper.getWidth(), assetJumper.getHeight());
        setRegion(jumperStand);
        stateTime = 0;

        // Box2d
        defineJumper();

        // Initial state
        currentState = State.IDLE;
    }

    private void defineJumper() {
        // Creates main body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2); // In b2box the origin is at the center of the body
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = gameWorld.createBody(bodyDef);
        body.setFixedRotation(true);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fixtureDef.filter.categoryBits = WorldContactListener.JUMPER_BIT; // Depicts what this fixture is
        fixtureDef.filter.maskBits = WorldContactListener.PLATFORM_BIT |
                WorldContactListener.OBSTACLE_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef).setUserData(this);

        // Creates the sensor
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(SENSOR_OFFSET_METERS, SENSOR_OFFSET_METERS, new Vector2(0, -CIRCLE_SHAPE_RADIUS_METERS - SENSOR_OFFSET_METERS), 0);
        FixtureDef sensor = new FixtureDef();
        sensor.shape = polygonShape;
        sensor.filter.categoryBits = WorldContactListener.JUMPER_BIT;  // Depicts what this fixture is
        sensor.filter.maskBits = WorldContactListener.PLATFORM_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        sensor.isSensor = true;
        body.createFixture(sensor).setUserData(this);

    }

    public void onSuccessfulJump() {
        gameWorld.addLevel();
        currentState = State.IDLE;
        stateTime = 0;
    }

    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    public void jump() {
        body.applyLinearImpulse(new Vector2(3.0f, 8f), body.getWorldCenter(), true); // todo
        currentState = State.JUMPING;
    }

    @Override
    public void update(float deltaTime) {
        switch (currentState) {
            case IDLE:
                stateIdle(deltaTime);
                break;
            case JUMPING:
                stateJumping(deltaTime);
                break;
            case DEAD:
                stateDead(deltaTime);
                break;
            case DISPOSE:
                break;
            default:
                break;
        }
    }

    private void stateIdle(float deltaTime) {
        // Update this Sprite to correspond with the position of the Box2D body.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) jumperIdleAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;
    }

    private void stateJumping(float deltaTime) {
        // Update this Sprite to correspond with the position of the Box2D body.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) jumperJumpAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;
    }

    private void stateDead(float deltaTime) {
        // Destroy box2D body
        gameWorld.destroyBody(body);
        currentState = State.DISPOSE;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
