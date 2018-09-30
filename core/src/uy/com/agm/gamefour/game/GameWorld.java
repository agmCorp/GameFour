package uy.com.agm.gamefour.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import uy.com.agm.gamefour.sprites.Jumper;
import uy.com.agm.gamefour.sprites.ParallaxSB;
import uy.com.agm.gamefour.sprites.Platforms;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    private int level;
    private GameCamera gameCamera;
    private boolean moveCamera;
    ParallaxSB parallaxSB;
    private Platforms platforms;
    private Jumper jumper;

    public GameWorld(int level) {
        this.level = level;
        gameCamera = new GameCamera();
        moveCamera = false;

        // Background
        createBackground();

        // Platforms
        platforms = new Platforms(gameCamera);

        // Jumper
        jumper = new Jumper(this, gameCamera.getWorldWidth() / 2, gameCamera.getWorldHeight() / 2);
    }

    private void createBackground() {
        boolean horizontal = true;
        byte signo = -1;

        /* todo
        LE ESTOY ERRANDO POR UN PIXEL PUTO AL UNIR TEXTURAS
         */

        parallaxSB = new ParallaxSB(gameCamera);
        parallaxSB.addFarawayLayer(new TextureRegion(new Texture("Layer10.png"))); // background
        parallaxSB.addFarawayLayer(new TextureRegion(new Texture("Layer9.png"))); // sol
        parallaxSB.addFarawayLayer(new TextureRegion(new Texture("Layer8.png"))); // estrellas

        parallaxSB.addDynamicLayer(new TextureRegion(new Texture("Layer7.png")), 2, horizontal, signo * 1.0f); // nubes 1
        parallaxSB.addDynamicLayer(new TextureRegion(new Texture("Layer6.png")), 2, horizontal, signo * 1.5f); // nubes 2
        parallaxSB.addDynamicLayer(new TextureRegion(new Texture("Layer5.png")), 2, horizontal, signo * 0.2f); // montanas mas distantes
        parallaxSB.addDynamicLayer(new TextureRegion(new Texture("Layer4.png")), 2, horizontal, signo * 1.0f);
        parallaxSB.addDynamicLayer(new TextureRegion(new Texture("Layer3.png")), 2, horizontal, signo * 1.5f);
        parallaxSB.addDynamicLayer(new TextureRegion(new Texture("Layer2.png")), 2, horizontal, signo * 2.0f);
        parallaxSB.addDynamicLayer(new TextureRegion(new Texture("Layer1.png")), 2, horizontal, signo * 5.0f);
    }

    public void update(float deltaTime) {
        Gdx.app.debug(TAG, "*** LEVEL A " + level);

        jumper.update(deltaTime);
        centerCamera(deltaTime);
        platforms.update(level, deltaTime);

        // Always at the end
        // Update the game camera with correct coordinates after changes
        gameCamera.update(deltaTime);

        Gdx.app.debug(TAG, "*** LEVEL B " + level);
    }

    private void centerCamera(float deltaTime) {
        if (moveCamera) {
            float velocityDeCamara = 1.0f;
            Gdx.app.debug(TAG, "**** muevo camara");

            // TODO movecamera se pone en false cuando llego a la poscion de hero.
            gameCamera.position().x = gameCamera.position().x + velocityDeCamara * deltaTime;


            parallaxSB.update(deltaTime);

            if (gameCamera.position().x - gameCamera.getWorldWidth() / 2 > jumper.position().x) {
                Gdx.app.debug(TAG, "**** dejo de mover camara " + (gameCamera.position().x - gameCamera.getWorldWidth() / 2) + " JUMPER: " + jumper.position().x );
                moveCamera = false;
            }
        }
    }

    public void render(SpriteBatch batch) {
        // This order is important.
        // This determines if a sprite has to be drawn in front or behind another sprite.
        parallaxSB.render(batch);
        platforms.render(batch);
        jumper.render(batch);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        parallaxSB.renderDebug(shapeRenderer);
        platforms.renderDebug(shapeRenderer);
        jumper.renderDebug(shapeRenderer);
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
    }
}

