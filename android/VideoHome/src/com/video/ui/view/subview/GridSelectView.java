package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.MetroLayout;

/**
 * Created by liuhuadong on 11/26/14.
 */
public class GridSelectView extends BaseCardView implements DimensHelper {
    public GridSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public GridSelectView(Context context, Block<DisplayItem> block) {
        super(context, null, 0);

        RelativeLayout Root = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, this);

        MetroLayout ml = new MetroLayout(context);
        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL);
        int step = 0;
        int row_count = block.ui_type.row_count;
        if(row_count == 0){
            row_count = 2;
        }

        int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.feature_media_view_width))/(row_count+1);
        int itemHeight = 0;
        for(DisplayItem item: block.items) {
            View child = new FeatureItemView(context, item);
            if(item.ui_type.id == LayoutConstant.grid_item_selection){
                ml.addItemViewPort(child, item.ui_type.id, step % row_count, step/row_count, padding);
                step++;
            }

            if(itemHeight == 0){
                itemHeight =  ((DimensHelper)child).getDimens().height;
            }
        }

        getDimens().height += (itemHeight)* ((step+1)/row_count) + padding*((step+1)/row_count - 1);
        Root.addView(ml, lp);
    }

    private DimensHelper.Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new DimensHelper.Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = 0;
        }
        return mDimens;
    }
}
