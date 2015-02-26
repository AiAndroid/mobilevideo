package com.video.ui.view.subview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.idata.iDataORM;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.MetroLayout;
import com.video.ui.view.detail.EpisodeContainerView;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 12/1/14.
 */
public class SelectItemsBlockView extends BaseCardView implements DimensHelper {
    public SelectItemsBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SelectItemsBlockView(Context context){
        this(context, null, 0);
    }


    private MetroLayout ml;
    private ArrayList<DisplayItem.FilterItem>filtes;
    public static final int Filter_Type  = LayoutConstant.linearlayout_filter;
    public static final int Episode_Type = LayoutConstant.linearlayout_episode;

    private OnClickListener mItemClick;

    private Drawable mNewBackground;
    public void setOnPlayClickListener(OnClickListener itemClick, Drawable newBackground){
        mItemClick     = itemClick;
        mNewBackground = newBackground;
    }

    public OnClickListener getItemClick(){
        return  mItemClick;
    }

    int mUIType = -1;
    public SelectItemsBlockView(final Context context, ArrayList<DisplayItem.FilterItem> filtes, final int uiType, final Block<DisplayItem> block) {
        super(context, null, 0);
        this.filtes = filtes;
        int selectIndex = 2;
        mUIType = uiType;

        RelativeLayout Root = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, this);

        if(uiType == Filter_Type) {
            Root.setBackgroundResource(R.drawable.com_block_n);
        }

        ml = new MetroLayout(context);
        int step      = 0;
        int row_count = 4;


