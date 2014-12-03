package com.video.ui.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.GenericItemList;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.view.detail.DetailView;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<GenericItemList<VideoItem>> {

    private static final String TAG = DetailFragment.class.getName();
    private DetailView detailView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView =" + this);
        detailView = new DetailView(getActivity());

        detailView.showLoadingView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                detailView.showContentView();
                detailView.setData((com.tv.ui.metro.model.DisplayItem) getArguments().getSerializable("item"));
            }
        }, 200);

        return detailView;
    }

    @Override
    public Loader<GenericItemList<VideoItem>> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<GenericItemList<VideoItem>> genericItemListLoader, GenericItemList<VideoItem> videoItemGenericItemList) {

    }

    @Override
    public void onLoaderReset(Loader<GenericItemList<VideoItem>> genericItemListLoader) {

    }
}
