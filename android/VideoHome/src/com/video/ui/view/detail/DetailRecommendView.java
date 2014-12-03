package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.Image;
import com.tv.ui.metro.model.ImageGroup;
import com.video.ui.view.subview.SubPortBlockView;

import java.util.ArrayList;

public class DetailRecommendView extends FrameLayout {

	private Context mContext;
	private View mDetailRecommendView;
	
	public DetailRecommendView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public DetailRecommendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
	}

	public DetailRecommendView(Context context) {
        this(context, null, 0);
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

        SubPortBlockView view = new SubPortBlockView(getContext(), item, new Integer(100));

        LinearLayout.LayoutParams flp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, view.getDimens().height);
        addView(view, flp);
	}
}
