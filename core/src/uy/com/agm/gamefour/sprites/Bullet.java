package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetEgg;
import uy.com.agm.gamefour.assets.sprites.AssetMuzzleFlash;
import uy.com.agm.gamefour.assets.sprites.AssetSprites;
import uy.com.agm.gamefour.game.GameCamera;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;
import uy.com.agm.gamefour.tools.AudioManager;

/**
 * Created by AGM on 11/3/2018.
 */

public class Bullet extends AbstractDynamicObject {
    private static final String TAG = Bullet.class.getName();

    private static final float CIRCLE_SHAPE_RADIUS_METERS = 20.0f / GameCamera.PPM;
    public static final float VELOCITY = 4.0f;
    public static final float GRAVITY_SCALE = 1.5f;

    private enum State {
        MUZZLE_FLASH, SHOOT, IMPACT, KNOCK_BACK, FINISH, DISPOSE
    }
    private GameWorld gameWorld;
    private TextureRegion eggStand;
    private float eggWidth;
    private float eggHeight;
    private TextureRegion muzzleFlashShoot;
    private TextureRegion muzzleFlashImpact;
    private float muzzleFlashWidth;
    private float muzzleFlashHeight;
    private Body body;
    private State currentState;

    // Coordinates x and y describe its center.
    public Bullet(GameWorld gameWorld, float x, float y) {
        this.gameWorld = gameWorld;

        // Random texture
        AssetSprites assetSprites = Assets.getInstance().getSprites();
        AssetEgg assetEgg = assetSprites.getEgg();
        eggStand = assetEgg.getRandomEgg();
        eggWidth = assetEgg.getWidth();
        eggHeight = assetEgg.getHeight();

        // Muzzle flash
        AssetMuzzleFlash assetMuzzleFlash = assetSprites.getMuzzleFlash();
        muzzleFlashShoot = assetMuzzleFlash.getMuzzleFlashShoot();
        muzzleFlashImpact = assetMuzzleFlash.getMuzzleFlashImpact();
        muzzleFlashWidth = assetMuzzleFlash.getWidth();
        muzzleFlashHeight = assetMuzzleFlash.getHeight();

        // Sets initial values for position, width and height and initial frame as muzzleFlashShoot.
        setBounds(x - muzzleFlashWidth / 2, y - muzzleFlashHeight / 2,
                muzzleFlashWidth, muzzleFlashHeight);
        setRegion(muzzleFlashShoot);

        // Box2d
        defineEgg();

        // Initial state
        currentState = State.MUZZLE_FLASH;

        // Stop the camera until this shot ends
        gameWorld.pauseCamera();

        // Audio effect
        AudioManager.getInstance().playSound(Assets.getInstance().getSounds().getThrowEgg());
    }

    private void defineEgg() {
        // Creates main body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2); // In box2D the origin is at the center of the body
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = gameWorld.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setGravityScale(GRAVITY_SCALE); // More gravity

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(CIRCLE_SHAPE_RADIUS_METERS);
        fixtureDef.filter.categoryBits = WorldContactListener.BULLET_BIT; // Depicts what this fixture is
        fixtureDef.filter.maskBits = WorldContactListener.ENEMY_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void onTarget(AbstractDynamicObject target) {
        currentState = State.IMPACT;
    }

    @Override
    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    @Override
    public void update(float deltaTime) {
        // Life cycle: MUZZLE_FLASH->SHOOT->IMPACT->KNOCK_BACK->FINISH->DISPOSE
        switch (currentState) {
            case MUZZLE_FLASH:
                stateMuzzleFlash(deltaTime);
                break;
            case SHOOT:
                stateShoot(deltaTime);
                break;
            case IMPACT:
                stateImpact(deltaTime);
                break;
            case KNOCK_BACK:
                stateKnockBack(deltaTime);
                break;
            case FINISH:
                stateFinish(deltaTime);
                break;
            case DISPOSE:
                break;
            default:
                break;
        }
    }

    private void stateMuzzleFlash(float deltaTime) {
        body.setLinearVelocity(0, VELOCITY);
        updateSpritePosition(deltaTime);
        currentState = State.SHOOT;
    }

    private void stateShoot(float deltaTime) {
        setBounds(getX() + getWidth() / 2 - eggWidth / 2,
                getY() + getHeight() / 2 - eggHeight / 2,
                eggWidth, eggHeight);
        setRegion(eggStand);
        updateSpritePosition(deltaTime);
        checkBoundaries();
    }

    private void stateImpact(float deltaTime) {
        // Muzzle flash
        setBounds(getX() + getWidth() / 2 - muzzleFlashWidth / 2,
                getY() + getHeight() / 2 - muzzleFlashHeight / 2,
                muzzleFlashWidth, muzzleFlashHeight);
        setRegion(muzzleFlashImpact); // Only one last frame
        updateSpritePosition(deltaTime);
        currentState = State.KNOCK_BACK;
    }

    private void stateKnockBack(float deltaTime) {
        // Knock back effect
        body.setLinearVelocity(0, VELOCITY);

        // Egg texture
        setBounds(getX() + getWidth() / 2 - eggWidth / 2,
                getY() + getHeight() / 2 - eggHeight / 2,
                eggWidth, eggHeight);
        setRegion(eggStand);
        updateSpritePosition(deltaTime);
        currentState = State.SHOOT;
    }

    private void checkBoundaries() {
        // When this bullet is outside the camera, it finishes.
        if (!isOnCamera()) {
            // Resume the camera movement
            gameWorld.resumeCamera();
            currentState = State.FINISH;
        }
    }

    private boolean isOnCamera() {
        return getY() + getHeight() > 0;
    }

    private void stateFinish(float deltaTime) {
        // Destroy box2D body
        gameWorld.destroyBody(body);
        currentState = State.DISPOSE;
    }

    private void updateSpritePosition(float deltaTime) {
        // Update this Sprite to correspond with the position of the Box2D body.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }

    @Override
    public boolean isDisposable() {
        return currentState == State.DISPOSE;
    }
}
