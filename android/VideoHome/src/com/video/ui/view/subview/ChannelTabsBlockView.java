package com.video.ui.view.subview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;

/**
 * Created by wangwei on 11/18/14.
 */
public class ChannelTabsBlockView<T> extends BaseCardView implements DimensHelper {
    private Block<T> content;
    private TabWidget mTabWidget;
    private boolean mGridLoaded = false;
    private View root;
    private int  currentIndex = 0;

    int mType =  LayoutConstant.grid_media_land;

    public ChannelTabsBlockView(Context context) {
        super(context, null, 0);
    }

    public ChannelTabsBlockView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    private Dimens mDimens;
    public ChannelTabsBlockView(Context context, Block<T> blocks, Object obj) {
        super(context, null, 0);
        setTag(R.integer.picasso_tag, obj);

        initUI(blocks);
    }

    @Override
    public DimensHelper.Dimens getDimens() {
        if(mDimens == null){
            mDimens = new DimensHelper.Dimens();

            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            if( mType == LayoutConstant.grid_media_land) {
                mDimens.height = getResources().getDimensionPixelSize(R.dimen.channel_tabs_horizontal_view_height);
            }else{
                mDimens.height = getResources().getDimensionPixelSize(R.dimen.channel_tabs_portrait_view_height);
            }
        }

        return mDimens;
    }

    @Override
    public void invalidateUI() {
       showTab(currentIndex);
    }

    @Override
    public void unbindDrawables(View view) {

    }

    private class TabClickListener implements OnClickListener {

        private int mIndex;
        private TabClickListener(int tabIndex) {
            mIndex = tabIndex;
        }

        public void onClick(View v) {
            currentIndex = mIndex;
            showTab(mIndex);
        }
    }

