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

public class Platforms {
    private static final String TAG = Platforms.class.getName();

    private static int MAX_PLATFORMS = 4;
    private static float MIN_PLATFORM_SPACING = 0.3f;
    private static float MAX_PLATFORM_SPACING = 2.0f;
    private static float OFFSET_Y = 2.5f;

    private PlayScreen playScreen;
    private GameWorld gameWorld;
    private Array<Platform> platforms;

    public Platforms(PlayScreen playScreen, GameWorld gameWorld) {
        this.playScreen = playScreen;
        this.gameWorld = gameWorld;
        platforms = new Array<Platform>();

        Platform platform;
        float x = 0;
        float y = gameWorld.getGameCamera().position().y;
        for (int i = 0; i < MAX_PLATFORMS; i++) {
            platform = new Platform(gameWorld, x, y);
            platforms.add(platform);
            x = platform.getX() + platform.getWidth() + getRandomSpacing();
            y = getRandomY();
        }
    }

    private float getRandomSpacing() {
        return MathUtils.random(MIN_PLATFORM_SPACING, MAX_PLATFORM_SPACING);
    }

    private float getRandomY() {
        GameCamera gameCamera = gameWorld.getGameCamera();
        return MathUtils.random(OFFSET_Y, gameCamera.position().y + gameCamera.getWorldHeight() / 2 - OFFSET_Y);
    }

    public Platform getPlatform(int index) {
        return platforms.get(index);
    }

    public void update(int level, float deltaTime) {
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
            pHead.reposition(pTail.getX() + pTail.getWidth() + getRandomSpacing(), getRandomY());
            platforms.add(pHead);
        }

        // todo ORQUESTA TODAS LAS VARIACIONES
        if (level == 3 || level == 4) {
            for (Platform platform : platforms) {
                // They could be two platforms
                if (platform.isTouched()) {
                    platform.startMovement();
                }
            }
        }

        if (level == 5) {
            for (Platform platform : platforms) {
                platform.stopMovement();
            }
        }

        if (level == 7) {
            for (Platform platform : platforms) {
                platform.startMovement();
            }
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
