package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

/**
 * Created by liuhuadong on 11/19/14.
 */
public class FeatureItemView extends BaseCardView  implements DimensHelper {
    public FeatureItemView(Context context, DisplayItem item) {
        this(context, null, 0);

        initUI(item);
    }

    public FeatureItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private View mContentView;

    //UI
    private CornerUpImageView mPosterView;
    private TextView  mDescView;
    private TextView  mFavoriteView;
    private ImageView mPoster;
    private View      mClickView;

    private void initUI(final DisplayItem item){
        this.item = item;

        mContentView = View.inflate(getContext(), R.layout.feature_item, null);
        FrameLayout.LayoutParams contentViewParams = new FrameLayout.LayoutParams(getDimens().width, getDimens().height);
        addView(mContentView, contentViewParams);
        mPosterView = (CornerUpImageView) mContentView.findViewById(R.id.feature_media_poster);

        mPoster     = (ImageView) mContentView.findViewById(R.id.feature_poster_bg);
        Picasso.with(getContext()).load(item.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).fit().transform(new CategoryBlockView.Round_Corners(getContext(), 4, 4, true)).into(mPoster);

        mDescView = (TextView) mContentView.findViewById(R.id.feature_media_desc);
        mDescView.setText(item.title);

        mFavoriteView = (TextView) mContentView.findViewById(R.id.feature_media_favorite);
        mFavoriteView.setText(String.valueOf(item.media.likes));

        mClickView = mContentView.findViewById(R.id.feature_media_click);

        mContentView.setClickable(true);
        mClickView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                launcherAction(getContext(), item);
            }
        });
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.feature_media_view_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.feature_media_view_height);
        }
        return mDimens;
    }
}
