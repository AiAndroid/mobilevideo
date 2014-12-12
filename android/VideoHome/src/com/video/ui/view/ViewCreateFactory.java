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
                view = new AdsBlockView(context, item.items, tag);
                break;
            case LayoutConstant.linearlayout_top:
                view = new QuickNavigationBlockView(context, item.items, tag);
                break;
            case LayoutConstant.linearlayout_left:
                view = new QuickLocalNavigateBlockView(context, item.items, tag);
                break;
            case LayoutConstant.list_category_land:
                view = new CategoryBlockView(context, item, tag);
                break;
            case LayoutConstant.list_rich_header:
                view = new RankBlockView(context, item, tag);
                break;
            case LayoutConstant.block_channel:
                view = new PortBlockView(context, item, tag);
                break;
            case LayoutConstant.tabs_horizontal:
                view = new ChannelTabsBlockView(context, item, tag);
                break;
            case LayoutConstant.block_sub_channel:
                view = new PortBlockView(context, item, tag);
                view.setBackgroundResource(R.drawable.com_block_n);
                break;
            case LayoutConstant.grid_block_selection:
                view = new GridSelectBlockView(context, item, tag);
                break;
            case LayoutConstant.linearlayout_land:
                view = new PosterEnterBlockView(context, item.items, tag);
                break;
            case LayoutConstant.linearlayout_poster:
                view = new BlockLinearButtonView(context, item.items, tag);
                break;
            case LayoutConstant.linearlayout_filter:
                view = new FilterBlockView(context, item.filters.filters(), FilterBlockView.Filter_Type);
                break;
            case LayoutConstant.linearlayout_episode:
                view = new FilterBlockView(context, item.filters.filters(), FilterBlockView.Episode_Type);
                break;
            case LayoutConstant.grid_media_land:
            case LayoutConstant.grid_media_port:
                view = new GridMediaBlockView(context, item, tag);
                //for single grid block, we help to set one background
                view.setBackgroundResource(R.drawable.com_block_n);
                break;
            case LayoutConstant.grid_media_port_title:
            case LayoutConstant.grid_media_land_title:
                PortBlockView sbv = new PortBlockView(context, tag);
                //add title
                Block<DisplayItem> itemBlock = new Block<DisplayItem>();
                itemBlock.ui_type = new DisplayItem.UI();
                itemBlock.ui_type.id = LayoutConstant.linearlayout_title;
                itemBlock.title = item.title;
                sbv.addChildView(itemBlock);

                //add content
                sbv.addChildView(item.blocks);

                sbv.getDimens().height += context.getResources().getDimensionPixelSize(R.dimen.media_item_padding);
                view = sbv;
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
