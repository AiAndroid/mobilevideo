package com.video.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.VideoBlocks;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.PlayUrlLoader;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.GenericDetailLoader;
import com.video.ui.view.DetailFragment;
import com.video.ui.view.RetryView;
import com.video.ui.view.detail.OfflineSelectEpisodeView;
import com.video.ui.view.detail.SelectSourcePopup;
import com.video.ui.view.subview.SelectItemsBlockView;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class MediaDetailActivity extends DisplayItemActivity implements LoaderCallbacks<VideoBlocks<VideoItem>> {

    private static String TAG = MediaDetailActivity.class.getName();
    private int loaderID;
    private TextView          playview ;
    private TextView          detail_download;
    private View              favorView;
    private View              titlebar;
    private ImageView         mSourceSelectLogo;
    private OfflineSelectEpisodeView mOfflineSelectView;
    private SelectSourcePopup    mSelectSourcePopup;
    private DisplayItem.Media.CP preferenceSource;

    private String default_cp_icon = "http://file.market.xiaomi.com/download/Duokan/dfddd21f-3be0-4def-8b5d-d293328ed800/symbol_default_normal.png";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_detail_layout);

        titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);
        tv.setText(item.title);

        showFilter(false);
        showSearch(true);

        mSourceSelectLogo = (ImageView) findViewById(R.id.channel_search_btn);
        mSourceSelectLogo.setBackground(null);
        Picasso.with(getBaseContext()).load(default_cp_icon).into(mSourceSelectLogo);
        mSourceSelectLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectSourceDialog();
            }
        });

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

                    DisplayItem.Media.Episode episode = mVidoeInfo.blocks.get(0).media.items.get(0);

                    EpisodePlayAdapter.playEpisode(getBaseContext(), (TextView) view, preferenceSource, episode, mVidoeInfo.blocks.get(0).media, mVidoeInfo.blocks.get(0));

                } catch (Exception ne) {
                    ne.printStackTrace();
                }
            }
        });

        final boolean exist = iDataORM.getInstance(getBaseContext()).existFavor(getBaseContext(), item.ns, iDataORM.FavorAction, item.id);
        final View favorView = findViewById(R.id.detail_favorite);
        favorView.setSelected(exist);

        favorView.setOnClickListener(bottomClick);
        detail_download = (TextView) findViewById(R.id.detail_download);
        detail_download.setOnClickListener(bottomClick);
        detail_download.setEnabled(false);

        mLoadingView = makeEmptyLoadingView(getBaseContext(), (RelativeLayout) findViewById(R.id.root_container));
        mLoadingView.setOnRetryListener(retryLoadListener);

        loaderID = GenericDetailLoader.VIDEO_LOADER_ID;
        getSupportLoaderManager().initLoader(GenericDetailLoader.VIDEO_LOADER_ID, savedInstanceState, this);
    }

    String downapk = "http://neirong.funshion.com/android/1660/FunshionAphone_SID_1660_zipalign.apk ";
    View.OnClickListener bottomClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mVidoeInfo != null) {
                final VideoItem vi = mVidoeInfo.blocks.get(0);

                switch (view.getId()) {
                    case R.id.detail_download: {
                        //current select cp
                        DisplayItem.Media.CP downCP = findDownloadableCP(vi.media.cps);
                        if(downCP != null) {
                            if (vi.media.items.size() == 1) {
                                //current episode
                                DisplayItem.Media.Episode episode = vi.media.items.get(0);
                                EpisodePlayAdapter.fetchOfflineEpisodeSource(getBaseContext(), (TextView) view, vi, downCP, episode, playSouceFetchListener);
                            } else {
                                Intent intent = new Intent(getBaseContext(), OfflineSelectEpisodeView.class);
                                intent.putExtra("item", vi);
                                intent.putExtra("cp",   downCP);
                                mOfflineSelectView = new OfflineSelectEpisodeView(MediaDetailActivity.this, intent);
                                mOfflineSelectView.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                            }
                        }
                        break;
                    }
                    case R.id.detail_favorite: {
                        boolean isFavored = iDataORM.getInstance(getBaseContext()).existFavor(getBaseContext(), vi.ns, iDataORM.FavorAction, vi.id);
                        if (isFavored) {
                            iDataORM.getInstance(getBaseContext()).removeFavor(getBaseContext(), vi.ns, iDataORM.FavorAction, vi.id);
                            MiPushClient.unsubscribe(getBaseContext(), vi.id, null);
                        } else {
                            iDataORM.getInstance(getBaseContext()).addFavor(getBaseContext(), vi.ns, iDataORM.FavorAction, vi.id, vi);
                            MiPushClient.subscribe(getBaseContext(), vi.id, null);
                        }
                        view.setSelected(!isFavored);
                    }
                    break;
                }
            }
        }
    };

    EpisodePlayAdapter.EpisodeSourceListener playSouceFetchListener = new EpisodePlayAdapter.EpisodeSourceListener() {
        @Override
        public void playSource(boolean result, final PlaySource ps, final  VideoItem item, final DisplayItem.Media.Episode episode) {
            if(result == false)
                return;

            PlayUrlLoader mUrlLoader = new PlayUrlLoader(getBaseContext(), ps.h5_url, ps.cp);
            mUrlLoader.get(30000, item, episode, h5LoadListener);
        }
    };

    PlayUrlLoader.H5OnloadListener h5LoadListener = new PlayUrlLoader.H5OnloadListener() {
        @Override
        public void playUrlFetched(boolean result, String playurl, WebView webView, VideoItem item, DisplayItem.Media.Episode episode) {
            webView.destroy();

            Log.d("download", "qiyi url:"+playurl);
            if(TextUtils.isEmpty(playurl) == true)
                return;

            long download_id = MVDownloadManager.getInstance(getBaseContext()).requestDownload(getBaseContext(), item, episode, playurl);
            if(download_id == MVDownloadManager.DOWNLOAD_IN) {
                Toast.makeText(getBaseContext(), "已经添加到队列，下载中", Toast.LENGTH_LONG).show();
            }
            else if(download_id != -1) {
                iDataORM.getInstance(getBaseContext()).addDownload(getBaseContext(), item.id, download_id, downapk, item, episode);
                MiPushClient.subscribe(getBaseContext(), item.id, null);

                Toast.makeText(getBaseContext(), "已经添加到队列，download id:"+download_id, Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getBaseContext(), "add download fail", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK){

            if(mOfflineSelectView != null && mOfflineSelectView.isShowing()){
                mOfflineSelectView.dismiss();
                return true;
            }else if(mSelectSourcePopup != null && mSelectSourcePopup.isShowing()){
                mSelectSourcePopup.dismiss();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mSelectSourcePopup != null && mSelectSourcePopup.isShowing()){
            mSelectSourcePopup.triggerDismissImmediately();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void showSelectSourceDialog(){
        if(mVidoeInfo == null ||
                mVidoeInfo.blocks.get(0).media.cps == null ||
                mVidoeInfo.blocks.get(0).media.cps.size() == 0){
            return;
        }
        if(mSelectSourcePopup == null){
            mSelectSourcePopup = new SelectSourcePopup(this, mVidoeInfo.blocks.get(0).media.cps);
            Log.d(TAG, "preferenceSource:" + preferenceSource);
            mSelectSourcePopup.setCurrentSource(preferenceSource);
            mSelectSourcePopup.setOnSourceSelectListener(new SelectSourcePopup.OnSourceSelectListener() {
                @Override
                public void onSourceSelect(int position, DisplayItem.Media.CP source) {
                    preferenceSource = source;
                    iDataORM.addSetting(getBaseContext(), iDataORM.KEY_PREFERENCE_SOURCE, preferenceSource.cp);
                    refreshSelectedSource(preferenceSource);
                    mSelectSourcePopup.dismiss();
                }
            });
        }
        mSelectSourcePopup.show((ViewGroup) findViewById(R.id.root_container), titlebar);
        titlebar.bringToFront();
    }

    private void refreshSelectedSource(DisplayItem.Media.CP preferenceSource) {
        this.preferenceSource = preferenceSource;
        mSourceSelectLogo.setImageBitmap(null);
        Picasso.with(getBaseContext()).load(preferenceSource.icon).fit().into(mSourceSelectLogo);
    }

    private DisplayItem.Media.CP findDownloadableCP( ArrayList<DisplayItem.Media.CP> cps){
        for(DisplayItem.Media.CP item:cps){
            if(item.name.equals("sohu") || item.name.equals("fengxing") || item.vitem_offline == true ){
                return item;
            }
        }

        return null;
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

        preferenceSource = mVidoeInfo.blocks.get(0).media.cps.get(0);
        Picasso.with(getBaseContext()).load(preferenceSource.icon).into(mSourceSelectLogo);

        DisplayItem.Media.CP downloadableCP = findDownloadableCP(mVidoeInfo.blocks.get(0).media.cps);
        if(downloadableCP != null){
            detail_download.setEnabled(true);
        }

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
                    data.putSerializable("cp",   preferenceSource);
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
            if(view instanceof SelectItemsBlockView.VarietyEpisode){
                view = view.findViewById(R.id.detail_variety_item_name);
            }
            EpisodePlayAdapter.playEpisode(getBaseContext(), (TextView) view, preferenceSource, ps, mVidoeInfo.blocks.get(0).media, mVidoeInfo.blocks.get(0));
            Log.d(TAG, "click episode:" + view.getTag());
        }
    };

    @Override
    public void onLoaderReset(Loader<VideoBlocks<VideoItem>> blocks) {
        Log.d("MediaDetailActivity", "onLoaderReset result:" + blocks);
    }

}
