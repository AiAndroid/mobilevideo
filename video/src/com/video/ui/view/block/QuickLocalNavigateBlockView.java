package com.video.ui.view.block;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/18/14.
 */
public class QuickLocalNavigateBlockView extends BaseCardView implements DimensHelper {
    public QuickLocalNavigateBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int []draws = {
            R.drawable.com_btn_left_bg,
            R.drawable.com_btn_mid_bg,
            R.drawable.com_btn_right_bg
    };

    private int []hodlerdraws = {
            R.drawable.quick_entry_play_his,
            R.drawable.quick_entry_offline,
            R.drawable.quick_entry_tv
    };

    private View root;
    private ArrayList<DisplayItem> content;
    public QuickLocalNavigateBlockView(Context context, ArrayList<DisplayItem> items, Object tag) {
        this(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        content = items;

        root = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);

        for (int i=0;i<items.size();i++) {
            final DisplayItem item = items.get(i);
            View view = View.inflate(getContext(), R.layout.qucik_local_entry_textview, null);
            view.setClickable(true);
            view.setBackgroundResource(draws[i%3]);

            TextView  tv = (TextView)view.findViewById(R.id.quick_entry_user);
            tv.setText(item.title);

            ImageView iv = (ImageView) view.findViewById(R.id.local_image_indicator);
            Picasso.with(getContext()).load(item.images.icon().url).placeholder(hodlerdraws[i]).priority(Picasso.Priority.HIGH).fit().into(iv);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcherAction(getContext(), item);
                }
            });
            mMetroLayout.addItemView(view, getDimens().width/3, getResources().getDimensionPixelSize(R.dimen.quick_entry_user_height), 0);
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

    @Override
    public void invalidateUI() {
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);
        for (int i=0;i<content.size();i++) {
            final DisplayItem item = content.get(i);
            View view =  mMetroLayout.getChildAt(i);

            ImageView iv = (ImageView) view.findViewById(R.id.local_image_indicator);
            if(iv != null) {
                Picasso.with(getContext()).load(item.images.icon().url).placeholder(R.drawable.quick_entry_play_his).priority(Picasso.Priority.HIGH).fit().into(iv);
            }
        }
    }

    @Override
    public void unbindDrawables(View view) {

    }
}

