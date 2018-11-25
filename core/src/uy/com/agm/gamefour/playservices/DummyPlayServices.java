package uy.com.agm.gamefour.playservices;

/**
 * Created by AGM on 11/24/2018.
 */
public class DummyPlayServices implements IPlayServices {
    @Override
    public void signIn() {
        // Nothing to do here
    }

    @Override
    public void signOut() {
        // Nothing to do here
    }

    @Override
    public void rateGame() {
        // Nothing to do here
    }

    @Override
    public void submitScore(int highScore) {
        // Nothing to do here
    }

    @Override
    public void showLeaderboards() {
        // Nothing to do here
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public boolean isWifiConnected() {
        return false;
    }
}
