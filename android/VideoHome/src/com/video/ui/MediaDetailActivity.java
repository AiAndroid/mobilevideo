package com.video.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoBlocks;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.GenericDetailLoader;
import com.video.ui.view.DetailFragment;
import com.video.ui.view.RetryView;
import com.video.ui.view.subview.FilterBlockView;
import com.xiaomi.mipush.sdk.MiPushClient;

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

        final View view = findViewById(R.id.detail_play);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mVidoeInfo == null || playview.getText().equals(getString(R.string.play_source_fetching))){
                        Log.d(TAG, "wait to fetch url information");
                        return;
                    }

                    playview.setText(getString(R.string.play_source_fetching));
                    DisplayItem.Media.Episode episode = mVidoeInfo.blocks.get(0).media.items.get(0);

                    EpisodePlayHelper.playEpisode(getBaseContext(), (TextView) view, currentCP, episode, mVidoeInfo.blocks.get(0).media, mVidoeInfo.blocks.get(0));

                } catch (Exception ne) {
                    ne.printStackTrace();
                }
            }
        });

        final boolean exist = iDataORM.getInstance(getBaseContext()).existFavor(getBaseContext(), item.ns, iDataORM.FavorAction, item.id);
        final View favorView = findViewById(R.id.detail_favorite);
        favorView.setSelected(exist);

        favorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVidoeInfo != null) {
                    VideoItem vi = mVidoeInfo.blocks.get(0);
                    boolean isFavored = iDataORM.getInstance(getBaseContext()).existFavor(getBaseContext(), vi.ns, iDataORM.FavorAction, vi.id);
                    if(isFavored ) {
                        iDataORM.getInstance(getBaseContext()).removeFavor(getBaseContext(), vi.ns, iDataORM.FavorAction, vi.id);
                        MiPushClient.unsubscribe(getBaseContext(), vi.id, null);
                    }
                    else {
                        iDataORM.getInstance(getBaseContext()).addFavor(getBaseContext(), vi.ns, iDataORM.FavorAction, vi.id, vi);
                        MiPushClient.subscribe(getBaseContext(), vi.id, null);
                    }
                    favorView.setSelected(!isFavored);
                }
            }
        });

        mLoadingView = makeEmptyLoadingView(getBaseContext(), (RelativeLayout) findViewById(R.id.tabs_content));
        mLoadingView.setOnRetryListener(retryLoadListener);

        loaderID = GenericDetailLoader.VIDEO_LOADER_ID;
        getSupportLoaderManager().initLoader(GenericDetailLoader.VIDEO_LOADER_ID, savedInstanceState, this);
    }

    private DisplayItem.Media.CP currentCP;
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

        currentCP = mVidoeInfo.blocks.get(0).media.cps.get(0);

        setTitle(mVidoeInfo.blocks.get(0).title);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Fragment fg = getSupportFragmentManager().findFragmentById(R.id.detail_view);
                if(fg == null) {
                    DetailFragment df = new DetailFragment();
                    df.setEpisodeClick(episodeClick);
                    Bundle data = new Bundle();
                    data.putSerializable("item", mVidoeInfo.blocks.get(0));
                    data.putSerializable("cp",   currentCP);
                    df.setArguments(data);
                    getSupportFragmentManager().beginTransaction().add(R.id.detail_view, df, "details").commit();
                }else {
                    DetailFragment df = (DetailFragment)fg;
                    df.updateVideo(mVidoeInfo.blocks.get(0));
                }
            }
        });
    }

    View.OnClickListener episodeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DisplayItem.Media.Episode ps = (DisplayItem.Media.Episode) view.getTag();
            if(view instanceof FilterBlockView.VarietyEpisode){
                view = view.findViewById(R.id.detail_variety_item_name);
            }
            EpisodePlayHelper.playEpisode(getBaseContext(), (TextView) view, currentCP, ps, mVidoeInfo.blocks.get(0).media, mVidoeInfo.blocks.get(0));
            Log.d(TAG, "click episode:" + view.getTag());
        }
    };

    @Override
    public void onLoaderReset(Loader<VideoBlocks<VideoItem>> blocks) {
        Log.d("MediaDetailActivity", "onLoaderReset result:" + blocks);
    }

}
