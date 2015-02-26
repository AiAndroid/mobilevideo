package com.video.ui.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import com.tv.ui.metro.model.*;
import com.video.ui.R;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 1/24/15.
 */
public class LoadingFragment extends Fragment {
    protected static final String TAG = LoadingFragment.class.getName();
    protected EmptyLoadingView   mLoadingView;
    protected EmptyLoadingView makeEmptyLoadingView(Context context,  RelativeLayout parentView){
        return makeEmptyLoadingView(context, parentView,  RelativeLayout.CENTER_IN_PARENT);
    }

    protected BaseGsonLoader mLoader;
    RetryView.OnRetryLoadListener retryLoadListener = new RetryView.OnRetryLoadListener() {
        @Override
        public void OnRetryLoad(View vClicked) {
            if(mLoader != null){
                mLoader.forceLoad();
            }
        }
    };

    protected EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView, int rule){
        EmptyLoadingView loadingView = new EmptyLoadingView(context);
        loadingView.setOnRetryListener(retryLoadListener);
        loadingView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(rule);
        parentView.addView(loadingView, rlp);
        return loadingView;
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();

        Log.d(TAG, "onDestroy=" + this);
        ViewUtils.unbindImageDrawables(getView());
    }

    protected static Block<DisplayItem> createTitleBlock(Context context, String title){
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = title;
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_title;
        return item;
    }

    protected static Block<DisplayItem> createLineBlock(Context context, String title) {
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = title;
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_none;

        item.target = new DisplayItem.Target();
        item.target.entity = "app_market";

        return item;
    }

    protected static Block<DisplayItem> createAppsBlock(){
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = "";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.app_grid;
        item.ui_type.row_count = 5;

        item.items = new ArrayList<DisplayItem>();
        //sohu
        {
            DisplayItem child = new DisplayItem();
            child.title = "搜狐";
            child.target = new DisplayItem.Target();
            child.target.entity = "app";
            child.target.url    = "http://upgrade.m.tv.sohu.com/channels/hdv/4.6.3/SohuTV_4.6.3_1309_201502022041.apk";
            child.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01AUSPmZ9K9/bIyRiiudsqkTsG.png";
            child.images.put("icon", image);

            item.items.add(child);
        }
        //pptv
        {
            DisplayItem child = new DisplayItem();
            child.title = "PPTV";
            child.target = new DisplayItem.Target();
            child.target.entity = "app";
            child.target.url    = "http://download.pplive.com/PPTV_aPhone_4.0.4_775_20140916.apk";
            child.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01ycTc2CWZ3/eSgjAsS58aTeqF.png";
            child.images.put("icon", image);

            item.items.add(child);
        }
        //iqiyi
        {
            DisplayItem child = new DisplayItem();
            child.title = "爱奇艺";
            child.target = new DisplayItem.Target();
            child.target.entity = "app";
            child.target.url    = "http://mbdapp.iqiyi.com/j/ap/iqiyi_xmshpmk.apk";
            child.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01d5PtLAhv6/yPF76vKE70eA1x.png";
            child.images.put("icon", image);

            item.items.add(child);
        }
        //fengxing
        {
            DisplayItem child = new DisplayItem();
            child.title = "风行";
            child.target = new DisplayItem.Target();
            child.target.entity = "app";
            child.target.url    = "http://m.app.mi.com/download/3581";
            child.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p013utylDQ9D/5tqyB2BfGQ1vEg.png";
            child.images.put("icon", image);

            item.items.add(child);
        }
        //phenix
        {
            DisplayItem child = new DisplayItem();
            child.title = "凤凰";
            child.target = new DisplayItem.Target();
            child.target.entity = "app";
            child.target.url    = "http://m.app.mi.com/download/2889";
            child.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p016LLWvD4DD/83MBZ9LtZmueev.png";
            child.images.put("icon", image);

            item.items.add(child);
        }

        return item;
    }

}
