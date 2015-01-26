package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import com.squareup.picasso.Picasso;
import com.video.ui.R;

public class DetailPosterView extends RelativeLayout {

    private Context mContext;
    private ImageView mPosterView;

    public DetailPosterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DetailPosterView(Context context) {
        this(context, null, 0);
    }
    public DetailPosterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initUI();
    }

    public void setImageUrlInfo(String url) {
        if(url != null) {
            refreshPosterDefaultBg();
            Picasso.with(getContext()).load(url).fit().centerInside().into(mPosterView);
        } else {
            refreshPosterDefaultBg();
        }
    }

    private void initUI() {
        mPosterView = new ImageView(mContext);
        mPosterView.setScaleType(ScaleType.FIT_XY);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addView(mPosterView, params);

        ImageView mask = new ImageView(mContext);
        mask.setImageResource(R.drawable.detail_poster_mask);
        mask.setScaleType(ScaleType.FIT_XY);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mask.setLayoutParams(p);
        addView(mask);
    }

    //packaged method
    private void refreshPosterDefaultBg() {
        mPosterView.setImageResource(R.drawable.transparent);
    }
}