package com.video.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;

/**
 * Created by liuhuadonbg on 1/26/15.
 */
public class BlockContainerView  extends MetroLayout {
    public BlockContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setVideo(VideoItem tab){
        int step = 0;
        for(Block<DisplayItem> item:tab.blocks){
            View blockView = inflateBlock(item, new Integer(0));

            if(item.ui_type.id == LayoutConstant.imageswitcher      ||
                    item.ui_type.id == LayoutConstant.linearlayout_top   ||
                    item.ui_type.id == LayoutConstant.linearlayout_left  ||
                    item.ui_type.id == LayoutConstant.list_category_land ||
                    item.ui_type.id == LayoutConstant.list_rich_header   ||
                    item.ui_type.id == LayoutConstant.block_channel      ||
                    item.ui_type.id == LayoutConstant.block_sub_channel  ||
                    item.ui_type.id == LayoutConstant.linearlayout_land  ||
                    item.ui_type.id == LayoutConstant.linearlayout_poster||
                    item.ui_type.id == LayoutConstant.grid_block_selection ||
                    item.ui_type.id == LayoutConstant.grid_media_land ||
                    item.ui_type.id == LayoutConstant.grid_media_port ||
                    item.ui_type.id == LayoutConstant.grid_media_land_title ||
                    item.ui_type.id == LayoutConstant.grid_media_port_title ||
                    item.ui_type.id == LayoutConstant.tabs_horizontal ||
                    item.ui_type.id == LayoutConstant.linearlayout_filter)
                addItemViewPort(blockView, item.ui_type.id, 0, step++);
            else
                addItemViewPort(blockView, MetroLayout.HorizontalMatchWith, 0, step++);
        }
    }


    protected View inflateBlock(Block<DisplayItem> item, Object tag){
        View view = ViewCreateFactory.CreateBlockView(getContext(), item, tag);
        if(view != null)
            return view;

        TextView notview =  new TextView(getContext());
        notview.setText("not support: "+item);
        return notview;
    }
}
