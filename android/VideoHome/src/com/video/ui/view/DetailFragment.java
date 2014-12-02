package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tv.ui.metro.model.GenericItemList;
import com.tv.ui.metro.model.VideoItem;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<GenericItemList<VideoItem>> {

    private static final String TAG = DetailFragment.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView =" + this);
        TextView textView = new TextView(getActivity());
        textView.setText("正在努力实现");
        return textView;
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
