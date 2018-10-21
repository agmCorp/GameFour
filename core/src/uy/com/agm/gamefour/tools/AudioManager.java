package uy.com.agm.gamefour.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import uy.com.agm.gamefour.game.GameSettings;

/**
 * Created by AGM on 10/21/2018.
 */

public class AudioManager {
    private static final String TAG = AudioManager.class.getName();

    private static  AudioManager instance;
    private Music playingMusic;

    // Singleton: prevent instantiation from other classes
    private AudioManager() {
    }

    // Singleton: retrieve instance
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playSound(Sound sound) {
        playSound(sound, 1);
    }

    public void playSound(Sound sound, float volume) {
        playSound(sound, volume, 1);
    }

    public void playSound(Sound sound, float volume, float pitch) {
        playSound(sound, volume, pitch, 0);
    }

    public void playSound(Sound sound, float volume, float pitch, float pan) {
        if (GameSettings.getInstance().isSound()) {
            sound.play(1, pitch, pan);
        }
    }

    public void playMusic(Music music) {
        // Stop previous music
        if (playingMusic != null && playingMusic != music) {
            playingMusic.stop();
        }

        // Play new music
        playingMusic = music;
        playMusic();
    }

    private void playMusic() {
        if (GameSettings.getInstance().isMusic()) {
            playingMusic.setLooping(true);
            playingMusic.setVolume(1);
            playingMusic.play();
        }
    }

    public void resumeMusic() {
        if (playingMusic != null) {
            playMusic();
        }
    }

    public void stopMusic() {
        if (playingMusic != null) {
            playingMusic.stop();
        }
    }

    public void pauseMusic() {
        if (playingMusic != null) {
            playingMusic.pause();
        }
    }
}
