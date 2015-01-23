package com.video.ui.view.subview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    public ChannelVideoItemView(Context context) {
        this(context, null, 0);
    }
    public ChannelVideoItemView(Context context, AttributeSet as) {
        this(context, as, 0);
    }
    public ChannelVideoItemView(Context context, AttributeSet as, int uiStyle) {
        super(context, as, uiStyle);
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
        // actor
        actors.setText(item.desc);

        // score
        if(ui_type == LayoutConstant.channel_list_long_hot){
            convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.GONE);
            convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.VISIBLE);
            score = (TextView) convertView.findViewById(R.id.channel_rank_item_hot);
            score.setText(String.format(" %1$s", item.value));
        }else{
            convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.GONE);
            convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.VISIBLE);
            score = (TextView) convertView.findViewById(R.id.channel_rank_item_score);
            score.setText(String.format("%1$s", item.value));
        }

        // place
        if(content.ui_type.show_rank == 1){
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
    TextView actors;
    TextView score;
    TextView place;
    public RelativeLayout layout;
    public View     line;
    public View     padding;

    private void initViews(Context ctx){
        convertView = LayoutInflater.from(ctx).inflate(R.layout.channel_rank_item, this);
        layout = (RelativeLayout) convertView.findViewById(R.id.channel_rank_item_layout);
        poster = (ImageView) convertView.findViewById(R.id.channel_rank_item_poster);
        title = (TextView) convertView.findViewById(R.id.channel_rank_item_title);
        subtitle = (TextView) convertView.findViewById(R.id.channel_rank_item_subtitle);
        actors = (TextView) convertView.findViewById(R.id.channel_rank_item_actor);
        place = (TextView) convertView.findViewById(R.id.channel_rank_item_place);
        line = convertView.findViewById(R.id.channel_rank_item_line);
        padding = convertView.findViewById(R.id.channel_rank_item_padding);
    }
}
