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
import uy.com.agm.gamefour.game.GameCamera;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;

/**
 * Created by AGM on 11/3/2018.
 */

    /*
    * tener audio
    * tener muzzleflash
    * destruirse al irse de pantalla por abajo.
    * */
public class Bullet extends AbstractDynamicObject {
    private static final String TAG = Bullet.class.getName();

    private static final float CIRCLE_SHAPE_RADIUS_METERS = 20.0f / GameCamera.PPM;
    public static final float VELOCITY = 4.0f;
    public static final float GRAVITY_SCALE = 1.5f;

    private enum State {
        SHOOT, IMPACT, FINISH, DISPOSE
    }
    private GameWorld gameWorld;
    private TextureRegion eggStand;
    private Body body;
    private State currentState;

    public Bullet(GameWorld gameWorld, float x, float y) {
        this.gameWorld = gameWorld;

        // Random texture
        setNewTexture(x, y);

        // Box2d
        defineEgg();

        body.setLinearVelocity(0, VELOCITY);
        currentState = State.SHOOT;

        // Stop the camera until this shot ends
        gameWorld.pauseCamera();
    }

    private void setNewTexture(float x, float y) {
        AssetEgg assetEgg = Assets.getInstance().getSprites().getEgg();
        eggStand = assetEgg.getRandomEgg();

        // Sets initial values for position, width and height and initial frame as platformStand.
        setBounds(x, y, assetEgg.getWidth(), assetEgg.getHeight());
        setRegion(eggStand);
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
        // Life cycle: SHOOT->IMPACT->KNOCK_BACK->DISPOSE
        switch (currentState) {
            case SHOOT:
                stateShoot(deltaTime);
                break;
            case IMPACT:
                stateImpact(deltaTime);
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

    private void stateShoot(float deltaTime) {
        updateSpritePosition(deltaTime);
        checkBoundaries();
    }

    private void stateImpact(float deltaTime) {
        // Knock back effect
        body.setLinearVelocity(0, VELOCITY);
        updateSpritePosition(deltaTime);
        currentState = State.SHOOT;
    }

    private void checkBoundaries() {
        // When this enemy is outside the camera, it dies.
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
