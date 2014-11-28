package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

/**
 * Created by liuhuadong on 11/28/14.
 */
public class SinglePosterBlockView extends BaseCardView implements DimensHelper {
    public SinglePosterBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private View root;
    private Block<DisplayItem> content;
    public SinglePosterBlockView(Context context, final Block<DisplayItem> block, Object tag){
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        content = block;

        root = View.inflate(getContext(), R.layout.subchannel_imageview_container, this);
        ImageView poster = (ImageView) root.findViewById(R.id.image_ads);
        Picasso.with(getContext()).load(block.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).placeholder(R.drawable.default_poster_pic).fit().into(poster);

        root.findViewById(R.id.ads_media_click).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            launcherAction(getContext(), block);
            }
        });

        getDimens().height += getResources().getDimensionPixelSize(R.dimen.media_banner_sub_channel_height);
    }

    private DimensHelper.Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new DimensHelper.Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_sub_channel_width);
            mDimens.height = 0;
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {
        ImageView poster = (ImageView) root.findViewById(R.id.image_ads);
        Picasso.with(getContext()).load(content.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).placeholder(R.drawable.default_poster_pic).fit().into(poster);
    }

    @Override
    public void unbindDrawables(View view) {

    }
}
