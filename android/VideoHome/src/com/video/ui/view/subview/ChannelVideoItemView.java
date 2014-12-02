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

/**
 * Created by liuhuadong on 12/2/14.
 */
public class ChannelVideoItemView extends RelativeLayout {

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
    public void setContent(DisplayItem item){
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
        subtitle.setText("分类");
        // actor
        actors.setText("演员");

        // score
        if(mShowScroe){
            score.setText(String.format("%.1f", 9.9f));
        }else{
            score.setText(String.format(" %d", 100));
        }

        // place
        if(mShowRank){
            place.setVisibility(View.VISIBLE);
            place.setText(getResources().getString(R.string.place_at, 0));
        }else{
            place.setVisibility(View.INVISIBLE);
        }
    }

    private boolean mShowRank = true;
    private boolean mShowScroe = true;

    ImageView poster;
    TextView title;
    TextView subtitle;
    TextView actors;
    TextView score;
    TextView place;
    public RelativeLayout layout;
    public View     line;
    public View     padding;


    public void setShowRank(boolean show){
        mShowRank = show;
    }

    public void setShowScroe(boolean show){
        mShowScroe = show;
    }

    private void initViews(Context ctx){
        View convertView = LayoutInflater.from(ctx).inflate(R.layout.channel_rank_item, this);

        if(!mShowScroe){
            convertView.findViewById(R.id.channel_rank_item_score_layout).setVisibility(View.GONE);
            score = (TextView) convertView.findViewById(R.id.channel_rank_item_hot);
        }else{
            convertView.findViewById(R.id.channel_rank_item_hot_layout).setVisibility(View.GONE);
            score = (TextView) convertView.findViewById(R.id.channel_rank_item_score);
        }
        layout = (RelativeLayout) convertView.findViewById(R.id.channel_rank_item_layout);
        poster = (ImageView) convertView.findViewById(R.id.channel_rank_item_poster);
        title = (TextView) convertView.findViewById(R.id.channel_rank_item_title);
        subtitle = (TextView) convertView.findViewById(R.id.channel_rank_item_subtitle);
        actors = (TextView) convertView.findViewById(R.id.channel_rank_item_actor);
        place = (TextView) convertView.findViewById(R.id.channel_rank_item_place);
        line = convertView.findViewById(R.id.channel_rank_item_line);
        padding = convertView.findViewById(R.id.channel_rank_item_padding);
    }

    OnClickListener clickListener = new OnClickListener(){
        @Override
        public void onClick(View view) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(content != null ){
                    content.type = "item";
                    intent.setData(Uri.parse("mvschema://" + content.ns + "/" + content.type + "?rid=" + content.id));
                    intent.putExtra("item", content);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }else {
                    //just for test
                    intent.setData(Uri.parse("mvschema://video/item" /*+ item.type */ + "?rid=" + content.id));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }

            }catch (Exception ne){ne.printStackTrace();}
        }
    };
}
