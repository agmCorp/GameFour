package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetJumper;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Jumper extends AbstractGameObject {
    private static final String TAG = Jumper.class.getName();

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
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2); // In b2box the origin is at the center of the body
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            body = gameWorld.getBox2DWorld().createBody(bodyDef);
            body.setFixedRotation(true);

            FixtureDef fixtureDef = new FixtureDef();
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(0.3f); // TODO este numero queda muy bien
            fixtureDef.filter.categoryBits = WorldContactListener.JUMPER_BIT; // Depicts what this fixture is
            fixtureDef.filter.maskBits = WorldContactListener.PLATFORM_BIT |
                    WorldContactListener.OBSTACLE_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
            fixtureDef.shape = circleShape;
            body.createFixture(fixtureDef).setUserData(this);
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
        body.applyLinearImpulse(new Vector2(3.0f, 11f), body.getWorldCenter(), true); // todo
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
        World box2DWorld = gameWorld.getBox2DWorld();
        if(!box2DWorld.isLocked()) {
            box2DWorld.destroyBody(body);
        }
        currentState = State.DISPOSE;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
