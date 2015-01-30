package com.video.ui.push;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

public class MediaPushMessageReceiver extends PushMessageReceiver {
	
	private String TAG = MediaPushMessageReceiver.class.getName();
	
	private final String FROM_SERVER = "notification from online video server";
	public  static final String AdSPREFIX = "com.xiaomi.miui.pushads.sdk";
	private final static String DEFAULT_TOPIC_ID = ":MiuiVideo";
	public static final String APP_ID = "2882303761517147566";
	public static final String APP_KEY = "5481714735566";
	public static final String APP_Category = "21352B8F52038B188540F1909B32726E";

	private Handler mHandler = new Handler(Looper.getMainLooper());
	//command result
	private long mResultCode = -1;
    private String mTopic;
    private String mRegId;
    private String mReason;

	private Context mContext;
    public void setAlias(Context context){
        String outerId = null;
		outerId = "35332675";

        MiPushClient.setAlias(context, AdSPREFIX + outerId, APP_Category);
    }
    
    public void setDefaultTopic(Context context){
    	mTopic = AdSPREFIX + DEFAULT_TOPIC_ID;
    	MiPushClient.subscribe(context, mTopic, APP_Category);
    }    
    
    public void setTopic(Context context, String topic){
    	if(TextUtils.isEmpty(topic)){
    		mTopic = AdSPREFIX + DEFAULT_TOPIC_ID;
    	}else{
    		mTopic = topic;
    	}
    	MiPushClient.subscribe(context, mTopic, APP_Category);
    }     
    
    public void unsetTopic(Context context, String topic){
    	if(TextUtils.isEmpty(topic)){
    		mTopic = AdSPREFIX + DEFAULT_TOPIC_ID;
    	}else{
    		mTopic = topic;
    	}
    	MiPushClient.unsubscribe(context, mTopic, APP_Category);
    }
    
	//客户端向服务器发送命令后的返回响应
	@Override
	public void onCommandResult(Context context, MiPushCommandMessage message) {
		Log.d(TAG, "command result");
		if(message == null){
			return;
		}

		mContext = context;
		mResultCode = message.getResultCode();
        mReason = message.getReason();
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        if(arguments != null) {
            if(MiPushClient.COMMAND_REGISTER.equals(command)
                    && arguments.size() == 1) {
                mRegId = arguments.get(0);
                setAlias(context);
                setDefaultTopic(context);
            } else if((MiPushClient.COMMAND_SET_ALIAS .equals(command)
                    || MiPushClient.COMMAND_UNSET_ALIAS.equals(command))
                    && arguments.size() == 1) {
            } else if(!TextUtils.isEmpty(MiPushClient.COMMAND_SUBSCRIBE_TOPIC)
            		&& (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command))
                    && arguments.size() == 1) {
            } else if(!TextUtils.isEmpty(MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC)
            		&& (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command))
                    && arguments.size() == 1) {
            } else if(MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)
                    && arguments.size() == 2) {
            }
        }
        
        if(mResultCode != 0){
        	Log.e(TAG, "onCommandResult message:" + message.toString());
        }
	}

	//服务器向客户端发送的推送信息
	@Override
	public void onReceiveMessage(Context context, MiPushMessage message) {
		Log.d(TAG, "message result: " +message);
		//must run on ui thread

		mContext = context;
		mHandler.post(new ProcessMessageRunnable(message));
	}
	
	//UI task
	private class ProcessMessageRunnable implements Runnable {
		
		MiPushMessage message;
		
		public ProcessMessageRunnable(MiPushMessage message) {
			this.message = message;
		}
		
		@Override
		public void run() {
			Log.d(TAG, "notification run");
			processMessage(message);
		}
	}
	
	//packaged method	
	private void processMessage(MiPushMessage message) {
		if(message == null) {
			return;
		}
		processMessageFromWeb(message);
	}
	

	
	private void processMessageFromWeb(MiPushMessage message){
		if(message != null) {
			Log.d(TAG, "process msg from web");
			MiPushManager.getInstance(mContext).processAds(message);
		}
	}
}
