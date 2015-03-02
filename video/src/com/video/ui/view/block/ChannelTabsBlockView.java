package com.video.ui.view.block;

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
import com.tv.ui.metro.model.LayoutConstant;

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

            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.rank_banner_width);
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

    private int secondHeight = -1;
    private int firstHeight  = -1;
    private int itemWidth    = -1;
    private int itemHeight   = -1;
    private int imageWidth   = -1;
    private  int imageHeight = -1;


    private void fetchLineTextViewDimens(Block<T> block){
        if(secondHeight == -1) {
            secondHeight = getResources().getDimensionPixelSize(R.dimen.size_81);
            firstHeight = getResources().getDimensionPixelSize(R.dimen.size_76);
            if (block.ui_type.id == LayoutConstant.grid_media_land) {
                secondHeight = getResources().getDimensionPixelSize(R.dimen.size_84);
                itemWidth       = getResources().getDimensionPixelSize(R.dimen.channel_media_view_width);
                itemHeight      = getResources().getDimensionPixelSize(R.dimen.channel_media_view_height);
                imageWidth  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_image_width);
                imageHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_image_height);
            }else {
                itemWidth  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width);
                itemHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_height);
                imageWidth  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_image_width);
                imageHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_image_height);
            }
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

        //for tab padding top
        int topMargin = item_padding;
        fetchLineTextViewDimens(content.blocks.get(0));

        for(int tabstep=0;tabstep<content.blocks.size();tabstep++) {
            Block<T> block = content.blocks.get(tabstep);
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
                FrameLayout grid = (FrameLayout)root.findViewById(R.id.channeltabcontent);
                if(size == 1){
                    grid.setBackground(null);
                    this.setBackground(null);
                    root.findViewById(R.id.tab_title_background).setBackground(null);
                    text.setGravity(Gravity.LEFT);
                    int textPadding = getResources().getDimensionPixelSize(R.dimen.rank_title_top);
                    text.setPadding(textPadding, textPadding, textPadding, textPadding);
                }

                //default defined as landscape
                int row_count =  block.ui_type.row_count <=0?2:block.ui_type.row_count;
                int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_width))/(row_count+1);
                mType = block.ui_type.id == LayoutConstant.grid_media_port?LayoutConstant.grid_media_port:LayoutConstant.grid_media_land;
                int res_id = block.ui_type.id == LayoutConstant.grid_media_port?R.layout.tab_media_port:R.layout.tab_media_hor;

                if(block.ui_type.id == LayoutConstant.grid_media_port){
                    padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width))/(row_count+1);
                }

                for(int i=0;i<block.items.size();++i) {
                    final DisplayItem item = (DisplayItem) block.items.get(i);

                    ViewGroup meida = new GridMediaBlockView.MediaItemView(getContext(), item, res_id, imageWidth, imageHeight, secondHeight);
                    meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            launcherAction(getContext(), item);
                        }
                    });

                    FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, itemHeight);

                    flp.leftMargin = getPaddingLeft() + (itemWidth*(i%row_count)) + padding*(i%row_count + 1);
                    flp.topMargin  = getPaddingTop() + (itemHeight*(i/row_count)) + item_padding*(i/row_count + 1) + topMargin;

                    grid.addView(meida,flp);
                }

                block_height += itemHeight*((block.items.size()-1)/row_count + 1) + item_padding*((block.items.size()-1)/row_count + 1) + topMargin;
            }else if(tabstep <size-1){
                text.setBackgroundResource(R.drawable.media_page_bg_mid);
            }
        }


        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, block_height+item_padding) ;
        addView(root,lp);

        getDimens().height = block_height + item_padding;
    }

    private void showTab(int index){
        FrameLayout grid = (FrameLayout)root.findViewById(R.id.channeltabcontent);
        Block<T> block = content.blocks.get(index);

        int size = mTabWidget.getChildCount();
        for(int i=0;i<size;i++){
            TextView text = (TextView) mTabWidget.getChildAt(i);
            if(i == index) {
                if(i == 0)
                    text.setBackgroundResource(R.drawable.media_pager_tab_left);
                else
                    text.setBackgroundResource(R.drawable.media_pager_tab_mid);
                text.setTextColor(getResources().getColor(R.color.orange));
            }else {
                if(i < index-1 ||
                   (i>=index+1 && i != (size -1))) {
                    text.setBackgroundResource(R.drawable.media_page_bg_mid);
                }else {
                    text.setBackground(null);
                }
                text.setTextColor(getResources().getColor(R.color.tab));
            }
        }


        fetchLineTextViewDimens(block);

        for(int i=0;i<grid.getChildCount();i++){
            View meida = grid.getChildAt(i);
            final DisplayItem item = (DisplayItem) block.items.get(i);
            setImageAndDesc(meida, item);
            LinearBaseCardView.setHintText(meida, item);

            meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                launcherAction(getContext(), item);
                }
            });
        }
    }

    private void setImageAndDesc(View media, DisplayItem item){
        ImageView image = (ImageView)media.findViewById(R.id.poster);
        Picasso.with(getContext()).load(item.images.get("poster").url).resize(imageWidth, imageHeight).transform(new BlendImageWithCover(getContext(), mType==LayoutConstant.grid_media_port?true:false)).into(image);

        TextView title = (TextView)media.findViewById(R.id.media_title);
        if(title != null) {
            TextView descrip = (TextView)media.findViewById(R.id.descrip);
            if(TextUtils.isEmpty(item.sub_title)){
                title.setSingleLine(false);
                title.setHeight(firstHeight + secondHeight);
                descrip.setVisibility(GONE);
            }else {
                descrip.setVisibility(VISIBLE);
                title.setSingleLine(true);
                descrip.setHeight(secondHeight);
            }
            title.setText(item.title);
            descrip.setText(item.sub_title);

        }else {
            TextView descrip = (TextView)media.findViewById(R.id.descrip);
            descrip.setText(item.title + " " +item.sub_title);
        }
    }
}
