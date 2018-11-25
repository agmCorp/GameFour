package uy.com.agm.gamefour;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
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

	// request codes we use when invoking an external activity
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;

	private AdView bannerAd;
	private View gameView;
	private InterstitialAd interstitialAd;
	private Runnable callbackOnAdClose;
	// Client used to sign in with Google APIs
	private GoogleSignInClient mGoogleSignInClient;

	// Client variables
	private LeaderboardsClient mLeaderboardsClient;
	private PlayersClient mPlayersClient;

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
		MobileAds.initialize(this, getString(R.string.admob_app_id));
		setupBannerAd();
		setupInterstitialAd();

		// Create the client used to sign in to Google services.
		mGoogleSignInClient = GoogleSignIn.getClient(this,
				new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());
	}

	private void setupBannerAd() {
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

	private void setupInterstitialAd() {
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(DebugConstants.TEST_ADS ? getString(R.string.admob_interstitial_unit_id_test) : getString(R.string.admob_interstitial_unit_id));
		callbackOnAdClose = null;
		setInterstitialAdListener();

		// Load the first interstitial
		loadInterstitialAd();
	}

	private void setInterstitialAdListener() {
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				app.debug(TAG, "**** Ad finishes loading");
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				app.debug(TAG, "**** Ad request fails with errorCode: " + errorCode);
			}

			@Override
			public void onAdOpened() {
				app.debug(TAG, "**** Ad is displayed");
			}

			@Override
			public void onAdLeftApplication() {
				app.debug(TAG, "**** User has left the app");
			}

			@Override
			public void onAdClosed() {
				app.debug(TAG, "**** Ad is closed");

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
				if (callbackOnAdClose != null) {
					// Therefore, instead of running callbackOnAdClose in the UI thread (callbackOnAdClose.run), we run it in
					// the badlogic rendering thread.
					app.postRunnable(callbackOnAdClose);
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
	public void showInterstitialAd(Runnable callbackOnAdClose) {
		// Code that is executed when the ad is closed.
		this.callbackOnAdClose = callbackOnAdClose;

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
					app.debug(TAG, "**** The interstitial wasn't loaded yet.");
				}
			}
		});
	}

	@Override
	public void showBannerAd() {
		if (bannerAd.getVisibility() != View.VISIBLE) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					bannerAd.setVisibility(View.VISIBLE);
					loadBannerAd();
				}
			});
		}
	}

	@Override
	public void hideBannerAd() {
		if (bannerAd.getVisibility() == View.VISIBLE) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					bannerAd.setVisibility(View.INVISIBLE);
				}
			});
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
	public void signIn() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startSignInIntent();
            }
        });
	}

	private void startSignInIntent() {
		startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
	}

    @Override
    protected void onResume() {
        super.onResume();

        // Since the state of the signed in user can change when the activity is not active
        // it is recommended to try and sign in silently from when the app resumes.
        signInSilently();
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

				onDisconnected();

				new AlertDialog.Builder(this)
						.setMessage(message)
						.setNeutralButton(android.R.string.ok, null)
						.show();
			}
		}
	}

	private void signInSilently() {
		Log.d(TAG, "signInSilently()");

		mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if (task.isSuccessful()) {
							Log.d(TAG, "signInSilently(): success");
							onConnected(task.getResult());
						} else {
							Log.d(TAG, "signInSilently(): failure", task.getException());
							onDisconnected();
						}
					}
				});
	}

	private void onConnected(GoogleSignInAccount googleSignInAccount) {
		Gdx.app.debug(TAG, "******* ME CONECTE " + googleSignInAccount.getEmail());

		mLeaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
		mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);

		// Set the greeting appropriately on main menu
		mPlayersClient.getCurrentPlayer()
				.addOnCompleteListener(new OnCompleteListener<Player>() {
					@Override
					public void onComplete(@NonNull Task<Player> task) {
						String displayName;
						if (task.isSuccessful()) {
							displayName = task.getResult().getDisplayName();
						} else {
							Exception e = task.getException();
							handleException(e, getString(R.string.players_exception));
							displayName = "???";
						}
						Gdx.app.debug(TAG, "******* Hello, " + displayName);
					}
				});
	}

	private void onDisconnected() {
		Gdx.app.debug(TAG, "******* ME CONECTE DESCONECTE");

		mLeaderboardsClient = null;
		mPlayersClient = null;
	}

	@Override
	public boolean isSignedIn() {
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
        if (!isSignedIn()) {
            Gdx.app.debug(TAG, "**** signOut() called, but was not signed in!");
        } else {
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            boolean successful = task.isSuccessful();
                            Gdx.app.debug(TAG, "**** signOut(): " + (successful ? "success" : "failed"));

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
                String str = "https://play.google.com/store/apps/details?id=uy.com.agm.gamefour"; // TODO
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
            }
        });
	}

	@Override
	public void submitScore(final int highScore) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // update leaderboards
                updateLeaderboards(highScore);
            }
        });
	}

    private void updateLeaderboards(int highScore) {
		Gdx.app.debug(TAG, "********* MANDO " + highScore);
        mLeaderboardsClient.submitScore(getString(R.string.gpgs_leaderboard), highScore);
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
        mLeaderboardsClient.getAllLeaderboardsIntent()
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

	private void handleException(Exception e, String details) {
		int status = 0;

		if (e instanceof ApiException) {
			ApiException apiException = (ApiException) e;
			status = apiException.getStatusCode();
		}

		String message = getString(R.string.status_exception_error, details, status, e);

		new AlertDialog.Builder(this)
				.setMessage(message)
				.setNeutralButton(android.R.string.ok, null)
				.show();
	}
}
