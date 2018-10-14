package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.backgrounds.AssetBackgrounds;
import uy.com.agm.gamefour.assets.backgrounds.AssetBeach;
import uy.com.agm.gamefour.assets.backgrounds.AssetDesert;
import uy.com.agm.gamefour.assets.backgrounds.AssetForest;
import uy.com.agm.gamefour.assets.backgrounds.AssetHills;
import uy.com.agm.gamefour.sprites.Jumper;
import uy.com.agm.gamefour.sprites.ParallaxSB;
import uy.com.agm.gamefour.sprites.Platforms;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    private World box2DWorld;
    private int level;
    private GameCamera gameCamera;
    private boolean moveCamera;
    private ParallaxSB parallaxSB;
    private Platforms platforms;
    private Jumper jumper;

    public GameWorld(World box2DWorld, int level) {
        this.box2DWorld = box2DWorld;
        this.level = level;
        gameCamera = new GameCamera();
        moveCamera = false;

        createSprites();
        createBackground();
    }

    private void createBackground() {
        parallaxSB = new ParallaxSB(gameCamera);

        int background = MathUtils.random(1, AssetBackgrounds.MAX_BACKGROUNDS);
        switch (background) {
            case 1:
                loadDesertBackground();
                jumper.setColor(Color.SKY);
                break;
            case 2:
                loadForestBackground();
                jumper.setColor(new Color(0xef524fff));
                break;
            case 3:
                loadBeachBackground();
                jumper.setColor(Color.CHARTREUSE);
                break;
            case 4:
                loadHillsBackground();
                jumper.setColor(Color.GOLD);
                break;
            default:
                break;
        }
    }

    private void createSprites() {
        // Platforms
        platforms = new Platforms(this);

        // Jumper
        jumper = new Jumper(this, platforms.getPlatform(0).getBodyPosition().x -
                Assets.getInstance().getSprites().getJumper().getWidth() / 2,
                gameCamera.position().y + gameCamera.getWorldHeight() / 2);
    }

    public void update(float deltaTime) {
        parallaxSB.update(deltaTime);
        jumper.update(deltaTime);
        centerCamera(deltaTime);
        platforms.update(level, deltaTime);

        // Always at the end
        // Update the game camera with correct coordinates after changes
        gameCamera.update(deltaTime);
    }

    private void centerCamera(float deltaTime) {
        // no borrar esto
        if (moveCamera) {
            float velocityDeCamara = 1.0f;
            //Gdx.app.debug(TAG, "**** muevo camara");

            // TODO movecamera se pone en false cuando llego a la poscion de pajaro.
            gameCamera.position().x = gameCamera.position().x + velocityDeCamara * deltaTime;

            if (gameCamera.position().x - gameCamera.getWorldWidth() / 2 > jumper.getBodyPosition().x - jumper.getWidth() / 2) {
                //Gdx.app.debug(TAG, "**** dejo de mover camara " + (gameCamera.position().x - gameCamera.getWorldWidth() / 2) + " JUMPER: " + jumper.getBodyPosition().x );
                moveCamera = false;
            }
        }
    }

    private void loadForestBackground() {
        AssetForest assetForest = Assets.getInstance().getBackgrounds().getForest();
        parallaxSB.addDynamicLayer(assetForest.getLayer6(), 2, true, -0.1f);
        parallaxSB.addDynamicLayer(assetForest.getLayer5(), 2, true, -0.3f);
        parallaxSB.addDynamicLayer(assetForest.getLayer4(), 2, true, -0.6f);
        parallaxSB.addDynamicLayer(assetForest.getLayer3(), 2, true, -1.0f);
        parallaxSB.addDynamicLayer(assetForest.getLayer2(), 2, true, -2.0f);
        parallaxSB.addDynamicLayer(assetForest.getLayer1(), 2, true, -5.0f);
    }

    private void loadDesertBackground() {
        AssetDesert assetDesert = Assets.getInstance().getBackgrounds().getDesert();
        parallaxSB.addFarawayLayer(assetDesert.getLayer10()); // background
        parallaxSB.addFarawayLayer(assetDesert.getLayer9()); // sun
        parallaxSB.addFarawayLayer(assetDesert.getLayer8()); // stars

        parallaxSB.addDynamicLayer(assetDesert.getLayer7(), 2, true, -0.5f); // clouds 1
        parallaxSB.addDynamicLayer(assetDesert.getLayer6(), 2, true, -1.0f); // clouds 2
        parallaxSB.addDynamicLayer(assetDesert.getLayer5(), 2, true, -0.1f); // distant mountains
        parallaxSB.addDynamicLayer(assetDesert.getLayer4(), 2, true, -0.5f);
        parallaxSB.addDynamicLayer(assetDesert.getLayer3(), 2, true, -1.0f);
        parallaxSB.addDynamicLayer(assetDesert.getLayer2(), 2, true, -2.0f);
        parallaxSB.addDynamicLayer(assetDesert.getLayer1(), 2, true, -5.0f);
    }

    private void loadBeachBackground() {
        AssetBeach assetBeach = Assets.getInstance().getBackgrounds().getBeach();
        parallaxSB.addFarawayLayer(assetBeach.getLayer7()); // background
        parallaxSB.addFarawayLayer(assetBeach.getLayer6()); // sun

        parallaxSB.addDynamicLayer(assetBeach.getLayer5(), 2, true, -0.1f); // sea
        parallaxSB.addDynamicLayer(assetBeach.getLayer4(), 2, true, -0.5f); // clouds 1
        parallaxSB.addDynamicLayer(assetBeach.getLayer3(), 2, true, -1.0f); // clouds 2

        Array<TextureRegion> colTextureRegion = new Array<TextureRegion>();
        colTextureRegion.add(assetBeach.getLayer2A());
        colTextureRegion.add(assetBeach.getLayer2B());
        parallaxSB.addDynamicLayer(colTextureRegion, true, -0.5f); // island, boat

        colTextureRegion.clear();
        colTextureRegion.add(assetBeach.getLayer1A());
        colTextureRegion.add(assetBeach.getLayer1B());
        parallaxSB.addDynamicLayer(colTextureRegion, true, -3.0f); // sand
    }

    private void loadHillsBackground() {
        AssetHills assetHills = Assets.getInstance().getBackgrounds().getHills();
        parallaxSB.addFarawayLayer(assetHills.getLayer11()); // background

        parallaxSB.addDynamicLayer(assetHills.getLayer10(), 2, true, -0.5f); // clouds 1
        parallaxSB.addDynamicLayer(assetHills.getLayer9(), 2, true, -0.8f); // clouds 2
        parallaxSB.addDynamicLayer(assetHills.getLayer8(), 2, true, -1.0f); // clouds 3
        parallaxSB.addDynamicLayer(assetHills.getLayer7(), 2, true, -0.1f); // distant hills
        parallaxSB.addDynamicLayer(assetHills.getLayer6(), 2, true, -0.4f);
        parallaxSB.addDynamicLayer(assetHills.getLayer5(), 2, true, -0.7f);
        parallaxSB.addDynamicLayer(assetHills.getLayer4(), 2, true, -0.9f);
        parallaxSB.addDynamicLayer(assetHills.getLayer3(), 2, true, -1.3f);
        parallaxSB.addDynamicLayer(assetHills.getLayer2(), 2, true, -2.0f);
        parallaxSB.addDynamicLayer(assetHills.getLayer1(), 2, true, -4.0f);
    }

    public void render(SpriteBatch batch) {
        // This order is important.
        // This determines if a sprite has to be drawn in front or behind another sprite.
        parallaxSB.render(batch);
        platforms.render(batch);
        jumper.render(batch);
    }

    public void renderSpriteDebug(ShapeRenderer shapeRenderer) {
        parallaxSB.renderDebug(shapeRenderer);
        platforms.renderDebug(shapeRenderer);
        jumper.renderDebug(shapeRenderer);
    }

    public void renderBox2DDebug(Box2DDebugRenderer box2DDebugRenderer) {
        box2DDebugRenderer.render(box2DWorld, gameCamera.getCombined());
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public Platforms getPlatforms() {
        return platforms;
    }

    public Jumper getJumper() {
        return jumper;
    }

    public void addLevel() {
        level++;
        moveCamera = true;

        // // TODO: 10/2/2018
        Gdx.app.debug(TAG, "****LEVEL " + level);
    }

    public int getLevel() {
        return level;
    }

    public Body createBody(BodyDef bodyDef) {
        return box2DWorld.createBody(bodyDef);
    }

    public void destroyBody (Body body) {
        if (!box2DWorld.isLocked()) {
            box2DWorld.destroyBody(body);
        }
    }
}

