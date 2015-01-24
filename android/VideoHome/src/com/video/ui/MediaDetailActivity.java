package com.video.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tv.ui.metro.model.VideoBlocks;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.loader.GenericDetailLoader;
import com.video.ui.view.DetailFragment;
import com.video.ui.view.RetryView;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class MediaDetailActivity extends DisplayItemActivity implements LoaderManager.LoaderCallbacks<VideoBlocks<VideoItem>> {

    private int loaderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_detail_layout);

        View titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);
        tv.setText(item.title);

        titlebar.findViewById(R.id.channel_filte_btn).setVisibility(View.GONE);
        titlebar.findViewById(R.id.channel_search_btn).setVisibility(View.GONE);

        View view = titlebar.findViewById(R.id.channel_filte_btn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("mvschema://video/filter?rid=" + item.id));
                    intent.putExtra("item", item);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                DetailFragment df = new DetailFragment();
                Bundle data = new Bundle();
                data.putSerializable("item", mVidoeInfo.blocks.get(0));
                df.setArguments(data);
                getSupportFragmentManager().beginTransaction().add(R.id.detail_view, df, "details").commit();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<VideoBlocks<VideoItem>> blocks) {
        Log.d("MediaDetailActivity", "onLoaderReset result:" + blocks);
    }

}
