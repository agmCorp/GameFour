package uy.com.agm.gamefour.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    private GameCamera gameCamera;

    // Objects of our game
    private Jumper jumper;

    public GameWorld() {
        gameCamera = new GameCamera();

        // Creates Jumper in the game world
        jumper = new Jumper(this, gameCamera.getFrustumWidth() / 2, gameCamera.getFrustumHeight() / 2);
    }

    public void update(float deltaTime) {
        jumper.update(deltaTime);

        // Always at the end
        // Update the game camera with correct coordinates after changes
        gameCamera.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        // This order is important.
        // This determines if a sprite has to be drawn in front or behind another sprite.
        jumper.render(batch);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        jumper.renderDebug(shapeRenderer);
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }
}

