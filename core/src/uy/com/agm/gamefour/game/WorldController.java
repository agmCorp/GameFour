package uy.com.agm.gamefour.game;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import uy.com.agm.gamefour.game.tools.WorldContactListener;
import uy.com.agm.gamefour.screens.play.PlayScreen;
import uy.com.agm.gamefour.sprites.Jumper;

/**
 * Created by AGMCORP on 21/9/2018.
 */

public class WorldController implements Disposable {
    private static final String TAG = WorldController.class.getName();

    private static final float GAME_OVER_OFFSET = 0.5f;

    // Reference to the play screen
    private PlayScreen playScreen;

    // Reference to the game world
    private GameWorld gameWorld;

    // World physics simulation parameters
    private static final float GRAVITY = -9.8f;
    private static final float MAX_FRAME_TIME = 0.25f;
    private static final float WORLD_TIME_STEP = 1 / 300.0f;
    private static final int WORLD_VELOCITY_ITERATIONS = 6;
    private static final int WORLD_POSITION_ITERATIONS = 2;

    // Box2d variables
    private World box2DWorld;
    private float accumulator;

    public WorldController(PlayScreen playScreen) {
        this.playScreen = playScreen;

        // Creates the Box2D world, setting no gravity in x and GRAVITY in y, and allow bodies to sleep
        box2DWorld = new World(new Vector2(0, GRAVITY), true);

        // Avoids "sticking to walls" when velocity is less than 1
        World.setVelocityThreshold(0.0f);

        // Sets accumulator for box2DWorld.step
        accumulator = 0;

        // Creates the collision listener
        box2DWorld.setContactListener(new WorldContactListener());

        // Creates our game world
        gameWorld = new GameWorld(playScreen, box2DWorld, 0);
    }

    public void update(float deltaTime) {
        // Handle user input first
        handleInput(deltaTime);

        // Handle creation of game objects first
        gameWorld.handleGameObjectsToCreate();

        // Step in the physics simulation
        doPhysicsStep(deltaTime);

        // Update game world
        gameWorld.update(deltaTime);
    }

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

    public InputProcessor getInputProcessor(GameController gameController) {
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
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playScreen.getInfoScreen().getInputProcessor()); // InfoScreen also receives events (pause button)
        multiplexer.addProcessor(new GestureDetector(gameController)); // Detects gestures (tap, long press, fling, pan, zoom, pinch)
        multiplexer.addProcessor(gameController); // User input handler
        return multiplexer;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public boolean isGameOver() {
        Jumper jumper = gameWorld.getJumper();
        GameCamera gameCamera = gameWorld.getGameCamera();

        return (jumper.getBodyPosition().y + jumper.getHeight() / 2 + GAME_OVER_OFFSET < gameCamera.position().y - gameCamera.getWorldHeight() / 2);
    }

    @Override
    public void dispose() {
        box2DWorld.dispose();
    }
}
