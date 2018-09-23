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

/*
LO QUE TENDRIA QUE PENSAR ACA SON DOS COSAS:
EL SCROLL VERTICAL
EL AGARRAR EL X INICIAL Y EL Y INICIAL Y PONERLO DE MANERA QUE LA PRIMER TEXTURA QUEDE CENTRADA.
ASI HAGO TEXTURAS BIEN GRANDES PARA ABARCAR LOS POSIBLES TAMANOS DE PANTALLAS.
VER DONDE DICE TODO. AHI DEBO CALCULAR EL X E Y CENTRANDO LA TEXTURA, LUEGO SIGUE TODO IGUAL.
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

    public void addLayer(Array<TextureRegion> colTextureRegion, boolean horizontalScroll, float velocity) {
        // TODO
        float x = gameCamera.position().x - gameCamera.getFrustumWidth() / 2;
        float y = gameCamera.position().y - gameCamera.getFrustumHeight() / 2;

        Array<BackgroundObject> colBgObject = new Array<BackgroundObject>();
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
        float frustumWidth = gameCamera.getFrustumWidth();
        float frustumHeight = gameCamera.getFrustumHeight();
        float gameCamLeft = gameCamera.position().x - frustumWidth / 2;
        float gameCamRight = gameCamera.position().x + frustumWidth / 2;
        float gameCamBottom = gameCamera.position().y - frustumHeight / 2;
        float gameCamTop = gameCamera.position().y + frustumHeight / 2;

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
                if (velocity < 0) { // Layer is moving left
                    BackgroundObject bgFirst = colBgObject.first();
                    if (gameCamLeft > bgFirst.getX() + bgFirst.getWidth()) {
                        addHLast(colBgObject.removeIndex(0));
                    }
                } else { // Layer is moving right
                    BackgroundObject bgLast = colBgObject.peek();
                    if (gameCamRight < bgLast.getX()) {
                        addHFirst(colBgObject.pop());
                    }
                }
            }

            // TODO ME FALTA RAZONAR SCROLL VERTICAL
        }

        private void addHLast(BackgroundObject backgroundObject) {
            float x = 0;
            for (BackgroundObject bgObject : colBgObject) {
                x += bgObject.getX() + bgObject.getWidth();
            }

            backgroundObject.setPosition(x, backgroundObject.getY());
            colBgObject.add(backgroundObject);
        }

        private void addHFirst(BackgroundObject backgroundObject) {
            BackgroundObject bgFirst = colBgObject.first();
            backgroundObject.setPosition(bgFirst.getX() - backgroundObject.getWidth(), bgFirst.getY());

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
            Gdx.app.debug(TAG, "********" + getWidth() + " " + getHeight());
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
