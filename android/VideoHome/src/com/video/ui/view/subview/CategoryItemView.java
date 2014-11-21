package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

/**
 * Created by liuhuadong on 11/18/14.
 * main copy from miuivideo
 */
public class CategoryItemView extends BaseCardView implements DimensHelper {
    public CategoryItemView(Context context, DisplayItem item) {
        this(context, null, 0);
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

    private void initUI(final DisplayItem item){
        this.item = item;
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.category_media_view_width);

        View mContentView = View.inflate(getContext(), R.layout.category_item, null);
        FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT);
        addView(mContentView, contentParams);

        mPosterView = (CornerUpImageView) mContentView.findViewById(R.id.category_media_poster);
        mPosterView.setRadius(getResources().getDimensionPixelSize(R.dimen.video_common_radius_9));
        mIconView = (ImageView) mContentView.findViewById(R.id.category_media_desc_icon);
        Picasso.with(getContext()).load(item.images.icon().url).placeholder(R.drawable.category_icon_default).error(R.drawable.category_icon_default).fit().into(mIconView);
        Picasso.with(getContext()).load(item.images.get("left_top_corner").url).placeholder(R.drawable.category_icon_default).error(R.drawable.category_icon_default).fit().into(mPosterView);

        mNameView = (TextView) mContentView.findViewById(R.id.category_media_desc_name);
        mNameView.setText(item.title);

        mCountView = (TextView) mContentView.findViewById(R.id.category_media_desc_count);
        mCountView.setText(item.sub_title);

        mMediaView = (TextView) mContentView.findViewById(R.id.category_media_desc_media);
        mMediaView.setText(item.desc);

        postImage  = (ImageView) mContentView.findViewById(R.id.poster_bg);
        Picasso.with(getContext()).load(item.images.get("poster").url).fit().transform(new Round_Corners(getContext(), 4, 4, true)).into(postImage);

        mContentView.setClickable(true);

        mContentView.findViewById(R.id.category_media_click).setOnClickListener(new OnClickListener() {
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
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.category_media_view_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.category_media_view_height);
        }
        return mDimens;
    }

    public static class Round_Corners implements Transformation {
        private int Round;
        private boolean justTopEffect;
        Round_Corners(Context context, int margin, int Round, boolean justTop) {
            this.Round = dpToPx(context, Round);
            justTopEffect = justTop;
        }

        public int dpToPx(Context context, int dp) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int px = Math.round(dp* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

        @Override
        public String key() {
            return "Round" + Round;
        }

        @Override
        public Bitmap transform(Bitmap arg0) {
            return getRoundedTopLeftCornerBitmap(arg0);
        }

        public Bitmap getRoundedTopLeftCornerBitmap(Bitmap bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float Px = Round;

            final Rect bottomRect = new Rect(0, bitmap.getHeight()/(justTopEffect?2:1), bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, Px, Px, paint);
            // Fill in upper right corner
            // canvas.drawRect(topRightRect, paint);
            // Fill in bottom corners
            canvas.drawRect(bottomRect, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            if (bitmap != output) {
                bitmap.recycle();
            }
            return output;
        }
    }
}
