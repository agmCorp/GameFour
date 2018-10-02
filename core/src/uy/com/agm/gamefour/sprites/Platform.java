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

public class Platform extends AbstractGameObject {
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
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = gameWorld.createBody(bodyDef);
        body.setFixedRotation(true);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(getWidth() / 2, getHeight() / 2);
        fixtureDef.filter.categoryBits = WorldContactListener.PLATFORM_BIT; // Depicts what this fixture is
        fixtureDef.filter.maskBits = WorldContactListener.JUMPER_BIT; // Depicts what this Fixture can collide with (see WorldContactListener)
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void setVelocity(float vx, float vy) {
        velocity.set(vx, vy);
    }

    public void reposition(float x) {
        setPosition(x, getY());
    }

    @Override
    public void update(float deltaTime) {
        // todo
        // Set velocity because It could have been changed (see reverseVelocity)..blabla
        //b2body.setLinearVelocity(velocity);

        setPosition(getX() + velocity.x * deltaTime, getY() + velocity.y * deltaTime);
        setRegion((TextureRegion) platformAnimation.getKeyFrame(stateTime, true));
        stateTime += deltaTime;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        draw(spriteBatch);
    }
}
