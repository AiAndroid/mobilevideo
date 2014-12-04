package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.subview.SubPortBlockView;

import java.util.ArrayList;

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

    private Block<DisplayItem> createTitleBlock(){
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = "共106集";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = 206;
        return item;
    }

    private ArrayList<String> createRecommendBlockItems(int count){
        ArrayList<String> episodes = new ArrayList<String>();
        for(int i=1;i<=count;i++){
            episodes.add(String.valueOf(i));
        }
        return episodes;
    }

    private Block<DisplayItem> createRecommendBlock() {
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = "episode";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_episode;
        item.ui_type.row_count     = 4;
        item.ui_type.display_count = 11;
        item.filters = createRecommendBlockItems(item.ui_type.display_count);

        return item;
    }

    private Block<DisplayItem> createLineBlock() {
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = "全部剧集";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.linearlayout_none;

        return item;
    }

	//init
	private void init() {
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = LayoutConstant.block_sub_channel;
        item.blocks = new ArrayList<Block<DisplayItem>>();
        item.blocks.add(createTitleBlock());
        item.blocks.add(createRecommendBlock());
        item.blocks.add(createLineBlock());

        SubPortBlockView view = new SubPortBlockView(getContext(), item, new Integer(100));

        LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams( view.getDimens().width, view.getDimens().height + getResources().getDimensionPixelSize(R.dimen.media_item_padding));
        addView(view, flp);
	}
}
