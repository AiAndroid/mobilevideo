package com.video.ui.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import com.tv.ui.metro.model.*;
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

}
