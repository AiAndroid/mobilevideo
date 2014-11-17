package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

import java.util.ArrayList;

public class MetroFragment extends Fragment {
    private final String TAG = "MetroFragment";
	public MetroLayout mMetroLayout;
    protected SmoothHorizontalScrollView mHorizontalScrollView;
    protected Block<DisplayItem> tab;
    protected boolean isUserTab = false;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView ="+this);
        View v = inflater.inflate(R.layout.metrofragment, container, false);
        mMetroLayout = (MetroLayout)v.findViewById(R.id.metrolayout);
        mMetroLayout.setMetroCursorView((MetroCursorView)v.findViewById(R.id.metrocursor));
        mHorizontalScrollView = (SmoothHorizontalScrollView)v.findViewById(R.id.horizontalScrollView);

        tab = (Block<DisplayItem>) this.getArguments().getSerializable("tab");
        isUserTab = getArguments().getBoolean("user_fragment", false);
        initViews();
        return v;
    }

    public View addViewPort(View view, int celltype, int x, int y){
        return mMetroLayout.addItemViewPort(view, celltype, x, y);
    }

    public ArrayList<View> createUserView(){
        return UserViewFactory.getInstance().createUserView(getActivity());
    }

    public void initViews(){

        if(isUserTab == true){
            ArrayList<View> views = createUserView();
            int Y = 0;
            for(View item: views){
                addViewPort(item, MetroLayout.HorizontalMatchWith, 0, Y);
                Y++;
            }
        }
        else if(tab != null && tab.items != null){
            int step = 0;
            for(DisplayItem item:tab.items){
               View view = inflateDisplayItem(item);
               addViewPort(view, MetroLayout.HorizontalMatchWith, 0, step++);
            }
        }else if(tab != null && tab.blocks != null){
            int step = 0;
            for(Block<DisplayItem> item:tab.blocks){
                View blockView = inflateBlock(item);
                addViewPort(blockView, MetroLayout.HorizontalMatchWith, 0, step++);
            }
        }
    }

    protected View inflateBlock(Block<DisplayItem> item){
        View view = ViewCreateFactory.CreateBlockView(getActivity(), item);
        if(view != null)
            return view;

        return  new RecommendCardView(getActivity()).bindData(item);
    }

    protected View inflateDisplayItem(DisplayItem item){
        View view = ViewCreateFactory.CreateSingleView(getActivity(), item);
        if(view != null)
            return view;

        return  new RecommendCardView(getActivity()).bindData(item);
    }
}