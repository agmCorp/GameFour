package uy.com.agm.gamefour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import uy.com.agm.gamefour.admob.IAdsController;
import uy.com.agm.gamefour.game.DebugConstants;
import uy.com.agm.gamefour.game.GameFour;
import uy.com.agm.gamefour.playservices.IPlayServices;

import static com.badlogic.gdx.Gdx.app;

public class AndroidLauncher extends AndroidApplication implements IAdsController, IPlayServices {
	private static final String TAG = AndroidLauncher.class.getName();

	// Request codes we use when invoking an external activity
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;

	// URL on PlayStore
	private static final String URL_PLAY_STORE = "https://play.google.com/store/apps/details?id=uy.com.agm.gamefour";

	// AdMob
	private AdView bannerAd;
	private View gameView;
	private InterstitialAd interstitialAd;

	// AdMob callback
	private Runnable callbackOnAdClosed;

	// Google play game services
	private GoogleSignInClient googleSignInClient;
	private LeaderboardsClient leaderboardsClient;

	// GPGS Callbacks
	private Runnable callbackOnConnected;
	private Runnable callbackOnDisconnected;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// We want to conserve battery
		config.useAccelerometer = false;
		config.useCompass = false;

		// Keep the screen on and hide status bar
		config.useWakelock = true;
		config.hideStatusBar = true;

		// Create a gameView
		gameView = initializeForView(new GameFour(this, this), config);

		// Advertisements
		adMobSetup();

