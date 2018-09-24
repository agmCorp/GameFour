package uy.com.agm.gamefour.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.sprites.Jumper;
import uy.com.agm.gamefour.sprites.ParallaxSB;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class GameWorld {
    private static final String TAG = GameWorld.class.getName();

    private GameCamera gameCamera;

    // Objects of our game
    ParallaxSB parallaxSB;
    private Jumper jumper;

    public GameWorld() {
        gameCamera = new GameCamera();

        // Background
        parallaxSB = new ParallaxSB(gameCamera);
        Array<TextureRegion> colTextureRegion = new Array<TextureRegion>();

        boolean horizontal = false;
        float signo = -1;

        if (horizontal) {
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer11.png")), 1, horizontal, signo * 0.0f); // background
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer12.png")), 1, horizontal, signo * 0.0f); // chol
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer10.png")), 1, horizontal, signo * 0.0f); // estrellas
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer8.png")), 2, horizontal, signo * 4.0f); // nubes 1
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer7.png")), 2, horizontal, signo * 5.0f); // nubes 2
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer5.png")), 2, horizontal, signo * 7.0f); // montanas mas distantes
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer4.png")), 2, horizontal, signo * 8.0f);
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer3.png")), 2, horizontal, signo * 9.0f);
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer2.png")), 2, horizontal, signo * 10.0f);
            parallaxSB.addLayer(new TextureRegion(new Texture("Layer1.png")), 2, horizontal, signo * 11.0f);
        } else {
            // TODO
            // SI HAY UNA IMAGEN SOLA DA ARRAY EMPY, DEBERIA FUNCIONAR CON 1 IMAGEN SOLA EN MOVIMIENTO.
            // CONCLUSION, NO DEBO USAR LAS DIMENSIONES DE LA CAMARA, SINO LAS DIMENSIONES DEL MUNDO.
            // YO SE QUE SI PONGO 00 ESTOY EN EL VERTICE DE ABAJO, NO SE EL TOP, ESO DEPENDE DE LA PANTALLA
            // ES PROBABLE QUE ESTE MAS ALTO QUE EL FRUSTUMTOP.
            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer11.png")), 2, horizontal, signo * 2.0f); // background
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer12.png")), 1, horizontal, signo * 0.0f); // chol
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer10.png")), 1, horizontal, signo * 0.0f); // estrellas
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer8.png")), 2, horizontal, signo * 4.0f); // nubes 1
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer7.png")), 2, horizontal, signo * 5.0f); // nubes 2
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer5.png")), 2, horizontal, signo * 7.0f); // montanas mas distantes
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer4.png")), 2, horizontal, signo * 8.0f);
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer3.png")), 2, horizontal, signo * 9.0f);
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer2.png")), 2, horizontal, signo * 10.0f);
//            parallaxSB.addLayer(new TextureRegion(new Texture("rotaLayer1.png")), 2, horizontal, signo * 11.0f);
        }

        // Creates Jumper in the game world
        jumper = new Jumper(this, gameCamera.getWorldWidth() / 2, gameCamera.getWorldHeight() / 2);
    }

    public void update(float deltaTime) {
        parallaxSB.update(deltaTime);
        jumper.update(deltaTime);

        // Always at the end
        // Update the game camera with correct coordinates after changes
        gameCamera.update(deltaTime);
    }

    public void render(SpriteBatch batch) {
        // This order is important.
        // This determines if a sprite has to be drawn in front or behind another sprite.
        parallaxSB.render(batch);
        jumper.render(batch);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        parallaxSB.renderDebug(shapeRenderer);
        jumper.renderDebug(shapeRenderer);
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }
}

