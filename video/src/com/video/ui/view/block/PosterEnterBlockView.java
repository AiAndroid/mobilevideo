package com.video.ui.view.block;

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
public class PosterEnterBlockView<T> extends BaseCardView implements DimensHelper {
    public PosterEnterBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private ArrayList<T> content;
    private View root;
    public PosterEnterBlockView(Context context, ArrayList<T> items, Object tag) {
        this(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        content = items;

        root = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);

        int width = getResources().getDimensionPixelSize(R.dimen.channel_media_poster_view_width);
        int padding = (getDimens().width-items.size()*width)/(items.size()+1);
        int leftPadding = padding;
        for (int i=0;i<items.size();i++) {
            final DisplayItem item = (DisplayItem) items.get(i);
            View view =  View.inflate(getContext(), R.layout.poster_enter_item, null);
            view.setClickable(true);

            ImageView tv = (ImageView) view.findViewById(R.id.quick_enter_imageview);
            Picasso.with(getContext()).load(item.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).placeholder(R.drawable.default_poster_pic).fit().transform(new CategoryBlockView.Round_Corners(getContext(),4, 4, false)).into(tv);
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

    @Override
    public void invalidateUI() {
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);
        for (int i=0;i<content.size();i++) {
            final DisplayItem item = (DisplayItem) content.get(i);
            View view =  mMetroLayout.getChildAt(i);
            ImageView tv = (ImageView) view.findViewById(R.id.quick_enter_imageview);
            if(tv != null) {
                Picasso.with(getContext()).load(item.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).placeholder(R.drawable.default_poster_pic).fit().transform(new CategoryBlockView.Round_Corners(getContext(), 4, 4, false)).into(tv);
            }
        }
    }

    @Override
    public void unbindDrawables(View view) {

    }
}
