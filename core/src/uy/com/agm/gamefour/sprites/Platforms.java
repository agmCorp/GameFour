package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;

/**
 * Created by AGMCORP on 26/9/2018.
 */

public class Platforms {
    private static final String TAG = Platforms.class.getName();

    private static int MAX_PLATFORMS = 4;
    private static float MIN_PLATFORM_SPACING = 0.5f;
    private static float MAX_PLATFORM_SPACING = 3.0f;
    private static float OFFSET_Y = 1.0f;

    private GameCamera gameCamera;
    private Array<Platform> platforms;

    public Platforms(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
        platforms = new Array<Platform>();

        Platform platform;
        float x = 0;
        float y = gameCamera.position().y;
        for (int i = 0; i < MAX_PLATFORMS; i++) {
            platform = new Platform(x, y);
            platforms.add(platform);
            x = platform.getX() + platform.getWidth() + MathUtils.random(MIN_PLATFORM_SPACING, MAX_PLATFORM_SPACING);
            y = MathUtils.random(0, gameCamera.position().y + gameCamera.getWorldHeight() / 2 - OFFSET_Y);
        }
    }

    public Platform getPlatform(int index) {
        return platforms.get(index);
    }

    public void update(int level, float deltaTime) {
        for (Platform platform : platforms) {
            platform.update(deltaTime);
        }

        Platform pFirst = platforms.first();
        Platform pHead;
        Platform pTail;
        if (gameCamera.position().x - gameCamera.getWorldWidth() / 2 > pFirst.getX() + pFirst.getWidth()) {
            pHead = platforms.removeIndex(0);
            pTail = platforms.size > 0 ? platforms.get(platforms.size - 1) : pHead;
            pHead.setPosition(pTail.getX() + pTail.getWidth() + MathUtils.random(MIN_PLATFORM_SPACING, MAX_PLATFORM_SPACING), MathUtils.random(0, gameCamera.position().y + gameCamera.getWorldHeight() / 2 - OFFSET_Y));
            platforms.add(pHead);
        }

        if (level == 4) {
            for (Platform platform : platforms) {
                platform.setVelocity(0, 0.3f);
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
