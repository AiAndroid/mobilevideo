package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/25/14.
 */
public class PosterEnterView extends BaseCardView implements DimensHelper {
    public PosterEnterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PosterEnterView(Context context, ArrayList<DisplayItem> items) {
        this(context, null, 0);

        View v = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)v.findViewById(R.id.metrolayout);

        int width = getResources().getDimensionPixelSize(R.dimen.channel_media_poster_view_width);
        int padding = (getDimens().width-items.size()*width)/(items.size()+1);
        int leftPadding = padding;
        for (int i=0;i<items.size();i++) {
            final DisplayItem item = items.get(i);
            View view =  View.inflate(getContext(), R.layout.poster_enter_item, null);
            view.setClickable(true);

            ImageView tv = (ImageView) view.findViewById(R.id.quick_enter_imageview);
            Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).fit().transform(new CategoryItemView.Round_Corners(getContext(),4, 4, false)).into(tv);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcherAction(getContext(), item);
                }
            });
            mMetroLayout.addItemView(view,width , getResources().getDimensionPixelSize(R.dimen.channel_media_poster_view_height), leftPadding, padding);
        }
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.channel_media_poster_view_height);
        }
        return mDimens;
    }
}