        int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.filter_button_width))/(row_count+1);
        if(uiType == LayoutConstant.linearlayout_episode) {
            padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_width))/(row_count+1);
        }
        int itemHeight = 0;
        for(final DisplayItem.FilterItem item: filtes) {
            View convertView = null;
            if(uiType == LayoutConstant.linearlayout_filter) {
                convertView = View.inflate(getContext(), R.layout.filter_item_layout, null);
                convertView.setBackgroundResource(R.drawable.editable_title_com_btn_bg);
            }else if(uiType == LayoutConstant.linearlayout_episode) {
                convertView = View.inflate(getContext(), R.layout.episode_item_layout, null);
                convertView.setBackgroundResource(R.drawable.com_btn_bg);
            }

            if(uiType == LayoutConstant.linearlayout_filter || uiType == LayoutConstant.linearlayout_episode){
                TextView mFiter = (TextView) convertView.findViewById(R.id.channel_filter_btn);
                if(uiType == LayoutConstant.linearlayout_episode) {
                    if (selectIndex == step)
                        mFiter.setTextColor(getResources().getColor(R.color.orange));
                    else
                        mFiter.setTextColor(getResources().getColor(R.color.p_80_black));
                }

                if (step == filtes.size()-1 && uiType == Filter_Type) {
                    Drawable filDrawble = getResources().getDrawable(R.drawable.detail_screening_icon);
                    filDrawble.setBounds(0+dpToPx(10), 0, filDrawble.getMinimumWidth()+dpToPx(10), filDrawble.getMinimumHeight());
                    mFiter.setCompoundDrawables(filDrawble, null, null, null);
                }

                mFiter.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mItemClick != null){
                            mItemClick.onClick(v);
                        }else {
                            if(uiType == LayoutConstant.linearlayout_filter){
                                if(DisplayItem.FilterItem.custom_filter.equals(item.target.url)) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("mvschema://video/filter?rid=" + block.id));
                                    intent.putExtra("item", block);
                                    intent.putExtra("title", item.title);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getContext().startActivity(intent);
                                }else {
                                    DisplayItem launchItem = new DisplayItem();
                                    launchItem.title = item.title;
                                    launchItem.type = "album";
                                    launchItem.id = item.target.url;
                                    launchItem.ns = "video";
                                    launchItem.target = item.target;
                                    launcherAction(context, launchItem);
                                }
                            }
                        }
                    }
                });

                mFiter.setText(item.title);
                ml.addItemViewPort(convertView, uiType == LayoutConstant.linearlayout_filter?LayoutConstant.linearlayout_filter_item:LayoutConstant.linearlayout_episode_item,step % row_count, step / row_count, padding);
                step++;

            }else if(uiType == LayoutConstant.linearlayout_episode_list){

            }
        }

        if(itemHeight == 0){
            if(uiType == LayoutConstant.linearlayout_filter) {
                itemHeight = getResources().getDimensionPixelSize(R.dimen.size_74);
            }else if(uiType == LayoutConstant.linearlayout_episode) {
                itemHeight = getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_height);
            }
        }

        getDimens().height += (itemHeight)* ((step+row_count-1)/row_count) + padding*((step+row_count-1)/row_count + 1);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDimens().height);
        lp.addRule(CENTER_HORIZONTAL);
        Root.addView(ml, lp);
    }

    public static SelectItemsBlockView createEpisodeButtonBlockView(final Context context, ArrayList<VideoItem.Media.Episode> episodes, int selectIndex, int maxVisible) {
        final SelectItemsBlockView filterView = new SelectItemsBlockView(context);

        RelativeLayout Root = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, filterView);
        MetroLayout ml = new MetroLayout(context);
        int step      = 0;
        int row_count = 4;

        int padding = (filterView.getDimens().width - row_count*filterView.getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_width))/(row_count+1);

        int itemHeight = 0;

        for(final DisplayItem.Media.Episode item: episodes) {
            View convertView = View.inflate(filterView.getContext(), R.layout.episode_item_layout, null);
            convertView.setBackgroundResource(R.drawable.com_btn_bg);

            TextView mFiter = (TextView) convertView.findViewById(R.id.channel_filter_btn);
            mFiter.setTextColor(selectIndex == step?filterView.getResources().getColor(R.color.orange):filterView.getResources().getColor(R.color.p_80_black));

            mFiter.setTag(item);
            mFiter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (filterView.getItemClick() != null) {
                        filterView.getItemClick().onClick(v);
                    }
                }
            });

            mFiter.setText(String.valueOf(item.episode));
            ml.addItemViewPort(convertView, LayoutConstant.linearlayout_episode_item, step % row_count, step / row_count, padding);
            step++;

            if(step >= maxVisible){
                break;
            }
        }

        if(itemHeight == 0){
            itemHeight = filterView.getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_height);
        }

        filterView.getDimens().height += (itemHeight)* ((step+row_count-1)/row_count) + padding*((step+row_count-1)/row_count + 1);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, filterView.getDimens().height);
        lp.addRule(CENTER_HORIZONTAL);
        Root.addView(ml, lp);

        return filterView;
    }

    public static SelectItemsBlockView createEpisodeListBlockView(final Context context, ArrayList<VideoItem.Media.Episode> episodes, int selectIndex, int maxVisible) {
        final SelectItemsBlockView filterView = new SelectItemsBlockView(context);
        RelativeLayout Root = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, filterView);
        MetroLayout ml = new MetroLayout(context);

        int padding    = 0;
        int itemHeight = 0;
        int size       = episodes.size();
        if(size > maxVisible)
            size = maxVisible;

        for(int i=0;i<size;i++){
            VideoItem.Media.Episode item = episodes.get(i);
            VarietyEpisode view = new VarietyEpisode(context);

            if(size == 1) {
                view.setBackgroundResource(R.drawable.com_item_bg_mid);
                view.findViewById(R.id.line).setVisibility(GONE);
            } else {
                if(i == 0) {
                    view.setBackgroundResource(R.drawable.com_item_bg_mid);
                }else if(i == size -1 ){
                    if(episodes.size() <= maxVisible) {
                        view.setBackgroundResource(R.drawable.com_item_bg_down);
                    }else {
                        view.setBackgroundResource(R.drawable.com_item_bg_mid);
                    }
                    view.findViewById(R.id.line).setVisibility(GONE);
                }
                else {
                    view.setBackgroundResource(R.drawable.com_item_bg_mid);
                }
            }

            view.setData(item, i == selectIndex);
            ml.addItemViewPort(view, LayoutConstant.linearlayout_episode_list_item,0, i , padding);

            view.setTag(item);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (filterView.getItemClick() != null) {
                        filterView.getItemClick().onClick(v);
                    }
                }
            });

            if(i+1 >= maxVisible){
                break;
            }
        }

        if(itemHeight == 0){
            itemHeight = context.getResources().getDimensionPixelSize(R.dimen.detail_variety_item_height);
        }

        filterView.getDimens().height += (itemHeight)* size ;

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, filterView.getDimens().height);
        lp.addRule(CENTER_HORIZONTAL);
        Root.addView(ml, lp);

        return filterView;
    }

    public ArrayList<String> getSelectedItems(){
        return selectedItems;
    }
    private ArrayList<String> selectedItems = new ArrayList<String>();

    public ArrayList<DisplayItem.Media.Episode> getSelectedEpisodeItems(){
        return selectedEpisodes;
    }
    private ArrayList<DisplayItem.Media.Episode> selectedEpisodes = new ArrayList<DisplayItem.Media.Episode>();

    private Handler mHandler = new Handler();
    public static SelectItemsBlockView createEpisodeSelectBlockView(final Context context, ArrayList<VideoItem.Media.Episode> episodes, int maxVisible) {
        final SelectItemsBlockView filterView = new SelectItemsBlockView(context);
        RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, filterView);

        final MetroLayout ml = new MetroLayout(context);
        int step      = 0;
        int row_count = 4;
        int padding = (filterView.getDimens().width - row_count*context.getResources().getDimensionPixelSize(R.dimen.filter_button_width))/(row_count+1);

        int itemHeight = 0;
        for(final VideoItem.Media.Episode episode: episodes) {
            final View convertView = View.inflate(context, R.layout.filter_check_item_layout, null);
            convertView.setTag(episode);
            convertView.setBackgroundResource(R.drawable.com_btn_bg);

            final TextView mFiter = (TextView) convertView.findViewById(R.id.channel_filter_btn);
            mFiter.setText(String.valueOf(episode.episode));
            ml.addItemViewPort(convertView, LayoutConstant.linearlayout_episode_item,step % row_count, step / row_count, padding);

            final ImageView imageView = (ImageView) convertView.findViewById(R.id.channel_filter_selected);
            filterView.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(iDataORM.existInPendingTask(imageView.getContext(), episode.id)){
                        imageView.setVisibility(VISIBLE);
                        convertView.setEnabled(false);
                        imageView.setImageDrawable(imageView.getResources().getDrawable(R.drawable.offline_already_pic));
                    }
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView.getVisibility() == VISIBLE) {
                        imageView.setVisibility(GONE);
                        mFiter.setTextColor(context.getResources().getColor(R.color.text_color_deep_dark));
                        mFiter.setBackgroundResource(0);

                        filterView.getSelectedEpisodeItems().remove((DisplayItem.Media.Episode) v.getTag());
                    } else {

                        filterView.getSelectedEpisodeItems().add((DisplayItem.Media.Episode) v.getTag());

                        imageView.setVisibility(VISIBLE);
                        mFiter.setTextColor(context.getResources().getColor(R.color.orange));
                        mFiter.setBackgroundResource(R.drawable.editable_title_com_btn_bg_s);
                    }

                    if (filterView.getItemClick() != null) {
                        filterView.getItemClick().onClick(v);
                    }
                }
            });
            step++;
        }

        if(itemHeight == 0){
            itemHeight = context.getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_height);
        }

        filterView.getDimens().height += (itemHeight)* ((step+row_count-1)/row_count) + padding*((step+row_count-1)/row_count);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, filterView.getDimens().height);
        lp.addRule(CENTER_HORIZONTAL);
        filterView.addView(ml, lp);

        return filterView;
    }

    public static SelectItemsBlockView createFilterSelectBlockView(final Context context, final DisplayItem.Filter.FilterType filterType, int maxVisible) {
        final SelectItemsBlockView filterView = new SelectItemsBlockView(context);
        RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, filterView);

        final MetroLayout ml = new MetroLayout(context);
        int step      = 0;
        int row_count = 4;
        int padding = (filterView.getDimens().width - row_count*context.getResources().getDimensionPixelSize(R.dimen.filter_button_width))/(row_count+1);

        int itemHeight = 0;
        for(String title: filterType.tags) {
            View convertView = View.inflate(context, R.layout.filter_check_item_layout, null);
            convertView.setTag(title + "("+filterType.type+")");
            convertView.setBackgroundResource(R.drawable.com_btn_bg);

            final TextView mFiter = (TextView) convertView.findViewById(R.id.channel_filter_btn);
            mFiter.setText(title);
            ml.addItemViewPort(convertView, LayoutConstant.linearlayout_episode_item,step % row_count, step / row_count, padding);

            final ImageView imageView = (ImageView) convertView.findViewById(R.id.channel_filter_selected);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (imageView.getVisibility() == VISIBLE) {
                        imageView.setVisibility(GONE);
                        mFiter.setTextColor(context.getResources().getColor(R.color.text_color_deep_dark));
                        mFiter.setBackgroundResource(0);

                        filterView.getSelectedItems().remove((String) v.getTag());
                    } else {

                        filterView.getSelectedItems().add((String) v.getTag());

                        imageView.setVisibility(VISIBLE);
                        mFiter.setTextColor(context.getResources().getColor(R.color.orange));
                        mFiter.setBackgroundResource(R.drawable.editable_title_com_btn_bg_s);

                        //remove others selected button
                        for(int i=0;i<ml.getChildCount();i++){
                            View view = ml.getChildAt(i);
                            if(view.findViewById(R.id.channel_filter_selected) != null){
                                ImageView iv = (ImageView) view.findViewById(R.id.channel_filter_selected);
                                if(iv.getVisibility() == VISIBLE && view != v){
                                    //remove the select
                                    iv.setVisibility(GONE);
                                    TextView tv = (TextView) view.findViewById(R.id.channel_filter_btn);
                                    tv.setTextColor(context.getResources().getColor(R.color.text_color_deep_dark));
                                    tv.setBackgroundResource(0);
                                    filterView.getSelectedItems().remove((String) v.getTag());
                                }
                            }
                        }
                    }

                    if (filterView.getItemClick() != null) {
                        filterView.getItemClick().onClick(v);
                    }
                }
            });
            step++;
        }

        if(itemHeight == 0){
            itemHeight = context.getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_height);
        }

        filterView.getDimens().height += (itemHeight)* ((step+row_count-1)/row_count) + padding*((step+row_count-1)/row_count);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, filterView.getDimens().height);
        lp.addRule(CENTER_HORIZONTAL);
        filterView.addView(ml, lp);

        return filterView;
    }

    public static SelectItemsBlockView createSearchBlockView(Context context,ArrayList<DisplayItem>items, int maxVisible) {
        final SelectItemsBlockView filterView = new SelectItemsBlockView(context);
        RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, filterView);

        MetroLayout ml = new MetroLayout(context);
        int step      = 0;
        int row_count = 4;


        int padding = (filterView.getDimens().width - row_count*context.getResources().getDimensionPixelSize(R.dimen.filter_button_width))/(row_count+1);

        int itemHeight = 0;
        int addedWidth = 0;
        int position     = 0;
        int row_position = 0;
        for(final DisplayItem item: items) {
            View convertView = View.inflate(context, R.layout.search_item_layout, null);
            convertView.setBackgroundResource(R.drawable.com_btn_bg);

            final TextView mFiter = (TextView) convertView.findViewById(R.id.search_keyword_btn);
            mFiter.setText(item.title);

            Paint paint = mFiter.getPaint();
            int itemWidth = (int) paint.measureText(item.title) + filterView.getTextPadding() * 2;
            addedWidth += itemWidth + padding;
            if(addedWidth > filterView.getDimens().width){
                position   = 0;
                row_position++;
                addedWidth = itemWidth + padding;
            }

            mFiter.setWidth(itemWidth);
            mFiter.setTag(item);

            ml.addItemViewPort(convertView, LayoutConstant.linearlayout_search_item, position++, row_position, padding);

            mFiter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(filterView.getItemClick() != null){
                        filterView.getItemClick().onClick(v);
                    }
                }
            });
            step++;
        }

        if(itemHeight == 0){
            itemHeight = context.getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_height);
        }

        filterView.getDimens().height += (itemHeight)* ((step+row_count-1)/row_count) + padding*((step+row_count-1)/row_count);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, filterView.getDimens().height);
        lp.addRule(CENTER_HORIZONTAL);
        filterView.addView(ml, lp);

        return filterView;
    }

    public int getTextPadding(){
        if(textPadding == -1){
            textPadding = getResources().getDimensionPixelSize(R.dimen.size_15);
        }

        return textPadding;
    }

    private int textPadding = -1;
    private DimensHelper.Dimens mDimens;
    @Override
    public DimensHelper.Dimens getDimens() {
        if(mDimens == null){
            mDimens = new DimensHelper.Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = 0;
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {
    }

    @Override
    public void unbindDrawables(View view) {

    }

    public static class VarietyEpisode extends RelativeLayout {
        //UI
        private View mPoster;
        private TextView mDate;
        private TextView mName;

        private int mColorNormal;
        private int mColorSelected;

        //data
        private VideoItem.Media.Episode mItem;

        public VarietyEpisode(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }
        public VarietyEpisode(Context context) {
            this(context, null, 0);
        }

        public VarietyEpisode(Context context, AttributeSet attrs, int uiStyle) {
            super(context, attrs, uiStyle);
            init();
        }


        public VideoItem.Media.Episode getData() {
            return mItem;
        }

        public void setData(VideoItem.Media.Episode item, boolean selected) {
            this.mItem = item;
            refresh(selected);
        }

        //init
        private void init() {
            View.inflate(getContext(), R.layout.detail_ep_item_variety, this);
            mColorNormal   = getResources().getColor(R.color.p_80_black);
            mColorSelected = getResources().getColor(R.color.orange);
            mPoster = findViewById(R.id.detail_variety_item_poster);
            mDate = (TextView) findViewById(R.id.detail_variety_item_date);
            mName = (TextView) findViewById(R.id.detail_variety_item_name);
        }

        //packaged method
        private void refresh(boolean selected) {
            if(mItem == null) {
                return;
            }
            mDate.setText(mItem.date);
            mName.setText(mItem.name);

            mPoster.setSelected(selected);
            if(selected) {
                mName.setTextColor(mColorSelected);
                mDate.setTextColor(mColorSelected);
            } else {
                mName.setTextColor(mColorNormal);
                mDate.setTextColor(mColorNormal);
            }
        }
    }
}
