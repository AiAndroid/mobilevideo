package com.video.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.RelativeLayout;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.model.DisplayItem;
import com.video.ui.view.EmptyLoadingView;

/**
 * Created by tv metro on 9/1/14.
 */
public class DisplayItemActivity extends FragmentActivity {

    protected DisplayItem item;
    protected EmptyLoadingView mLoadingView;
    protected BaseGsonLoader mLoader;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //when user start portrait, lock the screen orientation
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }

        Intent data = getIntent();

        item = (DisplayItem) this.getIntent().getSerializableExtra("item");

//        String itemid = data.getData().getQueryParameter("rid");
//        Toast.makeText(this, data.getData().toString() + " itemid="+itemid + " this="+this, Toast.LENGTH_LONG).show();
    }
    
    public static EmptyLoadingView makeEmptyLoadingView(Context context,  RelativeLayout parentView){
        return makeEmptyLoadingView(context, parentView,  RelativeLayout.CENTER_IN_PARENT);
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView, int rule){
        EmptyLoadingView loadingView = new EmptyLoadingView(context);
        loadingView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(rule);
        parentView.addView(loadingView, rlp);
        return loadingView;
    }
}
