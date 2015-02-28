package com.video.ui.view.block;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by liuhuadong on 11/21/14.
 */
public class LinearBaseCardView extends LinearLayout {
    public LinearBaseCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void launcherAction(Context context, DisplayItem item){
        BaseCardView.launcherAction(context, item);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public java.lang.Object getTag(int key) {
        Object obj = super.getTag(key);
        if(obj == null){
            return new Integer(-1);
        }

        return obj;
    }
}
