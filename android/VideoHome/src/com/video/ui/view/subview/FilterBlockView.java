package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    public FilterBlockView(Context context, ArrayList<String> filtes) {
        super(context, null, 0);
        this.filtes = filtes;

        RelativeLayout Root = (RelativeLayout) View.inflate(context, R.layout.relative_layout_container, this);

        Root.setBackgroundResource(R.drawable.com_block_n);
        ml = new MetroLayout(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL);
        int step      = 0;
        int row_count = 4;


        int padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.filter_button_width))/(row_count+1);
        int itemHeight = 0;
        for(String item: filtes) {
            View convertView = View.inflate(getContext(), R.layout.filter_item_layout, null);

            TextView mFiter = (TextView) convertView.findViewById(R.id.channel_filter_btn);
            convertView.setBackgroundResource(R.drawable.editable_title_com_btn_bg);
            mFiter.setText(item);

            ml.addItemViewPort(convertView, LayoutConstant.linearlayout_filter_item, step % row_count, step / row_count, padding);
            step++;

            if (step == filtes.size()){
                Drawable filDrawble = getResources().getDrawable(R.drawable.detail_screening_icon);
                filDrawble.setBounds(0, 0, filDrawble.getMinimumWidth(), filDrawble.getMinimumHeight());
                mFiter.setCompoundDrawables(filDrawble, null, null, null);
                mFiter.setText(item);
            }

            if(itemHeight == 0){
                itemHeight =  getResources().getDimensionPixelSize(R.dimen.size_74);
            }
        }

        getDimens().height += (itemHeight)* ((step+1)/row_count) + padding*((step+1)/row_count + 1);
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
