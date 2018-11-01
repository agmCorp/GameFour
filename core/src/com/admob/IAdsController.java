package com.admob;

/**
 * Created by AGMCORP on 10/31/2018.
 */

public interface IAdsController {
    void showInterstitialAd(Runnable callbackOnAdClose);
    void showBannerAd();
    void hideBannerAd();
    boolean isWifiConnected();
}
