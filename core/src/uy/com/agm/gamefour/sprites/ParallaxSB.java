package uy.com.agm.gamefour.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        float x = 0;
        float y = 0;

        Array<BackgroundObject> colBgObject = new Array<BackgroundObject>(colTextureRegion.size);
        BackgroundObject backgroundObject;
        for (TextureRegion textureRegion : colTextureRegion) {
            backgroundObject = new BackgroundObject(textureRegion, x, y, horizontalScroll, velocity);
            if (horizontalScroll) {
                x += textureRegion.getRegionWidth() / GameCamera.PPM;
            } else {
                y += textureRegion.getRegionHeight() / GameCamera.PPM;
            }
            colBgObject.add(backgroundObject);
        }
        Layer layer = new Layer(colBgObject, horizontalScroll, velocity);
        layers.add(layer);
    }

    public void update(float deltaTime) {
        // TODO, ACA ESTA LO JODIDO, esto deberian ser las medidas de la pantalla.
        float frustumWidth = gameCamera.getFrustumWidth();
        float frustumHeight = gameCamera.getFrustumHeight();
        float gameCamLeft = gameCamera.position().x - frustumWidth / 2;
        float gameCamRight = gameCamera.position().x + frustumWidth / 2;
        float gameCamBottom = gameCamera.position().y - frustumHeight / 2;
        float gameCamTop = gameCamera.position().y + frustumHeight / 2;

        Gdx.app.debug(TAG, "****** COMIENZO LOGUEOS *****");
        Gdx.app.debug(TAG, "****** ancho del mundo " + frustumWidth);
        Gdx.app.debug(TAG, "****** alto del mundo " + frustumWidth);
        Gdx.app.debug(TAG, "****** camara left (debe ser cero) " + gameCamLeft);
        Gdx.app.debug(TAG, "****** camara right " + gameCamRight);
        Gdx.app.debug(TAG, "****** camara bottom (debe ser cero) " + gameCamBottom);
        Gdx.app.debug(TAG, "****** camara top  " + gameCamTop);

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
                if (horizontalScroll) {
                    backgroundObject.setPosition(backgroundObject.getX() + velocity * deltaTime, backgroundObject.getY());
                } else {
                    backgroundObject.setPosition(backgroundObject.getX(), backgroundObject.getY() + velocity * deltaTime);
                }
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
                BackgroundObject bgFirst = colBgObject.first();
                if (gameCamTop > bgFirst.getY() + bgFirst.getHeight()) {
                    addVFirst(colBgObject.pop());
                }
            } else {
                if (velocity > 0) { // Layer is moving up
                    BackgroundObject bgLast = colBgObject.peek();
                    if (gameCamBottom < bgLast.getY()) {
                        addVLast(colBgObject.removeIndex(0));
                    }
                }
            }
        }

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
            Gdx.app.debug(TAG, "********" + getWidth() + " " + getHeight()); // TODO
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
