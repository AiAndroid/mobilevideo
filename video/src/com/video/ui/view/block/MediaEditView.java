/**
 *  Copyright(c) 2014 XiaoMi TV Group
 *    
 *  MediaEditView.java
 *
 *  @author tianli(tianli@xiaomi.com)
 *
 *  2014-11-22
 */
package com.video.ui.view.block;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

/**
 * @author tianli
 *
 */
public class MediaEditView extends FrameLayout {

    private ImageView mSelector;
    
    protected boolean mIsInEditMode;
    
    public MediaEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MediaEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaEditView(Context context) {
        this(context, null, 0);
    }
    
    private void init(){
        mSelector = new ImageView(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        mSelector.setLayoutParams(params);
        mSelector.setImageResource(R.drawable.media_view_image_status);
        addView(mSelector);
        setBackgroundResource(R.drawable.media_view_image_selector_border);
        setPadding(0, 0, 0, 0);
    }

    public void setInEditMode(boolean inEditMode){
        mIsInEditMode = inEditMode;
        if(mIsInEditMode){
            setVisibility(View.VISIBLE);
        }else {
            setVisibility(View.INVISIBLE);
        }
    }

    public void setMediaInfo(DisplayItem mediaInfo){
        if(mediaInfo != null && "1".equals(mediaInfo.settings.get(DisplayItem.Settings.selected))){
            setSelected(true);
            mSelector.setSelected(true);
        }else{
            setSelected(false);
            mSelector.setSelected(false);
        }
    }
    
    public void switchSelectState(DisplayItem mediaInfo){
        if(mediaInfo != null && "0".equals(mediaInfo.settings.get(DisplayItem.Settings.selected))){
            setSelected(true);
            mSelector.setSelected(true);
            mediaInfo.settings.put(DisplayItem.Settings.selected, "1");
        }else{
            setSelected(false);
            mSelector.setSelected(false);
            mediaInfo.settings.put(DisplayItem.Settings.selected, "0");
        }
    }
}
