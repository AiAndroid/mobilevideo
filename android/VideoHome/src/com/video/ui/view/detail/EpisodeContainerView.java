package com.video.ui.view.detail;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.tinyui.AllEpisodeActivity;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.subview.PortBlockView;

import java.util.ArrayList;

public class EpisodeContainerView extends FrameLayout {

    public EpisodeContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public EpisodeContainerView(Context context) {
        this(context, null, 0);
    }
	public EpisodeContainerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    public void setVideo(VideoItem videoItem){
        this.removeAllViews();
        createEpisodeView(getContext(), videoItem, this);
    }

    private static Block<VideoItem> createTitleBlock(Context context, VideoItem videoItem){
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = String.format(context.getString(R.string.total_episode), videoItem.media.items.size());
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_title;
        return item;
    }


    private static Block<VideoItem> createEpisodeBlock(VideoItem videoItem) {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "episode";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_episode;
        item.ui_type.row_count     = 4;
        item.ui_type.display_count = 11;
        item.media = videoItem.media;

        return item;
    }

    private static Block<VideoItem> createListEpisodeBlock(VideoItem videoItem) {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "episode";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_episode_list;
        item.ui_type.row_count     = 4;
        item.ui_type.display_count = 11;


        item.media = videoItem.media;

        return item;
    }

    private static Block<VideoItem> createLineBlock(Context context, VideoItem videoItem) {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = context.getString(R.string.all_episode);
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_none;

        item.media = new DisplayItem.Media();
        item.media.items = new ArrayList<DisplayItem.Media.Episode>();
        item.media.items.addAll(videoItem.media.items);

        return item;
    }



	//init
	public static void createEpisodeView(Context context, final VideoItem videoItem, final ViewGroup vg) {
        Block<VideoItem> item = new Block<VideoItem>();
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.block_sub_channel;
        item.blocks = new ArrayList<Block<VideoItem>>();
        item.blocks.add(createTitleBlock(context, videoItem));

        if(videoItem.media.display_layout != null && ("variety").equals(videoItem.media.display_layout.type)){
            item.blocks.add(createListEpisodeBlock(videoItem));
        }else {
            item.blocks.add(createEpisodeBlock(videoItem));
        }

        if(videoItem.media.items.size() > videoItem.media.display_layout.max_display ) {
            item.blocks.add(createLineBlock(context, videoItem));
        }


        PortBlockView view = new PortBlockView(vg.getContext(), item, new Integer(100));
        LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(view.getDimens().width, view.getDimens().height );

        vg.addView(view, flp);

        Button mAllEpisode = (Button) view.findViewById(R.id.enter_button);

        if(mAllEpisode != null){
            mAllEpisode.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(vg.getContext(), AllEpisodeActivity.class);
                    videoItem.media.display_layout.max_display = 1000;
                    videoItem.title = String.format("1-%1$s集", videoItem.media.items.size());
                    //TODO
                    //we should choose the cp from user selection
                    intent.putExtra("cp",   videoItem.media.cps.get(0));
                    intent.putExtra("item", videoItem);
                    vg.getContext().startActivity(intent);
                }
            });
        }
	}
}
