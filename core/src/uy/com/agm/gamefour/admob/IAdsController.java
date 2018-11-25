package uy.com.agm.gamefour.admob;

/**
 * Created by AGMCORP on 10/31/2018.
 */

public interface IAdsController {
    void showInterstitialAd(Runnable callbackOnAdClosed);
    void showBannerAd();
    void hideBannerAd();
    boolean isWifiConnected();
}
