package net.zhuoweizhang.mcpelauncher;

import android.app.*;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import com.google.ads.*;

public class LauncherAppActivity extends LauncherActivity {

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
}