		// Google play game services
		gpgsSetup();
	}

	private void adMobSetup() {
		MobileAds.initialize(this, getString(R.string.admob_app_id));
		bannerAdSetup();
		interstitialAdSetup();
	}

	private void gpgsSetup() {
		// Create the client used to sign in to Google services.
		googleSignInClient = GoogleSignIn.getClient(this,
				new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

		// Client variable
		leaderboardsClient = null;

		// GPGS Callbacks
		callbackOnConnected = null;
		callbackOnDisconnected = null;
	}

	private void bannerAdSetup() {
		bannerAd = new AdView(this);
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(Color.BLACK);
		bannerAd.setAdUnitId(DebugConstants.TEST_ADS ? getString(R.string.admob_banner_unit_id_test) : getString(R.string.admob_banner_unit_id));
		bannerAd.setAdSize(AdSize.SMART_BANNER);

		defineLayout();
	}

	private void loadBannerAd() {
		AdRequest request = new AdRequest.Builder()
				.addTestDevice(getString(R.string.admob_test_device))
				.build();
		bannerAd.loadAd(request);
	}

	private void defineLayout() {
		// Define the layout of our Views (gameView and bannerAd)
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(bannerAd, params);

		setContentView(layout);
	}

	private void interstitialAdSetup() {
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(DebugConstants.TEST_ADS ? getString(R.string.admob_interstitial_unit_id_test) : getString(R.string.admob_interstitial_unit_id));
		callbackOnAdClosed = null;
		setInterstitialAdListener();

		// Load the first interstitial
		loadInterstitialAd();
	}

	private void setInterstitialAdListener() {
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Gdx.app.debug(TAG, "**** Ad finishes loading");
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				Gdx.app.debug(TAG, "**** Ad request fails with errorCode: " + errorCode);
			}

			@Override
			public void onAdOpened() {
				Gdx.app.debug(TAG, "**** Ad is displayed");
			}

			@Override
			public void onAdLeftApplication() {
				Gdx.app.debug(TAG, "**** User has left the app");
			}

			@Override
			public void onAdClosed() {
				Gdx.app.debug(TAG, "**** Ad is closed");

				// All of the com.badlogic.gdx.ApplicationListener methods are called on the same thread.
				// This thread is the rendering thread on which OpenGL calls can be made.
				// For most games it is sufficient to implement both logic updates and rendering in the
				// ApplicationListener.render() method, and on the rendering thread.
				// Any graphics operations directly involving OpenGL need to be executed on the rendering thread.
				// Doing so on a different thread results in undefined behaviour.
				// This is due to the OpenGL context only being active on the rendering thread.
				// Making the context current on another thread has its problems on a lot of Android devices, hence it is unsupported.
				// To pass data from another thread to the rendering thread we must use Application.postRunnable().
				// This will run the code in the Runnable in the rendering thread in the next frame, before
				// ApplicationListener.render() is called.
				if (callbackOnAdClosed != null) {
					// Therefore, instead of running callbackOnAdClosed in the UI thread (callbackOnAdClosed.run), we run it in
					// the badlogic rendering thread.
					app.postRunnable(callbackOnAdClosed);
				}

				// Load the next interstitial
				loadInterstitialAd();
			}
		});
	}

	private void loadInterstitialAd() {
		AdRequest request = new AdRequest.Builder()
				.addTestDevice(getString(R.string.admob_test_device))
				.build();
		interstitialAd.loadAd(request);
	}

	@Override
	public void showInterstitialAd(Runnable callbackOnAdClosed) {
		// Code that is executed when the ad is closed.
		this.callbackOnAdClosed = callbackOnAdClosed;

		// This method (showInterstitialAd) is called from the rendering thread (com.badlogic.gdx.ApplicationListener.render() thread)
		// However, the method show() must be called on the main UI thread.
		// To pass data from the rendering thread to the main UI thread we must use Activity.runOnUiThread
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// Show ad
				if (interstitialAd.isLoaded()) {
					interstitialAd.show();
				} else {
					Gdx.app.debug(TAG, "**** The interstitial wasn't loaded yet.");
				}
			}
		});
	}

	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showBannerAdImp();
			}
		});
	}

	private void showBannerAdImp() {
		if (bannerAd.getVisibility() != View.VISIBLE) {
			bannerAd.setVisibility(View.VISIBLE);
			loadBannerAd();
		}
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideBannerAdImp();
			}
		});
	}

	private void hideBannerAdImp() {
		if (bannerAd.getVisibility() == View.VISIBLE) {
			bannerAd.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean isWifiConnected() {
		boolean isConnected = false;
		int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
				ConnectivityManager.TYPE_WIFI};
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
			for (int networkType : networkTypes) {
				if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) {
					isConnected = true;
					break;
				}
			}
		} catch (Exception e) {
			isConnected = false;
		}
		return isConnected;
	}

	@Override
	public void signIn(Runnable callbackOnConnected, Runnable callbackOnDisconnected) {
		// Code that is executed on connection success or on connection failure
		this.callbackOnConnected = callbackOnConnected;
		this.callbackOnDisconnected = callbackOnDisconnected;

		runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startSignInIntent();
            }
        });
	}

	private void startSignInIntent() {
		if (!isSignedIn()) {
			startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task =
					GoogleSignIn.getSignedInAccountFromIntent(intent);

			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				onConnected(account);
			} catch (ApiException apiException) {
				String message = apiException.getMessage();
				if (message == null || message.isEmpty()) {
					message = getString(R.string.signin_other_error);
				}

				Gdx.app.debug(TAG, "**** onActivityResult(): failure " + message);
				onDisconnected();
			}
		}
	}

	private void signInSilently() {
		googleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							onConnected(task.getResult());
						} else {
							Gdx.app.debug(TAG, "**** signInSilently(): failure " + task.getException());
							onDisconnected();
						}
					}
				});
	}

	private void onConnected(GoogleSignInAccount googleSignInAccount) {
		leaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);

		if (callbackOnConnected != null) {
			// Instead of running callbackOnConnected in the UI thread (callbackOnConnected.run), we run it in
			// the badlogic rendering thread.
			app.postRunnable(callbackOnConnected);
		}
	}

	private void onDisconnected() {
		leaderboardsClient = null;

		if (callbackOnDisconnected != null) {
			// Instead of running callbackOnDisconnected in the UI thread (callbackOnDisconnected.run), we run it in
			// the badlogic rendering thread.
			app.postRunnable(callbackOnDisconnected);
		}
	}

	@Override
	public boolean isSignedIn() {
		// TODO
		Gdx.app.debug(TAG, "****** ESTA LOGUEADO? " + (GoogleSignIn.getLastSignedInAccount(this) != null)); // esto siempre me devuelve true
		return GoogleSignIn.getLastSignedInAccount(this) != null;
	}

	@Override
	public void signOut() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				signOutImp();
			}
		});
	}

	private void signOutImp() {
		if (isSignedIn()) {
			googleSignInClient.signOut().addOnCompleteListener(this,
					new OnCompleteListener<Void>() {
						@Override
						public void onComplete(@NonNull Task<Void> task) {
							boolean successful = task.isSuccessful();
							onDisconnected();
						}
					});
		}
    }

	@Override
	public void rateGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rateGameImp();
            }
        });
	}

	private void rateGameImp() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAY_STORE)));
	}

	@Override
	public void submitScore(final int highScore) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
				submitScoreImp(highScore);
            }
        });
	}

	private void submitScoreImp(int highScore) {
		if (isSignedIn()) {
			leaderboardsClient.submitScore(getString(R.string.gpgs_leaderboard), highScore);
		}
	}

	@Override
	public void showLeaderboards() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLeaderboardsImp();
            }
        });
	}

	private void showLeaderboardsImp() {
		if (isSignedIn()) {
			leaderboardsClient.getAllLeaderboardsIntent()
					.addOnSuccessListener(new OnSuccessListener<Intent>() {
						@Override
						public void onSuccess(Intent intent) {
							startActivityForResult(intent, RC_UNUSED);
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							handleException(e, getString(R.string.leaderboards_exception));
						}
					});
		}
    }

	private void handleException(Exception e, String details) {
		int status = 0;

		if (e instanceof ApiException) {
			ApiException apiException = (ApiException) e;
			status = apiException.getStatusCode();
		}

		String message = getString(R.string.status_exception_error, details, status, e);

		Gdx.app.debug(TAG, "**** handleException(): " + message);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Since the state of the signed in user can change when the activity is not active
		// it is recommended to try and sign in silently from when the app resumes.
		signInSilently();
	}
}
