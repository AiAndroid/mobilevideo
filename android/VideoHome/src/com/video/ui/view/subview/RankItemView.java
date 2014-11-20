package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/19/14.
 */
public class RankItemView extends BaseCardView implements DimensHelper {
    public RankItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RankItemView(Context context, ArrayList<DisplayItem> items, String container, int row_count){
        this(context, null, 0);

        int height = 0;
        View v = View.inflate(getContext(), R.layout.rank_item, this);

        //add title top height
        height += getResources().getDimensionPixelSize(R.dimen.rank_title_top);

        TextView title = (TextView) v.findViewById(R.id.rank_title);
        title.setText(container);

        //add text view height
        height +=  Math.ceil(title.getPaint().getFontMetrics().descent - title.getPaint().getFontMetrics().top) +2;
        //add top height
        height += getResources().getDimensionPixelSize(R.dimen.rank_video_show_top);

        if(row_count == 0)
            row_count = 3;

        int padding = (getDimens().width-row_count*getResources().getDimensionPixelSize(R.dimen.media_port_width))/(row_count+1);
        LinearFrame header = (LinearFrame)v.findViewById(R.id.header);
        for (int i=0;i<row_count;i++) {
            DisplayItem item = items.get(i);
            final View tv =  View.inflate(getContext(), R.layout.media_port_item, null);
            ((TextView)tv.findViewById(R.id.click_count)).setText(item.sub_title);
            ((TextView)tv.findViewById(R.id.name)).setText(item.title);
            ImageView iv = (ImageView) tv.findViewById(R.id.poster);

            header.addItemView(tv, getResources().getDimensionPixelSize(R.dimen.media_port_width), getResources().getDimensionPixelSize(R.dimen.rank_media_item_height), padding, padding);
            Picasso.with(getContext()).load(item.images.get("poster").url).fit().into(iv);
        }

        //add show movie height
        height += getResources().getDimensionPixelSize(R.dimen.rank_media_item_height);

        //add top height
        height += getResources().getDimensionPixelSize(R.dimen.rank_video_list_top);

        LinearFrame list = (LinearFrame)v.findViewById(R.id.list);
        for (int i=3;i<items.size();i++) {
            DisplayItem item = items.get(i);
            final TextView tv = (TextView) View.inflate(getContext(), R.layout.media_item_textview, null);
            tv.setText(String.format("%1$s . %2$s", i, item.title));

            list.addItemViewPort(tv, getResources().getDimensionPixelSize(R.dimen.rank_banner_width),getResources().getDimensionPixelSize(R.dimen.rank_media_text_height), 0);

            //add text view height
            height +=getResources().getDimensionPixelSize(R.dimen.rank_media_text_height);

            if(i < (items.size()-1)){
                View divider = View.inflate(getContext(), R.layout.rank_divider, null);
                list.addItemViewPort(divider, getResources().getDimensionPixelSize(R.dimen.rank_banner_width), getResources().getDimensionPixelSize(R.dimen.rank_port_divider_height), 0);

                //add divider height
                height +=getResources().getDimensionPixelSize(R.dimen.rank_port_divider_height);
            }
        }

        //add height
        height += dpToPx(4);
        height += getResources().getDimensionPixelSize(R.dimen.rank_button_height);
        ((Button)v.findViewById(R.id.enter_button)).setText(container);

        height += dpToPx(4);
        getDimens().height = height;
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.rank_banner_width);
            mDimens.height = 1920;//getResources().getDimensionPixelSize(R.dimen.quick_entry_user_height);
        }
        return mDimens;
    }
}
