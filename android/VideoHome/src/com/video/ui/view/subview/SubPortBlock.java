package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.LinearFrame;

/**
 * Created by wangwei on 11/20/14.
 */
public class SubPortBlock extends LinearBaseCardView implements DimensHelper{
    Block<DisplayItem> content;

    public SubPortBlock(Context context) {
        super(context, null, 0);
    }

    public SubPortBlock(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    private Dimens mDimens;

    public SubPortBlock(Context context, Block<DisplayItem> blocks, Object tag) {
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);

        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.com_block_n);
        initUI(blocks);
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

    private void initUI(Block<DisplayItem> rootblock) {

        content = rootblock;
        int size = content.blocks.size();
        for (int i=0;i<size;i++){
            final Block<DisplayItem> block = content.blocks.get(i);
            if(block.ui_type.id == LayoutConstant.linearlayout_title){
                View root = View.inflate(getContext(), R.layout.sub_channel_title, null);
                TextView title = (TextView) root.findViewById(R.id.channel_title);
                title.setText(block.title);

                addView(root);
                getDimens().height += getResources().getDimensionPixelSize(R.dimen.title_height);
            }else if(block.ui_type.id == LayoutConstant.linearlayout_single_poster){
                View root = View.inflate(getContext(), R.layout.subchannel_imageview_container, null);
                ImageView poster = (ImageView) root.findViewById(R.id.image_ads);
                Picasso.with(getContext()).load(block.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).placeholder(R.drawable.default_poster_pic).fit().into(poster);
                addView(root);

                getDimens().height += getResources().getDimensionPixelSize(R.dimen.media_banner_sub_channel_height);
                addOnePadding();
            }else if (block.ui_type.id == LayoutConstant.grid_media_land || block.ui_type.id == LayoutConstant.grid_media_port) {
                int res_id = R.layout.tab_media_hor;
                int row_count = block.ui_type.row_count;
                if (row_count == 0)
                    row_count = 2;
                int padding = (getDimens().width - row_count * getResources().getDimensionPixelSize(R.dimen.channel_media_view_width)) / (row_count + 1);
                int item_padding = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);

                int width = getResources().getDimensionPixelSize(R.dimen.channel_media_view_width);
                int height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_height);

                if(block.ui_type.id == LayoutConstant.grid_media_port) {
                    if(row_count == 0)
                        row_count = 3;

                    res_id = R.layout.tab_media_port;
                    width  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width);
                    height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_height);
                    padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width))/(row_count+1);
                }

                View v = View.inflate(getContext(), R.layout.quick_navigation, null);
                LayoutParams flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                addView(v, flp);
                LinearFrame mMetroLayout = (LinearFrame)v.findViewById(R.id.metrolayout);
                for(int step=0;step<block.items.size();step++) {
                    final DisplayItem item = block.items.get(step);

                    ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(res_id, null);
                    ImageView image = (ImageView)meida.findViewById(R.id.poster);
                    Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).fit().into(image);

                    if(block.ui_type.id == LayoutConstant.grid_media_port) {
                        TextView title = (TextView) meida.findViewById(R.id.media_title);
                        title.setText(item.title);

                        TextView desc = (TextView) meida.findViewById(R.id.descrip);
                        desc.setText(item.sub_title);
                    }else {
                        TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                        descrip.setText(item.title + " " +item.sub_title);
                    }

                    meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launcherAction(getContext(), item);
                        }
                    });

                    FrameLayout.LayoutParams itemflp = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    itemflp.leftMargin = getPaddingLeft() + (width*(step%row_count) ) + padding*(step%row_count + 1);
                    itemflp.topMargin  = getPaddingTop()  + (height*(step/row_count)) + item_padding*(step/row_count + 1);

                    mMetroLayout.addView(meida,itemflp);
                }

                int lines = (block.items.size()+1)/row_count;
                getDimens().height += (height + item_padding)*lines;

                //add padding view
                addOnePadding();
            }else if (block.ui_type.id == LayoutConstant.linearlayout_none) {
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

                addOnePadding();
            } else if (block.ui_type.id == LayoutConstant.linearlayout_poster) {
                BlockLinearButtonView bv = new BlockLinearButtonView(getContext(), block.items, getTag(R.integer.picasso_tag));

                addView(bv);
                getDimens().height += bv.getDimens().height;

                addOnePadding();
            }else if (block.ui_type.id == LayoutConstant.linearlayout_land) {
                PosterEnterView bv = new PosterEnterView(getContext(), block.items, getTag(R.integer.picasso_tag));

                addView(bv);
                getDimens().height += bv.getDimens().height;

                addOnePadding();
            }
        }

        //more than one padding
        getDimens().height += media_item_padding;
    }

    private void addOnePadding(){
        View view = new View(getContext());
        LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, media_item_padding);
        addView(view, flp);

        //add padding
        getDimens().height += media_item_padding;
    }
}
