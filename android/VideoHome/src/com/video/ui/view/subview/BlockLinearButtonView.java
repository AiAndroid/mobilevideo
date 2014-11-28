package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class BlockLinearButtonView extends BaseCardView implements DimensHelper {
    public BlockLinearButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int []draws = {
        R.drawable.quick_entry_tv_series_bg,
        R.drawable.quick_entry_film_bg,
        R.drawable.quick_entry_variety_bg,
        R.drawable.quick_entry_all_bg
    };
    public BlockLinearButtonView(Context context, ArrayList<DisplayItem> items, Object tag) {
        this(context, null, 0);
        setTag(R.integer.picasso_tag, tag);

        View v = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)v.findViewById(R.id.metrolayout);

        int width = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_block_width);
        int padding = (getDimens().width-items.size()*width)/(items.size()+1);
        int leftPadding = padding;
        for (int i=0;i<items.size();i++) {
            final DisplayItem item = items.get(i);
            View view =  View.inflate(getContext(), R.layout.qucik_entry_block_textview, null);
            view.setClickable(true);
            view.setBackgroundResource(draws[i%4]);

            TextView tv = (TextView) view.findViewById(R.id.quick_enter);
            tv.setText(item.title);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcherAction(getContext(), item);
                }
            });
            mMetroLayout.addItemView(view,width , getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_block_height), leftPadding, padding);
        }
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_block_height);
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
