/**
 *   Copyright(c) 2014 XiaoMi TV Group
 *   
 *   QiyiVideoView.java
 *  
 *   @author tianli(tianli@xiaomi.com)
 * 
 *   @date 2014-6-19
 */

package com.xiaomi.video.player.duokan;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.miui.videoplayer.media.IMediaPlayer.*;
import com.xiaomi.video.player.duokan.media.AdsPlayListener;
import com.xiaomi.video.player.duokan.media.DuoKanMediaPlayer;
import com.xiaomi.video.player.duokan.media.DuoKanPlayer;
import com.xiaomi.video.player.duokan.media.OnMediaPlayerListener;


/**
 * @author tianli
 *
 */
public abstract class SdkVideoView implements IVideoView{
	
	public static final String TAG = "SdkVideoView";

	protected DuoKanPlayer mPlayer;
	
	private RelativeLayout mViewGroup;
	
	private Context mContext;
	
	protected boolean mIsAdsPlaying = false;
	
	private OnBufferingUpdateListener mBufferingUpdateListener;
	private OnErrorListener mErrorListener;
	private OnCompletionListener mCompletionListener;
	private OnInfoListener mInfoListener;
	private OnPreparedListener mPreparedListener;
	private OnSeekCompleteListener mSeekCompleteListener;
	private OnVideoSizeChangedListener mVideoSizeChangedListener;
	
	protected OnVideoLoadingListener mOnVideoLoadingListener;
	
	protected AdsPlayListener mAdsPlayListener;
	public SdkVideoView(Context context){
		mContext = context;
		init();
	}
	
	private void init(){
		mViewGroup = new RelativeLayout(mContext);
		DuoKanMediaPlayer player = new DuoKanMediaPlayer();
		mPlayer = new DuoKanPlayer(player);

		mPlayer.setMediaPlayerListener(mMediaPlayerListener);
	}
	
	public DuoKanPlayer getPlayer(){
	    return mPlayer;
	}
	
	@Override
	public void start() {
		mPlayer.start();
	}

	@Override
	public void pause() {
		mPlayer.pause();
	}

	@Override
	public int getDuration() {
		return mPlayer.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return mPlayer.getCurrentPosition();
	}

	@Override
    public int getRealPlayPosition() {
        return getCurrentPosition();
    }

    @Override
	public void seekTo(int pos) {
    	Log.d(TAG, "seekTo " + pos);
    	mPlayer.seekTo(pos);
	}

	@Override
	public boolean isPlaying() {
		return mPlayer.isPlaying() && mPlayer.isInPlaybackState();
	}

	@Override
    public boolean isInPlaybackState() {
        return mPlayer.isInPlaybackState();
    }

    @Override
	public boolean canPause() {
		return !mIsAdsPlaying;
	}

	@Override
	public boolean canSeekBackward() {
		return !mIsAdsPlaying;
	}

	@Override
	public boolean canSeekForward() {
		return !mIsAdsPlaying;
	}

	@Override
	public View asView() {
		return mViewGroup;
	}

	@Override
	public boolean canBuffering() {
		return true;
	}
	
	@Override
    public boolean isAirkanEnable() {
	    if(isAdsPlaying()){
	        return false;
	    }
        return true;
    }

    @Override
	public boolean isAdsPlaying() {
		return mIsAdsPlaying;
	}

	@Override
	public Uri getUri() {
		return mPlayer.getUri();
	}

	@Override
	public void onActivityPause() {
	    if(mPlayer != null){
	        mPlayer.onActivityPause();
	    }     
	}

    @Override
    public void onActivityResume() {
        if(mPlayer != null){
            mPlayer.onActivityResume();
        } 
    }



	@Override
	public int getBufferPercentage() {
		return mPlayer.getBufferPercentage();
	}

	@Override
	final public void setOnPreparedListener(OnPreparedListener listener) {
		mPreparedListener = listener;
	}

	@Override
	final public void setOnCompletionListener(OnCompletionListener listener) {
		mCompletionListener = listener;
	}

	@Override
	final public void setOnErrorListener(OnErrorListener listener) {
		mErrorListener = listener;
	}

	@Override
	final public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
		mSeekCompleteListener = listener;
	}

	@Override
	final public void setOnInfoListener(OnInfoListener listener) {
		mInfoListener = listener;
	}

	@Override
	final public void setOnBufferingUpdateListener(
			OnBufferingUpdateListener onBufferingUpdateListener) {
		mBufferingUpdateListener = onBufferingUpdateListener;
	}

	@Override
	final public void setOnVideoSizeChangedListener(
			OnVideoSizeChangedListener onVideoSizeChangedListener) {
		mVideoSizeChangedListener = onVideoSizeChangedListener;
	}
	
	@Override
	public void setOnVideoLoadingListener(OnVideoLoadingListener loadingListener) {
		mOnVideoLoadingListener = loadingListener;
	}


    @Override
    public void setPlayInfo(Object playInfo) {
    }

    @Override
    public void setForceFullScreen(boolean forceFullScreen) {
    }
	

    @Override
    public void requestVideoLayout() {
    }

    @Override
    public boolean hasLoadingAfterAd() {
        return true;
    }

    private OnMediaPlayerListener mMediaPlayerListener = new OnMediaPlayerListener(){

        @Override
        public com.miui.videoplayer.media.IMediaPlayer.OnErrorListener getOnErrorListener() {
            return mErrorListener;
        }

        @Override
        public com.miui.videoplayer.media.IMediaPlayer.OnCompletionListener getOnCompletionListener() {
            return mCompletionListener;
        }

        @Override
        public com.miui.videoplayer.media.IMediaPlayer.OnPreparedListener getOnPreparedListener() {
            return mPreparedListener;
        }

        @Override
        public com.miui.videoplayer.media.IMediaPlayer.OnSeekCompleteListener getOnSeekCompleteListener() {
            return mSeekCompleteListener;
        }

        @Override
        public com.miui.videoplayer.media.IMediaPlayer.OnInfoListener getOnInfoListener() {
            return mInfoListener;
        }

        @Override
        public com.miui.videoplayer.media.IMediaPlayer.OnBufferingUpdateListener getOnBufferingUpdateListener() {
            return mBufferingUpdateListener;
        }

        @Override
        public com.miui.videoplayer.media.IMediaPlayer.OnVideoSizeChangedListener getOnVideoSizeChangedListener() {
            return mVideoSizeChangedListener;
        }
    };
}
