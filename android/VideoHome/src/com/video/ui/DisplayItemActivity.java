package com.video.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.loader.BaseGsonLoader;
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

        item = (DisplayItem) this.getIntent().getSerializableExtra("item");


    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        if(findViewById(R.id.title_top_back) != null) {
            findViewById(R.id.title_top_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        if(findViewById(R.id.title_top_name) != null) {
            findViewById(R.id.title_top_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    protected void setTitle(String title){
        if(findViewById(R.id.title_top_name) != null) {
            ((TextView)findViewById(R.id.title_top_name)).setText(title);
        }
    }

    protected void showFilter(boolean show){
        if(findViewById(R.id.channel_filte_btn) != null){
            findViewById(R.id.channel_filte_btn).setVisibility(show?View.VISIBLE:View.GONE);
        }
    }

    protected void showSearch(boolean show){
        if(findViewById(R.id.channel_search_btn) != null){
            findViewById(R.id.channel_search_btn).setVisibility(show?View.VISIBLE:View.GONE);
        }
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
