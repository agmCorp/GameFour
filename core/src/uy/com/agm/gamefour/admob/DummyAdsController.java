package uy.com.agm.gamefour.admob;

/**
 * Created by AGMCORP on 31/10/2018.
 */

public class DummyAdsController implements IAdsController {

    @Override
    public void showInterstitialAd(Runnable callbackOnAdClose) {
        // Nothing to do here
    }

    @Override
    public void showBannerAd() {
        // Nothing to do here
    }

    @Override
    public void hideBannerAd() {
        // Nothing to do here
    }

    @Override
    public boolean isWifiConnected() {
        return false;
    }
}
