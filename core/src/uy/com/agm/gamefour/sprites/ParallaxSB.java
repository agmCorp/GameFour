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
    private Array<StaticBackground> staticBackgrounds;

    public ParallaxSB(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
        staticBackgrounds = new Array<StaticBackground>();
        layers = new Array<Layer>();
    }

    public void addStaticLayer(TextureRegion textureRegion) {
        StaticBackground staticBackground = new StaticBackground(textureRegion, gameCamera);
        staticBackgrounds.add(staticBackground);
    }

    public void addDynamicLayer(TextureRegion textureRegion, int repeat, boolean horizontalScroll, float velocity) {
        if (repeat > 0) {
            Array<TextureRegion> colTextureRegion = new Array<TextureRegion>(repeat);
            for (int i = 0; i < repeat; i++) {
                colTextureRegion.add(textureRegion);
            }
            addDynamicLayer(colTextureRegion, horizontalScroll, velocity);
        }
    }

    public void addDynamicLayer(Array<TextureRegion> colTextureRegion, boolean horizontalScroll, float velocity) {
        Vector3 gameCamPos = gameCamera.position();
        TextureRegion currTr;
        float x, y;
        DynamicBackground currDBg, prevDBg;
        Array<DynamicBackground> dynamicBackgrounds = new Array<DynamicBackground>(colTextureRegion.size);

        // IMPORTANT: We set up the "grow direction" of dynamicBackgrounds at our convenience.
        for (int i = 0, n = colTextureRegion.size; i < n; i++) {
            currTr = colTextureRegion.get(i);
            if (i > 0) {
                prevDBg = dynamicBackgrounds.get(i - 1);
                if (horizontalScroll) {
                    y = gameCamPos.y - (currTr.getRegionHeight() / GameCamera.PPM ) / 2;
                    if (velocity < 0) { // Layer is moving to the left
                        x = prevDBg.getX() + prevDBg.getWidth(); // Grows to the right
                    } else { // Layer is moving to the right
                        x = prevDBg.getX() - currTr.getRegionWidth() / GameCamera.PPM; // Grows to the left
                    }
                } else {
                    x = gameCamPos.x - (currTr.getRegionWidth() / GameCamera.PPM ) / 2;
                    if (velocity < 0) { // Layer is moving down
                        y = prevDBg.getY() + prevDBg.getHeight(); // Grows up
                    } else { // Layer is moving up
                        y = prevDBg.getY() - currTr.getRegionHeight() / GameCamera.PPM; // Grows down
                    }
                }
            } else {
                x = gameCamPos.x - (currTr.getRegionWidth() / GameCamera.PPM ) / 2;
                y = gameCamPos.y - (currTr.getRegionHeight() / GameCamera.PPM ) / 2;
            }
            currDBg = new DynamicBackground(currTr, x, y, horizontalScroll, velocity);
            dynamicBackgrounds.add(currDBg);
        }
        Layer layer = new Layer(dynamicBackgrounds, horizontalScroll, velocity, gameCamera);
        layers.add(layer);
    }

    public void update(float deltaTime) {
        for (StaticBackground staticBackground : staticBackgrounds) {
            staticBackground.update(deltaTime);
        }

        for (Layer layer : layers) {
            layer.update(deltaTime);
        }
    }

    public void render(SpriteBatch spriteBatch) {
        for (StaticBackground staticBackground : staticBackgrounds) {
            staticBackground.render(spriteBatch);
        }

        for (Layer layer : layers) {
            layer.render(spriteBatch);
        }
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        for (StaticBackground staticBackground : staticBackgrounds) {
            staticBackground.renderDebug(shapeRenderer);
        }

        for (Layer layer : layers) {
            layer.renderDebug(shapeRenderer);
        }
    }

    private class Layer {
        private Array<DynamicBackground> dynamicBackgrounds;
        private boolean horizontalScroll;
        private float velocity;
        private GameCamera gameCamera;

        public Layer(Array<DynamicBackground> dynamicBackgrounds, boolean horizontalScroll, float velocity, GameCamera gameCamera) {
            this.dynamicBackgrounds = dynamicBackgrounds;
            this.horizontalScroll = horizontalScroll;
            this.velocity = velocity;
            this.gameCamera = gameCamera;
        }

        public void update(float deltaTime) {
            for (DynamicBackground dynamicBackground : dynamicBackgrounds) {
                dynamicBackground.update(deltaTime);
            }

            float worldWidth = gameCamera.getWorldWidth();
            float worldHeight = gameCamera.getWorldHeight();
            Vector3 gameCameraPos = gameCamera.position();

            if (horizontalScroll) {
                    updateHorizontal(gameCameraPos.x - worldWidth / 2, gameCameraPos.x + worldWidth / 2);
            } else {
                updateVertical(gameCameraPos.y - worldHeight / 2, gameCameraPos.y + worldHeight / 2);
            }
        }

        private void updateHorizontal(float gameCamLeft, float gameCamRight) {
            DynamicBackground dBgHead;
            DynamicBackground dBgTail;

            if (velocity < 0) { // Layer is moving to the left
                DynamicBackground bgFirst = dynamicBackgrounds.first();
                if (gameCamLeft > bgFirst.getX() + bgFirst.getWidth()) {
                    dBgHead = dynamicBackgrounds.removeIndex(0);
                    dBgTail = dynamicBackgrounds.size > 0 ? dynamicBackgrounds.get(dynamicBackgrounds.size - 1) : dBgHead;
                    dBgHead.setPosition(dBgTail.getX() + dBgTail.getWidth(), dBgHead.getY());
                    dynamicBackgrounds.add(dBgHead);
                }
            } else {
                if (velocity > 0) { // Layer is moving to the right
                    DynamicBackground bgFirst = dynamicBackgrounds.first();
                    if (gameCamRight < bgFirst.getX()) {
                        dBgHead = dynamicBackgrounds.removeIndex(0);
                        dBgTail = dynamicBackgrounds.size > 0 ? dynamicBackgrounds.get(dynamicBackgrounds.size - 1) : dBgHead;
                        dBgHead.setPosition(dBgTail.getX() - dBgHead.getWidth(), dBgHead.getY());
                        dynamicBackgrounds.add(dBgHead);
                    }
                }
            }
        }

        private void updateVertical(float gameCamBottom, float gameCamTop) {
            DynamicBackground dBgHead;
            DynamicBackground dBgTail;

            if (velocity < 0) { // Layer is moving down
                DynamicBackground bgFirst = dynamicBackgrounds.first();
                if (gameCamBottom > bgFirst.getY() + bgFirst.getHeight()) {
                    dBgHead = dynamicBackgrounds.removeIndex(0);
                    dBgTail = dynamicBackgrounds.size > 0 ? dynamicBackgrounds.get(dynamicBackgrounds.size - 1) : dBgHead;
                    dBgHead.setPosition(dBgHead.getX(), dBgTail.getY() + dBgTail.getHeight());
                    dynamicBackgrounds.add(dBgHead);
                }
            } else {
                if (velocity > 0) { // Layer is moving up
                    DynamicBackground bgFirst = dynamicBackgrounds.first();
                    if (gameCamTop < bgFirst.getY()) {
                        dBgHead = dynamicBackgrounds.removeIndex(0);
                        dBgTail = dynamicBackgrounds.size > 0 ? dynamicBackgrounds.get(dynamicBackgrounds.size - 1) : dBgHead;
                        dBgHead.setPosition(dBgHead.getX(), dBgTail.getY() - dBgHead.getHeight());
                        dynamicBackgrounds.add(dBgHead);
                    }
                }
            }
        }

        public void render(SpriteBatch spriteBatch) {
            for (DynamicBackground dynamicBackground : dynamicBackgrounds) {
                dynamicBackground.draw(spriteBatch);
            }
        }

        public void renderDebug(ShapeRenderer shapeRenderer) {
            for (DynamicBackground dynamicBackground : dynamicBackgrounds) {
                dynamicBackground.renderDebug(shapeRenderer);
            }
        }
    }

    private class DynamicBackground extends AbstractGameObject {
        private boolean horizontalScroll;
        private float velocity;

        public DynamicBackground(TextureRegion textureRegion, float x, float y, boolean horizontalScroll, float velocity) {
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

    private class StaticBackground extends AbstractGameObject {
        private GameCamera gameCamera;
        private float trWidth;
        private float trHeight;

        public StaticBackground(TextureRegion textureRegion, GameCamera gameCamera) {
            this.gameCamera = gameCamera;
            trWidth =  textureRegion.getRegionWidth() / GameCamera.PPM;
            trHeight = textureRegion.getRegionHeight() / GameCamera.PPM;
            setBounds(getCenterX(), getCenterY(), trWidth, trHeight);
            setRegion(textureRegion);
        }

        @Override
        public void update(float deltaTime) {
            setPosition(getCenterX(), getCenterY());
        }

        @Override
        public void render(SpriteBatch spriteBatch) {
            draw(spriteBatch);
        }

        private float getCenterX() {
            return gameCamera.position().x - trWidth / 2;
        }

        private float getCenterY() {
            return gameCamera.position().y - trHeight / 2;
        }
    }
}
