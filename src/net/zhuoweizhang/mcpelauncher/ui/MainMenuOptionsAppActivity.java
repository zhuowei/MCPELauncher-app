package net.zhuoweizhang.mcpelauncher.ui;

import android.os.*;
import android.view.*;
import android.widget.*;

import com.google.ads.*;

import net.zhuoweizhang.mcpelauncher.ui.*;

public class MainMenuOptionsAppActivity extends MainMenuOptionsActivity {

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		try {
			addAds();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addAds() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		LinearLayout content = (LinearLayout) view.getChildAt(0);

		AdView adView = new AdView(this, AdSize.SMART_BANNER, "a151b8f9c5d33b5");
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice("DF28838C26BDFAE7EB063BFEB7A241D3");
		content.addView(adView, 0);
		adView.loadAd(adRequest);
	}

}
