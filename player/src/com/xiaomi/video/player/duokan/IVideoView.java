/**
 *   Copyright(c) 2014 XiaoMi TV Group
 *   
 *   IVideoView.java
 *  
 *   @author tianli(tianli@xiaomi.com)
 * 
 *   @date 2014-6-19
 */

package com.xiaomi.video.player.duokan;

import android.view.View;
import com.miui.videoplayer.media.IMediaPlayer.*;
import com.miui.videoplayer.widget.AdView;
import com.xiaomi.video.player.duokan.media.AdsPlayListener;
import com.xiaomi.video.player.duokan.media.MediaPlayerControl;


/**
 * @author tianli
 *
 */
public interface IVideoView extends MediaPlayerControl {

	public View asView();
	public void requestVideoLayout();
	

	
	public boolean hasLoadingAfterAd();
	
	public void setPlayInfo(Object playInfo);
	
	public void onActivityPause();
	public void onActivityResume();
	public void onActivityDestroy();
	
	public boolean isSupportZoom();
	public void setForceFullScreen(boolean forceFullScreen);

	public void setAdsPlayListener(AdsPlayListener adPlayListener);
	public void attachAdView(AdView adView);
	
	public void setOnVideoLoadingListener(OnVideoLoadingListener loadingListener);
    public void setOnPreparedListener(OnPreparedListener listener);
    public void setOnCompletionListener(OnCompletionListener listener);
	public void setOnErrorListener(OnErrorListener listener);
	public void setOnSeekCompleteListener(OnSeekCompleteListener listener);
	public void setOnInfoListener(OnInfoListener listener);
	public void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener);
	public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener);
	
	public static interface OnVideoLoadingListener{
		public void onVideoLoading(IVideoView videoView);
		public void onVideoHideLoading(IVideoView videoView);
	}
	
}
