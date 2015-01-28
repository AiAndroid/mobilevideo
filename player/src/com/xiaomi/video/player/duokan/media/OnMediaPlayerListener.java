/**
 *   Copyright(c) 2014 XiaoMi TV Group
 *   
 *   MediaPlayerListener.java
 *  
 *   @author tianli(tianli@xiaomi.com)
 * 
 *   @date 2014-7-21
 */

package com.xiaomi.video.player.duokan.media;


import com.miui.videoplayer.media.IMediaPlayer;

/**
 * @author tianli
 *
 */
public interface OnMediaPlayerListener {
	
	public IMediaPlayer.OnErrorListener getOnErrorListener();
	public IMediaPlayer.OnCompletionListener getOnCompletionListener();
	public IMediaPlayer.OnPreparedListener getOnPreparedListener();
	public IMediaPlayer.OnSeekCompleteListener getOnSeekCompleteListener();
	public IMediaPlayer.OnInfoListener getOnInfoListener();
	public IMediaPlayer.OnBufferingUpdateListener getOnBufferingUpdateListener();
	public IMediaPlayer.OnVideoSizeChangedListener getOnVideoSizeChangedListener();

}
