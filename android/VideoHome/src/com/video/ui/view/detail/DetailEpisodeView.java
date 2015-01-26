package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.subview.PortBlockView;

import java.util.ArrayList;
import java.util.Date;

public class DetailEpisodeView extends FrameLayout {

    public DetailEpisodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DetailEpisodeView(Context context) {
        this(context, null, 0);
    }
	public DetailEpisodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    public void setVideo(VideoItem videoItem){
        init(videoItem);
    }

    private Block<VideoItem> createTitleBlock(VideoItem videoItem){
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = String.format("共%1$s集", videoItem.media.items.size());
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = 206;
        return item;
    }


    private Block<VideoItem> createEpisodeBlock(VideoItem videoItem) {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "episode";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_episode;
        item.ui_type.row_count     = 4;
        item.ui_type.display_count = 11;
        item.media = new DisplayItem.Media();
        item.media.items = new ArrayList<DisplayItem.Media.Episode>();
        item.media.items.addAll(videoItem.media.items);

        return item;
    }

    private Block<VideoItem> createListEpisodeBlock(VideoItem videoItem) {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "episode";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_episode_list;
        item.ui_type.row_count     = 4;
        item.ui_type.display_count = 11;


        item.media = new DisplayItem.Media();
        item.media.items = new ArrayList<DisplayItem.Media.Episode>();
        item.media.items.addAll(videoItem.media.items);

        return item;
    }

    private Block<VideoItem> createLineBlock(VideoItem videoItem) {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "全部剧集";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_none;

        item.media = new DisplayItem.Media();
        item.media.items = new ArrayList<DisplayItem.Media.Episode>();
        item.media.items.addAll(videoItem.media.items);

        return item;
    }

	//init
	private void init(VideoItem videoItem) {
        if(videoItem.media.display_layout.equals("variety")){
            Block<VideoItem> item = new Block<VideoItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<VideoItem>>();

            Block<VideoItem> video = createListEpisodeBlock(videoItem);
            item.blocks.add(video);
            if(videoItem.media.items.size() > 4) {
                item.blocks.add(createLineBlock(videoItem));
            }

            PortBlockView view = new PortBlockView(getContext(), item, new Integer(100));
            LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(view.getDimens().width, view.getDimens().height);
            addView(view, flp);

        }else {
            Block<VideoItem> item = new Block<VideoItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<VideoItem>>();
            item.blocks.add(createTitleBlock(videoItem));
            item.blocks.add(createEpisodeBlock(videoItem));

            if(videoItem.media.items.size() > 4) {
                item.blocks.add(createLineBlock(videoItem));
            }

            PortBlockView view = new PortBlockView(getContext(), item, new Integer(100));
            LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(view.getDimens().width, view.getDimens().height );
            addView(view, flp);
        }
	}
}
