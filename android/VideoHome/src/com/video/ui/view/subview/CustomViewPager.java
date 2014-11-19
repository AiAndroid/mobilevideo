package com.video.ui.view.subview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class CustomViewPager  extends ViewPager implements View.OnTouchListener {
    public CustomViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public CustomViewPager(final Context context) {
        this(context, null);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                ((ViewParent) v.getParent()).requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                ((ViewParent) v.getParent()).requestDisallowInterceptTouchEvent(false);
                break;
        }
        return v.onTouchEvent(event);
    }
}
