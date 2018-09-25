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

// Parallax scrolling background (horizontal or vertical)
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
        TextureRegion currTr;
        float x, y;
        BackgroundObject currBo, prevBo;
        Array<BackgroundObject> colBgObject = new Array<BackgroundObject>(colTextureRegion.size);

        // IMPORTANT: We set up the "grow direction" of colBgObject at our convenience.
        for (int i = 0, n = colTextureRegion.size; i < n; i++) {
            currTr = colTextureRegion.get(i);
            if (i > 0) {
                prevBo = colBgObject.get(i - 1);
                if (horizontalScroll) {
                    y = gameCamPos.y - (currTr.getRegionHeight() / GameCamera.PPM ) / 2;
                    if (velocity < 0) {
                        x = prevBo.getX() + prevBo.getWidth(); // Grows to the right
                    } else {
                        x = prevBo.getX() - currTr.getRegionWidth() / GameCamera.PPM; // Grows to the left
                    }
                } else {
                    x = gameCamPos.x - (currTr.getRegionWidth() / GameCamera.PPM ) / 2;
                    if (velocity < 0) {
                        y = prevBo.getY() + prevBo.getHeight(); // Grows up
                    } else {
                        y = prevBo.getY() - currTr.getRegionHeight() / GameCamera.PPM; // Grows down
                    }
                }
            } else {
                x = gameCamPos.x - (currTr.getRegionWidth() / GameCamera.PPM ) / 2;
                y = gameCamPos.y - (currTr.getRegionHeight() / GameCamera.PPM ) / 2;
            }
            currBo = new BackgroundObject(currTr, x, y, horizontalScroll, velocity);
            colBgObject.add(currBo);
        }
        Layer layer = new Layer(colBgObject, horizontalScroll, velocity);
        layers.add(layer);
    }

    public void update(float deltaTime) {
        float worldWidth = gameCamera.getWorldWidth();
        float worldHeight = gameCamera.getWorldHeight();
        Vector3 gameCameraPos = gameCamera.position();
        float gameCamLeft = gameCameraPos.x - worldWidth / 2;
        float gameCamRight = gameCameraPos.x + worldWidth / 2;
        float gameCamBottom = gameCameraPos.y - worldHeight / 2;
        float gameCamTop = gameCameraPos.y + worldHeight / 2;

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
            BackgroundObject bgHead;
            BackgroundObject bgTail;

            if (velocity < 0) { // Layer is moving left
                BackgroundObject bgFirst = colBgObject.first();
                if (gameCamLeft > bgFirst.getX() + bgFirst.getWidth()) {
                    bgHead = colBgObject.removeIndex(0);
                    bgTail = colBgObject.size > 0 ? colBgObject.get(colBgObject.size - 1) : bgHead;
                    bgHead.setPosition(bgTail.getX() + bgTail.getWidth(), bgHead.getY());
                    colBgObject.add(bgHead);
                }
            } else {
                if (velocity > 0) { // Layer is moving right
                    BackgroundObject bgFirst = colBgObject.first();
                    if (gameCamRight < bgFirst.getX()) {
                        bgHead = colBgObject.removeIndex(0);
                        bgTail = colBgObject.size > 0 ? colBgObject.get(colBgObject.size - 1) : bgHead;
                        bgHead.setPosition(bgTail.getX() - bgHead.getWidth(), bgHead.getY());
                        colBgObject.add(bgHead);
                    }
                }
            }
        }

        private void updateVertical(float gameCamBottom, float gameCamTop) {
            BackgroundObject bgHead;
            BackgroundObject bgTail;

            if (velocity < 0) { // Layer is moving down
                BackgroundObject bgFirst = colBgObject.first();
                if (gameCamBottom > bgFirst.getY() + bgFirst.getHeight()) {
                    bgHead = colBgObject.removeIndex(0);
                    bgTail = colBgObject.size > 0 ? colBgObject.get(colBgObject.size - 1) : bgHead;
                    bgHead.setPosition(bgHead.getX(), bgTail.getY() + bgTail.getHeight());
                    colBgObject.add(bgHead);
                }
            } else {
                if (velocity > 0) { // Layer is moving up
                    BackgroundObject bgFirst = colBgObject.first();
                    if (gameCamTop < bgFirst.getY()) {
                        bgHead = colBgObject.removeIndex(0);
                        bgTail = colBgObject.size > 0 ? colBgObject.get(colBgObject.size - 1) : bgHead;
                        bgHead.setPosition(bgHead.getX(), bgTail.getY() - bgHead.getHeight());
                        colBgObject.add(bgHead);
                    }
                }
            }
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

        public BackgroundObject(TextureRegion textureRegion, float x, float y, boolean horizontalScroll, float velocity) {
            this.horizontalScroll = horizontalScroll;
            this.velocity = velocity;
            setBounds(x, y, textureRegion.getRegionWidth() / GameCamera.PPM, textureRegion.getRegionHeight() / GameCamera.PPM);
            setRegion(textureRegion);
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
