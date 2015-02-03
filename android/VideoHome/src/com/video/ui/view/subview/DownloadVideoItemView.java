package com.video.ui.view.subview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.Image;
import com.tv.ui.metro.model.ImageGroup;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.idata.iDataORM;
import com.video.ui.view.LayoutConstant;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class DownloadVideoItemView extends RelativeLayout {

    public DownloadVideoItemView(Context context) {
        super(context);
        initViews(context);
    }


    private int width  = -1;
    private int height = -1;
    iDataORM.ActionRecord content;
    Gson gson = new Gson();
    VideoItem displayItem;
    DisplayItem.Media.Episode episode;

    public void setContent(iDataORM.ActionRecord item){
        content = item;
        if(item.object == null){
            item.object = gson.fromJson(item.json, VideoItem.class);
        }

        if(item.object != null){
            displayItem = (VideoItem) item.object;

            if(displayItem.images == null || displayItem.images.poster() == null) {
                displayItem.images = new ImageGroup();
                Image image = new Image();
                image.url = displayItem.media.poster;
                displayItem.images.put("poster", image);
            }
        }

        episode = gson.fromJson(item.sub_value, DisplayItem.Media.Episode.class);
        if(width == -1){
            width = getResources().getDimensionPixelSize(R.dimen.media_list_cover_v_width);
            height = getResources().getDimensionPixelSize(R.dimen.media_list_cover_v_height);
        }

        String logo = "";
        if(displayItem.images != null) {
            if (displayItem.images.poster() != null)
                logo = displayItem.images.poster().url;

            if(logo != null && logo.length() > 0) {
                Picasso.with(getContext()).load(logo).tag(getContext()).centerCrop().resize(width, height).into(poster);
            }
        }

        // title
        title.setText(displayItem.title);
        // subtitle
        subtitle.setText(episode.name);
        // desc
        desc.setText("");

        // value
        convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.GONE);
        convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.GONE);
        place.setVisibility(View.INVISIBLE);
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
