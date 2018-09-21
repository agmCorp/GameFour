package uy.com.agm.gamefour.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import uy.com.agm.gamefour.game.tools.Box2DCreator;
import uy.com.agm.gamefour.game.tools.Shaker;
import uy.com.agm.gamefour.game.tools.WorldContactListener;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class WorldController implements Disposable {

    // World physics simulation parameters
    private static final float GRAVITY = -9.8f;
    private static final float MAX_FRAME_TIME = 0.25f;
    private static final float WORLD_TIME_STEP = 1/300.0f;
    private static final int WORLD_VELOCITY_ITERATIONS = 6;
    private static final int WORLD_POSITION_ITERATIONS = 2;

    // Box2d variables
    private World box2DWorld;
    private float accumulator;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Box2DCreator box2DCreator;

    // Main character
    private Jumper jumper;

    // Screen shaker
    private Shaker shaker;

    public WorldController() {
        // Creates the Box2D world, setting no gravity in x and GRAVITY in y, and allow bodies to sleep
        box2DWorld = new World(new Vector2(0, GRAVITY), true);

        // Avoids "sticking to walls" when velocity is less than 1
        box2DWorld.setVelocityThreshold(0.0f);

        // Sets accumulator for box2DWorld.step
        accumulator = 0;

        // Gets the main character
        jumper = box2DCreator.getJumper();

        // Creates the collision listener
        box2DWorld.setContactListener(new WorldContactListener());

        // Screen shaker
        shaker = new Shaker();
    }

    public void update(float deltaTime) {
        // Handle user input first
        handleInput(deltaTime);

        // Step in the physics simulation
        doPhysicsStep(deltaTime);

        // The order is not important
        updateJumper(deltaTime);

        // Always at the end
        updateCamera(deltaTime);
    }

    // Key control
    private void handleInput(float deltaTime) {
        // We use GameController instead of input.isKeyPressed.
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

    private void updateJumper(float deltaTime) {
        jumper.update(deltaTime);
    }

    private void updateCamera(float deltaTime) {
        // Updates camera
//        tmp.set(gameCamera.position.x, gameCamera.position.y);
//        shaker.update(deltaTime, gameCamera, tmp);
        // EL SHAKER DEBERIA PODER LLEGAR A LA CAMARA Y ARREGLARSELAS EL MISMO.
    }

    public World getBox2DWorld() {
        return box2DWorld;
    }

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
    public void dispose() {
        box2DWorld.dispose();
    }
}
