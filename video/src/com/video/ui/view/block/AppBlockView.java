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
public class AppBlockView<T> extends BaseCardView implements  DimensHelper{
    public AppBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AppBlockView(Context context, Block<T> block) {
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

        int padding = (getDimens().width - row_count*getContext().getResources().getDimensionPixelSize(R.dimen.recommend_business_item_width))/(row_count+1);

        int itemHeight = 0;
        for(int i=0;i< block.items.size();i++) {
            final DisplayItem episode = (DisplayItem) block.items.get(i);
            final View convertView = View.inflate(getContext(), R.layout.app_view_grid_business, null);
            convertView.setTag(episode);

            final TextView mFiter = (TextView) convertView.findViewById(R.id.business_text);
            mFiter.setText(String.valueOf(episode.title));

            ImageView imageView = (ImageView) convertView.findViewById(R.id.business_icon);
            if(episode.images.icon() != null)
                Picasso.with(getContext()).load(episode.images.icon().url).placeholder(R.drawable.default_poster_media).into(imageView);
            else
                Picasso.with(getContext()).load(default_cp_icon).placeholder(R.drawable.default_poster_media).into(imageView);

            ml.addItemViewPort(convertView, LayoutConstant.app_grid_item,step % row_count, step / row_count, padding);

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseCardView.launcherAction(getContext(), episode);
                }
            });
            step++;
        }

        if(itemHeight == 0){
            itemHeight = getContext().getResources().getDimensionPixelSize(R.dimen.recommend_business_item_height);
        }

        getDimens().height += (itemHeight)* ((step+row_count-1)/row_count) + padding*((step+row_count-1)/row_count);
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
