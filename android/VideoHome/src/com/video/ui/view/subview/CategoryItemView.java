package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

/**
 * Created by liuhuadong on 11/18/14.
 */
public class CategoryItemView extends RelativeLayout implements DimensHelper {
    public CategoryItemView(Context context, DisplayItem item) {
        super(context);

        initUI(item);
    }
    public CategoryItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private CornerUpImageView mPosterView;
    private ImageView postImage;
    private ImageView mIconView;
    private TextView  mNameView;
    private TextView  mCountView;
    private TextView  mMediaView;

    private void initUI(DisplayItem item){
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.category_media_view_width);

        View mContentView = View.inflate(getContext(), R.layout.category_item, null);
        FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT);
        addView(mContentView, contentParams);

        mPosterView = (CornerUpImageView) mContentView.findViewById(R.id.category_media_poster);
        mPosterView.setRadius(getResources().getDimensionPixelSize(R.dimen.video_common_radius_9));
        mIconView = (ImageView) mContentView.findViewById(R.id.category_media_desc_icon);
        Picasso.with(getContext()).load(item.images.icon().url).placeholder(R.drawable.category_icon_default).error(R.drawable.category_icon_default).fit().into(mIconView);

        mNameView = (TextView) mContentView.findViewById(R.id.category_media_desc_name);
        mNameView.setText(item.title);

        mCountView = (TextView) mContentView.findViewById(R.id.category_media_desc_count);
        mCountView.setText(item.sub_title);

        mMediaView = (TextView) mContentView.findViewById(R.id.category_media_desc_media);
        mMediaView.setText(item.desc);

        postImage  = (ImageView) mContentView.findViewById(R.id.poster_bg);
        Picasso.with(getContext()).load(item.images.get("poster").url).fit().into(postImage);
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.category_media_view_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.category_media_view_height);
        }
        return mDimens;
    }
}
