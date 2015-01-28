package com.xiaomi.video.player;

import android.app.Activity;
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
import com.xiaomi.video.player.duokan.IVideoView;
import com.xiaomi.video.player.duokan.media.AdsPlayListener;

/**
 * Created by liuhuadong on 9/19/14.
 */
public class PlayerActivity extends Activity implements MediaPlayer.OnCompletionListener{
    public static final String TAG = "Videoplayer";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_player);

        try{
            mHandler = new Handler();

            String vid = getIntent().getStringExtra("vid");
            videoView  = new QiyiVideoView(PlayerActivity.this);

            mAdView = new AdView(PlayerActivity.this);
            mAdView.setVisibility(View.GONE);
            videoView.setAdsPlayListener(mOnAdsPlayListener);
            videoView.attachAdView(mAdView);



            videoView.setForceFullScreen(true);
            videoView.asView().setVisibility(View.VISIBLE);

            /*
            videoView.setOnPreparedListener(mOnPreapredListener);
            videoView.setOnInfoListener(mOnInfoListener);
            videoView.setOnErrorListener(mOnErrorListener);
            videoView.setOnVideoLoadingListener(mOnVideoLoadingListener);
            videoView.setOnCompletionListener(mOnCompletionListener);
            videoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            videoView.setOnBufferingUpdateListener(mOnBufferUpdateListener);
            */
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            videoView.asView().setLayoutParams(params);



            RelativeLayout rl = (RelativeLayout)findViewById(R.id.video_contain_view) ;
            rl.addView(videoView.asView(), params);

            rl.addView(mAdView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                    , FrameLayout.LayoutParams.MATCH_PARENT));

            videoView.setDataSource(String.format("{\"vid\":\"%1$s\"}", vid));

            //IQiyiVideoPlayer.getInstance().setDataSource(vid, videoView.getPlayer(), mAdPlayListenerQiyi);

        }catch(Exception t){
            Log.d(TAG, t.getMessage());
            t.printStackTrace();
        }
    }

    private IMediaPlayer.OnVideoSizeChangedListener mCacheVideoSizeChangedListener;
    private IMediaPlayer.OnBufferingUpdateListener mCacheBufferingUpdateListener;
    private IMediaPlayer.OnInfoListener mCacheInfoListener;
    private IMediaPlayer.OnSeekCompleteListener mCacheSeekCompleteListener;
    private IMediaPlayer.OnErrorListener mCacheOnErrorListener;
    private IMediaPlayer.OnCompletionListener mCacheOnCompleteListener;
    private IMediaPlayer.OnPreparedListener mCacheOnPreparedListener;
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


    private void continuePlay(){
        if(!videoView.isAdsPlaying()){
            videoView.start();
        }
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

    public void onCompletion(MediaPlayer mp) {
        this.finish();
    }

}