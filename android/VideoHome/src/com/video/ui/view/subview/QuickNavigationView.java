package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class QuickNavigationView extends RelativeLayout implements DimensHelper {
    public QuickNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int []draws = {
        R.drawable.quick_entry_tv_series_bg,
        R.drawable.quick_entry_film_bg,
        R.drawable.quick_entry_variety_bg,
        R.drawable.quick_entry_all_bg
    };
    public QuickNavigationView(Context context, ArrayList<DisplayItem> items) {
        this(context, null, 0);

        View v = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)v.findViewById(R.id.metrolayout);

        for (int i=0;i<items.size();i++) {
            DisplayItem item = items.get(i);
            final TextView tv = (TextView) View.inflate(getContext(), R.layout.qucik_entry_textview, null);
            tv.setText(item.title);
            tv.setBackgroundResource(draws[i%4]);

            Target topDrawable = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    if(bitmap != null){
                        BitmapDrawable image = new BitmapDrawable(getResources(), bitmap);
                        int h = image.getIntrinsicHeight();
                        int w = image.getIntrinsicWidth();
                        image.setBounds( 0, 0, w, h );
                        tv.setCompoundDrawables(null, image, null, null);
                    }
                }

                @Override public void onBitmapFailed(Drawable drawable) {}
                @Override public void onPrepareLoad(Drawable drawable) {}
            };

            mMetroLayout.addItemView(tv, getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_width), getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_height), getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_intervalH));
            Picasso.with(getContext()).load(item.images.icon().url).into(topDrawable);
        }
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_height);
        }
        return mDimens;
    }
}
