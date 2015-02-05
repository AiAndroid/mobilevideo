package com.video.ui.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.video.ui.R;
import com.video.ui.view.subview.DimensHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MetroLayout extends FrameLayout implements View.OnFocusChangeListener{
    public static final int Vertical   = 0; //occupy two vertical cells
    public static final int Horizontal = 1; //occupy two horizontal cells
    public static final int Normal     = 2; //square rectangle
    public static final int HorizontalMatchWith = 3; //occupy full horizontal cells

    
	Context mContext;
	int[] rowOffset = new int[2];
	static  int DIVIDE_SIZE = 6;
    static  int paddingLeft = 0;
    static  int twoItemspaddingLeft = 0;
	boolean mMirror = true;
	AnimatorSet mScaleAnimator;
	List<WeakReference<View>> mViewList = new ArrayList<WeakReference<View>>();

    View mLeftView;
    View mRightView;
	
	float mDensityScale = 1.0f;
    private static int ITEM_V_WIDTH  = -1;
    private static int ITEM_V_HEIGHT = -1;
    private static int ITEM_H_WIDTH  = -1;
    private static int ITEM_H_HEIGHT  = -1;
    private static int ITEM_NORMAL_SIZE = -1;
    private static int mirror_ref_height= -1;
    int orientation = Configuration.ORIENTATION_LANDSCAPE;


    public class Item{
		public Item( int type, int row){
			mType = type;
			mRow = row;
		}
		public int mType;
		public int mRow;
	}
	
	public MetroLayout(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	public MetroLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}


	private void init(){
        if(ITEM_V_WIDTH == -1){
            DIVIDE_SIZE   = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
            ITEM_V_WIDTH  = getResources().getDimensionPixelSize(R.dimen.ITEM_V_WIDTH);
            ITEM_V_HEIGHT = getResources().getDimensionPixelSize(R.dimen.ITEM_V_HEIGHT);
            ITEM_H_WIDTH  = getResources().getDimensionPixelSize(R.dimen.ITEM_H_WIDTH);
            ITEM_H_HEIGHT = getResources().getDimensionPixelSize(R.dimen.ITEM_H_HEIGHT);
            ITEM_NORMAL_SIZE = getResources().getDimensionPixelSize(R.dimen.ITEM_NORMAL_SIZE);
            mirror_ref_height = getResources().getDimensionPixelSize(R.dimen.mirror_ref_height);

            paddingLeft = (getResources().getDisplayMetrics().widthPixels - getResources().getDimensionPixelSize(R.dimen.media_banner_width))/2;
            twoItemspaddingLeft = (getResources().getDisplayMetrics().widthPixels - 2*getResources().getDimensionPixelSize(R.dimen.feature_media_view_width))/3;
        }

		mDensityScale = 1;//mContext.getResources().getDisplayMetrics().densityDpi/320.0f;
        setClipChildren(false);
        setClipToPadding(false);

        orientation = getResources().getConfiguration().orientation;
	}

	public View getItemView(int index){
		if(index>=mViewList.size()) return null;
		return mViewList.get(index).get();
	}

    public View addItemView(View child, int celltype , int row){
        return addItemView(child, celltype , row, DIVIDE_SIZE);
    }

    public void clearItems(){
        removeAllViews();
        rowOffset[1]=rowOffset[0]=0;
        mViewList.clear();
        mLeftView = null;
        mRightView = null;
    }

    public View addItemViewPort(View child, int celltype , int x, int y){
        return addItemViewPort(child, celltype , x, y, DIVIDE_SIZE);
    }
    public View addItemViewPort(View child, int celltype , int x, int y, int padding){
        if(mLeftView==null){
            mLeftView = child;
        }

        child.setFocusable(true);
        child.setOnFocusChangeListener(this);
        LayoutParams flp;
        mViewList.add(new WeakReference<View>(child));
        View result = child;
        switch(celltype){
            case LayoutConstant.linearlayout_single_desc: {
                int width = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
                flp = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);

                flp.leftMargin = getPaddingLeft() + paddingLeft;
                flp.topMargin = getPaddingTop() + rowOffset[0] + padding * (y + 1);
                flp.rightMargin = getPaddingRight() + paddingLeft;
                child.setFocusable(true);
                addView(child, flp);
                break;
            }
            case LayoutConstant.single_view: {
                int width = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
                flp = new LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);

                flp.leftMargin = getPaddingLeft() + paddingLeft;
                flp.topMargin = getPaddingTop() + rowOffset[0] + padding * (y + 1);
                flp.rightMargin = getPaddingRight() + paddingLeft;
                child.setFocusable(true);
                addView(child, flp);
                //rowOffset[0] += height;
                break;
            }
            case LayoutConstant.grid_item_selection:{
                int height = getResources().getDimensionPixelSize(R.dimen.feature_media_view_height);
                int width = getResources().getDimensionPixelSize(R.dimen.feature_media_view_width);
                flp = new LayoutParams(width, height);
                if (child instanceof DimensHelper) {
                    height = ((DimensHelper) child).getDimens().height;
                    flp = new LayoutParams(((DimensHelper) child).getDimens().width, height);
                }
                flp.leftMargin = getPaddingLeft() + (width + padding)*x + padding;
                flp.topMargin = getPaddingTop() + height*y + padding* (y); //no need one more
                child.setFocusable(true);
                addView(child, flp);
                rowOffset[0] += height;
                break;
            }
            case LayoutConstant.linearlayout_filter_item:{
                int height = getResources().getDimensionPixelSize(R.dimen.size_74);
                int width = getResources().getDimensionPixelSize(R.dimen.filter_button_width);
                flp = new LayoutParams(width, height);
                flp.leftMargin = getPaddingLeft() + (width + padding)*x + padding;
                flp.topMargin = getPaddingTop() + height*y + padding* (y+1); //no need one more
                child.setFocusable(true);
                addView(child, flp);
                rowOffset[0] += height;
                break;
            }
            case LayoutConstant.linearlayout_episode_item: {
                int height = getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_height);
                int width = getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_width);
                flp = new LayoutParams(width, height);
                flp.leftMargin = getPaddingLeft() + (width + padding) * x + padding;
                flp.topMargin = getPaddingTop() + height * y + padding * (y + 1); //no need one more
                child.setFocusable(true);
                addView(child, flp);
                rowOffset[0] += height;
                break;
            }
            case LayoutConstant.linearlayout_search_item: {
                //reset to first position
                if(x == 0)rowOffset[1] = 0;

                int height = getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_height);
                int width  = ((TextView)child).getMaxWidth();//getResources().getDimensionPixelSize(R.dimen.detail_ep_multy_btn_width);
                flp = new LayoutParams(width, height);
                flp.leftMargin = getPaddingLeft() + rowOffset[1] + (padding) * x + padding;
                flp.topMargin = getPaddingTop() + height * y + padding * (y + 1); //no need one more
                child.setFocusable(true);
                addView(child, flp);
                rowOffset[0] += height;
                rowOffset[1] += width;
                break;
            }
            case LayoutConstant.linearlayout_episode_list_item:{
                int height = getResources().getDimensionPixelSize(R.dimen.detail_variety_item_height);
                int width = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
                flp = new LayoutParams(width, height);
                flp.leftMargin = getPaddingLeft() + padding;
                flp.topMargin = getPaddingTop() + height * y ; //no padding
                child.setFocusable(true);
                addView(child, flp);
                rowOffset[0] += height;
                break;
            }
            case LayoutConstant.list_category_land:
            case LayoutConstant.imageswitcher:
            case LayoutConstant.linearlayout_top:
            case LayoutConstant.linearlayout_left:
            case LayoutConstant.linearlayout_poster:
            case LayoutConstant.linearlayout_land:
            case LayoutConstant.linearlayout_none:
            case LayoutConstant.list_rich_header:
            case LayoutConstant.grid_block_selection:
            case LayoutConstant.grid_media_port:
            case LayoutConstant.grid_media_land:
            case LayoutConstant.grid_media_port_title:
            case LayoutConstant.grid_media_land_title:
            case LayoutConstant.tabs_horizontal:
            case LayoutConstant.linearlayout_filter:
            {
                int paddingStart = paddingLeft;
                int height = getResources().getDimensionPixelSize(R.dimen.media_banner_height);
                int width = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
                flp = new LayoutParams(width, height);
                if (child instanceof DimensHelper) {
                    width  = ((DimensHelper) child).getDimens().width;
                    height = ((DimensHelper) child).getDimens().height;

                    paddingStart =  (getResources().getDisplayMetrics().widthPixels - width)/2;
                    flp = new LayoutParams(((DimensHelper) child).getDimens().width, height);
                }

                flp.leftMargin = getPaddingLeft() + paddingStart;
                flp.topMargin = getPaddingTop() + rowOffset[0] + padding * (y+1);
                flp.rightMargin = getPaddingRight() + paddingStart;
                child.setFocusable(true);
                addView(child, flp);
                rowOffset[0] += height;
                break;
            }
            case LayoutConstant.block_channel:
            case LayoutConstant.block_sub_channel:
            {
                int height = getResources().getDimensionPixelSize(R.dimen.media_banner_height);
                int width = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
                flp = new LayoutParams(width, height);
                if (child instanceof DimensHelper) {
                    flp = new LayoutParams(((DimensHelper) child).getDimens().width, ((DimensHelper) child).getDimens().height);
                    height = ((DimensHelper) child).getDimens().height;
                }
                flp.leftMargin = getPaddingLeft() + paddingLeft;
                flp.topMargin = getPaddingTop() + rowOffset[0] + padding * (y+1);
                flp.rightMargin = getPaddingRight() + paddingLeft;
                child.setFocusable(true);
                addView(child, flp);
                child.forceLayout();
                rowOffset[0] += height;
                break;
            }
            case HorizontalMatchWith:
            {
                int height = getResources().getDimensionPixelSize(R.dimen.media_banner_height);
                int width = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
                flp = new LayoutParams(width, height);
                if (child instanceof DimensHelper) {
                    flp = new LayoutParams(((DimensHelper) child).getDimens().width, ((DimensHelper) child).getDimens().height);
                }
                flp.leftMargin = getPaddingLeft() + paddingLeft;
                flp.topMargin = getPaddingTop() + rowOffset[0] + padding * (y+1);
                flp.rightMargin = getPaddingRight() + paddingLeft;
                child.setFocusable(true);
                addView(child, flp);
                rowOffset[0] += ITEM_V_HEIGHT;
                break;
            }
            case Horizontal:
                flp = new LayoutParams((int)(ITEM_H_WIDTH*mDensityScale), (int)(ITEM_H_HEIGHT*mDensityScale));
                flp.leftMargin = getPaddingLeft()+x*ITEM_NORMAL_SIZE+padding*x;
                flp.topMargin = getPaddingTop()+ITEM_NORMAL_SIZE*y+padding*y;
                flp.rightMargin = getPaddingRight();
                child.setFocusable(true);
                child.setOnFocusChangeListener(this);
                child.setTag(R.integer.tag_view_postion, 0);
                addView(child,flp);
                rowOffset[0]+=ITEM_H_WIDTH*mDensityScale+padding;
                break;
        }
        return result;
    }

    public View addItemView(View child, int celltype , int row, int padding){
        if(mLeftView==null){
            mLeftView = child;
        }
        if(row==0) {
            mRightView = child;
        }
		child.setFocusable(true);
		child.setOnFocusChangeListener(this);
		LayoutParams flp;
		mViewList.add(new WeakReference<View>(child));
		View result = child;
		switch(celltype){
		case Vertical:
			flp = new LayoutParams(
					(int)(ITEM_V_WIDTH*mDensityScale),
					(int)(ITEM_V_HEIGHT*mDensityScale));
			{
				child.setFocusable(true);
				child.setOnFocusChangeListener(this);
                child.setTag(R.integer.tag_view_postion, 0);
                flp.leftMargin = rowOffset[0];
    			flp.topMargin = getPaddingTop();
    			flp.rightMargin = getPaddingRight();
				addView(child, flp);
			}
			rowOffset[0]+=ITEM_V_WIDTH*mDensityScale+padding;
			rowOffset[1]=rowOffset[0];
			break;
		case Horizontal:
			flp = new LayoutParams((int)(ITEM_H_WIDTH*mDensityScale), (int)(ITEM_H_HEIGHT*mDensityScale));
			switch(row){
			case 0:
				flp.leftMargin = rowOffset[0];
				flp.topMargin = getPaddingTop();
				flp.rightMargin = getPaddingRight();
				child.setFocusable(true);
				child.setOnFocusChangeListener(this);
                child.setTag(R.integer.tag_view_postion, 0);
				addView(child,flp);
				rowOffset[0]+=ITEM_H_WIDTH*mDensityScale+padding;
				break;
			case 1:
				{
					child.setFocusable(true);
					child.setOnFocusChangeListener(this);
                    child.setTag(R.integer.tag_view_postion, 1);
					flp.leftMargin = rowOffset[1];
					flp.topMargin = getPaddingTop();
					flp.rightMargin = getPaddingRight();
					flp.topMargin += ITEM_NORMAL_SIZE*mDensityScale+padding;
					addView(child,flp);
				}
				rowOffset[1]+=ITEM_H_WIDTH*mDensityScale+padding;
				break;
			}
			break;
		case Normal:
			flp = new LayoutParams(
					(int)(ITEM_NORMAL_SIZE*mDensityScale),
					(int)(ITEM_NORMAL_SIZE*mDensityScale));
			switch(row){
			case 0:
				flp.leftMargin = rowOffset[0];
				child.setFocusable(true);
				child.setOnFocusChangeListener(this);
                child.setTag(R.integer.tag_view_postion, 0);
    			flp.topMargin = getPaddingTop();
    			flp.rightMargin = getPaddingRight();
				addView(child,flp);
				rowOffset[0]+=ITEM_NORMAL_SIZE*mDensityScale+padding;
				break;
			case 1:
				{
					child.setFocusable(true);
                    child.setTag(R.integer.tag_view_postion, 1);
					child.setOnFocusChangeListener(this);
					flp.leftMargin = rowOffset[1];
					flp.topMargin = getPaddingTop();
					flp.rightMargin = getPaddingRight();
					flp.topMargin += ITEM_NORMAL_SIZE*mDensityScale+padding;
					addView(child,flp);
				}
				rowOffset[1]+=ITEM_NORMAL_SIZE*mDensityScale+padding;
				break;
			}
			break;
		}
		return result;
	}

    private View lastFocusedView;

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (lastFocusedView!=null&&lastFocusedView.requestFocus(direction, previouslyFocusedRect)) {
            return true;
        }

        int index;
        int increment;
        int end;
        int count = this.getChildCount();
        if ((direction & FOCUS_FORWARD) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }

        for (int i = index; i != end; i += increment) {
            View child = this.getChildAt(i);
            {
                if (child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child,focused);
    }

    public void onFocusChange(final View v, boolean hasFocus) {
        if (mScaleAnimator != null) mScaleAnimator.end();
        {
            if (hasFocus) {
                lastFocusedView = v;
                bringChildToFront(v);
                invalidate();
                ObjectAnimator animX = ObjectAnimator.ofFloat(v, "ScaleX",
                        new float[]{1.0F, 1.1F}).setDuration(200);
                ObjectAnimator animY = ObjectAnimator.ofFloat(v, "ScaleY",
                        new float[]{1.0F, 1.1F}).setDuration(200);
                mScaleAnimator = new AnimatorSet();
                mScaleAnimator.playTogether(new Animator[]{animX, animY});
                mScaleAnimator.start();

                //v.setScaleX(1.1f);
                //v.setScaleY(1.1f);
            } else {
                v.setScaleX(1.0f);
                v.setScaleY(1.0f);
            }
        }

    }
}
