package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.backgrounds.AssetBackgrounds;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.sprites.Enemy;
import uy.com.agm.gamefour.sprites.Jumper;
import uy.com.agm.gamefour.sprites.ParallaxSB;
import uy.com.agm.gamefour.sprites.Platform;
import uy.com.agm.gamefour.sprites.PlatformController;
import uy.com.agm.gamefour.sprites.Weapon;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    private static final float CAMERA_VELOCITY = 4.0f;

    private PlayScreen playScreen;
    private World box2DWorld;
    private int level;
    private GameCamera gameCamera;
    private boolean moveCamera;
    private ParallaxSB parallaxSB;
    private PlatformController platformController;
    private Jumper jumper;
    private Array<Enemy> enemies;

    public GameWorld(PlayScreen playScreen, World box2DWorld, int level) {
        this.playScreen = playScreen;
        this.box2DWorld = box2DWorld;
        this.level = level;
        gameCamera = new GameCamera();
        moveCamera = false;

        createSprites();
        createBackground();
    }

    private void createBackground() {
        parallaxSB = new ParallaxSB(gameCamera);

        if (!DebugConstants.HIDE_BACKGROUND) {
            loadBackground();
        }
    }

    private void loadBackground() {
        AssetBackgrounds assetBackgrounds = Assets.getInstance().getBackgrounds();
        GameSettings prefs = GameSettings.getInstance();
        int backgroundId = prefs.getBackgroundId();
        prefs.setBackgroundId((backgroundId % AssetBackgrounds.MAX_BACKGROUNDS) + 1);
        prefs.save();

        switch (backgroundId) {
            case 1:
                assetBackgrounds.getDesert().build(parallaxSB);
                jumper.setColor(Color.SKY);
                break;
            case 2:
                assetBackgrounds.getForest().build(parallaxSB);
                jumper.setColor(Color.SALMON);
                break;
            case 3:
                assetBackgrounds.getBeach().build(parallaxSB);
                jumper.setColor(Color.CHARTREUSE);
                break;
            case 4:
                assetBackgrounds.getWaterfall().build(parallaxSB);
                jumper.setColor(Color.VIOLET);
                break;
            case 5:
                assetBackgrounds.getHills().build(parallaxSB);
                jumper.setColor(Color.GOLD);
                break;
            default:
                break;
        }
    }

    private void createSprites() {
        // Platforms
        platformController = new PlatformController(playScreen, this);

        // Jumper
        jumper = new Jumper(playScreen, this, platformController.getPlatforms().get(0).getBodyPosition().x -
                Assets.getInstance().getSprites().getJumper().getWidth() / 2,
                gameCamera.position().y + gameCamera.getWorldHeight() / 2);

        // Enemies
        enemies = new Array<Enemy>();
    }

    public void update(float deltaTime) {
        parallaxSB.update(deltaTime);
        jumper.update(deltaTime);
        centerCamera(deltaTime);
        platformController.update(deltaTime);
        updateEnemies(deltaTime);

        // Always at the end
        // Update the game camera with correct coordinates after changes
        gameCamera.update(deltaTime);
    }

    private void updateEnemies(float deltaTime) {
        Enemy enemy;
        Iterator<Enemy> iterator = enemies.iterator();
        while(iterator.hasNext()) {
            enemy = iterator.next();
            enemy.update(deltaTime);
            if(enemy.isDisposable()){
                iterator.remove();
            }
        }
    }

    private void centerCamera(float deltaTime) {
        if (moveCamera) {
            gameCamera.position().x = gameCamera.position().x + CAMERA_VELOCITY * deltaTime;
            moveCamera = gameCamera.position().x - gameCamera.getWorldWidth() / 2 <= jumper.getBodyPosition().x - jumper.getWidth() / 2;
        }
    }

    public void render(SpriteBatch batch) {
        // This order is important.
        // This determines if a sprite has to be drawn in front or behind another sprite.
        parallaxSB.render(batch);
        platformController.render(batch);
        renderEnemies(batch);
        jumper.render(batch);
    }

    private void renderEnemies(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
    }

    public void renderSpriteDebug(ShapeRenderer shapeRenderer) {
        parallaxSB.renderDebug(shapeRenderer);
        platformController.renderDebug(shapeRenderer);
        jumper.renderDebug(shapeRenderer);
        renderDebugEnemies(shapeRenderer);
    }

    private void renderDebugEnemies(ShapeRenderer shapeRenderer) {
        for (Enemy enemy : enemies) {
            enemy.renderDebug(shapeRenderer);
        }
    }

    public void renderBox2DDebug(Box2DDebugRenderer box2DDebugRenderer) {
        box2DDebugRenderer.render(box2DWorld, gameCamera.getCombined());
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public PlatformController getPlatformController() {
        return platformController;
    }

    public Jumper getJumper() {
        return jumper;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public void addLevel() {
        level++;
        moveCamera = true;
        newLevel();

        // todo
        Gdx.app.debug(TAG, "ESTOY EN NIVEL: " + level);

    }


public void laConchaDeTuMadre() { // todo
    for(Enemy enemy : enemies) {
        enemy.onHit(new Weapon());
    }
}
    private void newLevel() {
        Array<Platform> platforms = platformController.getPlatforms();

//        // todo ORQUESTA TODAS LAS VARIACIONES
//        if (level > 0) {
//            for (Platform platform : platforms) {
//                platform.startMovement();
//            }
//        }
//
//        if (level == 5) {
//            for (Platform platform :platforms) {
//                platform.stopMovement();
//            }
//        }

        //if (level == 3 || level == 9) {
        if (level > 0) {
            // There must be at least two platforms to have a valid game.
            Platform secondLastPlatform = platforms.get(platforms.size - 2);
            Platform lastPlatform = platforms.get(platforms.size - 1);
            enemies.add(new Enemy(playScreen, this, secondLastPlatform, lastPlatform));
        }
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

