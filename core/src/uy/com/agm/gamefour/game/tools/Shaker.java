package uy.com.agm.gamefour.game.tools;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by AGMCORP on 19/9/2018.
 */

public class Shaker {
    private static final String TAG = Shaker.class.getName();

    private static final int SAMPLE_DURATION_SECONDS = 10; // Make it longer if you want more variation
    private static final int FREQUENCY_HZ = 35;
    private static final float DEFAULT_AMPLITUDE_METERS = 0.2f;
    private static final boolean FALL_OFF = true; // If the shake should decay as it expires

    private Camera camera;
    private float internalTimer;
    private float amplitude;
    private float shakeDuration;
    private int sampleCount;
    private Random rand;
    private float[] samples;
    private Vector2 stayCenteredOn;
    private boolean isShaking;

    public Shaker(Camera camera) {
        this.camera = camera;
        internalTimer = 0;
        amplitude = DEFAULT_AMPLITUDE_METERS;
        shakeDuration = 0;
        sampleCount = SAMPLE_DURATION_SECONDS * FREQUENCY_HZ;
        rand = new Random();
        samples = new float[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            samples[i] = rand.nextFloat() * 2f - 1f;
        }
        stayCenteredOn = new Vector2();
        isShaking = false;
    }

    public void update(float dt) {
        if (isShaking) {
            internalTimer += dt;
            if (internalTimer > SAMPLE_DURATION_SECONDS) {
                internalTimer -= SAMPLE_DURATION_SECONDS;
            }
            if (shakeDuration > 0) {
                shakeDuration -= dt;
                float shakeTime = (internalTimer * FREQUENCY_HZ);
                int first = (int) shakeTime;
                int second = (first + 1) % sampleCount;
                int third = (first + 2) % sampleCount;
                float deltaT = shakeTime - (int) shakeTime;
                float deltaX = samples[first] * deltaT + samples[second] * (1f - deltaT);
                float deltaY = samples[second] * deltaT + samples[third] * (1f - deltaT);

                camera.position.x = stayCenteredOn.x + deltaX * amplitude * (FALL_OFF ? Math.min(shakeDuration, 1f) : 1f);
                camera.position.y = stayCenteredOn.y + deltaY * amplitude * (FALL_OFF ? Math.min(shakeDuration, 1f) : 1f);
            } else {
                camera.position.x = stayCenteredOn.x;
                camera.position.y = stayCenteredOn.y;
                isShaking = false;
            }
            camera.update();
        }
    }

    public void shake(float duration) {
        // We ignore the request if it is actually shaking
        if (!isShaking) {
            performShake(DEFAULT_AMPLITUDE_METERS, duration, camera.position.x, camera.position.y);
        }
    }

    public void shake(float amplitude, float duration) {
        // We ignore the request if it is actually shaking
        if (!isShaking) {
            performShake(amplitude, duration, camera.position.x, camera.position.y);
        }
    }

    private void performShake(float amplitude, float duration, float stayCenteredOnX, float stayCenteredOnY) {
        this.amplitude = amplitude;
        shakeDuration = duration;
        stayCenteredOn.set(stayCenteredOnX, stayCenteredOnY);
        isShaking = true;
    }
}

