package com.video.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.VideoBlocks;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;
import com.video.ui.loader.GenericDetailLoader;
import com.video.ui.view.DetailFragment;
import com.video.ui.view.RetryView;
import com.xiaomi.video.player.PlayerActivity;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class MediaDetailActivity extends DisplayItemActivity implements LoaderCallbacks<VideoBlocks<VideoItem>> {

    private static String TAG = MediaDetailActivity.class.getName();
    private int loaderID;
    private TextView playview ;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_detail_layout);

        View titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);
        tv.setText(item.title);

        titlebar.findViewById(R.id.channel_filte_btn).setVisibility(View.GONE);
        titlebar.findViewById(R.id.channel_search_btn).setVisibility(View.GONE);

        playview = (TextView) findViewById(R.id.detail_play);

        View view = findViewById(R.id.detail_play);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mVidoeInfo == null || playview.getText().equals(getString(R.string.play_source_fetching))){
                        Log.d(TAG, "wait to fetch url information");
                        return;
                    }

                    playview.setText(getString(R.string.play_source_fetching));
                    String id = mVidoeInfo.blocks.get(0).media.items.get(0).id;
                    String url = CommonUrl.BaseURL + "play?id=" +id.substring(id.indexOf('/', 0) + 1) + "&cp="+mVidoeInfo.blocks.get(0).media.cps.get(0).cp;
                    String calledURL = new CommonUrl(getBaseContext()).addCommonParams(url);

                    Response.Listener<PlaySource> listener = new Response.Listener<PlaySource>() {
                        @Override
                        public void onResponse(PlaySource response) {
                            playview.setText(getString(R.string.play));
                            Log.d(TAG, "play source:"+response);

                            Intent intent = new Intent(getBaseContext(), PlayerActivity.class);
                            intent.putExtra("vid", response.cp_id);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getBaseContext().startActivity(intent);
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            playview.setText(getString(R.string.play));

                            Log.d(TAG, "fail to fetch play source");
                        }
                    };

                    RequestQueue requestQueue = VolleyHelper.getInstance(getBaseContext()).getAPIRequestQueue();
                    BaseGsonLoader.GsonRequest<PlaySource> gsonRequest = new BaseGsonLoader.GsonRequest<PlaySource>(calledURL, new TypeToken<PlaySource>(){}.getType(), null, listener, errorListener);
                    gsonRequest.setCacheNeed(getBaseContext().getCacheDir() + "/" + id + ".playsource.cache");
                    requestQueue.add(gsonRequest);

                } catch (Exception ne) {
                    ne.printStackTrace();
                }
            }
        });


        mLoadingView = makeEmptyLoadingView(getBaseContext(), (RelativeLayout) findViewById(R.id.tabs_content));
        mLoadingView.setOnRetryListener(retryLoadListener);

        loaderID = GenericDetailLoader.VIDEO_LOADER_ID;
        getSupportLoaderManager().initLoader(GenericDetailLoader.VIDEO_LOADER_ID, savedInstanceState, this);
    }

    @Override
    public Loader<VideoBlocks<VideoItem>> onCreateLoader(int id, Bundle bundle) {

        if(id == loaderID) {
            mLoader = GenericDetailLoader.generateVideotLoader(getBaseContext(), item);
            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        }

        return null;
    }

    RetryView.OnRetryLoadListener retryLoadListener = new RetryView.OnRetryLoadListener() {
        @Override
        public void OnRetryLoad(View vClicked) {
            if(mLoader != null){
                mLoader.forceLoad();
            }
        }
    };

    VideoBlocks<VideoItem> mVidoeInfo;
    @Override
    public void onLoadFinished(Loader<VideoBlocks<VideoItem>> genericItemListLoader, VideoBlocks<VideoItem> blocks) {
        if(blocks == null){
            if(mVidoeInfo == null){
                //no data from server, we need fetch the data again
                mLoadingView.stopLoading(false, false);
            }
            return;
        }

        mLoadingView.stopLoading(true, false);
        mVidoeInfo = blocks;

        setTitle(mVidoeInfo.blocks.get(0).title);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Fragment fg = getSupportFragmentManager().findFragmentById(R.id.detail_view);
                if(fg == null) {
                    DetailFragment df = new DetailFragment();
                    Bundle data = new Bundle();
                    data.putSerializable("item", mVidoeInfo.blocks.get(0));
                    df.setArguments(data);
                    getSupportFragmentManager().beginTransaction().add(R.id.detail_view, df, "details").commit();
                }else {
                    DetailFragment df = (DetailFragment)fg;
                    df.updateVideo(mVidoeInfo.blocks.get(0));
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<VideoBlocks<VideoItem>> blocks) {
        Log.d("MediaDetailActivity", "onLoaderReset result:" + blocks);
    }

}
