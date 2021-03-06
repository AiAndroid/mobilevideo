package com.video.ui.view.block;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.tv.ui.metro.model.LayoutConstant;

import java.util.ArrayList;

/**
 * Created by wangwei on 11/20/14.
 */
public class PortBlockView<T> extends LinearBaseCardView implements DimensHelper{
    Block<T> content;

    public PortBlockView(Context context) {
        super(context, null, 0);
    }

    public PortBlockView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    private Dimens mDimens;

    public PortBlockView(Context context, Block<T> blocks, Object tag) {
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);

        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.com_block_n);
        initUI(blocks);
    }

    public PortBlockView(Context context, Object tag) {
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);

        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.com_block_n);
    }

    public void addChildView(final ArrayList<Block<T>> blocks){
        for(Block<T> block:blocks){
            addChildView(block);
        }
    }
    public void addChildView(final Block<T> block){
        switch (block.ui_type.id){
            //tabs_horizontal and linearlayout_title show just has one, and they should be the first item
            case LayoutConstant.tabs_horizontal: {
                View blockView = new ChannelTabsBlockView(getContext(), block, getTag(R.integer.picasso_tag));
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                addView(blockView, flp);

                if (blockView instanceof DimensHelper) {
                    getDimens().height += ((DimensHelper) blockView).getDimens().height;
                }
                break;
            }
            case LayoutConstant.linearlayout_title:{
                View root = View.inflate(getContext(), R.layout.sub_channel_title, null);
                TextView title = (TextView) root.findViewById(R.id.channel_title);
                title.setText(block.title);

                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  getResources().getDimensionPixelSize(R.dimen.title_height));
                addView(root, flp);
                getDimens().height += getResources().getDimensionPixelSize(R.dimen.title_height);

                break;
            }
            case LayoutConstant.linearlayout_single_poster:{
                addOnePadding();
                SinglePosterBlockView view = new SinglePosterBlockView(getContext(), block, getTag(R.integer.picasso_tag));

                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  view.getDimens().height);
                addView(view, flp);

                getDimens().height += view.getDimens().height;
                break;
            }

            case LayoutConstant.list_rich_header: {
                addOnePadding();
                RankBlockView view = new RankBlockView(getContext(), block, getTag(R.integer.picasso_tag));
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  view.getDimens().height);
                addView(view, flp);
                getDimens().height += view.getDimens().height;
                break;
            }
            case LayoutConstant.grid_media_land:
            case LayoutConstant.grid_media_port:
            case LayoutConstant.grid_media_land_title:
            case LayoutConstant.grid_media_port_title:{
                //grid already have one top padding, so ignore

                GridMediaBlockView view = new GridMediaBlockView(getContext(), block, getTag(R.integer.picasso_tag));
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  view.getDimens().height);
                addView(view, flp);
                getDimens().height += view.getDimens().height;
                break;
            }
            case LayoutConstant.linearlayout_none: {
                //addOnePadding();
                View buttonContain = View.inflate(getContext(), R.layout.button_enter, null);
                Button blockView = (Button) buttonContain.findViewById(R.id.enter_button);
                blockView.setText(block.title);

                blockView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launcherAction(getContext(), block);
                    }
                });
                addView(buttonContain);
                getDimens().height += getResources().getDimensionPixelSize(R.dimen.rank_button_height);

                //last one
                //addOnePadding();
                break;
            }
            case LayoutConstant.linearlayout_poster:{
                addOnePadding();
                BlockLinearButtonView bv = new BlockLinearButtonView(getContext(), block.items, getTag(R.integer.picasso_tag));
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  bv.getDimens().height);
                addView(bv, flp);

                getDimens().height += bv.getDimens().height;
                //add one more for seperate
                //addOnePadding();
                break;
            }
            case LayoutConstant.linearlayout_land: {
                addOnePadding();
                PosterEnterBlockView bv = new PosterEnterBlockView(getContext(), block.items, getTag(R.integer.picasso_tag));
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  bv.getDimens().height);
                addView(bv, flp);

                getDimens().height += bv.getDimens().height;

                //for last sep, because no padding
                //addOnePadding();
                break;
            }
            case LayoutConstant.linearlayout_filter:{

                SelectItemsBlockView bv = new SelectItemsBlockView(getContext(), block.filters.filters(), block.ui_type.id, (Block<DisplayItem>) block);

                addView(bv);
                getDimens().height += bv.getDimens().height;

                addOnePadding();
                break;
            }
            case LayoutConstant.linearlayout_episode:{
                SelectItemsBlockView bv = SelectItemsBlockView.createEpisodeButtonBlockView(getContext(), block.media.items, 0,  block.media.display_layout==null?8:block.media.display_layout.max_display);
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  bv.getDimens().height);
                addView(bv, flp);

                getDimens().height += bv.getDimens().height;
                break;
            }
            case LayoutConstant.linearlayout_episode_list:{
                SelectItemsBlockView bv = SelectItemsBlockView.createEpisodeListBlockView(getContext(), block.media.items, 0, block.media.display_layout==null?4:block.media.display_layout.max_display);

                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT,  bv.getDimens().height);
                addView(bv, flp);

                getDimens().height += bv.getDimens().height;
                break;
            }
            case LayoutConstant.linearlayout_filter_select:{
                SelectItemsBlockView child = SelectItemsBlockView.createFilterSelectBlockView(getContext(), block.filters.all.get(0), 12);

                addView(child);
                getDimens().height += child.getDimens().height;
                addOnePadding();
                break;
            }
            case LayoutConstant.list_small_icon:
            case LayoutConstant.grid_small_icon: {
                TableSmallIconBlockView child = new TableSmallIconBlockView(getContext(), block);

                addView(child);
                getDimens().height += child.getDimens().height;
                addOnePadding();
                break;
            }
            case LayoutConstant.linearlayout_episode_select:{
                SelectItemsBlockView child = SelectItemsBlockView.createEpisodeSelectBlockView(getContext(), block.media.items, block.media.display_layout==null?12:block.media.display_layout.max_display);

                addView(child);
                getDimens().height += child.getDimens().height;
                addOnePadding();
                break;
            }
            case LayoutConstant.linearlayout_search:{
                SelectItemsBlockView child = SelectItemsBlockView.createSearchBlockView(getContext(), (ArrayList<com.tv.ui.metro.model.DisplayItem>) block.items, 12);

                addView(child);
                getDimens().height += child.getDimens().height;
                addOnePadding();
                break;
            }
        }
    }

    private static int media_item_padding = -1;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = 0;
            media_item_padding = getResources().getDimensionPixelSize(R.dimen.media_item_padding);
        }

        return mDimens;
    }

    private void initUI(Block<T> rootblock) {

        content = rootblock;
        int size = content.blocks.size();
        for (int i=0;i<size;i++){
            addChildView(content.blocks.get(i));
        }

        //more than one padding
        getDimens().height += media_item_padding;
    }

    private void addOnePadding(){
        View view = new View(getContext());
        view.setBackgroundColor(Color.TRANSPARENT);
        LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, media_item_padding);
        addView(view, flp);

        //add padding
        getDimens().height += media_item_padding;
    }

    @Override
    public void invalidateUI() {
        for(int i=0;i<getChildCount();i++){
            View view =  getChildAt(i);
            if(view instanceof DimensHelper){
                ((DimensHelper) view).invalidateUI();
            }
        }
    }

    @Override
    public void unbindDrawables(View view) {

    }
}
