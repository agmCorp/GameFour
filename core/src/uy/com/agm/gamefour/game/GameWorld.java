package uy.com.agm.gamefour.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import uy.com.agm.gamefour.assets.Assets;
import uy.com.agm.gamefour.assets.backgrounds.AssetBackgrounds;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.sprites.AbstractGameObject;
import uy.com.agm.gamefour.sprites.Bullet;
import uy.com.agm.gamefour.sprites.Enemy;
import uy.com.agm.gamefour.sprites.Jumper;
import uy.com.agm.gamefour.sprites.ParallaxSB;
import uy.com.agm.gamefour.sprites.Platform;
import uy.com.agm.gamefour.sprites.PlatformController;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    private static final float CAMERA_VELOCITY = 4.0f;
    private static final float ENEMY_PROBABILITY = 0.3f; // 30%

    private PlayScreen playScreen;
    private World box2DWorld;
    private int level;
    private GameCamera gameCamera;
    private boolean moveCamera;
    private boolean pauseCamera;
    private ParallaxSB parallaxSB;
    private PlatformController platformController;
    private Jumper jumper;
    private Array<Enemy> enemies;
    private Array<Bullet> bullets;
    private Array<AbstractGameObject> gameObjectsToCreate;

    public GameWorld(PlayScreen playScreen, World box2DWorld, int level) {
        this.playScreen = playScreen;
        this.box2DWorld = box2DWorld;
        this.level = level;
        gameCamera = new GameCamera();
        moveCamera = false;
        pauseCamera = false;

        createSprites();
        createBackground();

        // Queue
        gameObjectsToCreate = new Array<AbstractGameObject>();
    }

    private void createSprites() {
        // Platforms
        platformController = new PlatformController(playScreen, this);

        Array<Platform> platforms = platformController.getPlatforms();
        Platform p0 = platforms.get(0);
        Platform p1 = platforms.get(1);
        Platform p2 = platforms.get(2);

        // Jumper
        jumper = new Jumper(playScreen, this, p0.getBodyPosition().x -
                Assets.getInstance().getSprites().getJumper().getWidth() / 2,
                gameCamera.position().y + gameCamera.getWorldHeight() / 2);

        // Enemies
        enemies = new Array<Enemy>();
        Enemy sampleEnemy = new Enemy(playScreen, this, p1, p2);
        enemies.add(sampleEnemy);

        // Eggs
        bullets = new Array<Bullet>();
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


        backgroundId = 10;
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
            case 6:
                assetBackgrounds.getCastle().build(parallaxSB);
                jumper.setColor(Color.OLIVE);
                break;
            case 7:
                assetBackgrounds.getZombie().build(parallaxSB);
                jumper.setColor(Color.WHITE);
                break;
            case 8:
                assetBackgrounds.getSpider().build(parallaxSB);
                jumper.setColor(Color.CYAN);
                break;
            case 9:
                assetBackgrounds.getGhost().build(parallaxSB);
                jumper.setColor(Color.YELLOW);
                break;
            case 10:
                assetBackgrounds.getNight().build(parallaxSB);
                jumper.setColor(Color.RED);
                break;
            case 11:
                assetBackgrounds.getCandy().build(parallaxSB);
                jumper.setColor(Color.ORANGE);
                break;
            default:
                break;
        }
    }

    public void handleGameObjectsToCreate() {
        while (gameObjectsToCreate.size > 0) {
            AbstractGameObject gameObject = gameObjectsToCreate.pop();

            if (gameObject instanceof Bullet) {
                bullets.add((Bullet) gameObject);
            }
            if (gameObject instanceof Enemy) {
                enemies.add((Enemy) gameObject);
            }
        }
    }

    public void createGameObject(AbstractGameObject gameObject) {
        gameObjectsToCreate.add(gameObject);
    }

    public void update(float deltaTime) {
        parallaxSB.update(deltaTime);
        jumper.update(deltaTime);
        centerCamera(deltaTime);
        platformController.update(deltaTime);
        updateEnemies(deltaTime);
        updateBullets(deltaTime);

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

    private void updateBullets(float deltaTime) {
        Bullet bullet;
        Iterator<Bullet> iterator = bullets.iterator();
        while(iterator.hasNext()) {
            bullet = iterator.next();
            bullet.update(deltaTime);
            if(bullet.isDisposable()){
                iterator.remove();
            }
        }
    }

    private void centerCamera(float deltaTime) {
        if (moveCamera && !pauseCamera) {
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
        renderBullets(batch);
        jumper.render(batch);
    }

    private void renderEnemies(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
    }

    private void renderBullets(SpriteBatch batch) {
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    public void renderSpriteDebug(ShapeRenderer shapeRenderer) {
        parallaxSB.renderDebug(shapeRenderer);
        platformController.renderDebug(shapeRenderer);
        renderDebugEnemies(shapeRenderer);
        renderDebugBullets(shapeRenderer);
        jumper.renderDebug(shapeRenderer);
    }

    private void renderDebugEnemies(ShapeRenderer shapeRenderer) {
        for (Enemy enemy : enemies) {
            enemy.renderDebug(shapeRenderer);
        }
    }

    private void renderDebugBullets(ShapeRenderer shapeRenderer) {
        for (Bullet bullet : bullets) {
            bullet.renderDebug(shapeRenderer);
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

    public Array<Bullet> getBullets() {
        return bullets;
    }

    public void addLevel() {
        level++;
        moveCamera = true;
        newLevel();
    }

    private void newLevel() {
        Array<Platform> platforms = platformController.getPlatforms();

        // Current platform starts to move in levels 6, 7 and 8.
        if (level == 5 || level == 6 || level == 7) {
            jumper.getCurrentPlatform().startMovement();
        }

        // Platforms start to move in levels above 8.
        if (level >= 8) {
            for (Platform platform : platforms) {
                platform.startMovement();
            }
        }

        // Enemies turn up in levels above 2 with ENEMY_PROBABILITY chance (except for the sample enemy in level 1)
        if (level > 2) {
            if (MathUtils.random() <= ENEMY_PROBABILITY) {
                // There must be at least two platforms to have a valid game.
                Platform secondLastPlatform = platforms.get(platforms.size - 2);
                Platform lastPlatform = platforms.get(platforms.size - 1);
                createGameObject(new Enemy(playScreen, this, secondLastPlatform, lastPlatform));
            }
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

    public void pauseCamera() {
        pauseCamera = true;
    }

    public void resumeCamera() {
        pauseCamera = false;
    }
}

