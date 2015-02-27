package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

/**
 * Created by wangwei on 1/14/15.
 */
public class RecyclerFragment extends Fragment {
    private final String TAG = "RecyclerFragment";
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    protected Block<DisplayItem> tab;
    protected int                index;
    protected boolean isUserTab = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView =" + this);
        View v = inflater.inflate(R.layout.recyclerfragment, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        //mRecyclerView = new RecyclerView(getActivity());
        tab = (Block<DisplayItem>) this.getArguments().getSerializable("tab");
        isUserTab = getArguments().getBoolean("user_fragment", false);
        index     = getArguments().getInt("index", 0);
        //a litter delay to construct UI
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new BlockAdapter(getActivity(),tab));
        mRecyclerView.getItemAnimator().setSupportsChangeAnimations(true);
        //onRecyclerViewInit(mRecyclerView);
        return v;
    }
}
