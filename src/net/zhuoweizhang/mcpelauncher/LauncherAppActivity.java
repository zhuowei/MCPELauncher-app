package net.zhuoweizhang.mcpelauncher;

import android.app.*;
import android.content.Intent;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.google.android.gms.ads.*;

import net.zhuoweizhang.mcpelauncher.ui.LauncherActivity;

public class LauncherAppActivity extends LauncherActivity {

	private InterstitialAd interstitial;
	private boolean needsShowAd = false;
	private boolean adError = false;
	private PopupWindow shadePopup;
	private static final int MESSAGE_SHOW_AD = 608;
	private static final int MESSAGE_AD_TIMEOUT = 609;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		loadInterstitialAdvertisement();
		TextView text = new TextView(this);
		text.setText("Please wait...");
		shadePopup = new PopupWindow(text, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		shadePopup.setBackgroundDrawable(new ColorDrawable(0xff000000));
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
	private boolean hasCalledShowAdvertisement = false;

	@Override
	public void leaveGameCallback() {
		super.leaveGameCallback();
		hasCalledShowAdvertisement = false;
		// wait until we come back from the advertisement thread
		this.runOnUiThread(new Runnable() {
			public void run() {
				showAdvertisement();
				hasCalledShowAdvertisement = true;
			}
		});
		while (!hasCalledShowAdvertisement) {
			try {
				Thread.sleep(20);
			} catch (Exception e) {}
		}
		try {
			Thread.sleep(100);
		} catch (Exception e) {}
	}

	protected void loadInterstitialAdvertisement() {
		adError = false;
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("ca-app-pub-2652482030334356/8558350222");
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				System.out.println("Ad loaded!");
				adError = false;
				if (needsShowAd) {
					actuallyShowAdvertisement();
				}
			}
			public void onAdFailedToLoad(int reason) {
				adError = true;
				if (needsShowAd) {
					needsShowAd = false;
					adOver();
				}
			}
			public void onAdClosed() {
				adOver();
			}
		});
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice("DF28838C26BDFAE7EB063BFEB7A241D3")
			.build();
		interstitial.loadAd(adRequest);
	}

	public void showAdvertisement() {
		if (adError) {
			loadInterstitialAdvertisement(); // try again next time.
			return;
		}
		shadePopup.showAtLocation(getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
		ScriptManager.nativeSetExitEnabled(false);
		adHandler.removeMessages(MESSAGE_AD_TIMEOUT);
		// wait for 0.5 seconds
		adHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_AD, 500);
	}

	private Handler adHandler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			if (message.what == MESSAGE_SHOW_AD) {
				actuallyShowAdvertisement();
			} else if (message.what == MESSAGE_AD_TIMEOUT) {
				adTimedOut();
			}
		}
	};

	private void actuallyShowAdvertisement() {
		ScriptManager.nativeSetExitEnabled(true);
		if (!shadePopup.isShowing()) {
			// WTF?!
			System.out.println("No longer ready to show ad.");
			needsShowAd = false;
			return;
		}
		if (interstitial.isLoaded()) {
			needsShowAd = false;
			interstitial.show();
			adHandler.removeMessages(MESSAGE_AD_TIMEOUT);
			loadInterstitialAdvertisement();
		} else if (adError) {
			// do nothing
			needsShowAd = false;
			adHandler.removeMessages(MESSAGE_AD_TIMEOUT);
			adOver();
		} else {
			needsShowAd = true;
			adHandler.sendEmptyMessageDelayed(MESSAGE_AD_TIMEOUT, 5000);
		}
	}

	private void adOver() {
		needsShowAd = false;
		shadePopup.dismiss();
		ScriptManager.nativeSetExitEnabled(true);
	}

	private void adTimedOut() {
		adOver();
	}
}
