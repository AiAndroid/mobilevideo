package com.video.ui.push;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.video.ui.MainActivity;

public class PendingIntentFactory {


	public PendingIntentFactory() {
	
	}

	public static Intent[] buildIntent(Context context, String actionUrl, String actiontype){

		Intent[] intents = new Intent[2];
		intents[0] = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
		intents[1] = Intent.makeRestartActivityTask(new ComponentName(context, MainActivity.class));
		return intents;

	}

	public static PendingIntent createPendingIntent(Context context, String actionUrl, String actiontype){
		if(context == null){
			return null;
		}
		PendingIntent clickPendingIntent = null;
		Intent[] resultIntents = buildIntent(context, actionUrl, actiontype);
		if(resultIntents == null){
			return null;
		}else{
			clickPendingIntent = PendingIntent.getActivities(context, actionUrl.hashCode(),
					resultIntents, PendingIntent.FLAG_UPDATE_CURRENT);
			return clickPendingIntent;
		}
	}

	
	public static void startIntent(Context context, String actionUrl, String actiontype){
		if(context == null){
			return;
		}
		Intent[] resultIntents = buildIntent(context, actionUrl, actiontype);
		if(resultIntents != null){
			context.startActivities(resultIntents);
		}
	}
}
