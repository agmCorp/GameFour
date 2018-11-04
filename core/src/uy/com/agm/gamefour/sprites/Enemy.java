package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.sprites.AssetEnemy;
import uy.com.agm.gamefour.game.GameCamera;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.game.tools.WorldContactListener;
import uy.com.agm.gamefour.screens.play.PlayScreen;

/**
 * Created by AGM on 11/3/2018.
 */

public class Enemy extends AbstractDynamicObject {
    private static final String TAG = Enemy.class.getName();

    private static final float CIRCLE_SHAPE_RADIUS_METERS = 30.0f / GameCamera.PPM;

    private enum State {
        INACTIVE, ALIVE, KNOCK_BACK, EXPLODING, SPLAT, DEAD
    }
    private PlayScreen playScreen;
    private GameWorld gameWorld;
    private AssetEnemy assetEnemy;
    private TextureRegion enemyStand;
    private Animation enemyAnimation;
    protected State currentState;
    private float stateTime;
    private Body body;

    public Enemy(PlayScreen playScreen, GameWorld gameWorld) {
        this.playScreen = playScreen;
        this.gameWorld = gameWorld;

        assetEnemy = Assets.getInstance().getSprites().getEnemy();
        enemyStand = assetEnemy.getEnemyStand();
        enemyAnimation = assetEnemy.getEnemyAnimation();

        currentState = State.INACTIVE;
    }

    public void show(float x, float y) {
        // Sets initial values for location, width and height and initial frame as enemyStand.
        setBounds(x, y, assetEnemy.getWidth(), assetEnemy.getHeight());
        setRegion(enemyStand);
        stateTime = 0;

        // Box2d
        defineEnemy();

        // Enemy is alive
        currentState = State.ALIVE;
    }

    private void defineEnemy() {
        // Creates main body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2); // In box2D the origin is at the center of the body
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

        Gdx.app.debug(TAG, "CREO AL ENEMIGO***********************"); // TODO
    }

    public void onHit(Weapon weapon) { // todo es lamado por worldcontactlistener cuando lo revientan
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

    @Override
    public Vector2 getBodyPosition() {
        return body.getPosition();
    }

    @Override
    public void update(float deltaTime) {
        // Life cycle: INACTIVE->ALIVE->KNOCK_BACK->EXPLODING->SPLAT->DEAD->DISPOSE
        switch (currentState) {
            case INACTIVE:
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
            default:
                break;
        }
    }

    private void checkBoundaries() {
        // When this enemy is on camera, it activates (it moves and can collide).
        // When this enemy is outside the camera, it dies.
        if (isOnCamera()) {
            body.setActive(true);
        } else {
            if (body.isActive()) { // Was on camera
                currentState = State.DEAD;
            }
        }
    }

    private boolean isOnCamera() {
        GameCamera gameCamera = gameWorld.getGameCamera();
        float gameCameraPosX = gameCamera.position().x;
        float leftEdge = gameCameraPosX - gameCamera.getWorldWidth() / 2;
        float rightEdge = gameCameraPosX + gameCamera.getWorldWidth() / 2;

        return leftEdge <= getX() + getWidth() && getX() <= rightEdge;
    }

    private void stateAlive(float deltaTime) {
        // Update this Sprite to correspond with the position of the Box2D body.
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion((TextureRegion) enemyAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;

        checkBoundaries();
    }

    private void stateKnockBack(float deltaTime) {
        // todo
    }

    private void stateExploding(float deltaTime) {
        // todo aca poner filtro nothing!!!
    }

    private void stateSplat(float deltaTime) {
        // todo
    }

    private void stateDead(float deltaTime) {
        // Destroy box2D body
        gameWorld.destroyBody(body);
        currentState = State.INACTIVE;
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
            // Draw Enemy
            draw(spriteBatch);
        }
    }
}
