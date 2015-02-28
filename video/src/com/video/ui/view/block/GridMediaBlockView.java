package com.video.ui.view.block;

import android.content.Context;
import android.text.TextUtils;
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
import com.video.ui.view.LinearFrame;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Observer;

/**
 * Created by liuhuadong on 11/27/14.
 */
public class GridMediaBlockView<T> extends LinearBaseCardView implements DimensHelper{
    public GridMediaBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GridMediaBlockView(Context context, Block<T> block, Object tag){
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        initUI(context, block, tag);
    }

    private int row_count = 0;
    private int padding;
    private int item_padding;
    private int width;
    private int height;
    private int imageWidth;
    private int imageHeight;
    private int res_id;
    private Block<T> content;
    private View root;

    private int secondHeight;

    private ArrayList<WeakReference<MediaItemView>> allMesiaView = new ArrayList<WeakReference<MediaItemView>>();
    public ArrayList<WeakReference<MediaItemView>> getChildMediaViews(){
        return allMesiaView;
    }

    private void initDimens(Block<T> block){
        content = block;
        item_padding = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
        if(block.ui_type.id == LayoutConstant.grid_media_port || block.ui_type.id == LayoutConstant.grid_media_port_title ) {
            row_count = block.ui_type.row_count;
            if(row_count == 0) row_count = 3;

            res_id = R.layout.tab_media_port;
            width  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width);
            height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_height);
            imageWidth  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_image_width);
            imageHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_image_height);
            padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width))/(row_count+1);

            secondHeight = getResources().getDimensionPixelSize(R.dimen.size_81);

        }else if(block.ui_type.id == LayoutConstant.grid_media_land || block.ui_type.id == LayoutConstant.grid_media_land_title) {
            row_count = block.ui_type.row_count;
            if (row_count == 0)row_count = 2;

            secondHeight = getResources().getDimensionPixelSize(R.dimen.size_84);

            res_id = R.layout.tab_media_hor;
            width = getResources().getDimensionPixelSize(R.dimen.channel_media_view_width);
            height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_height);
            imageWidth  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_image_width);
            imageHeight = getResources().getDimensionPixelSize(R.dimen.channel_media_view_image_height);
            padding = (getDimens().width - row_count * getResources().getDimensionPixelSize(R.dimen.channel_media_view_width)) / (row_count + 1);
        }
    }

    private void initUI(Context context, Block<T> block, Object tag){
        initDimens(block);
        content = block;

        root = View.inflate(getContext(), R.layout.quick_navigation, this);
        allMesiaView.clear();
        //LayoutParams flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //addView(root, flp);
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);
        for(int step=0;step<block.items.size();step++) {
            final DisplayItem item = (DisplayItem) block.items.get(step);

            MediaItemView meida = new MediaItemView(context, item, res_id, imageWidth, imageHeight, secondHeight);
            allMesiaView.add(new WeakReference<MediaItemView>(meida));

            FrameLayout.LayoutParams itemflp = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height);
            itemflp.leftMargin = getPaddingLeft() + (width*(step%row_count) ) + padding*(step%row_count + 1);
            itemflp.topMargin  = getPaddingTop()  + (height*(step/row_count)) + item_padding*(step/row_count + 1);

            mMetroLayout.addView(meida,itemflp);
        }

        int lines = (block.items.size()+row_count -1)/row_count;
        getDimens().height += (height + item_padding)*lines;
    }

    private Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.rank_banner_width);
            mDimens.height = 0;
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);

        for(int i=0;i<mMetroLayout.getChildCount();i++){
            View view = mMetroLayout.getChildAt(i);
            ImageView image = (ImageView)view.findViewById(R.id.poster);
            if(image != null) {
                DisplayItem item = (DisplayItem) content.items.get(i);
                Picasso.with(getContext()).load(item.images.get("poster").url).resize(imageWidth, imageHeight).transform(new BaseCardView.BlendImageWithCover(getContext(), res_id == R.layout.tab_media_port?true:false)).into(image);
            }
        }
    }

    @Override
    public void unbindDrawables(View view) {

    }

    public static class MediaItemView extends LinearLayout {

        public MediaItemView(Context context, DisplayItem item, int res_id, int imageWidth, int imageHeight, int secondHeight) {
            super(context);

            mItem = item;
            setOrientation(VERTICAL);
            init(res_id, item, imageWidth, imageHeight, secondHeight);
        }

        private DisplayItem mItem;
        private void init(int res_id, final DisplayItem item, int imageWidth, int imageHeight, int secondHeight){
            ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(res_id, this);
            ImageView image = (ImageView)meida.findViewById(R.id.poster);
            Picasso.with(getContext()).load(item.images.get("poster").url).resize(imageWidth, imageHeight).transform(new BaseCardView.BlendImageWithCover(getContext(), res_id == R.layout.tab_media_port?true:false)).into(image);

            TextView title = (TextView)meida.findViewById(R.id.media_title);
            TextView desc = (TextView)meida.findViewById(R.id.descrip);
            if(TextUtils.isEmpty(item.sub_title)){
                title.setSingleLine(false);
                title.setHeight(getResources().getDimensionPixelSize(R.dimen.size_76) + secondHeight);
                desc.setVisibility(GONE);
            }else {
                desc.setVisibility(VISIBLE);
                title.setSingleLine(true);
                title.setHeight(getResources().getDimensionPixelSize(R.dimen.size_76));
            }
            title.setText(item.title);
            desc.setText(item.sub_title);

            meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.settings != null && "1".equals(item.settings.get(DisplayItem.Settings.edit_mode))){
                        selectItem();
                    }else {
                        BaseCardView.launcherAction(getContext(), item);
                    }
                }
            });

            setHintText(meida, item);

            if(item.settings != null && "1".equals(item.settings.get(DisplayItem.Settings.edit_mode))){
                MediaEditView mev = (MediaEditView) meida.findViewById(R.id.edit_mode);
                mev.setVisibility(VISIBLE);
                mev.setInEditMode(true);
            }
        }

        public void selectItem(){
            MediaEditView mev = (MediaEditView) findViewById(R.id.edit_mode);
            mev.setMediaInfo(mItem);

            if(mSelectListener != null){
                mSelectListener.onSelected(this, mItem,  "1".equals(mItem.settings.get(DisplayItem.Settings.selected)));
            }
        }

        private OnItemSelectListener mSelectListener;
        public void setOnItemSelectListener(OnItemSelectListener listener){
            mSelectListener = listener;
        }
        public interface OnItemSelectListener{
            public void onSelected(View view, DisplayItem item, boolean selected);
        }
    }
}
