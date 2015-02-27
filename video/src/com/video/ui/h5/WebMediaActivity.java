package com.video.ui.h5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import com.video.ui.utils.NoWifiAlertManager;

/**
 *@author tangfuling
 *
 */

public class WebMediaActivity extends BaseWebMediaActivity {
	
	public static final String TAG = WebMediaActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    setTheme(android.R.style.Theme_Light_NoTitleBar);
		super.onCreate(savedInstanceState);
		if(NoWifiAlertManager.isShowNoWifiAlertDialog(this)){
			NoWifiAlertManager.popupNoWifiAlertDialog(this, new NoWifiAlertManager.AlertDialogResultListener() {
				@Override
				public void onNotifyClickPositive() {
					loadHtml5();					
				}
				
				@Override
				public void onNotifyClickNegative() {
					startSystemSettingActivity();
				}
			});
		}else{

			//delay to laod html5
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					loadHtml5();
				}
			}, 200);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void startSystemSettingActivity() {
		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		startActivity(intent);	
	}
}
