package com.video.ui.view.block;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.MetroLayout;

/**
 * Created by liuhuadonbg on 2/26/15.
 */
public class TableSmallIconBlockView<T> extends BaseCardView implements  DimensHelper{
    public TableSmallIconBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TableSmallIconBlockView(Context context, Block<T> block) {
        super(context, null, 0);
        createUI(block);
    }

    private String default_cp_icon = "http://file.market.xiaomi.com/download/Duokan/dfddd21f-3be0-4def-8b5d-d293328ed800/symbol_default_normal.png";
    private void createUI(Block<T> block){
        RelativeLayout view = (RelativeLayout) View.inflate(getContext(), R.layout.relative_layout_container, this);

        final MetroLayout ml = new MetroLayout(getContext());
        int step      = 0;
        int row_count = 6;
        if(block.ui_type != null && block.ui_type.row_count> 0)
            row_count = block.ui_type.row_count;

        int width  = 0;
        int height = 0;

        if(block.ui_type.id == LayoutConstant.grid_small_icon){
            width = getContext().getResources().getDimensionPixelSize(R.dimen.recommend_businessmediaview_width);
            height = getContext().getResources().getDimensionPixelSize(R.dimen.recommend_businessmediaview_height);
        }else {
            width = getContext().getResources().getDimensionPixelSize(R.dimen.list_single_item_width);
            height = getContext().getResources().getDimensionPixelSize(R.dimen.list_single_item_height);
        }
        int padding = (getDimens().width - row_count*width)/(row_count+1);

        int res_id = R.layout.grid_small_icon_business;
        if(block.ui_type.id == LayoutConstant.list_small_icon){
            res_id = R.layout.list_small_icon_business;
        }

        int itemHeight = 0;
        for(int i=0;i< block.items.size();i++) {
            final DisplayItem episode = (DisplayItem) block.items.get(i);
            final View convertView = View.inflate(getContext(), res_id, null);
            convertView.setTag(episode);

            final TextView mFiter = (TextView) convertView.findViewById(R.id.business_text);
            mFiter.setText(String.valueOf(episode.title));

            ImageView imageView = (ImageView) convertView.findViewById(R.id.business_icon);
            if(episode.images.icon() != null)
                Picasso.with(getContext()).load(episode.images.icon().url).placeholder(R.drawable.default_business_icon).into(imageView);
            else
                Picasso.with(getContext()).load(default_cp_icon).placeholder(R.drawable.default_business_icon).into(imageView);

            ml.addItemViewPort(convertView, block.ui_type.id == LayoutConstant.list_small_icon?LayoutConstant.list_small_icon_item:LayoutConstant.grid_small_icon_item,step % row_count, step / row_count, padding);

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseCardView.launcherAction(getContext(), episode);
                }
            });

            final TextView desc = (TextView) convertView.findViewById(R.id.business_desc);
            if(desc != null)
                desc.setText(String.valueOf(episode.sub_title));

            step++;
        }

        if(itemHeight == 0){
            itemHeight = height;
        }

        int lines = (block.items.size()+row_count -1)/row_count;
        getDimens().height += (itemHeight + media_item_padding)*lines;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimens().height);
        lp.addRule(CENTER_HORIZONTAL);
        addView(ml, lp);
    }

    private DimensHelper.Dimens mDimens;
    @Override
    public DimensHelper.Dimens getDimens() {
        if(mDimens == null){
            mDimens = new DimensHelper.Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = 0;
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {

    }

    @Override
    public void unbindDrawables(View view) {

    }
}
