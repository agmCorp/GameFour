package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import uy.com.agm.gamefour.game.GameCamera;

/**
 * Created by AGM on 9/23/2018.
 */

// Scrolling background
public class ParallaxSB {
    private static final String TAG = ParallaxSB.class.getName();

    private GameCamera gameCamera;
    private Array<Layer> layers;

    public ParallaxSB(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
        layers = new Array<Layer>();
    }

    public void addLayer(TextureRegion textureRegion, int repeat, boolean horizontalScroll, float velocity) {
        if (repeat > 0) {
            Array<TextureRegion> colTextureRegion = new Array<TextureRegion>(repeat);
            for (int i = 0; i < repeat; i++) {
                colTextureRegion.add(textureRegion);
            }
            addLayer(colTextureRegion, horizontalScroll, velocity);
        }
    }

    public void addLayer(Array<TextureRegion> colTextureRegion, boolean horizontalScroll, float velocity) {
        Vector3 gameCamPos = gameCamera.position();
        TextureRegion bgFirst = colTextureRegion.first();
        float x = gameCamPos.x - ( bgFirst.getRegionWidth() / GameCamera.PPM ) / 2;
        float y = gameCamPos.y - ( bgFirst.getRegionHeight() / GameCamera.PPM ) / 2;

        Array<BackgroundObject> colBgObject = new Array<BackgroundObject>(colTextureRegion.size);
        BackgroundObject backgroundObject;
        for (TextureRegion textureRegion : colTextureRegion) {
            backgroundObject = new BackgroundObject(textureRegion, x, y, horizontalScroll, velocity);
            if (horizontalScroll) {
                x += textureRegion.getRegionWidth() / GameCamera.PPM; // Grows to the right
            } else {
                y += textureRegion.getRegionHeight() / GameCamera.PPM; // Grows up
            }
            colBgObject.add(backgroundObject);
        }
        Layer layer = new Layer(colBgObject, horizontalScroll, velocity);
        layers.add(layer);

//        TODO
//        float frustumWidth = gameCamera.getWorldWidth();
//        float frustumHeight = gameCamera.getWorldHeight();
//        float gameCamLeft = gameCamera.position().x - frustumWidth / 2;
//        float gameCamRight = gameCamera.position().x + frustumWidth / 2;
//        float gameCamBottom = gameCamera.position().y - frustumHeight / 2;
//        float gameCamTop = gameCamera.position().y + frustumHeight / 2;
//        Gdx.app.debug(TAG, "****** COMIENZO LOGUEOS 1*****");
//        Gdx.app.debug(TAG, "****** ancho del mundo " + frustumWidth);
//        Gdx.app.debug(TAG, "****** alto del mundo " + frustumHeight);
//        Gdx.app.debug(TAG, "****** camara left (debe ser cero) " + gameCamLeft);
//        Gdx.app.debug(TAG, "****** camara right " + gameCamRight);
//        Gdx.app.debug(TAG, "****** camara bottom (debe ser cero) " + gameCamBottom);
//        Gdx.app.debug(TAG, "****** camara top  " + gameCamTop);
    }

    public void update(float deltaTime) {
        float worldWidth = gameCamera.getWorldWidth();
        float worldHeight = gameCamera.getWorldHeight();
        float gameCamLeft = gameCamera.position().x - worldWidth / 2;
        float gameCamRight = gameCamera.position().x + worldWidth / 2;
        float gameCamBottom = gameCamera.position().y - worldHeight / 2;
        float gameCamTop = gameCamera.position().y + worldHeight / 2;

        for (Layer layer : layers) {
            layer.update(gameCamLeft, gameCamRight, gameCamBottom, gameCamTop, deltaTime);
        }
    }

