package com.video.ui.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.video.ui.R;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushMessage;
import org.json.JSONException;
import org.json.JSONObject;

public class MiPushManager {

    private final String OPEN_APP = "open";

    private static final String ACTIONURL = "actionUrl";
    private static final String PRITEXT = "priText";
    private static final String SECTEXT = "secText";
    private static final String TITTEXT = "titText";
    private static final String TYPE = "type";
    private static final String TAG_ID = "id";
    private boolean mIsRegistered  = false;

    public static final String APP_ID = "2882303761517147566";
    public static final String APP_KEY = "5481714735566";
    public static final String APP_Category = "21352B8F52038B188540F1909B32726E";

    private Context mContext;
    private static MiPushManager _instance;
    public static MiPushManager getInstance(Context context){
        if(_instance == null){
            _instance = new MiPushManager();
            _instance.registerMiPushEnv(context);
        }

        return _instance;
    }
    private void registerMiPushEnv(Context context){
        if(!mIsRegistered){
            mIsRegistered = true;
            MiPushClient.registerPush(context, APP_ID, APP_KEY);
        }

        mContext = context;
    }

    public void processAds(MiPushMessage message) {
    	int passThrough = message.getPassThrough();
        String msgid = message.getMessageId();
        String actionUrl = "";
        String priText = "";
        String secText = "";
        String titText = "";
        String type = "";
        String ad_id = "";
        try {
            JSONObject mJSONObject = new JSONObject(message.getContent());
            actionUrl = mJSONObject.optString(ACTIONURL);
            priText = mJSONObject.optString(PRITEXT);
            secText = mJSONObject.optString(SECTEXT);
            titText = mJSONObject.optString(TITTEXT);
            type = mJSONObject.optString(TYPE);
            ad_id = mJSONObject.optString(TAG_ID);
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        
        if ((TextUtils.isEmpty(priText)) || (TextUtils.isEmpty(secText))
                || (TextUtils.isEmpty(titText)) || (TextUtils.isEmpty(type))) {
            return;
        }
        
        if(OPEN_APP.equalsIgnoreCase(type) && !TextUtils.isEmpty(actionUrl) && passThrough == 1){
        	Uri uri = Uri.parse(actionUrl);
        	String actiontype = uri.getQueryParameter("actiontype");
        	String force = uri.getQueryParameter("forcepush");
        	if("true".equalsIgnoreCase(force) ){
        		NotificationManager mNotificationManager = 
        	            (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        		PendingIntent contentIntent = PendingIntentFactory.createPendingIntent(mContext, actionUrl, actiontype);
        		if(contentIntent == null){
        			return;
        		}
        		Notification mNotification = new Notification.Builder(mContext)
        		.setAutoCancel(true)
        		.setTicker(titText)
        		.setContentTitle(priText)
        		.setContentText(secText)
        		.setContentIntent(contentIntent)
        		.setSmallIcon(R.drawable.ic_launcher)
        		.setWhen(System.currentTimeMillis())
        		.build();

                int notification_id = actionUrl.hashCode();
                mNotificationManager.notify(notification_id, mNotification);
        	}
    	} else if(OPEN_APP.equalsIgnoreCase(type) && !TextUtils.isEmpty(actionUrl) && passThrough == 0){
        	Uri uri = Uri.parse(actionUrl);
        	String actiontype = uri.getQueryParameter("actiontype");
        	PendingIntentFactory.startIntent(mContext, actionUrl, actiontype);
    	}
    }
}