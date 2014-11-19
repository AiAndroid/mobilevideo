package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by liuhuadong on 11/19/14.
 */
public class SelectItemView extends BaseCardView  implements DimensHelper {
    public SelectItemView(Context context, DisplayItem item) {
        this(context, null, 0);
    }

    public SelectItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public Dimens getDimens() {
        return null;
    }
}
