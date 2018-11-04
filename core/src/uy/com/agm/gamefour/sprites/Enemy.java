package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
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

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetEnemy;
import uy.com.agm.gamefour.assets.sprites.AssetExplosion;
import uy.com.agm.gamefour.assets.sprites.AssetSprites;
import uy.com.agm.gamefour.game.GameCamera;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.tools.AudioManager;

/**
 * Created by AGM on 11/3/2018.
 */

public class Enemy extends AbstractDynamicObject {
    private static final String TAG = Enemy.class.getName();

    private static final float CIRCLE_SHAPE_RADIUS_METERS = 20.0f / GameCamera.PPM;
    private static final float MIN_OFFSET_Y = 0.0f;
    private static final float MAX_OFFSET_Y = 2.0f;
    private static final float KNOCK_BACK_SECONDS = 0.2f;
    private static final float KNOCK_BACK_FORCE_X = 2000.0f;
    private static final float KNOCK_BACK_FORCE_Y = 2000.0f;
    private static final Color KNOCK_BACK_COLOR = Color.BLACK;
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final int SCORE = 1;

    private enum State {
        INACTIVE, ALIVE, KNOCK_BACK, EXPLODING, SPLAT, DEAD, DISPOSE
    }
    private PlayScreen playScreen;
    private GameWorld gameWorld;
    private TextureRegion enemyStand;
    private TextureRegion enemySplat;
    private Animation enemyAnimation;
    private Animation explosionAnimation;
    private float enemyWidth;
    private float enemyHeight;
    private float explosionWidth;
    private float explosionHeight;
    private float stateTime;
    private Body body;
    protected State currentState;
    private boolean knockBackStarted;
    private float knockBackTime;

    public Enemy(PlayScreen playScreen, GameWorld gameWorld, Platform secondLastPlatform, Platform lastPlatform) {
        this.playScreen = playScreen;
        this.gameWorld = gameWorld;

        // Animations
        AssetSprites assetSprites = Assets.getInstance().getSprites();
        AssetEnemy assetEnemy = assetSprites.getEnemy();
        AssetExplosion assetExplosion = assetSprites.getExplosion();

        enemyStand = assetEnemy.getEnemyStand();
        enemySplat = assetEnemy.getEnemySplat();
        enemyAnimation = assetEnemy.getEnemyAnimation();
        explosionAnimation = assetExplosion.getExplosionAnimation();

        enemyWidth = assetEnemy.getWidth();
        enemyHeight = assetEnemy.getHeight();
        explosionWidth = assetExplosion.getWidth();
        explosionHeight = assetExplosion.getHeight();

        // Calculates position
        float x1 = secondLastPlatform.getX() + secondLastPlatform.getWidth();
        float x2 = lastPlatform.getX() - enemyWidth;
        float x = x1 < x2 ? MathUtils.random(x1, x2) : MathUtils.random(x2, x1);
        float y = Math.max(secondLastPlatform.getY(), lastPlatform.getY()) - enemyHeight - MathUtils.random(MIN_OFFSET_Y, MAX_OFFSET_Y);

        // Sets initial values for position, width and height and initial frame as enemyStand.
        setBounds(x, y, enemyWidth, enemyHeight);
        setRegion(enemyStand);
        stateTime = 0;

        // Box2d
        defineEnemy();

        currentState = State.INACTIVE;
        knockBackStarted = false;
        knockBackTime = 0;
    }

    private void defineEnemy() {
        // Creates main body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + enemyWidth / 2, getY() + enemyHeight / 2); // In box2D the origin is at the center of the body
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = gameWorld.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setGravityScale(0); // No gravity

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fixtureDef.filter.categoryBits = WorldContactListener.ENEMY_BIT; // Depicts what this fixture is
        fixtureDef.filter.maskBits = WorldContactListener.WEAPON_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef).setUserData(this);

