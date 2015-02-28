package com.video.ui.view.block;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

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

    protected static void setHintText(View meida, DisplayItem item){
        if(item.hint != null && TextUtils.isEmpty(item.hint.left()) == false) {
            TextView leftView = (TextView) meida.findViewById(R.id.left_textview);
            leftView.setText(item.hint.left());
        }

        if(item.hint != null && TextUtils.isEmpty(item.hint.mid()) == false) {
            TextView midView = (TextView) meida.findViewById(R.id.mid_textview);
            midView.setText(item.hint.mid());
        }

        if(item.hint != null && TextUtils.isEmpty(item.hint.right()) == false) {
            TextView rightView = (TextView) meida.findViewById(R.id.right_textview);
            rightView.setText(item.hint.right());
        }
    }
}
