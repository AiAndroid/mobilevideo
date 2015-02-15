package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean mIsListViewUI = true;
    public void setUIType(int type){
        ui_type = type;
    }
    public ChannelVideoItemView(Context context, int uitype, boolean liststyle) {
        super(context);
        ui_type = uitype;
        mIsListViewUI = liststyle;
        initViews(context);
    }


    private int width  = -1;
    private int height = -1;
    DisplayItem content;
    public void setContent(DisplayItem item, int position){
        content = item;
        if(mIsListViewUI == true){
            setListItemUI(item, position);
        }else {
            setGridItemUI(item, position);
        }
    }

    static int imageWidth  = -1;
    static int imageHeight = -1;
    static int secondHeight = -1;
    private TextView leftView, midView, rightView;

    private void setGridItemUI(DisplayItem item, int position){
        if(convertView == null) {
            convertView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.grid_media_port, this);
            poster = (ImageView) convertView.findViewById(R.id.poster);
            title = (TextView) convertView.findViewById(R.id.media_title);
            desc  = (TextView) convertView.findViewById(R.id.descrip);
            leftView  = (TextView) convertView.findViewById(R.id.left_textview);
            midView   = (TextView) convertView.findViewById(R.id.mid_textview);
            rightView = (TextView) convertView.findViewById(R.id.right_textview);
            tab_media_click = convertView.findViewById(R.id.tab_media_click);
        }

        tab_media_click.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseCardView.launcherAction(getContext(), content);
            }
        });
        if(imageWidth == -1){
            imageWidth  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_image_width);
            imageHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_image_height);
            secondHeight = getResources().getDimensionPixelSize(R.dimen.size_81);
        }

        Picasso.with(getContext()).load(item.images.get("poster").url).resize(imageWidth, imageHeight).into(poster);
        if(TextUtils.isEmpty(item.sub_title)){
            title.setSingleLine(false);
            title.setHeight(getResources().getDimensionPixelSize(R.dimen.size_76) + secondHeight);
            desc.setHeight(0);
            desc.setVisibility(GONE);
        }else {
            title.setSingleLine(true);
            title.setHeight(getResources().getDimensionPixelSize(R.dimen.size_76));
            desc.setHeight(secondHeight);
            desc.setVisibility(VISIBLE);
        }
        title.setText(item.title);
        desc.setText(item.sub_title);

        setHintText(item);
    }

    private void setHintText(DisplayItem item){
        leftView.setText("");
        midView.setText("");
        rightView.setText("");

        if(item.hint != null && TextUtils.isEmpty(item.hint.left()) == false) {
            leftView.setText(item.hint.left());
        }

        if(item.hint != null && TextUtils.isEmpty(item.hint.mid()) == false) {
            midView.setText(item.hint.mid());
        }

        if(item.hint != null && TextUtils.isEmpty(item.hint.right()) == false) {
            rightView.setText(item.hint.right());
        }
    }

    private void setListItemUI(DisplayItem item, int position){
        if(width == -1){
            if(ui_type == LayoutConstant.channel_list_short){
                width = getResources().getDimensionPixelSize(R.dimen.info_channel_list_poster_width);
                height = getResources().getDimensionPixelSize(R.dimen.info_channel_list_poster_height);
            }else {
                width = getResources().getDimensionPixelSize(R.dimen.media_list_cover_v_width);
                height = getResources().getDimensionPixelSize(R.dimen.media_list_cover_v_height);
            }
        }

        title.setText(item.title);
        String logo = "";
        if(item.images != null) {
            if (item.images.poster() != null)
                logo = item.images.poster().url;

            if(logo != null && logo.length() > 0) {
                Picasso.with(getContext()).load(logo).tag(getContext()).centerCrop().resize(width, height).into(poster);
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

            left = (TextView) convertView.findViewById(R.id.channel_rank_item_hint_right);
            if(left != null) {
                if (item.hint != null && TextUtils.isEmpty(item.hint.right()) == false) {
                    left.setVisibility(VISIBLE);
                    left.setText(item.hint.right());
                } else {
                    left.setVisibility(GONE);
                }
            }
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
    View     tab_media_click;
    public RelativeLayout layout;
    public View     line;
    public View     padding;

    private void initViews(Context ctx){
        if(mIsListViewUI == true) {
            int res_id = R.layout.channel_rank_item;
            if (ui_type == LayoutConstant.channel_list_short) {
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
            //tab_media_click = convertView.findViewById(R.id.tab_media_click);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseCardView.launcherAction(getContext(), content);
                }
            });
        }
    }
}
