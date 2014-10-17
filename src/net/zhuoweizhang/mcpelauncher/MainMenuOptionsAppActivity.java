package net.zhuoweizhang.mcpelauncher;

import android.os.*;
import android.view.*;
import android.widget.*;

import com.google.android.gms.ads.*;

import net.zhuoweizhang.mcpelauncher.ui.*;

public class MainMenuOptionsAppActivity extends MainMenuOptionsActivity {

	private AdView adView;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		try {
			addAds();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addAds() {
		//ViewGroup view = (ViewGroup) getWindow().getDecorView();
		//LinearLayout content = (LinearLayout) view.getChildAt(0);
		/* http://stackoverflow.com/a/21686506 */
		View listView = findViewById(android.R.id.list);
		ViewParent parentOfListView = listView.getParent();
		if (parentOfListView == null) {
			System.out.println("Main menu options: no parent (is this device Batman?)");
			return;
		}
		ViewParent parentOfParent = parentOfListView.getParent();
		if (parentOfParent == null) {
			System.out.println("Main menu options: no parent of parent");
			return;
		}

		ViewParent parentOfParentOfParent = parentOfParent.getParent();
		if (parentOfParentOfParent == null || !(parentOfParentOfParent instanceof LinearLayout)) {
			System.out.println("Main menu options: no parent of parent of parent");
			return;
		}

		LinearLayout content = (LinearLayout) parentOfParentOfParent;

		adView = new AdView(this);
		adView.setAdUnitId(AdConfiguration.AD_UNIT_ID);
		adView.setAdSize(AdSize.SMART_BANNER);
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice(AdConfiguration.DEVICE_ID_TESTER)
			.build();
		content.addView(adView, 0);
		adView.loadAd(adRequest);
	}

	@Override
	public void onPause() {
		if (adView != null) adView.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) adView.resume();
	}

	@Override
	public void onDestroy() {
		if (adView != null) adView.destroy();
		super.onDestroy();
	}

}
