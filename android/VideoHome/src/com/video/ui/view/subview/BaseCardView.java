package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by liuhuadong on 11/19/14.
 */
public abstract class BaseCardView  extends RelativeLayout {
    public BaseCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected DisplayItem item;
    public DisplayItem getMediaContent(){
        return item;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
