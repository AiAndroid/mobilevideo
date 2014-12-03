package com.video.ui.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.utils.ViewUtils;
import com.video.ui.view.subview.AdsAnimationListener;
import com.video.ui.view.subview.DimensHelper;

import java.util.ArrayList;

public class MetroFragment extends Fragment implements AdsAnimationListener {
    private final String TAG = "MetroFragment";
	public MetroLayout mMetroLayout;
    protected SmoothHorizontalScrollView mHorizontalScrollView;
    protected Block<DisplayItem> tab;
    protected int                index;
    protected boolean isUserTab = false;

    private AdsAnimationListener mAdsAL;
    public void registerAnimationListener(AdsAnimationListener al){
        mAdsAL = al;
    }

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
        index     = getArguments().getInt("index", 0);
        //a litter delay to construct UI
        if(ViewUtils.LargerMemoryMode() == true) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initViews();
                }
            }, 500 * (index > 0 ? 1 : 0));
        }else {
            initViews();
        }

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
                if(item.ui_type.id == LayoutConstant.grid_item_selection){
                    int row_count = tab.ui_type.row_count;
                    if(row_count == 0){
                        row_count = 2;
                    }
                    addViewPort(view, item.ui_type.id, step%row_count, step/row_count);
                    step++;
                }else {
                    addViewPort(view, MetroLayout.HorizontalMatchWith, 0, step++);
                }
            }
        }else if(tab != null && tab.blocks != null){
            int step = 0;
            for(Block<DisplayItem> item:tab.blocks){
                View blockView = inflateBlock(item, new Integer(index));
                if(blockView instanceof AdsAnimationListener){
                    registerAnimationListener((AdsAnimationListener)blockView);
                }

                if(item.ui_type.id == LayoutConstant.imageswitcher      ||
                   item.ui_type.id == LayoutConstant.linearlayout_top   ||
                   item.ui_type.id == LayoutConstant.linearlayout_left  ||
                   item.ui_type.id == LayoutConstant.list_category_land ||
                   item.ui_type.id == LayoutConstant.list_rich_header   ||
                   item.ui_type.id == LayoutConstant.block_channel      ||
                   item.ui_type.id == LayoutConstant.block_sub_channel  ||
                   item.ui_type.id == LayoutConstant.linearlayout_land  ||
                   item.ui_type.id == LayoutConstant.linearlayout_poster||
                   item.ui_type.id == LayoutConstant.grid_block_selection ||
                   item.ui_type.id == LayoutConstant.grid_media_land ||
                   item.ui_type.id == LayoutConstant.grid_media_port ||
                   item.ui_type.id == LayoutConstant.grid_media_land_title ||
                   item.ui_type.id == LayoutConstant.grid_media_port_title ||
                   item.ui_type.id == LayoutConstant.tabs_horizontal ||
                   item.ui_type.id == LayoutConstant.linearlayout_filter)
                    addViewPort(blockView, item.ui_type.id, 0, step++);
                else
                    addViewPort(blockView, MetroLayout.HorizontalMatchWith, 0, step++);
            }
        }
    }

    protected View inflateBlock(Block<DisplayItem> item, Object tag){
        View view = ViewCreateFactory.CreateBlockView(getActivity(), item, tag);
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

    @Override
    public void startAnimation() {
        if(mAdsAL != null)
            mAdsAL.startAnimation();
    }

    @Override
    public void stopAnimation() {
        if(mAdsAL != null)
            mAdsAL.stopAnimation();
    }

    @Override
    public AdsAnimationListener getAnimationListener() {
        return mAdsAL;
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume="+this);
        Picasso.with(getActivity().getApplicationContext()).resumeTag(index);
    }

    private void invalidateUI(View view){
        if (null == view) {
            return;
        }

        if(view instanceof DimensHelper){
            DimensHelper imageView = (DimensHelper)view;
            imageView.invalidateUI();
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                invalidateUI(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause="+this);
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();

        Log.d(TAG, "onDestroy="+this);
        Picasso.with(getActivity().getApplicationContext()).pauseTag(index);
        ViewUtils.unbindImageDrawables(getView());
    }
}