        // By default this enemy doesn't interact in our world
        body.setActive(false);
    }

    public void onHit(Weapon weapon) {
        playScreen.getHud().addScore(SCORE);
        weapon.onTarget();

        /*
         * We must remove its body to avoid collisions.
         * This can't be done here because this method is called from WorldContactListener that is invoked
         * from PlayScreen.wordController.update.doPhysicsStep.box2DWorld.step(...).
         * No body can be removed when the simulation is occurring, we must wait for the next update cycle.
         * Therefore, we use a flag (state) in order to point out this behavior and remove it later.
         */
        currentState = State.KNOCK_BACK;
    }

    public boolean isDisposable() {
        return currentState == State.DISPOSE;
    }

    @Override
    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    @Override
    public void update(float deltaTime) {
        // Life cycle: INACTIVE->ALIVE->KNOCK_BACK->EXPLODING->SPLAT->DEAD->DISPOSE
        switch (currentState) {
            case INACTIVE:
                stateInactive(deltaTime);
                break;
            case ALIVE:
                stateAlive(deltaTime);
                break;
            case KNOCK_BACK:
                stateKnockBack(deltaTime);
                break;
            case EXPLODING:
                stateExploding(deltaTime);
                break;
            case SPLAT:
                stateSplat(deltaTime);
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

    private void checkBoundaries() {
        // When this enemy is outside the camera, it dies.
        if (!isOnCamera()) {
            currentState = State.DEAD;
        }
    }

    private boolean isOnCamera() {
        GameCamera gameCamera = gameWorld.getGameCamera();
        float gameCameraPosX = gameCamera.position().x;
        float worldWidth = gameCamera.getWorldWidth();
        float leftEdge = gameCameraPosX - worldWidth / 2;
        float rightEdge = gameCameraPosX + worldWidth / 2;

        return leftEdge <= getX() + getWidth() && getX() <= rightEdge;
    }

    private void stateInactive(float deltaTime) {
        if (isOnCamera()) {
            body.setActive(true);
            currentState = State.ALIVE;
        }
    }

    private void stateAlive(float deltaTime) {
        updateSpritePosition(deltaTime);
        checkBoundaries();
    }

    private void updateSpritePosition(float deltaTime) {
        // Update this Sprite to correspond with the position of the Box2D body.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) enemyAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;
    }

    private void stateKnockBack(float deltaTime) {
        if (!knockBackStarted) {
            initKnockBack();
        }
        holdEnemy();
        updateSpritePosition(deltaTime);
        knockBackTime += deltaTime;
        if (knockBackTime > KNOCK_BACK_SECONDS) {
            body.setLinearVelocity(0.0f, 0.0f);
            currentState = State.EXPLODING;
            stateTime = 0;
        }
    }

    private void holdEnemy() {
        // We don't let this Enemy go beyond the screen
        GameCamera gameCamera = gameWorld.getGameCamera();
        Vector3 gameCameraPos = gameCamera.position();
        float gameCameraPosX = gameCameraPos.x;
        float gameCameraPosY = gameCameraPos.y;
        float worldWidth = gameCamera.getWorldWidth();
        float worldHeight = gameCamera.getWorldHeight();
        float camLeftEdge = gameCameraPosX - worldWidth / 2;
        float camRightEdge = gameCameraPosX + worldWidth / 2;
        float camUpperEdge = gameCameraPosY + worldHeight / 2;
        float camBottomEdge = gameCameraPosY - worldHeight / 2;
        float enemyLeftEdge = getX();
        float enemyRightEdge = getX() + enemyWidth;
        float enemyUpperEdge = getY() + enemyHeight;
        float enemyBottomEdge = getY();

        if (camUpperEdge <= enemyUpperEdge ||
                enemyLeftEdge <= camLeftEdge ||
                camRightEdge <= enemyRightEdge ||
                enemyBottomEdge <= camBottomEdge) {
            body.setLinearVelocity(0.0f, 0.0f); // Stop
        }
    }

    private void initKnockBack() {
        // Knock back effect
        body.applyForce(MathUtils.randomSign() * KNOCK_BACK_FORCE_X,
                MathUtils.randomSign() * KNOCK_BACK_FORCE_Y,
                body.getPosition().x, body.getPosition().y, true);

        // Enemy can't collide with anything
        Filter filter = new Filter();
        filter.maskBits = WorldContactListener.NOTHING_BIT;

        // We set the previous filter in every fixture
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
        knockBackStarted = true;
    }

    private void stateExploding(float deltaTime) {
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            currentState = State.SPLAT;
        } else {
            if (stateTime == 0) { // Explosion starts
                // Audio effect
                AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getPum());

                // Determines the size of the explosion on the screen
                setBounds(getX() + getWidth() / 2 - explosionWidth / 2,
                        getY() + getHeight() / 2 - explosionHeight / 2,
                        explosionWidth, explosionHeight);
            }
            setRegion((TextureRegion) explosionAnimation.getKeyFrame(stateTime, true));
            stateTime += deltaTime;
        }
    }

    private void stateSplat(float deltaTime) {
        // Determines the size of the splat on the screen
        setBounds(getX() + getWidth() / 2 - enemyWidth / 2,
                getY() + getHeight() / 2 - enemyHeight / 2,
                enemyWidth, enemyHeight);
        setRegion(enemySplat);
        checkBoundaries();
    }

    private void stateDead(float deltaTime) {
        // Destroy box2D body
        gameWorld.destroyBody(body);
        currentState = State.DISPOSE;
    }

    private boolean isDrawable() {
        return currentState == State.ALIVE |
                currentState == State.KNOCK_BACK |
                currentState == State.EXPLODING |
                currentState == State.SPLAT;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (isDrawable()) {
            // Set the tint
            if (currentState == State.KNOCK_BACK) {
                setColor(KNOCK_BACK_COLOR);
            } else {
                if (currentState == State.EXPLODING) {
                    setColor(DEFAULT_COLOR);
                }
            }

            // Draw Enemy
            super.draw(spriteBatch);
        }
    }
}
