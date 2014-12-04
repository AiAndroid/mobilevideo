package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;

/**
 * Created by wangwei on 11/20/14.
 */
public class PortBlockView extends LinearBaseCardView implements DimensHelper{
    public PortBlockView(Context context) {
        super(context, null, 0);
    }

    public PortBlockView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    private DimensHelper.Dimens mDimens;
    private Block<DisplayItem>  content;

    public PortBlockView(Context context, Block<DisplayItem> blocks, Object tag) {
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        content = blocks;

        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.com_block_n);
        initUI(blocks);
    }

    private static int media_item_padding = -1;
    @Override
    public DimensHelper.Dimens getDimens() {
        if(mDimens == null){
            mDimens = new DimensHelper.Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = 0;
            media_item_padding = getResources().getDimensionPixelSize(R.dimen.media_item_padding);
        }

        return mDimens;
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

    private void initUI(Block<DisplayItem> rootblock) {

        content = rootblock;
        int size = content.blocks.size();
        for (int i=0;i<size;i++){
            final Block<DisplayItem> block = content.blocks.get(i);
            if (block.ui_type.id == LayoutConstant.tabs_horizontal) {
                View blockView = new ChannelTabsBlockView(getContext(), block,getTag(R.integer.picasso_tag));
                LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                addView(blockView, flp);
                if (blockView instanceof DimensHelper) {
                    getDimens().height += ((DimensHelper) blockView).getDimens().height;
                }

                addOnePadding();
            } else if (block.ui_type.id == LayoutConstant.linearlayout_none) {
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
    }

    private void addOnePadding(){
        View view = new View(getContext());
        LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, media_item_padding);
        addView(view, flp);

        //add padding
        getDimens().height += media_item_padding;
    }
}
