package com.video.ui.view.block;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.tv.ui.metro.model.LayoutConstant;
import com.video.ui.view.MetroLayout;

/**
 * Created by liuhuadong on 11/26/14.
 */
public class GridSelectBlockView extends BaseCardView implements DimensHelper {
    public GridSelectBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private MetroLayout        ml;
    private Block<DisplayItem> content;
    public GridSelectBlockView(Context context, Block<DisplayItem> block, Object tag) {
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        content = block;

        RelativeLayout Root = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, this);

        ml = new MetroLayout(context);
        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL);
        int step = 0;
        int row_count = block.ui_type.row_count;
        if(row_count == 0){
            row_count = 2;
        }

        //why -1, because we don't want padding at begin and at end.
        int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.feature_media_view_width))/(row_count-1);
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
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.rank_banner_width);
            mDimens.height = 0;
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {
        for(int i=0;i<ml.getChildCount();i++){
            View view = ml.getChildAt(i);
            ImageView mPoster  = (ImageView) view.findViewById(R.id.feature_poster_bg);
            if(mPoster != null)
                Picasso.with(getContext()).load(content.items.get(i).images.get("poster").url).tag(getTag(R.integer.picasso_tag)).fit().transform(new CategoryBlockView.Round_Corners(getContext(), 4, 4, true)).into(mPoster);

        }
    }

    @Override
    public void unbindDrawables(View view) {

    }
}
