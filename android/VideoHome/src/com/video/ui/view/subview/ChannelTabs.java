package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;

/**
 * Created by wangwei on 11/18/14.
 */
public class ChannelTabs extends BaseCardView implements DimensHelper {
    Block<DisplayItem> content;
    private TabWidget mTabWidget;
    private boolean mGridLoaded = false;

    int mType =  LayoutConstant.grid_media_land;

    public ChannelTabs(Context context) {
        super(context, null, 0);
    }

    public ChannelTabs(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    private static Dimens mDimens;

    public ChannelTabs(Context context, Block<DisplayItem> blocks) {
        super(context, null, 0);
        initUI(blocks);
    }

    @Override
    public DimensHelper.Dimens getDimens() {
        if(mDimens == null){
            mDimens = new DimensHelper.Dimens();
        }
        mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
        if( mType == LayoutConstant.grid_media_land) {
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.channel_tabs_horizontal_view_height);
        }else{
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.channel_tabs_portrait_view_height);
        }
        return mDimens;
    }
    private class TabClickListener implements OnClickListener {

        private final int mTabIndex;

        private TabClickListener(int tabIndex) {
            mTabIndex = tabIndex;
        }

        public void onClick(View v) {
            showTab(mTabIndex);
        }
    }

    private View root;
    private void initUI(Block<DisplayItem> rootblock){
        root = LayoutInflater.from(getContext()).inflate(R.layout.channel_tabs,null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(root,lp);
        if(rootblock.ui_type.id != LayoutConstant.tabs_horizontal) return ;
        content = rootblock;
        int size = content.blocks.size();
        mTabWidget = (TabWidget)root.findViewById(R.id.channeltabs);
        mTabWidget.setStripEnabled(false);

        for(Block<DisplayItem> block: content.blocks) {
            TextView text = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.tab_text,null);
            text.setText(block.title);
            mTabWidget.addView(text);
            text.setOnClickListener(new TabClickListener(mTabWidget.getTabCount() - 1));

            if(mGridLoaded ==false && block.items != null){
                mGridLoaded = true;
                text.setBackgroundResource(R.drawable.media_pager_tab_left);
                text.setTextColor(getResources().getColor(R.color.orange));

                FrameLayout grid = (FrameLayout)root.findViewById(R.id.channeltabcontent);

                if(block.ui_type.id == LayoutConstant.grid_media_land) {
                    mType = LayoutConstant.grid_media_land;

                    int row_count = block.ui_type.row_count;
                    if(row_count == 0)
                        row_count = 2;

                    int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_width))/(row_count+1);

                    for(int i=0;i<block.items.size();++i) {
                        final DisplayItem item = block.items.get(i);

                        ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tab_media_hor, null);
                        ImageView image = (ImageView)meida.findViewById(R.id.poster);
                        Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).fit().into(image);
                        TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                        descrip.setText(item.title);
                        meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                launcherAction(getContext(), item);
                            }
                        });

                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        int width  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_width);
                        int height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_height);
                        flp.leftMargin = getPaddingLeft() + (width*(i%row_count) ) + padding*(i%row_count + 1);
                        flp.topMargin  = getPaddingTop()  + (height*(i/row_count) );

                        grid.addView(meida,flp);
                    }
                }else if(block.ui_type.id == LayoutConstant.grid_media_port){
                    int row_count = block.ui_type.row_count;
                    if(row_count == 0)
                        row_count = 3;

                    int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width))/(row_count+1);
                    mType = LayoutConstant.grid_media_port;
                    for(int i=0;i<block.items.size();++i) {
                        final DisplayItem item = block.items.get(i);

                        ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tab_media_port, null);
                        ImageView image = (ImageView)meida.findViewById(R.id.poster);
                        Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).fit().into(image);
                        TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                        descrip.setText(item.title);

                        meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                launcherAction(getContext(), item);
                            }
                        });

                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        int width  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width);
                        int height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_height);
                        flp.leftMargin = getPaddingLeft() + (width*(i%row_count)) + padding*(i%row_count + 1);
                        flp.topMargin  = getPaddingTop() + (height*(i/row_count));

                        grid.addView(meida,flp);
                    }
                }
            }
        }
    }

    private void showTab(int index){
        FrameLayout grid = (FrameLayout)root.findViewById(R.id.channeltabcontent);
        Block<DisplayItem> block = content.blocks.get(index);

        for(int i=0;i<mTabWidget.getChildCount();i++){
            TextView text = (TextView) mTabWidget.getChildAt(i);
            if(i == index) {
                if(i == 0)
                    text.setBackgroundResource(R.drawable.media_pager_tab_left);
                else
                    text.setBackgroundResource(R.drawable.media_pager_tab_mid);
                text.setTextColor(getResources().getColor(R.color.orange));
            }else {
                text.setBackground(null);
                text.setTextColor(getResources().getColor(R.color.tab));
            }
        }

        for(int i=0;i<grid.getChildCount();i++){
            View meida = grid.getChildAt(i);
            final DisplayItem item = block.items.get(i);
            ImageView image = (ImageView)meida.findViewById(R.id.poster);
            Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).into(image);
            TextView descrip = (TextView)meida.findViewById(R.id.descrip);
            descrip.setText(item.title);

            meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    launcherAction(getContext(), item);
                }
            });
        }
    }
}
