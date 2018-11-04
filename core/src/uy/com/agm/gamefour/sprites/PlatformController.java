package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;
import uy.com.agm.gamefour.game.GameWorld;
import uy.com.agm.gamefour.screens.play.PlayScreen;

/**
 * Created by AGMCORP on 26/9/2018.
 */

public class PlatformController {
    private static final String TAG = PlatformController.class.getName();

    private static int MAX_PLATFORMS = 5;
    public static float MIN_OFFSET_X = 0.3f;
    public static float MAX_OFFSET_X = 2.0f;
    public static float MIN_OFFSET_Y = 2.5f;
    public static float MAX_OFFSET_Y = 6.5f;
    public static final float MAX_VELOCITY = 1.5f;
    public static final float MIN_VELOCITY = 0.5f;

    private PlayScreen playScreen;
    private GameWorld gameWorld;
    private Array<Platform> platforms;

    public PlatformController(PlayScreen playScreen, GameWorld gameWorld) {
        this.playScreen = playScreen;
        this.gameWorld = gameWorld;
        platforms = new Array<Platform>();

        Platform platform;
        float x = 0;
        float y = gameWorld.getGameCamera().position().y;
        for (int i = 0; i < MAX_PLATFORMS; i++) {
            platform = new Platform(gameWorld, x, y);
            platforms.add(platform);
            x = platform.getX() + platform.getWidth() + getRandomX();
            y = getRandomY();
        }
    }

    private float getRandomX() {
        return MathUtils.random(MIN_OFFSET_X, MAX_OFFSET_X);
    }

    private float getRandomY() {
        return MathUtils.random(MIN_OFFSET_Y, MAX_OFFSET_Y);
    }

    public Array<Platform> getPlatforms() {
        return platforms;
    }

    public void update(float deltaTime) {
        for (Platform platform : platforms) {
            platform.update(deltaTime);
        }

        GameCamera gameCamera = gameWorld.getGameCamera();
        Platform pFirst = platforms.first();
        Platform pHead;
        Platform pTail;
        if (gameCamera.position().x - gameCamera.getWorldWidth() / 2 > pFirst.getX() + pFirst.getWidth()) {
            pHead = platforms.removeIndex(0);
            pTail = platforms.size > 0 ? platforms.get(platforms.size - 1) : pHead;
            pHead.reposition(pTail.getX() + pTail.getWidth() + getRandomX(), getRandomY());
            platforms.add(pHead);
        }
    }

    public void render(SpriteBatch batch) {
        for (Platform platform : platforms) {
            platform.render(batch);
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        for (Platform platform : platforms) {
            platform.renderDebug(shapeRenderer);
        }
    }
}