    private void initUI(Block<T> rootblock){
        content = rootblock;
        int block_height = 0;
        int item_padding = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
        root = LayoutInflater.from(getContext()).inflate(R.layout.channel_tabs,null);
        if(rootblock.ui_type.id != LayoutConstant.tabs_horizontal)
            return ;

        content = rootblock;
        int size = content.blocks.size();
        mTabWidget = (TabWidget)root.findViewById(R.id.channeltabs);
        mTabWidget.setStripEnabled(false);

        block_height += getResources().getDimensionPixelSize(R.dimen.media_pager_title_text_height);
        //TODO also have .67dp

        for(Block<T> block: content.blocks) {
            TextView text = (TextView)LayoutInflater.from(getContext()).inflate(R.layout.tab_text,null);
            text.setText(block.title);
            mTabWidget.addView(text);
            text.setOnClickListener(new TabClickListener(mTabWidget.getTabCount() - 1));

            if(mGridLoaded ==false && block.items != null){
                mGridLoaded = true;
                text.setBackgroundResource(R.drawable.media_pager_tab_left);
                if(size == 1){
                    text.setBackground(null);
                }

                text.setTextColor(getResources().getColor(R.color.orange));

                int secondHeight = 0;
                secondHeight = getResources().getDimensionPixelSize(R.dimen.size_81);
                if(block.ui_type.id == LayoutConstant.grid_media_land) {
                    secondHeight = getResources().getDimensionPixelSize(R.dimen.size_84);
                }

                FrameLayout grid = (FrameLayout)root.findViewById(R.id.channeltabcontent);
                if(size == 1){
                    grid.setBackground(null);
                    this.setBackground(null);
                    root.findViewById(R.id.tab_title_background).setBackground(null);
                    text.setGravity(Gravity.LEFT);
                    int textPadding = getResources().getDimensionPixelSize(R.dimen.rank_title_top);
                    text.setPadding(textPadding, textPadding, textPadding, textPadding);
                }

                if(block.ui_type.id == LayoutConstant.grid_media_land) {
                    mType = LayoutConstant.grid_media_land;

                    int row_count = block.ui_type.row_count;
                    if(row_count == 0)
                        row_count = 2;

                    int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_width))/(row_count+1);

                    int width  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_width);
                    int height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_height);
                    int imageHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_image_height);
                    for(int i=0;i<block.items.size();++i) {
                        final DisplayItem item = (DisplayItem) block.items.get(i);

                        ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tab_media_hor, null);
                        ImageView image = (ImageView)meida.findViewById(R.id.poster);
                        Picasso.with(getContext()).load(item.images.get("poster").url).resize(width, imageHeight).into(image);

                        TextView title = (TextView)meida.findViewById(R.id.media_title);
                        TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                        if(TextUtils.isEmpty(item.sub_title)){
                            title.setSingleLine(false);
                            descrip.setVisibility(GONE);
                        }else {
                            title.setSingleLine(true);
                            descrip.setHeight(secondHeight);
                            descrip.setVisibility(VISIBLE);
                        }
                        title.setText(item.title);
                        descrip.setText(item.sub_title);

                        title.requestLayout();
                        descrip.requestLayout();

                        meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                launcherAction(getContext(), item);
                            }
                        });

                        setHintText(meida, item);

                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
                        flp.leftMargin = getPaddingLeft() + (width*(i%row_count) ) + padding*(i%row_count + 1);
                        flp.topMargin  = getPaddingTop()  + (height*(i/row_count)) + item_padding*(i/row_count + 1);

                        grid.addView(meida,flp);
                    }

                    block_height += height*((block.items.size()-1)/row_count + 1) + item_padding*((block.items.size()-1)/row_count + 1);
                }else if(block.ui_type.id == LayoutConstant.grid_media_port){
                    int row_count = block.ui_type.row_count;
                    if(row_count == 0)
                        row_count = 3;

                    int width  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width);
                    int height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_height);
                    int imageHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_image_height);
                    int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width))/(row_count+1);
                    mType = LayoutConstant.grid_media_port;
                    for(int i=0;i<block.items.size();++i) {
                        final DisplayItem item = (DisplayItem) block.items.get(i);

                        ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tab_media_port, null);
                        ImageView image = (ImageView)meida.findViewById(R.id.poster);
                        Picasso.with(getContext()).load(item.images.get("poster").url).resize(width, imageHeight).into(image);
                        TextView title = (TextView)meida.findViewById(R.id.media_title);
                        TextView desc = (TextView)meida.findViewById(R.id.descrip);
                        if(TextUtils.isEmpty(item.sub_title)){
                            title.setSingleLine(false);
                            desc.setVisibility(GONE);
                        }else {
                            title.setSingleLine(true);
                            desc.setHeight(secondHeight);
                            desc.setVisibility(VISIBLE);
                        }
                        title.setText(item.title);
                        desc.setText(item.sub_title);

                        title.requestLayout();
                        desc.requestLayout();

                        setHintText(meida, item);

                        meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                launcherAction(getContext(), item);
                            }
                        });

                        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);

                        flp.leftMargin = getPaddingLeft() + (width*(i%row_count)) + padding*(i%row_count + 1);
                        flp.topMargin  = getPaddingTop() + (height*(i/row_count)) + item_padding*(i/row_count + 1);

                        grid.addView(meida,flp);
                    }

                    block_height += height*((block.items.size()-1)/row_count + 1) + item_padding*((block.items.size()-1)/row_count + 1);
                }
            }
        }


        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, block_height+item_padding);
        addView(root,lp);

        //size == 1 is special
        if(size > 1 && false) {
            TextView text = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_text, null);
            text.setText(" ");
            mTabWidget.addView(text);
            text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //do nothing
                }
            });
        }

        getDimens().height = block_height + item_padding;
    }

    private void showTab(int index){
        FrameLayout grid = (FrameLayout)root.findViewById(R.id.channeltabcontent);
        Block<T> block = content.blocks.get(index);

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


        int secondHeight = getResources().getDimensionPixelSize(R.dimen.size_81);
        if(block.ui_type.id == LayoutConstant.grid_media_land) {
            secondHeight = getResources().getDimensionPixelSize(R.dimen.size_84);
        }

        for(int i=0;i<grid.getChildCount();i++){
            View meida = grid.getChildAt(i);
            final DisplayItem item = (DisplayItem) block.items.get(i);
            ImageView image = (ImageView)meida.findViewById(R.id.poster);
            Picasso.with(getContext()).load(item.images.get("poster").url).resize(image.getWidth(), image.getHeight()).into(image);

            TextView title = (TextView)meida.findViewById(R.id.media_title);
            if(title != null) {
                TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                if(TextUtils.isEmpty(item.sub_title)){
                    title.setSingleLine(false);
                    descrip.setVisibility(GONE);
                }else {
                    descrip.setVisibility(VISIBLE);
                    title.setSingleLine(true);
                    descrip.setHeight(secondHeight);
                }
                title.setText(item.title);
                descrip.setText(item.sub_title);

                title.requestLayout();
                descrip.requestLayout();
            }else {
                TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                descrip.setText(item.title + " " +item.sub_title);
            }

            setHintText(meida, item);

            meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                launcherAction(getContext(), item);
                }
            });
        }
    }

    private void setHintText(View meida, DisplayItem item){
        if(item.hint != null && TextUtils.isEmpty(item.hint.left()) == false) {
            TextView leftView = (TextView) meida.findViewById(R.id.left_textview);
            leftView.setText(item.hint.left());
        }

        if(item.hint != null && TextUtils.isEmpty(item.hint.mid()) == false) {
            TextView midView = (TextView) meida.findViewById(R.id.mid_textview);
            midView.setText(item.hint.mid());
        }

        if(item.hint != null && TextUtils.isEmpty(item.hint.right()) == false) {
            TextView rightView = (TextView) meida.findViewById(R.id.right_textview);
            rightView.setText(item.hint.right());
        }
    }
}
