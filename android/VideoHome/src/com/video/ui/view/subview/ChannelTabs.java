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
public class ChannelTabs extends LinearBaseCardView implements DimensHelper {
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
            //mSelectionChangedListener.onTabSelectionChanged(mTabIndex, true);
        }
    }

    private void initUI(Block<DisplayItem> rootblock){
        View root = LayoutInflater.from(getContext()).inflate(R.layout.channel_tabs,null);
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

            if(mGridLoaded==false&&block.items!=null){
                mGridLoaded = true;

                FrameLayout grid = (FrameLayout)root.findViewById(R.id.channeltabcontent);

                if(block.ui_type.id == LayoutConstant.grid_media_land) {
                    mType = LayoutConstant.grid_media_land;
                    for(int i=0;i<block.items.size();++i) {
                        DisplayItem item = block.items.get(i);

                        ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tab_media_hor, null);
                        ImageView image = (ImageView)meida.findViewById(R.id.poster);
                        Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).into(image);
                        TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                        descrip.setText(item.title);
                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        int width = getResources().getDimensionPixelSize(R.dimen.channel_media_view_width);
                        int height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_height);
                        flp.leftMargin = getPaddingLeft() + (width*(i%2) );
                        flp.topMargin = getPaddingTop() + (height*(i/2) );

                        grid.addView(meida,flp);
                    }
                }else if(block.ui_type.id == LayoutConstant.grid_media_port){
                    mType = LayoutConstant.grid_media_port;
                    for(int i=0;i<block.items.size();++i) {
                        DisplayItem item = block.items.get(i);
                        ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tab_media_port, null);

                        ImageView image = (ImageView)meida.findViewById(R.id.poster);
                        Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).into(image);
                        TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                        descrip.setText(item.title);
                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        int width = getResources().getDimensionPixelSize(R.dimen.media_port_width);
                        int height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_height);
                        flp.leftMargin = getPaddingLeft() + (width*(i%3) );
                        flp.topMargin = getPaddingTop() + (height*(i/3) );

                        grid.addView(meida,flp);
                    }
                }


            }

        }


    }
}
