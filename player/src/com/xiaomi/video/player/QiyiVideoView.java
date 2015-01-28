/**
 *   Copyright(c) 2014 XiaoMi TV Group
 *   
 *   QiyiVideoView.java
 *  
 *   @author tianli(tianli@xiaomi.com)
 * 
 *   @date 2014-6-19
 */

package com.xiaomi.video.player;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;
import com.miui.videoplayer.widget.AdView;
import com.qiyi.playersdk.AdPlayListener;
import com.qiyi.playersdk.IQiyiVideoPlayer;
import com.xiaomi.video.player.duokan.SdkVideoView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author tianli
 *
 */
public class QiyiVideoView extends SdkVideoView {
	
	public static final String TAG = "QiyiVideoView";

	private Handler mHandler ;

	private Activity mContext;
	
	private int mAdsDuration = 0; 
	
	public QiyiVideoView(Context context){
		super(context);
		mContext = (Activity)context;
		init();
	}
	
	private void init(){

		mHandler = new Handler();
	    try{
	        IQiyiVideoPlayer.getInstance().initVideoPlayer(mContext, mContext.getApplicationContext(),
	                getHandler(), (RelativeLayout)asView());
	    }catch(Throwable t){
	    }
	}
	
	public Handler getHandler(){
		return mHandler;
	}
	
	@Override
    public int getDuration() {
	    return Math.max(super.getDuration() - mAdsDuration, 0);
    }

    @Override
    public void seekTo(int pos) {
        int realpos = pos;
        realpos = pos + mAdsDuration;
        super.seekTo(realpos);
    }

    @Override
	public int getCurrentPosition() {
		int currentPosition = super.getCurrentPosition();
		currentPosition = Math.max(0, currentPosition - mAdsDuration);
		return currentPosition;
	}

	@Override
    public int getRealPlayPosition() {
        return super.getCurrentPosition();
    }

    private String getVid(String uri){
		try {
			JSONObject json = new JSONObject(uri);
			return json.getString("vid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return uri;
	}
	
	@Override
	public void setDataSource(String uri) {
		Log.d(TAG, "setDataSource : " + uri.toString());
		setDataSource(uri, null);
	}

	@Override
	public void setDataSource(String uri, Map<String, String> headers) {
		Log.d(TAG, "setDataSource : " + uri.toString());
		mAdsDuration = 0;
		IQiyiVideoPlayer.getInstance().setDataSource(getVid(uri), getPlayer(), mAdPlayListenerQiyi);
	}

	@Override
	public void close() {
		IQiyiVideoPlayer.getInstance().reset();
		IQiyiVideoPlayer.getInstance().release(true);
	}


	@Override
	public void onActivityPause() {
	    super.onActivityPause();
		IQiyiVideoPlayer.getInstance().hiddenAdsFrame();
		IQiyiVideoPlayer.getInstance().onPause();
	}

	@Override
	public void onActivityResume() {
	    super.onActivityResume();
	    IQiyiVideoPlayer.getInstance().onResume();
	}

	
	private AdPlayListener mAdPlayListenerQiyi = new AdPlayListener() {
		
		@Override
		public void onAdPlayStart() {
			Log.d(TAG, "onAdPlayStart");
			mIsAdsPlaying = true;
			if(mAdsPlayListener != null){
				mAdsPlayListener.onAdsPlayStart();
			}
		}
		
		@Override
		public void onAdPlayEnd() {
			Log.d(TAG, "onAdPlayEnd");
			mIsAdsPlaying = false;
			if(mAdsPlayListener != null){
				mAdsPlayListener.onAdsPlayEnd();
			}
	         int currentPosition = getCurrentPosition();
			mAdsDuration = Math.max(currentPosition, mAdsDuration);
		}
		
		@Override
		public void onAdDuration(int duration) {
			Log.d(TAG, "onAdDuration : " + duration);
			mAdsDuration = duration;
			if(mAdsPlayListener != null){
				mAdsPlayListener.onAdsDuration(duration);
			}
		}
	};

	@Override
	public void onActivityDestroy() {
	}

    @Override
    public void setForceFullScreen(boolean forceFullScreen) {
    }


	@Override
	public void attachAdView(AdView adView) {
		adView.setDisableView(true);
	}


	@Override
	public boolean isSupportZoom() {
		return false;
	}

	@Override
    public boolean hasLoadingAfterAd() {
        return false;
    }
}
