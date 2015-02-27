package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/19/14.
 */
public class RankBlockView<T> extends BaseCardView implements DimensHelper {
    public RankBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean haveTitle = false;
    private View root;
    private Block<T> content;
    public RankBlockView(Context context, Block<T> block, Object tag){
        this(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        content = block;

        //no need background, it will be set outside
        //setBackgroundResource(R.drawable.com_block_n);

        ArrayList<T> items = block.items;
        String container = block.title;
        String subtitle  = block.sub_title;
        int row_count    = block.ui_type.row_count;

        int height = 0;
        root = View.inflate(getContext(), R.layout.rank_item, this);

        //add title top height
        if(haveTitle) {
            height += getResources().getDimensionPixelSize(R.dimen.rank_title_top);

            TextView title = (TextView) root.findViewById(R.id.rank_title);
            title.setText(container);

            //add text view height
            height += Math.ceil(title.getPaint().getFontMetrics().descent - title.getPaint().getFontMetrics().top) + 2;

            //add top height
            height += getResources().getDimensionPixelSize(R.dimen.rank_video_show_top);
        }

        if(row_count == 0)
            row_count = 3;

        int padding = (getDimens().width-row_count*getResources().getDimensionPixelSize(R.dimen.media_port_width))/(row_count+1);
        LinearFrame header = (LinearFrame)root.findViewById(R.id.header);
        for (int i=0;i<row_count;i++) {
            final DisplayItem item = (DisplayItem) items.get(i);
            final View tv =  View.inflate(getContext(), R.layout.media_port_item, null);
            if(item.hint != null && item.hint.mid() != null) {
                ((TextView) tv.findViewById(R.id.click_count)).setText(item.hint.mid());
            }
            ((TextView)tv.findViewById(R.id.name)).setText(item.title);

            ImageView rightCorner = (ImageView) tv.findViewById(R.id.rank_media_corner);
            if(item.images.get("right_top_corner") != null)
                Picasso.with(getContext()).load(item.images.get("right_top_corner").url).tag(getTag(R.integer.picasso_tag)).fit().transform(new CategoryBlockView.Round_Corners(getContext(), 4, 4, true)).into(rightCorner);


            ImageView iv = (ImageView) tv.findViewById(R.id.poster);
            tv.findViewById(R.id.rank_media_item_click).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcherAction(getContext(), item);
                }
            });

            header.addItemView(tv, getResources().getDimensionPixelSize(R.dimen.media_port_width), getResources().getDimensionPixelSize(R.dimen.rank_media_item_height), padding, padding);
            Picasso.with(getContext()).load(item.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).fit().into(iv);
        }

        //add show movie height
        height += getResources().getDimensionPixelSize(R.dimen.rank_media_item_height);

        //add top height
        if(haveTitle) {
            height += getResources().getDimensionPixelSize(R.dimen.rank_video_list_top);
        }

        LinearFrame list = (LinearFrame)root.findViewById(R.id.list);
        //add top line
        {
            View divider = View.inflate(getContext(), R.layout.rank_divider, null);
            list.addItemViewPort(divider, getResources().getDimensionPixelSize(R.dimen.rank_banner_width), 1, 0);
            height += 1;
        }

        for (int i=row_count;i<items.size();i++) {
            final DisplayItem item = (DisplayItem) items.get(i);
            View view =  View.inflate(getContext(), R.layout.media_item_textview, null);
            final TextView tv = (TextView)view.findViewById(R.id.quick_entry_user);
            tv.setText(String.format("%1$s . %2$s", i+1, getTitle(item.title)));

            final TextView actor = (TextView)view.findViewById(R.id.rank_media_item_actor);
            actor.setText(item.sub_title);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcherAction(getContext(), item);
                }
            });

            list.addItemViewPort(view, getResources().getDimensionPixelSize(R.dimen.rank_banner_width),getResources().getDimensionPixelSize(R.dimen.rank_media_text_height), 0);

            //add text view height
            height +=getResources().getDimensionPixelSize(R.dimen.rank_media_text_height);

            if(i < (items.size()-1)){
                View divider = View.inflate(getContext(), R.layout.rank_divider, null);
                list.addItemViewPort(divider, getResources().getDimensionPixelSize(R.dimen.rank_banner_width), 1, 0);

                //add divider height
                height += 1;
            }
        }

        //add height
        if(haveTitle) {
            height += getResources().getDimensionPixelSize(R.dimen.video_common_interval_24);
            height += getResources().getDimensionPixelSize(R.dimen.rank_button_height);
            height += getResources().getDimensionPixelSize(R.dimen.video_common_interval_24);

            ((Button) root.findViewById(R.id.enter_button)).setText(subtitle);
        }
        getDimens().height = height;
    }
    private String getTitle(String title){
        String trim = title;
        if(title.length() > 11){
            trim = title.substring(0, 11);
            trim += ("...");
        }

        return trim;
    }


    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.rank_banner_width);
            mDimens.height = 0;//getResources().getDimensionPixelSize(R.dimen.quick_entry_user_height);
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {
        LinearFrame header = (LinearFrame)root.findViewById(R.id.header);
        for (int i=0;i<header.getChildCount();i++) {
            final DisplayItem item = (DisplayItem) content.items.get(i);
            View view =  header.getChildAt(i);

            ImageView rightCorner = (ImageView) view.findViewById(R.id.rank_media_corner);
            if(item.images.get("right_top_corner") != null)
                Picasso.with(getContext()).load(item.images.get("right_top_corner").url).tag(getTag(R.integer.picasso_tag)).fit().transform(new CategoryBlockView.Round_Corners(getContext(), 4, 4, true)).into(rightCorner);

            ImageView iv = (ImageView) view.findViewById(R.id.poster);
            Picasso.with(getContext()).load(item.images.get("poster").url).tag(getTag(R.integer.picasso_tag)).fit().into(iv);
        }
    }

    @Override
    public void unbindDrawables(View view) {

    }
}
