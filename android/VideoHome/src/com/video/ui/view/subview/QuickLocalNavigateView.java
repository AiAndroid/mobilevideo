package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/18/14.
 */
public class QuickLocalNavigateView extends BaseCardView implements DimensHelper {
    public QuickLocalNavigateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int []draws = {
            R.drawable.com_btn_left_bg,
            R.drawable.com_btn_mid_bg,
            R.drawable.com_btn_right_bg
    };
    public QuickLocalNavigateView(Context context, ArrayList<DisplayItem> items) {
        this(context, null, 0);

        View v = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)v.findViewById(R.id.metrolayout);

        for (int i=0;i<items.size();i++) {
            DisplayItem item = items.get(i);
            final TextView tv = (TextView) View.inflate(getContext(), R.layout.qucik_local_entry_textview, null);
            tv.setText(item.title);
            tv.setBackgroundResource(draws[i%3]);

            Target topDrawable = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    if(bitmap != null){
                        BitmapDrawable image = new BitmapDrawable(getResources(), bitmap);
                        int h = image.getIntrinsicHeight();
                        int w = image.getIntrinsicWidth();
                        image.setBounds( 0, 0, w, h );
                        tv.setCompoundDrawables(image, null, null, null);
                    }
                }

                @Override public void onBitmapFailed(Drawable drawable) {}
                @Override public void onPrepareLoad(Drawable drawable) {}
            };

            Picasso.with(getContext()).load(item.images.icon().url).into(topDrawable);
            mMetroLayout.addItemView(tv, getDimens().width/3, getResources().getDimensionPixelSize(R.dimen.quick_entry_user_height), 0);
        }
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.quick_entry_user_height);
        }
        return mDimens;
    }
}

