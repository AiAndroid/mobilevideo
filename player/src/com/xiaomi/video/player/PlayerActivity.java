package com.xiaomi.video.player;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.duokan.MediaPlayer;
import com.miui.videoplayer.media.IMediaPlayer;
import com.miui.videoplayer.widget.AdView;
import com.qiyi.playersdk.AdPlayListener;
import com.qiyi.playersdk.IQiyiVideoPlayer;
import com.xiaomi.video.player.duokan.IVideoView;
import com.xiaomi.video.player.duokan.media.AdsPlayListener;
import org.qiyi.android.coreplayer.QIYIVideoView;

/**
 * Created by liuhuadong on 9/19/14.
 */
public class PlayerActivity extends Activity implements MediaPlayer.OnCompletionListener{
    public static final String TAG = "Videoplayer";
    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            Log.d(TAG, "onError");
            return false;
        }
    };
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height) {
            Log.d(TAG, "onVideoSizeChanged");
            videoView.requestVideoLayout();
        }
    };
    private IMediaPlayer.OnBufferingUpdateListener mOnBufferUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            Log.d(TAG, "onBufferingUpdate");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_player);

        try{
            mHandler = new Handler();
            RelativeLayout root = (RelativeLayout)findViewById(R.id.video_contain_view) ;
            String vid = getIntent().getStringExtra("vid");
            videoView  = new QiyiVideoView(PlayerActivity.this, root);
            videoView.asView().setKeepScreenOn(true);

            mAdView = new AdView(PlayerActivity.this);
            mAdView.setVisibility(View.GONE);
            videoView.setAdsPlayListener(mOnAdsPlayListener);
            videoView.attachAdView(mAdView);



            videoView.setForceFullScreen(true);
            videoView.asView().setVisibility(View.VISIBLE);
            videoView.setOnPreparedListener(mOnPreapredListener);
//            videoView.setOnInfoListener(mOnInfoListener);
            videoView.setOnErrorListener(mOnErrorListener);
//            videoView.setOnVideoLoadingListener(mOnVideoLoadingListener);
//            videoView.setOnCompletionListener(mOnCompletionListener);
            videoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            videoView.setOnBufferingUpdateListener(mOnBufferUpdateListener);

            videoView.getPlayer().setOnPreparedListener(mOnPreapredListener);
            videoView.getPlayer().setOnErrorListener(mOnErrorListener);

//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 400);
//            params.gravity = Gravity.CENTER;
//            videoView.asView().setLayoutParams(params);
//            videoView.asView().setBackgroundColor(Color.GREEN);


//            RelativeLayout rl = (RelativeLayout)findViewById(R.id.video_contain_view) ;
//            params.topMargin = 400;
//            rl.addView(videoView.asView(), params);
//
//            mAdView.setBackgroundColor(Color.BLUE);
//            rl.addView(mAdView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 400));

            videoView.setDataSource(String.format("{\"vid\":\"%1$s\"}", vid));

            //com.qiyi.playersdk.IQiyiVideoPlayer.getInstance().setDataSource(vid, videoView.getPlayer(), mAdPlayListenerQiyi);


            videoView.start();

        }catch(Exception t){
            t.printStackTrace();
        }
    }

    private IMediaPlayer.OnVideoSizeChangedListener mCacheVideoSizeChangedListener;
    private IMediaPlayer.OnInfoListener mCacheInfoListener;
    private IMediaPlayer.OnSeekCompleteListener mCacheSeekCompleteListener;
    private IMediaPlayer.OnErrorListener mCacheOnErrorListener;
    private IMediaPlayer.OnCompletionListener mCacheOnCompleteListener;
    private IVideoView.OnVideoLoadingListener mOnVideoLoadingListener;

    private Handler mHandler;
    private AdView mAdView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    QiyiVideoView videoView;
    @Override
    protected void onNewIntent(android.content.Intent intent){

    }

    @Override
    protected void onResume() {
        super.onResume();
        IQiyiVideoPlayer.getInstance().onResume();
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        IQiyiVideoPlayer.getInstance().onPause();
    }

    private void continuePlay(){

    }

    private AdsPlayListener mOnAdsPlayListener = new AdsPlayListener() {
        @Override
        public void onAdsTimeUpdate(int leftSeconds) {
            if(mAdView != null){
                mAdView.onAdsTimeUpdate(leftSeconds);
            }
        }

        @Override
        public void onAdsPlayStart() {
            if(mAdView != null){
                mAdView.onAdsPlayStart();
            }

            Log.d(TAG, "onAdPlayStart");
        }

        @Override
        public void onAdsPlayEnd() {
            if(mAdView != null){
                mAdView.onAdsPlayEnd();
            }
            if(videoView != null && !videoView.hasLoadingAfterAd()){
                continuePlay();
            }

            Log.d(TAG, "onAdPlayEnd");
        }

        @Override
        public void onAdsDuration(int duration) {
            if(mAdView != null){
                mAdView.onAdsDuration(duration);
            }
        }
    };


    private AdPlayListener mAdPlayListenerQiyi = new AdPlayListener() {

        @Override
        public void onAdPlayStart() {
            Log.d(TAG, "onAdPlayStart");

        }

        @Override
        public void onAdPlayEnd() {
            Log.d(TAG, "onAdPlayEnd");

        }

        @Override
        public void onAdDuration(int duration) {
            Log.d(TAG, "onAdDuration : " + duration);

        }
    };

    private IMediaPlayer.OnPreparedListener mOnPreapredListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            Log.d(TAG, "onPrepared");
            if(videoView != null){
                videoView.start();

                IQiyiVideoPlayer.getInstance().start();
            }
        }
    };

    public void onCompletion(MediaPlayer mp) {
        this.finish();
    }

}