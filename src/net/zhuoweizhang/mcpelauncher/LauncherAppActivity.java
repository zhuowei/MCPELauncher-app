package net.zhuoweizhang.mcpelauncher;

import android.app.*;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import com.google.android.gms.ads.*;

import net.zhuoweizhang.mcpelauncher.ui.LauncherActivity;

public class LauncherAppActivity extends LauncherActivity {

	private InterstitialAd interstitial;
	private boolean needsShowAd = false;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		loadInterstitialAdvertisement();
	}

	/*@Override
	protected Intent getOptionsActivityIntent() {
		return new Intent(this, MainMenuOptionsAppActivity.class);
	}*/

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
			adView = new AdView(this);
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId(AdConfiguration.AD_UNIT_ID);
			adView.setId(0xbeefbeef);
			view.addView(adView);
		}
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice(AdConfiguration.DEVICE_ID_TESTER)
			.build();
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
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(AdConfiguration.AD_UNIT_ID);
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				System.out.println("Ad loaded!");
				if (needsShowAd) {
					showAdvertisement();
				}
			}
		});
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice("DF28838C26BDFAE7EB063BFEB7A241D3")
			.build();
		interstitial.loadAd(adRequest);
	}

	public void showAdvertisement() {
		if (interstitial.isLoaded()) {
			needsShowAd = false;
			interstitial.show();
			loadInterstitialAdvertisement();
		} else {
			needsShowAd = true;
		}
	}
}
