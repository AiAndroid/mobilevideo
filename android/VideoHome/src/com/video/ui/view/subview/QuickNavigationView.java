package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.MetroLayout;
import com.video.ui.view.RecommendCardView;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class QuickNavigationView extends RelativeLayout {
    public QuickNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public QuickNavigationView(Context context, ArrayList<DisplayItem> items) {
        super(context);

        View v = View.inflate(getContext(), R.layout.quick_navigation, this);
        MetroLayout mMetroLayout = (MetroLayout)v.findViewById(R.id.metrolayout);

        mMetroLayout.addItemView(new RecommendCardView(getContext()).bindData(items.get(0)), MetroLayout.Normal, 0);
        mMetroLayout.addItemView(new RecommendCardView(getContext()).bindData(items.get(0)), MetroLayout.Normal, 0);
        mMetroLayout.addItemView(new RecommendCardView(getContext()).bindData(items.get(0)), MetroLayout.Normal, 0);
        mMetroLayout.addItemView(new RecommendCardView(getContext()).bindData(items.get(0)), MetroLayout.Normal, 0);
        mMetroLayout.addItemView(new RecommendCardView(getContext()).bindData(items.get(0)), MetroLayout.Normal, 0);
    }
}
