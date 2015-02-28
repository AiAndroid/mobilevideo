package com.video.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.tv.ui.metro.model.*;
import com.video.ui.view.block.PortBlockView;

import java.util.ArrayList;

public class RecommendBlockView extends FrameLayout {

    public RecommendBlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public RecommendBlockView(Context context) {
        this(context, null, 0);
    }
	public RecommendBlockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

    public void setVideo(VideoItem videoItem){

    }

    private Block<DisplayItem> createTitleBlock(){
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = "类似电影";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = 206;
        return item;
    }

    private Block<DisplayItem> createRecommendBlock(){
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.title = "类似电影";
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = 412;
        item.ui_type.row_count = 3;

        item.items = new ArrayList<DisplayItem>();
        item.items.add(createTestMovie());
        item.items.add(createTestMovie());
        item.items.add(createTestMovie());
        item.items.add(createTestMovie());
        item.items.add(createTestMovie());
        item.items.add(createTestMovie());
        return item;
    }

    private DisplayItem createTestMovie(){
        DisplayItem item = new DisplayItem();
        item.images = new ImageGroup();
        Image image = new Image();
        image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01cxqklg0Hk/XZAm0PUkdNeJIX.jpg";
        item.images.put("poster", image);

        item.title = "主名字";
        item.sub_title = "副标题";

        return item;
    }

	//init
	private void init() {
        Block<DisplayItem> item = new Block<DisplayItem>();
        item.ui_type = new DisplayItem.UI();
        item.ui_type.id = 401;
        item.blocks = new ArrayList<Block<DisplayItem>>();
        item.blocks.add(createTitleBlock());
        item.blocks.add(createRecommendBlock());

        PortBlockView view = new PortBlockView(getContext(), item, new Integer(100));

        LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, view.getDimens().height);
        addView(view, flp);
	}
}
