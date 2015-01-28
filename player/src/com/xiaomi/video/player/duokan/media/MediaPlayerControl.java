package com.xiaomi.video.player.duokan.media;

import android.net.Uri;

import java.util.Map;

public interface MediaPlayerControl {
	void setDataSource(String uri);
	void setDataSource(String uri, Map<String, String> headers);
	void start();
	void pause();
	int getDuration();
	int getCurrentPosition();
	int getRealPlayPosition();
	void seekTo(int pos);
	boolean isPlaying();
	boolean isInPlaybackState();
	int getBufferPercentage();
	boolean canPause();
	boolean canSeekBackward();
	boolean canSeekForward();
	boolean canBuffering();
	boolean isAirkanEnable();
	void close();
	
	Uri getUri();
	
	public boolean isAdsPlaying();
//	MediaPlayer.MediaInfo getMediaInfo();
//	public boolean get3dMode();
//	public void set3dMode(boolean mode);
}
