package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetJumper;
import uy.com.agm.gamefour.game.GameCamera;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.tools.AudioManager;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Jumper extends AbstractDynamicObject {
    private static final String TAG = Jumper.class.getName();

    public static final float CIRCLE_SHAPE_RADIUS_METERS = 30.0f / GameCamera.PPM;
    private static final float SENSOR_HX = 0.1f;
    private static final float SENSOR_HY = 0.01f;
    private static final float IMPULSE_Y = 7.5f;
    private static final float SCALE_IMPULSE_X = 30.0f;

    private enum State {
        IDLE, JUMPING, DEAD, DISPOSE
    }
    private PlayScreen playScreen;
    private GameWorld gameWorld;
    private TextureRegion jumperStand;
    private Animation jumperIdleAnimation;
    private Animation jumperJumpAnimation;
    private float stateTime;
    private Body body;
    private State currentState;
    private boolean stopJumper;
    private boolean activateJumper;
    private Platform currentPlatform;
    private ParticleEffect magic;
    private ParticleEffect fireworks;

    public Jumper(PlayScreen playScreen, GameWorld gameWorld, float x, float y) {
        this.playScreen = playScreen;
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
        currentState = State.JUMPING;
        stopJumper = false;
        activateJumper = true;
        currentPlatform = null;

        // Particles effect
        float gameCameraX = gameWorld.getGameCamera().position().x;
        float gameCameraY = gameWorld.getGameCamera().position().y;

        // Particle magic
        magic = new ParticleEffect();
        magic.load(Gdx.files.internal("effects/magic.p"), Gdx.files.internal("effects"));
        magic.setPosition(gameCameraX, gameCameraY);

        // Particle firework
        fireworks = new ParticleEffect();
        fireworks.load(Gdx.files.internal("effects/firework_large.p"), Gdx.files.internal("effects"));
        fireworks.setPosition(gameCameraX, gameCameraY);
    }

    private void defineJumper() {
        // Creates main body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2); // In b2box the origin is at the center of the body
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = gameWorld.createBody(bodyDef);
        body.setFixedRotation(true);

        // Creates main fixture
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.0f; // Light weight
        body.createFixture(fixtureDef).setUserData(this);

        // Creates the sensor
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(SENSOR_HX, SENSOR_HY, new Vector2(0, -CIRCLE_SHAPE_RADIUS_METERS - SENSOR_HY), 0);
        FixtureDef sensor = new FixtureDef();
        sensor.shape = polygonShape;
        sensor.isSensor = true;
        body.createFixture(sensor).setUserData(this);

        setFilters();
    }

    private void setFilters() {
        Filter filterJumper = new Filter();
        filterJumper.categoryBits = WorldContactListener.JUMPER_BIT; // Depicts what this fixture is
        filterJumper.maskBits = WorldContactListener.PLATFORM_BIT |
                WorldContactListener.OBSTACLE_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

        Filter filterSensor = new Filter();
        filterSensor.categoryBits = WorldContactListener.JUMPER_BIT; // Depicts what this fixture is
        filterSensor.maskBits = WorldContactListener.PLATFORM_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

        // We set the previous filters in every fixture
        for (Fixture fixture : body.getFixtureList()) {
            if (fixture.isSensor()) {
                fixture.setFilterData(filterSensor);
            } else {
                fixture.setFilterData(filterJumper);
            }
        }
    }

    private void removeFilters() {
        // Jumper can't collide with anything
        Filter filter = new Filter();
        filter.categoryBits = WorldContactListener.JUMPER_BIT; // Depicts what this fixture is
        filter.maskBits = WorldContactListener.NOTHING_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

        // We set the previous filter in every fixture
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    public void onSuccessfulJump(Platform platform) {
        // Particle effect
        fireworks.setPosition(body.getPosition().x, body.getPosition().y);
        fireworks.start();

        // Current platform, score and level
        currentPlatform = platform;
        playScreen.getHud().addScore(gameWorld.getLevel());
        gameWorld.addLevel();

        onLanding();
    }

    public void onLanding() {
        // State variables
        currentState = State.IDLE;
        stateTime = 0;
        stopJumper = true;

        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBodyImpact());
    }

    public void onHit() {
        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHit());
    }

    public void jump(float impulse) {
        // Particle effect
        magic.setPosition(body.getPosition().x, body.getPosition().y);
        magic.start();

        // Jump impulse
        body.setGravityScale(1);
        body.applyLinearImpulse(new Vector2(impulse / SCALE_IMPULSE_X, IMPULSE_Y), body.getWorldCenter(), true);

        // State variables
        currentState = State.JUMPING;
        stateTime = 0;
        activateJumper = true;

        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getJump());
    }

    @Override
    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    public boolean isIdle() {
        return currentState == State.IDLE;
    }

    @Override
    public void update(float deltaTime) {
        // Update particles
        magic.update(deltaTime);
        fireworks.update(deltaTime);

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
        // Disables Jumper
        if (stopJumper) {
            body.setLinearVelocity(0.0f, 0.0f);
            body.setGravityScale(0);
            removeFilters();
            stopJumper = false;
        }

        // Update the position of the Box2D body since the platform can be moving.
        float y = currentPlatform.getBodyPosition().y + currentPlatform.getBodyHeight() / 2;
        body.setTransform(body.getPosition().x, y + CIRCLE_SHAPE_RADIUS_METERS, body.getAngle());

        // Update this Sprite to correspond with the position of the Box2D body.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) jumperIdleAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;
    }

    private void stateJumping(float deltaTime) {
        // Enables Jumper
        if (activateJumper) {
            setFilters();
            activateJumper = false;
        }

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
        // Draw Particles (behind jumper)
        magic.draw(spriteBatch);
        fireworks.draw(spriteBatch);

        // Draw Jumper
        draw(spriteBatch);
    }
}
