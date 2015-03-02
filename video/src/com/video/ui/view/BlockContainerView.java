package com.video.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.view.block.GridMediaBlockView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 1/26/15.
 */
public class BlockContainerView  extends MetroLayout {
    public BlockContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHander = new Handler();
    private Block<DisplayItem> content;
    public Block<DisplayItem> getContent(){
        return content;
    }
    public void setBlocks(Block<DisplayItem> tab){
        content = tab;
        this.removeAllViews();
        rowOffset[0] = 0;
        rowOffset[1] = 0;

        int step = 0;
        for(Block<DisplayItem> item:tab.blocks){
            View blockView = inflateBlock(item, new Integer(0));

            if(item.ui_type.id == LayoutConstant.imageswitcher      ||
                    item.ui_type.id == LayoutConstant.linearlayout_top   ||
                    item.ui_type.id == LayoutConstant.linearlayout_left  ||
                    item.ui_type.id == LayoutConstant.list_category_land ||
                    item.ui_type.id == LayoutConstant.list_rich_header   ||
                    item.ui_type.id == LayoutConstant.block_channel      ||
                    item.ui_type.id == LayoutConstant.block_sub_channel  ||
                    item.ui_type.id == LayoutConstant.linearlayout_land  ||
                    item.ui_type.id == LayoutConstant.linearlayout_poster||
                    item.ui_type.id == LayoutConstant.grid_block_selection ||
                    item.ui_type.id == LayoutConstant.grid_media_land ||
                    item.ui_type.id == LayoutConstant.grid_media_port ||
                    item.ui_type.id == LayoutConstant.grid_media_land_title ||
                    item.ui_type.id == LayoutConstant.grid_media_port_title ||
                    item.ui_type.id == LayoutConstant.tabs_horizontal ||
                    item.ui_type.id == LayoutConstant.linearlayout_filter)
                addItemViewPort(blockView, item.ui_type.id, 0, step++);
            else
                addItemViewPort(blockView, MetroLayout.HorizontalMatchWith, 0, step++);
        }

        //find MediaItemView and set select block
        mHander.post(new Runnable() {
            @Override
            public void run() {
                containers.clear();
                findMediaBlockView(BlockContainerView.this);
                for (GridMediaBlockView view:containers){
                    ArrayList<WeakReference<GridMediaBlockView.MediaItemView>> childs = view.getChildMediaViews();
                    for(WeakReference<GridMediaBlockView.MediaItemView> mview : childs){
                        if(mview.get() != null) {
                            mview.get().setOnItemSelectListener(mSelectListener);
                            mview.get().setOnItemLongClick(mLongClickListener);
                        }
                    }
                }
            }
        });
    }

    private ArrayList<GridMediaBlockView> containers = new ArrayList<GridMediaBlockView>();
    public  View findMediaBlockView(ViewGroup view){
        if(view == null)
            return null;

        if(GridMediaBlockView.class.isInstance(view ))
            containers.add((GridMediaBlockView)view);

        int size = view.getChildCount();
        for (int i=0;i<size;i++){
            View item =  view.getChildAt(i);

            if(GridMediaBlockView.class.isInstance(item ))
                containers.add((GridMediaBlockView)item);

            if(item instanceof ViewGroup){
                View result = findMediaBlockView((ViewGroup) item);
                if(result != null){
                    return result;
                }
            }
        }

        return null;
    }

    private GridMediaBlockView.MediaItemView.OnItemSelectListener mSelectListener;
    public void setOnItemSelectListener(GridMediaBlockView.MediaItemView.OnItemSelectListener listener){
        mSelectListener = listener;
    }

    private View.OnLongClickListener mLongClickListener;
    public void setOnItemLongClickLitener(View.OnLongClickListener litener){
        mLongClickListener = litener;
    }

    private VideoItem videoItem;
    public void setVideo(VideoItem tab){
        videoItem = tab;
        this.removeAllViews();
        rowOffset[0] = 0;
        rowOffset[1] = 0;

        int step = 0;
        for(Block<DisplayItem> item:tab.blocks){
            View blockView = inflateBlock(item, new Integer(0));

            if(item.ui_type.id == LayoutConstant.imageswitcher      ||
                    item.ui_type.id == LayoutConstant.linearlayout_top   ||
                    item.ui_type.id == LayoutConstant.linearlayout_left  ||
                    item.ui_type.id == LayoutConstant.list_category_land ||
                    item.ui_type.id == LayoutConstant.list_rich_header   ||
                    item.ui_type.id == LayoutConstant.block_channel      ||
                    item.ui_type.id == LayoutConstant.block_sub_channel  ||
                    item.ui_type.id == LayoutConstant.linearlayout_land  ||
                    item.ui_type.id == LayoutConstant.linearlayout_poster||
                    item.ui_type.id == LayoutConstant.grid_block_selection ||
                    item.ui_type.id == LayoutConstant.grid_media_land ||
                    item.ui_type.id == LayoutConstant.grid_media_port ||
                    item.ui_type.id == LayoutConstant.grid_media_land_title ||
                    item.ui_type.id == LayoutConstant.grid_media_port_title ||
                    item.ui_type.id == LayoutConstant.tabs_horizontal ||
                    item.ui_type.id == LayoutConstant.linearlayout_filter||
                    item.ui_type.id == LayoutConstant.grid_small_icon ||
                    item.ui_type.id == LayoutConstant.list_small_icon)

                addItemViewPort(blockView, item.ui_type.id, 0, step++);
            else
                addItemViewPort(blockView, MetroLayout.HorizontalMatchWith, 0, step++);
        }
    }



    protected View inflateBlock(Block<DisplayItem> item, Object tag){
        View view = ViewCreateFactory.CreateBlockView(getContext(), item, tag);
        if(view != null)
            return view;

        TextView notview =  new TextView(getContext());
        notview.setText("not support: "+item);
        return notview;
    }

    public void setInEditMode(boolean editMode) {
        if(content != null && content.blocks != null){
            setInEditMode(content, editMode, false);
        }

        //refresh UI
        setBlocks(content);
    }

    private void setInEditMode(Block<DisplayItem> block, boolean editMode, boolean selected){
        if(block.blocks != null){
            for(Block<DisplayItem> item: block.blocks){
                setInEditMode(item, editMode, selected);
            }
        }

        if(block.items != null){
            for(DisplayItem item: block.items){
                if(item.settings == null){
                    item.settings = new DisplayItem.Settings();
                }
                item.settings.put(DisplayItem.Settings.edit_mode, editMode==true?"1":"0");
                item.settings.put(DisplayItem.Settings.selected, selected?"1":"0");
            }
        }
    }

    public void selectAll(boolean selectAll) {
        setInEditMode(content, true, selectAll);

        setBlocks(content);

        mSelectListener.onSelected(this, content, selectAll, GridMediaBlockView.MediaItemView.FLAG_SELECT_ALL);
    }
}
