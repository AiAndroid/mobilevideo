package com.video.ui.view.subview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class ChannelVideoItemView extends RelativeLayout {

    private int ui_type = LayoutConstant.channel_list_long_hot;
    public void setUIType(int type){
        ui_type = type;
    }
    public ChannelVideoItemView(Context context, int uitype) {
        super(context);
        ui_type = uitype;
        initViews(context);
    }


    DisplayItem content;
    public void setContent(DisplayItem item, int position){
        content = item;

        title.setText(item.title);
        String logo = "";
        if(item.images != null) {
            if (item.images.poster() != null)
                logo = item.images.poster().url;

            if(logo != null && logo.length() > 0) {
                Picasso.with(getContext()).load(logo).tag(getContext()).fit().into(poster);
            }
        }

        // title
        title.setText(item.title);
        // subtitle
        subtitle.setText(item.sub_title);
        // desc
        desc.setText(item.desc);

        // value
        if (content.ui_type.show_value == 1) {
            if (ui_type == LayoutConstant.channel_list_long_hot) {
                convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.GONE);
                convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.VISIBLE);
                left = (TextView) convertView.findViewById(R.id.channel_rank_item_hot);
                left.setText(String.format(" %1$s", item.value));
            } else if (ui_type == LayoutConstant.channel_list_long_rate) {
                convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.GONE);
                convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.VISIBLE);
                left = (TextView) convertView.findViewById(R.id.channel_rank_item_score);
                left.setText(String.format("%1$s", item.value));

            } else {
                convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.GONE);
                convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.GONE);
                left = (TextView) convertView.findViewById(R.id.channel_rank_item_hint_right);
                if (item.hint != null && TextUtils.isEmpty(item.hint.right()) == false) {
                    left.setVisibility(VISIBLE);
                    left.setText(item.hint.right());
                } else {
                    left.setVisibility(GONE);
                }
            }
        }else {
            convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.GONE);
            convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.GONE);
        }

        // rank
        if(content.ui_type.show_rank == 1 ){
            place.setVisibility(View.VISIBLE);
            place.setText(getResources().getString(R.string.place_at, position+1));
        }else{
            place.setVisibility(View.INVISIBLE);
        }
    }

    View      convertView;
    ImageView poster;
    TextView title;
    TextView subtitle;
    TextView desc;
    TextView left;
    TextView place;
    public RelativeLayout layout;
    public View     line;
    public View     padding;

    private void initViews(Context ctx){
        int res_id = R.layout.channel_rank_item;
        if(ui_type == LayoutConstant.channel_list_short){
            res_id = R.layout.channel_rank_short_item;
        }
        convertView = LayoutInflater.from(ctx).inflate(res_id, this);
        layout = (RelativeLayout) convertView.findViewById(R.id.channel_rank_item_layout);

        poster = (ImageView) convertView.findViewById(R.id.channel_rank_item_poster);
        title = (TextView) convertView.findViewById(R.id.channel_rank_item_title);
        subtitle = (TextView) convertView.findViewById(R.id.channel_rank_item_subtitle);

        desc = (TextView) convertView.findViewById(R.id.channel_rank_item_desc);
        place = (TextView) convertView.findViewById(R.id.channel_rank_item_place);
        line = convertView.findViewById(R.id.channel_rank_item_line);
        padding = convertView.findViewById(R.id.channel_rank_item_padding);
    }
}
