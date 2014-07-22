package net.zhuoweizhang.mcpelauncher;

import android.os.*;

import com.google.android.gms.ads.*;

import net.zhuoweizhang.mcpelauncher.ui.*;

public class ManagePatchesAppActivity extends ManagePatchesActivity {
	private AdView adView;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		adView = (AdView) findViewById(R.id.ad);
		AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice(AdConfiguration.DEVICE_ID_TESTER)
			.build();
		adView.loadAd(adRequest);
	}	
}
