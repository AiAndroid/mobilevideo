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

	private Context mContext;
	private View mDetailRecommendView;

	public DetailEpisodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public DetailEpisodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
	}

	public DetailEpisodeView(Context context) {
        this(context, null, 0);
	}

    private Block<VideoItem> createTitleBlock(){
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "共106集";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = 206;
        return item;
    }

    private ArrayList<DisplayItem.FilterItem> createRecommendBlockItems(int count){
        ArrayList<DisplayItem.FilterItem> filterItems = new ArrayList<DisplayItem.FilterItem>();
        for(int i=1;i<=count;i++){
            DisplayItem.FilterItem item = new DisplayItem.FilterItem();
            item.title  = String.valueOf(i);
            item.target = new DisplayItem.Target();
            filterItems.add(item);
        }
        return filterItems;
    }

    private Block<VideoItem> createEpisodeBlock() {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "episode";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_episode;
        item.ui_type.row_count     = 4;
        item.ui_type.display_count = 11;
        item.filters = new DisplayItem.Filter();
        item.filters.filters = createRecommendBlockItems(item.ui_type.display_count);

        return item;
    }

    private Block<VideoItem> createListEpisodeBlock() {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "episode";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_episode_list;
        item.ui_type.row_count     = 4;
        item.ui_type.display_count = 11;

        item.items = new ArrayList<VideoItem>();
        VideoItem video = new VideoItem();
        video.videos = new ArrayList<VideoItem.Video>();

        item.items.add(video);
        int size = 4;
        if((Math.random()*100) > 50) {
            size = 5;
        }
        for(int i=0;i<size;i++) {
            VideoItem.Video item1 = new VideoItem.Video();
            item1.name = new Date().toGMTString();
            item1.desc = "快跑把， 这里游戏\n我陪配额额";
            video.videos.add(item1);
        }

        return item;
    }

    private Block<VideoItem> createLineBlock() {
        Block<VideoItem> item = new Block<VideoItem>();
        item.title = "全部剧集";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_none;

        return item;
    }

    static  int step = 0;
	//init
	private void init() {
        if (step % 4 == 0) {
            Block<VideoItem> item = new Block<VideoItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<VideoItem>>();
            item.blocks.add(createTitleBlock());
            item.blocks.add(createEpisodeBlock());
            item.blocks.add(createLineBlock());

            PortBlockView view = new PortBlockView(getContext(), item, new Integer(100));
            LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(view.getDimens().width, view.getDimens().height );
            addView(view, flp);
        }else {
            Block<VideoItem> item = new Block<VideoItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<VideoItem>>();

            Block<VideoItem> video = createListEpisodeBlock();
            item.blocks.add(video);
            if(video.items.get(0).videos.size() > 4) {
                item.blocks.add(createLineBlock());
            }

            PortBlockView view = new PortBlockView(getContext(), item, new Integer(100));
            LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(view.getDimens().width, view.getDimens().height);
            addView(view, flp);
        }

        step++;
	}
}
