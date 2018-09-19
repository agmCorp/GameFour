package uy.com.agm.gamefour.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameController;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.game.tools.Box2DCreator;
import uy.com.agm.gamefour.game.tools.Shaker;
import uy.com.agm.gamefour.game.tools.WorldContactListener;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 17/9/2018.
 */

public class PlayScreen extends GameAbstractScreen {
    private static final String TAG = PlayScreen.class.getName();

    // Temporary GC friendly vector
    private Vector2 tmp;

    // World physics simulation parameters
    private static final float MAX_FRAME_TIME = 0.25f;
    private static final float WORLD_TIME_STEP = 1/300.0f;
    private static final int WORLD_VELOCITY_ITERATIONS = 6;
    private static final int WORLD_POSITION_ITERATIONS = 2;

    // Game state
    private enum PlayScreenState {
        PAUSED, RUNNING
    }
    private PlayScreenState playScreenState;

    // Basic PlayScreen variables
    private OrthographicCamera gameCamera;
    private Viewport gameViewPort;

    // Box2d variables
    private World box2DWorld;
    private float accumulator;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Box2DCreator box2DCreator;

    // Main character
    private Jumper jumper;

    // Screen shaker
    private Shaker shaker;

    public PlayScreen(GameFour game) {
        super(game);

        // Creates a ExtendViewport to maintain virtual aspect ratio despite screen size
        // We use the convention 100 pixels = 1 meter to work with meters and therefore meters per seconds in velocity and so on.
        gameCamera = new OrthographicCamera();
        gameViewPort = new ExtendViewport(GameFour.APPLICATION_WIDTH / GameFour.PPM, GameFour.APPLICATION_HEIGHT / GameFour.PPM, gameCamera);

        // Places the gameCamera in the middle of the screen
        gameCamera.position.set(gameViewPort.getWorldWidth() / 2, gameViewPort.getWorldHeight() / 2, 0);

        // Creates the Box2D world, setting no gravity in x and -9.8f gravity in y, and allow bodies to sleep
        box2DWorld = new World(new Vector2(0, -9.8f), true);

        // Avoids "sticking to walls" when velocity is less than 1
        box2DWorld.setVelocityThreshold(0.0f);

        // Sets accumulator for box2DWorld.step
        accumulator = 0;

        // Allows debug lines of the Box2D world.
        if (DebugConstants.DEBUG_LINES) {
            box2DDebugRenderer = new Box2DDebugRenderer();
        }

        // Get the main character
        box2DCreator = new Box2DCreator(this);
        jumper = box2DCreator.getJumper();

        // Create the collision listener
        box2DWorld.setContactListener(new WorldContactListener());

        // Screen shaker
        shaker = new Shaker();

        // PlayScreen running
        playScreenState = PlayScreenState.RUNNING;
    }

    public boolean isPlayScreenStateRunning() {
        return playScreenState == PlayScreenState.RUNNING;
    }

    // Key control
    private void handleInput(float deltaTime) {
        // We use GameController instead of input.isKeyPressed.
    }

    private void updateLogic(float deltaTime) {
        // Handle user input first
        handleInput(deltaTime);

        // Step in the physics simulation
        doPhysicsStep(deltaTime);

        // The order is not important
        updateJumper(deltaTime);

        // Always at the end
        updateCamera(deltaTime);
    }

    private void updateJumper(float deltaTime) {
        jumper.update(deltaTime);
    }

    private void updateCamera(float deltaTime) {
        // Update camera
        tmp.set(gameCamera.position.x, gameCamera.position.y);
        shaker.update(deltaTime, gameCamera, tmp);
    }

    private void doPhysicsStep(float dt) {
        // Fixed time step
        // Max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(dt, MAX_FRAME_TIME);
        accumulator += frameTime;
        while (accumulator >= WORLD_TIME_STEP) {
            box2DWorld.step(WORLD_TIME_STEP, WORLD_VELOCITY_ITERATIONS, WORLD_POSITION_ITERATIONS);
            accumulator -= WORLD_TIME_STEP;
        }
    }

    private void renderLogic(float deltaTime) {
        clearScreen();

        // Render Box2DDebugLines
        if (DebugConstants.DEBUG_LINES) {
            box2DDebugRenderer.render(box2DWorld, gameCamera.combined);
        }

        // Set our batch to now draw what the gameCamera sees.
        SpriteBatch batch = game.getGameBatch();
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();

        // This order is important
        // This determine if a sprite has to be drawn in front or behind another sprite
        renderJumper(batch);

        batch.end();

        // Debug
        if (DebugConstants.DEBUG_LINES) {
            // Set our batch to now draw what the gameCamera sees.
            ShapeRenderer shapeRenderer = game.getGameShapeRenderer();
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);

            renderDebugJumper(shapeRenderer);

            shapeRenderer.end();
        }
    }

    private void renderJumper(SpriteBatch batch) {
        jumper.draw(batch);
    }

    private void renderDebugJumper(ShapeRenderer shapeRenderer) {
        jumper.renderDebug(shapeRenderer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float deltaTime) {
        if (playScreenState == PlayScreenState.RUNNING) {
            updateLogic(deltaTime);
        }

        renderLogic(deltaTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public InputProcessor getInputProcessor() {
        /* GameController is an InputAdapter because it extends that class and
         * it's also a GestureListener because it implements that interface.
         * In GameController then I can recognize gestures (like fling) and I can
         * recognize events such as touchUp that doesn't exist within the interface
         * GestureListener but exists within an InputAdapter.
         * As the InputAdapter methods are too many, I decided to extend that
         * class (to implement within GameController only the method that I'm interested in) and
         * implemented the GestureListener interface because, after all, there are only few extra methods that I must declare.
         * To work with both InputProcessors at the same time, I must use a InputMultiplexer.
         * The fling and touchUp events, for example, always run at the same time.
         * First I registered GestureDetector so that fling is executed before touchUp and as they are related,
         * when I return true in the fling event the touchUp is canceled. If I return false both are executed.
         * */
        GameController gameController = new GameController(this);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GestureDetector(gameController)); // Detects gestures (tap, long press, fling, pan, zoom, pinch)
        multiplexer.addProcessor(gameController); // User input handler on PlayScreen
        return multiplexer;
    }

    @Override
    public Viewport getViewport() {
        return gameViewPort;
    }

    @Override
    public OrthographicCamera getCamera() {
        return gameCamera;
    }
}
