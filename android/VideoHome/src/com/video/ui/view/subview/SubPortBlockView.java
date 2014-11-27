package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;

/**
 * Created by wangwei on 11/20/14.
 */
public class SubPortBlockView extends LinearBaseCardView implements DimensHelper{
    Block<DisplayItem> content;

    public SubPortBlockView(Context context) {
        super(context, null, 0);
    }

    public SubPortBlockView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    private Dimens mDimens;

    public SubPortBlockView(Context context, Block<DisplayItem> blocks, Object tag) {
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
            //tabs_horizontal and linearlayout_title show just has one, and they should be the first item
            if (block.ui_type.id == LayoutConstant.tabs_horizontal) {
                View blockView = new ChannelTabsBlockView(getContext(), block, getTag(R.integer.picasso_tag));
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                addView(blockView, flp);
                if (blockView instanceof DimensHelper) {
                    getDimens().height += ((DimensHelper) blockView).getDimens().height;
                }
                addOnePadding();
            }else if(block.ui_type.id == LayoutConstant.linearlayout_title){
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

                root.findViewById(R.id.ads_media_click).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launcherAction(getContext(), block);
                    }
                });

                getDimens().height += getResources().getDimensionPixelSize(R.dimen.media_banner_sub_channel_height);
                addOnePadding();
            }else if (block.ui_type.id == LayoutConstant.grid_media_land || block.ui_type.id == LayoutConstant.grid_media_port) {

                GridMediaBlockView view = new GridMediaBlockView(getContext(), block, getTag(R.integer.picasso_tag));
                addView(view);

                getDimens().height += view.getDimens().height;
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
                PosterEnterBlockView bv = new PosterEnterBlockView(getContext(), block.items, getTag(R.integer.picasso_tag));

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
