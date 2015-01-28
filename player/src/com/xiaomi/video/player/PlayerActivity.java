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
import com.qiyi.playersdk.AdPlayListener;

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


            videoView.setDataSource(String.format("{\"vid\":\"%1$s\"}", vid));

            //IQiyiVideoPlayer.getInstance().setDataSource(vid, videoView.getPlayer(), mAdPlayListenerQiyi);

        }catch(Exception t){
            Log.d(TAG, t.getMessage());
            t.printStackTrace();
        }
    }

    private Handler mHandler;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    QiyiVideoView videoView;
    @Override
    protected void onNewIntent(android.content.Intent intent){

    }

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
        }
    };

    public void onCompletion(MediaPlayer mp) {
        this.finish();
    }

}