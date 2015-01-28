/**
 *   Copyright(c) 2014 XiaoMi TV Group
 *   
 *   IVideoPlayer.java
 *  
 *   @author tianli(tianli@xiaomi.com)
 * 
 *   @date 2014-8-17
 */

package com.xiaomi.video.player.duokan.media;

import android.view.SurfaceHolder;
import android.widget.Button;
import com.miui.videoplayer.media.IMediaPlayer;

/**
 * @author tianli
 *
 */
public interface IVideoPlayer {
	
	public void onActivityResume();
	
	public void onActivityPause();
	
	public void onActivityDestroy();
	
	public void play(String video, int resolution, Button button, IMediaPlayer adPlayer, IMediaPlayer videoPlayer);
	
	public void play(String video, int resolution, IMediaPlayer videoPlayer);
	
	public void offlineDownload(String video, String path);
	
	public void setAdsPlayListener(AdsPlayListener listener);
	
	public void onSurfaceChanged(SurfaceHolder holder, int format, int w, int h);
	
	public void onSurfaceCreated(SurfaceHolder holder);
	
	public void onSurfaceDestroyed(SurfaceHolder holder);
	
}
