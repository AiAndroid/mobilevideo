package com.video.ui.miui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.video.ui.R;
import com.video.ui.SettingActivity;
import com.video.ui.idata.iDataORM;
import com.video.ui.utils.AndroidUtils;

public class NoWifiAlertManager {

	private static final String TAG = NoWifiAlertManager.class.getName();
	
	
	public static boolean isShowNoWifiAlertDialog(Context context){
		if (AndroidUtils.isNetworkConncected(context)
				&& !AndroidUtils.isFreeNetworkConnected(context) 
				&& iDataORM.isOpenCellularPlayHint(context)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void popupNoWifiAlertDialog(Context context, final AlertDialogResultListener listener) {
		if(context == null){
			return;
		}
		View contentView = View.inflate(context.getApplicationContext(), R.layout.vp_play_datastream_hint_view, null);
		String negativeStr = context.getResources().getString(R.string.vp_datastream_alert_negative_button);
		String positiveStr = context.getResources().getString(R.string.vp_datastream_alert_positive_button);
		AlertDialog dialog = new AlertDialog.Builder(context, miui.R.style.Theme_Light_Dialog_Alert).create();
		dialog.setTitle(R.string.vp_datastream_alert_title);
        dialog.setView(contentView);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeStr, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				listener.onNotifyClickNegative();
			}
		 } );
        
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveStr, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				listener.onNotifyClickPositive();
			}
        });
		dialog.setCancelable(false);
		
		try {
			dialog.show();
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
	}
	
	public static interface AlertDialogResultListener{
		public void onNotifyClickPositive();
		public void onNotifyClickNegative();
	}

	public static void showUseDataStreamDialog(final Context context, final int ci) {
		View contentView = View.inflate(context, R.layout.download_datastream_hint_view, null);
		String negativeStr = context.getResources().getString(R.string.datastream_alert_negative_button);
		String positiveStr = context.getResources().getString(R.string.datastream_alert_positive_button);
		miui.app.AlertDialog dialog = new miui.app.AlertDialog.Builder(context, miui.R.style.Theme_Light_Dialog_Alert).create();
		dialog.setTitle(R.string.datastream_alert_title);
		dialog.setView(contentView);
		dialog.setButton(miui.app.AlertDialog.BUTTON_NEGATIVE, negativeStr, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				startSystemSettingActivity(context);
			}
		} );

		dialog.setButton(miui.app.AlertDialog.BUTTON_POSITIVE, positiveStr, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//startDownload(ci);
				Intent intent = new Intent(context, SettingActivity.class);
				context.startActivity(intent);
			}
		});
		dialog.setCancelable(false);

		try {
			dialog.show();
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
	}

	private static void startSystemSettingActivity(Context context) {
		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		context.startActivity(intent);
	}
}
