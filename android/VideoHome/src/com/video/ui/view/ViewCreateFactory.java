package com.video.ui.view;

import android.content.Context;
import android.view.View;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.view.subview.*;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class ViewCreateFactory {
    public static View CreateBlockView(Context context,  Block<DisplayItem> item){
        View view = null;
        switch (item.ui_type.id){
            case LayoutConstant.imageswitcher:
                view = new AdsView(context, item.items);
                break;
            case LayoutConstant.linearlayout_top:
                view = new QuickNavigationView(context, item.items);
                break;
            case LayoutConstant.linearlayout_left:
                view = new QuickLocalNavigateView(context, item.items);
                break;
            case LayoutConstant.list_category_land:
                view = new CategoryItemView(context, item);
                break;
            case LayoutConstant.list_rich_header:
                view = new RankItemView(context, item.items, item.title, item.sub_title, item.ui_type.row_count);
                break;
            case LayoutConstant.block_channel:
                view = new PortBlock(context, item);
                break;
            case LayoutConstant.grid_block_selection:
                view = new GridSelectView(context, item);
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
