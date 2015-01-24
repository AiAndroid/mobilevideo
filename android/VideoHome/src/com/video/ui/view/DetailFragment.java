package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.view.detail.DetailView;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class DetailFragment extends LoadingFragment {

    private static final String TAG = DetailFragment.class.getName();
    private DetailView detailView;
    private VideoItem mItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView =" + this);
        detailView = new DetailView(getActivity());
        return detailView;
    }


    public void setData(VideoItem videoItem) {
        mItem = videoItem;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        mItem = (VideoItem) getArguments().getSerializable("item");
        detailView.setData(mItem);
    }
}
