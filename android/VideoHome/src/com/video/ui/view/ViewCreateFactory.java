package com.video.ui.view;

import android.content.Context;
import android.view.View;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.subview.*;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class ViewCreateFactory {
    public static View CreateBlockView(Context context,  Block<DisplayItem> item, Object tag){
        View view = null;
        switch (item.ui_type.id){
            case LayoutConstant.imageswitcher:
                view = new AdsView(context, item.items, tag);
                break;
            case LayoutConstant.linearlayout_top:
                view = new QuickNavigationView(context, item.items, tag);
                break;
            case LayoutConstant.linearlayout_left:
                view = new QuickLocalNavigateView(context, item.items, tag);
                break;
            case LayoutConstant.list_category_land:
                view = new CategoryItemView(context, item, tag);
                break;
            case LayoutConstant.list_rich_header:
                view = new RankItemView(context, item, tag);
                break;
            case LayoutConstant.block_channel:
                view = new PortBlock(context, item, tag);
                break;
            case LayoutConstant.block_sub_channel:
                view = new SubPortBlock(context, item, tag);
                break;
            case LayoutConstant.grid_block_selection:
                view = new GridSelectView(context, item, tag);
                break;
            case LayoutConstant.linearlayout_land:
                view = new PosterEnterView(context, item.items, tag);
                break;
            case LayoutConstant.linearlayout_poster:
                view = new BlockLinearButtonView(context, item.items, tag);
                break;
            case LayoutConstant.grid_media_land:
            case LayoutConstant.grid_media_port:
                view = new GridMediaView(context, item, tag);
                //for single grid block, we help to set one background
                view.setBackgroundResource(R.drawable.com_block_n);
                break;
        }
        return view;
    }

    public static View CreateSingleView(Context context, DisplayItem item){
        View view = null;
        switch (item.ui_type.id){
            case LayoutConstant.grid_item_selection:
                view = new FeatureItemView(context, item);
                break;
        }
        return view;
    }
}