    public void render(SpriteBatch spriteBatch) {
        for (Layer layer : layers) {
            layer.render(spriteBatch);
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        for (Layer layer : layers) {
            layer.renderDebug(shapeRenderer);
        }
    }

    private class Layer {
        private Array<BackgroundObject> colBgObject;
        private boolean horizontalScroll;
        private float velocity;

        public Layer(Array<BackgroundObject> colBgObject, boolean horizontalScroll, float velocity) {
            this.colBgObject = colBgObject;
            this.horizontalScroll = horizontalScroll;
            this.velocity = velocity;
        }

        public void update(float gameCamLeft, float gameCamRight, float gameCamBottom, float gameCamTop, float deltaTime) {
            for (BackgroundObject backgroundObject : colBgObject) {
                backgroundObject.update(deltaTime);
            }

            if (horizontalScroll) {
                    updateHorizontal(gameCamLeft, gameCamRight);
            } else {
                updateVertical(gameCamBottom, gameCamTop);
            }
        }

        private void updateHorizontal(float gameCamLeft, float gameCamRight) {
            if (velocity < 0) { // Layer is moving left
                BackgroundObject bgFirst = colBgObject.first();
                if (gameCamLeft > bgFirst.getX() + bgFirst.getWidth()) {
                    addHLast(colBgObject.removeIndex(0));
                }
            } else {
                if (velocity > 0) { // Layer is moving right
                    BackgroundObject bgLast = colBgObject.peek();
                    if (gameCamRight < bgLast.getX()) {
                        addHFirst(colBgObject.pop());
                    }
                }
            }
        }

        private void updateVertical(float gameCamBottom, float gameCamTop) {
            if (velocity < 0) { // Layer is moving down
                BackgroundObject bgLast = colBgObject.peek();
                if (gameCamBottom > bgLast.getY() + bgLast.getHeight()) {
                    addVFirst(colBgObject.pop());
                }
            } else {
                if (velocity > 0) { // Layer is moving up
                    BackgroundObject bgFirst = colBgObject.first();
                    if (gameCamTop < bgFirst.getY()) {
                        addVLast(colBgObject.removeIndex(0));
                    }
                }
            }
        }

        // todo estas funciones deberian considerar si la coleccion no es vacia, para mi habría que asumir que tienen 2 o más elementos.
        private void addHLast(BackgroundObject backgroundObject) {
            BackgroundObject bgLast = colBgObject.get(colBgObject.size - 1);
            backgroundObject.setPosition(bgLast.getX() + bgLast.getWidth(), bgLast.getY());
            colBgObject.add(backgroundObject);
        }

        private void addVLast(BackgroundObject backgroundObject) {
            BackgroundObject bgLast = colBgObject.get(colBgObject.size - 1);
            backgroundObject.setPosition(bgLast.getX(), bgLast.getY() - backgroundObject.getHeight());
            colBgObject.add(backgroundObject);
        }

        private void addHFirst(BackgroundObject backgroundObject) {
            BackgroundObject bgFirst = colBgObject.first();
            backgroundObject.setPosition(bgFirst.getX() - backgroundObject.getWidth(), bgFirst.getY());
            colBgObject.insert(0, backgroundObject);
        }

        private void addVFirst(BackgroundObject backgroundObject) {
            BackgroundObject bgFirst = colBgObject.first();
            backgroundObject.setPosition(bgFirst.getX(), bgFirst.getY() +  bgFirst.getHeight());
            colBgObject.insert(0, backgroundObject);
        }
        // fin todo estas funciones deberian considerar si la coleccion no es vacia

        public void render(SpriteBatch spriteBatch) {
            for (BackgroundObject backgroundObject : colBgObject) {
                backgroundObject.draw(spriteBatch);
            }
        }

        public void renderDebug(ShapeRenderer shapeRenderer) {
            for (BackgroundObject backgroundObject : colBgObject) {
                backgroundObject.renderDebug(shapeRenderer);
            }
        }
    }

    private class BackgroundObject extends AbstractGameObject {
        private boolean horizontalScroll;
        private float velocity;

        public BackgroundObject(TextureRegion bgTextureRegion, float x, float y, boolean horizontalScroll, float velocity) {
            this.horizontalScroll = horizontalScroll;
            this.velocity = velocity;
            setBounds(x, y, bgTextureRegion.getRegionWidth() / GameCamera.PPM, bgTextureRegion.getRegionHeight() / GameCamera.PPM);
            setRegion(bgTextureRegion);
        }

        @Override
        public void update(float deltaTime) {
            if (horizontalScroll) {
                setPosition(getX() + velocity * deltaTime, getY());
            } else {
                setPosition(getX(), getY() + velocity * deltaTime);
            }
        }

        @Override
        public void render(SpriteBatch spriteBatch) {
            draw(spriteBatch);
        }
    }
}
