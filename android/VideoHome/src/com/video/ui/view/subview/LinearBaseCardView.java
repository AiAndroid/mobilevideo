package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by liuhuadong on 11/21/14.
 */
public class LinearBaseCardView extends LinearLayout {
    public LinearBaseCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void launcherAction(Context context, DisplayItem item){
        Toast.makeText(context, "prepare to launch=" + item.title + "/" + item.id + "/" + item.type + "/" + item.ns + item.ui_type, Toast.LENGTH_SHORT).show();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
