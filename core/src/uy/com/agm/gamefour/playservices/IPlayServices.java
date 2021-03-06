package uy.com.agm.gamefour.playservices;

/**
 * Created by AGM on 11/24/2018.
 */
public interface IPlayServices {
    void signInSilently(Runnable callbackOnSilentlyConnected, Runnable callbackOnSilentlyDisconnected);
    void signIn(Runnable callbackOnConnected, Runnable callbackOnDisconnected);
    boolean isSignedIn();
    void signOut();
    void rateGame();
    void submitScore(int highScore);
    void showLeaderboards();
    boolean isWifiConnected();
    void showToast(String message);
}
