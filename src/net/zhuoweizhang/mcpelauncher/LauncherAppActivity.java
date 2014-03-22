package net.zhuoweizhang.mcpelauncher;

import android.app.*;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import com.google.ads.*;

import net.zhuoweizhang.mcpelauncher.ui.*;

public class LauncherAppActivity extends net.zhuoweizhang.mcpelauncher.ui.LauncherActivity implements AdListener {

	private InterstitialAd interstitial;
	private boolean needsShowAd = false;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		loadInterstitialAdvertisement();
	}

	@Override
	protected Intent getOptionsActivityIntent() {
		return new Intent(this, MainMenuOptionsAppActivity.class);
	}

	@Override
	public void onPrepareDialog(int dialogId, Dialog dialog) {
		switch (dialogId) {
			case DIALOG_RUNTIME_OPTIONS:
				prepareRuntimeOptionsDialog(dialog);
			default:
				super.onPrepareDialog(dialogId, dialog);
		}
	}

	protected void prepareRuntimeOptionsDialog(Dialog dialog) {
		FrameLayout view = (FrameLayout) dialog.findViewById(android.R.id.custom);
		view.setVisibility(View.VISIBLE);
		ViewParent parent = view.getParent();
		if (parent != null) ((View) parent).setVisibility(View.VISIBLE);

		AdView adView = (AdView) view.findViewById(0xbeefbeef);
		if (adView == null) {
			adView = new AdView(this, AdSize.BANNER, "a151b8f9c5d33b5");
			adView.setId(0xbeefbeef);
			view.addView(adView);
		}
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice("DF28838C26BDFAE7EB063BFEB7A241D3");
		adView.loadAd(adRequest);
	}

	@Override
	public void leaveGameCallback() {
		this.runOnUiThread(new Runnable() {
			public void run() {
				showAdvertisement();
			}
		});
	}

	protected void loadInterstitialAdvertisement() {
		if (interstitial == null) {
			interstitial = new InterstitialAd(this, "a151b8f9c5d33b5");
			interstitial.setAdListener(this);
		}
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice("DF28838C26BDFAE7EB063BFEB7A241D3");
		interstitial.loadAd(adRequest);
	}

	public void showAdvertisement() {
		if (interstitial.isReady()) {
			interstitial.show();
			loadInterstitialAdvertisement();
			needsShowAd = false;
		} else {
			needsShowAd = true;
		}
	}

	/** Called when an ad is clicked and about to return to the application. */
	@Override
	public void onDismissScreen(Ad ad) {

	}

	/** Called when an ad was not received. */
	@Override
	public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {
	}

	/**
	 * Called when an ad is clicked and going to start a new Activity that will
	 * leave the application (e.g. breaking out to the Browser or Maps
	 * application).
	 */
	@Override
	public void onLeaveApplication(Ad ad) {
	}

	/**
	 * Called when an Activity is created in front of the app (e.g. an
	 * interstitial is shown, or an ad is clicked and launches a new Activity).
	 */
	@Override
	public void onPresentScreen(Ad ad) {
	}

	/** Called when an ad is received. */
	@Override
	public void onReceiveAd(Ad ad) {
		if (ad == interstitial && needsShowAd) {
			interstitial.show();
			loadInterstitialAdvertisement();
			needsShowAd = false;
		}
	}
}
