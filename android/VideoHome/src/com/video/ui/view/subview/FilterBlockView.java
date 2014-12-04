package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.MetroLayout;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 12/1/14.
 */
public class FilterBlockView  extends BaseCardView implements DimensHelper {
    public FilterBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private MetroLayout ml;
    private ArrayList<String>filtes;
    public static final int Filter_Type  = LayoutConstant.linearlayout_filter;
    public static final int Episode_Type = LayoutConstant.linearlayout_episode;

    private OnClickListener mItemClick;
    public void setOnClickListener(OnClickListener itemClick){
        mItemClick = itemClick;
    }

    public FilterBlockView(Context context, ArrayList<String> filtes, int uiType) {
        super(context, null, 0);
        this.filtes = filtes;
        int selectIndex = 2;

        RelativeLayout Root = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, this);

        if(uiType != Episode_Type) {
            Root.setBackgroundResource(R.drawable.com_block_n);
        }

        ml = new MetroLayout(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL);
        int step      = 0;
        int row_count = 4;


        int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.filter_button_width))/(row_count+1);
        if(uiType == LayoutConstant.linearlayout_episode) {
            padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_width))/(row_count+1);
        }
        int itemHeight = 0;
        for(String item: filtes) {
            View convertView = null;
            if(uiType == LayoutConstant.linearlayout_filter) {
                convertView = View.inflate(getContext(), R.layout.filter_item_layout, null);
                convertView.setBackgroundResource(R.drawable.editable_title_com_btn_bg);
            }
            else if(uiType == LayoutConstant.linearlayout_episode) {
                convertView = View.inflate(getContext(), R.layout.episode_item_layout, null);
                convertView.setBackgroundResource(R.drawable.com_btn_bg);
            }

            TextView mFiter = (TextView) convertView.findViewById(R.id.channel_filter_btn);
            mFiter.setText(item);

            if(uiType == LayoutConstant.linearlayout_episode) {
                if (selectIndex == step)
                    mFiter.setTextColor(getResources().getColor(R.color.orange));
                else
                    mFiter.setTextColor(getResources().getColor(R.color.p_80_black));
            }

            ml.addItemViewPort(convertView, uiType == LayoutConstant.linearlayout_filter?
                                            LayoutConstant.linearlayout_filter_item:LayoutConstant.linearlayout_episode_item,
                    step % row_count, step / row_count, padding);

            step++;

            if (step != filtes.size() || uiType == Episode_Type){
                mFiter.setCompoundDrawables(null, null, null, null);
            }

            mFiter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClick != null){
                        mItemClick.onClick(v);
                    }
                }
            });

            if(itemHeight == 0){
                itemHeight =  getResources().getDimensionPixelSize(R.dimen.size_74);
            }
        }

        getDimens().height += (itemHeight)* ((step+row_count-1)/row_count) + padding*((step+row_count-1)/row_count + 1);
        Root.addView(ml, lp);
    }

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
}
