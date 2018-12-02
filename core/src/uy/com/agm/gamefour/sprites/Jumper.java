package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import uy.com.agm.gamefour.screens.gui.Hud;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.tools.AudioManager;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Jumper extends AbstractDynamicObject {
    private static final String TAG = Jumper.class.getName();

    private static final float CIRCLE_SHAPE_RADIUS_METERS = 30.0f / GameCamera.PPM;
    private static final float SENSOR_HX = 0.1f;
    private static final float SENSOR_HY = 0.01f;
    private static final float IMPULSE_Y = 7.5f;
    private static final float SCALE_IMPULSE_X = 30.0f;
    private static final float POWER_JUMP_OFFSET_Y = 1.0f;
    private static final int SUCCESSFUL_JUMP_SCORE = 2;
    private static final int PERFECT_JUMP_SCORE = 3;
    private static final float PERFECT_JUMP_TOLERANCE = 0.1f;
    private static final float MIN_SPEAK_TIME = 7.0f;
    private static final float MAX_SPEAK_TIME = 10.0f;
    private static final int MAX_BULLETS = 1;

    private enum State {
        IDLE, JUMP, DEAD, DISPOSE
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
    private float speakTime;
    private float timeToSpeak;
    private ParticleEffect magic;
    private ParticleEffect fireworks;
    private int bullets;

    // Coordinates x and y describe its bottom left corner.
    public Jumper(PlayScreen playScreen, GameWorld gameWorld, float x, float y) {
        this.playScreen = playScreen;
        this.gameWorld = gameWorld;

        AssetJumper assetJumper = Assets.getInstance().getSprites().getJumper();
        jumperStand = assetJumper.getJumperStand();
        jumperIdleAnimation = assetJumper.getJumperIdleAnimation();
        jumperJumpAnimation = assetJumper.getJumperJumpAnimation();

        // Sets initial values for position, width and height and initial frame as jumperStand.
        setBounds(x, y, assetJumper.getWidth(), assetJumper.getHeight());
        setRegion(jumperStand);
        stateTime = 0;

        // Box2d
        defineJumper();

        // Initial state
        currentState = State.JUMP;
        stopJumper = false;
        activateJumper = true;
        currentPlatform = null;

        // Voice
        initVoice();

        // Particles effect
        Vector3 gameCameraPos = gameWorld.getGameCamera().position();
        float gameCameraX = gameCameraPos.x;
        float gameCameraY = gameCameraPos.y;

        // Particle magic
        magic = new ParticleEffect();
        magic.load(Gdx.files.internal("effects/magic.p"), Gdx.files.internal("effects"));
        magic.setPosition(gameCameraX, gameCameraY);

        // Particle firework
        fireworks = new ParticleEffect();
        fireworks.load(Gdx.files.internal("effects/firework_large.p"), Gdx.files.internal("effects"));
        fireworks.setPosition(gameCameraX, gameCameraY);

        // We only have one shot!
        bullets = MAX_BULLETS;
    }

    private void initVoice() {
        speakTime = 0;
        timeToSpeak = MathUtils.random(MIN_SPEAK_TIME, MAX_SPEAK_TIME);
    }

    private void defineJumper() {
        // Creates main body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2); // In box2D the origin is at the center of the body
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
        filterJumper.maskBits = WorldContactListener.PLATFORM_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)

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
        Hud hud = playScreen.getHud();

        // Particle effect
        fireworks.setPosition(body.getPosition().x, body.getPosition().y);
        fireworks.start();

        // Current platform, score and level
        currentPlatform = platform;
        if (isPerfectJump(platform)) {
            hud.showPerfect();
            hud.addScore(PERFECT_JUMP_SCORE);

            // Audio effect
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPerfect());
        } else {
            hud.addScore(SUCCESSFUL_JUMP_SCORE);
        }
        playScreen.setLevelIsCompleted();

        // Reset bullets
        bullets = MAX_BULLETS;

        onLanding();
    }

    private boolean isPerfectJump(Platform platform) {
        float platformCenter = platform.getBodyPosition().x;
        float jumperCenter = body.getPosition().x;
        float left = platformCenter - PERFECT_JUMP_TOLERANCE;
        float right = platformCenter + PERFECT_JUMP_TOLERANCE;

        return gameWorld.getLevel() > 1 && left <= jumperCenter && jumperCenter <= right;
    }

    public void onLanding() {
        // State variables
        currentState = State.IDLE;
        stateTime = 0;
        stopJumper = true;
        initVoice();

        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBodyImpact());
    }

    public void onHit() {
        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getHit());
    }

    public void onDead() {
        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getBloodSplash());
        currentState = State.DEAD;
    }

    public void powerJump() {
        startJump();

        // Jump teleport
        Vector2 platformPos = gameWorld.getPlatformController().getPlatforms().get(1).getBodyPosition();
        body.setGravityScale(1);
        body.setTransform(platformPos.x, platformPos.y + POWER_JUMP_OFFSET_Y, body.getAngle());
        body.setAwake(true); // awakes the body to enable physics calculations
    }

    public void jump(float impulse) {
        startJump();

        // Perform jump
        body.setGravityScale(1);
        body.applyLinearImpulse(new Vector2(impulse / SCALE_IMPULSE_X, IMPULSE_Y), body.getWorldCenter(), true);
    }

    private void startJump() {
        // Particle effect
        magic.setPosition(body.getPosition().x, body.getPosition().y);
        magic.start();

        // State variables
        currentState = State.JUMP;
        stateTime = 0;
        activateJumper = true;

        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getJump());
    }

    public void shoot() {
        if (bullets > 0) {
            gameWorld.createGameObject(new Bullet(gameWorld, body.getPosition().x, body.getPosition().y));
            bullets--;
        }
    }

    @Override
    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    public boolean isIdle() {
        return currentState == State.IDLE;
    }

    public boolean isJumping() {
        return currentState == State.JUMP;
    }

    public boolean isDead() {
        return currentState == State.DEAD || currentState == State.DISPOSE;
    }

    @Override
    public void update(float deltaTime) {
        // Update particles
        magic.update(deltaTime);
        fireworks.update(deltaTime);

        // Life cycle: IDLE->JUMP->DEAD->DISPOSE
        switch (currentState) {
            case IDLE:
                stateIdle(deltaTime);
                break;
            case JUMP:
                stateJump(deltaTime);
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

        speakTime += deltaTime;
        if (speakTime > timeToSpeak) {
            initVoice();

            // Audio effect
            AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getVoice());
        }
    }

    private void stateJump(float deltaTime) {
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

    public Platform getCurrentPlatform() {
        return currentPlatform;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        // Draw Particles (behind jumper)
        magic.draw(spriteBatch);
        fireworks.draw(spriteBatch);

        // Draw Jumper
        draw(spriteBatch);
    }

    @Override
    public boolean isDisposable() {
        return currentState == State.DISPOSE;
    }
}
