package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;

/**
 * Created by AGMCORP on 26/9/2018.
 */

public class Platforms {
    private static final String TAG = Platforms.class.getName();

    private GameCamera gameCamera;
    private Array<Platform> platforms;
    private static int MAX_PLATFORMS = 4;

    public Platforms(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
        platforms = new Array<Platform>();
        for (int i = 0; i < MAX_PLATFORMS; i++) {
            platforms.add(new Platform( 1f * i, 4f));
        }
    }

    public void update(int level, float deltaTime) {
        // TODO ACA DEBERIA VER POR NIVEL QUE HACER. y hacer el enroque
        for (Platform platform : platforms) {
            platform.update(deltaTime);
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
