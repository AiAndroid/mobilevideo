package com.video.ui.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.video.ui.R;
import com.video.ui.idata.iDataORM;

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
		AlertDialog dialog = new AlertDialog.Builder(context, android.R.style.Holo_Light_ButtonBar_AlertDialog).create();
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
}
