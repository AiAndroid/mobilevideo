package com.video.ui.view.detail;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

import java.util.ArrayList;

public class SelectSourcePopup extends RelativeLayout {

    public interface OnShowHideListener<T> {
        public void onShow(T t);
        public void onHide(T t);
    }

	private Context mContext;
	private ListView mListView;
	private SelectSourceAdapter mAdapter;
	private ArrayList<DisplayItem.Media.CP> mOnlineEpisodeSources;
	private OnSourceSelectListener mListener;

	
	//*********************************
    protected LinearLayout mContentView;
    private boolean mIsShowing = false;
    private Animator mAnimator;
    private OnShowHideListener<SelectSourcePopup> mShowHideListener;
  //*********************************
	public SelectSourcePopup(Context context, ArrayList<DisplayItem.Media.CP> mSourceList) {
		super(context);
		mContext = context;
		mOnlineEpisodeSources = mSourceList;
		init();
	}

	private void init(){
        int width = getPopupWidth();
        mContentView = new LinearLayout(getContext());
        LayoutParams p = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        mContentView.setLayoutParams(p);
        mContentView.setGravity(Gravity.CENTER_VERTICAL);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        addView(mContentView);
		
		mListView = new ListView(mContext);
		mAdapter = new SelectSourceAdapter(getContext(), mOnlineEpisodeSources);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mOnIemClickListener);
		mListView.setDivider(mContext.getResources().getDrawable(R.drawable.com_3_black));
		mListView.setDividerHeight(1);
		mContentView.setBackgroundResource(R.drawable.mediadetail_selectsource);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mListView.setLayoutParams(params);
		setFocusable(true);
		mContentView.addView(mListView);
		setOnClickListener(mDismissClick);
	}
	
	public void setOnSourceSelectListener(OnSourceSelectListener l) {
		mListener = l;
	}
    
	public void setCurrentSource(DisplayItem.Media.CP source) {
		if(mAdapter != null){
			mAdapter.setCurrentSource(source);			
		}
	}
	
	public DisplayItem.Media.CP getCurrentSource() {
		if(mAdapter != null){
			return mAdapter.getCurrentSource();		
		}
		return null;
	}
	
	public static interface OnSourceSelectListener {
		public void onSourceSelect(int position, DisplayItem.Media.CP source);
	}
	
	private OnItemClickListener mOnIemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(mOnlineEpisodeSources == null || mOnlineEpisodeSources.size() == 0){
				return;
			}
			DisplayItem.Media.CP episodeSource = mOnlineEpisodeSources.get(position);
			if(getCurrentSource().cp.equals(episodeSource.cp)){
				dismiss();
				return;
			}else{
				mAdapter.setCurrentSource(episodeSource);
				if(mListener != null){
					mListener.onSourceSelect(position, episodeSource);
				}
			}
		}
	};
	
    protected int getPopupWidth(){
        return mContext.getResources().getDimensionPixelSize(R.dimen.mediadetail_selectsource_width);
    }
    
    private void adjustPopupWidth(){
        ViewGroup.LayoutParams p = mContentView.getLayoutParams();
        if(p != null){
            p.width = getPopupWidth();
            mContentView.setLayoutParams(p);
        }
    }

    public void show(ViewGroup parent, View anchor){
        if(!isShowing()){
            adjustPopupWidth();
            clearAnimator();
            remove();
            LayoutParams params = new LayoutParams(getPopupWidth(),
            		LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, anchor.getId());
            params.addRule(RelativeLayout.ALIGN_RIGHT, anchor.getId());
        	int margintop = getResources().getDimensionPixelSize(R.dimen.mediadetail_selectsource_popup_margintop);
            params.topMargin = -margintop;
            parent.addView(this, params);
            animateIn();
            mIsShowing = true;
            onShowStateChanged();
//            triggerAutoDismiss();
        }
    }
    
    public void show(ViewGroup anchor){
        if(!isShowing()){
            adjustPopupWidth();
            clearAnimator();
            remove();
            anchor.addView(this, generateParams());
            animateIn();
            mIsShowing = true;
            onShowStateChanged();
            triggerAutoDismiss();
        }
    }
    
    public void dismiss(){
        if(isShowing()){
            clearAnimator();
            animateOut();
            mIsShowing = false;
            onShowStateChanged();
            clearAutoDismiss();
        }
    }
    
    private void onShowStateChanged(){
        if(mShowHideListener != null){
            if(mIsShowing){
                mShowHideListener.onShow(this);
            }else{
                mShowHideListener.onHide(this);
            }
        }
    }
    
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(changedView == this){
            if(visibility == VISIBLE){
                if(!mIsShowing){
                    mIsShowing = true;
                    onShowStateChanged();
                }
            }else{
                if(mIsShowing){
                    mIsShowing = false;
                    onShowStateChanged();
                }
                remove();
            }
        }
    }

    public boolean isShowing(){
        return mIsShowing;
    }
    
    private void remove(){
        if(getParent() instanceof ViewGroup){
            ViewGroup parent = (ViewGroup)getParent();
            parent.removeView(this);
        }
    }
    
    private ViewGroup.LayoutParams generateParams(){
        return   new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
    
    private void clearAnimator(){
        if(mAnimator != null){
            mAnimator.end();
        }
    }
    
    private void animateIn(){
        mAnimator = AnimatorFactory.animateInTopView(this);
    }
    
    private void animateOut(){
        mAnimator = AnimatorFactory.animateOutTopView(this);
    }

    public static final int ANIMATE_DURATION = 450;

    public static final int AUTO_DISMISS_TIMER = 3000;
    public static final int AUTO_IMMEDIATELY_DISMISS_TIMER = 100;

    private Handler mHandler = new Handler();
    private void triggerAutoDismiss(){
        mHandler.removeCallbacks(mAutoDismissRunner);
        mHandler.postDelayed(mAutoDismissRunner, AUTO_DISMISS_TIMER);
    }
    
    private void clearAutoDismiss(){
        mHandler.removeCallbacks(mAutoDismissRunner);
    }
    
    public void triggerDismissImmediately(){
        mHandler.removeCallbacks(mAutoDismissRunner);
        mHandler.postDelayed(mAutoDismissRunner, AUTO_IMMEDIATELY_DISMISS_TIMER);
    }
    
    public void setShowHideListener(
            OnShowHideListener<SelectSourcePopup> mShowHideListener) {
        this.mShowHideListener = mShowHideListener;
    }

    public boolean needPauseVideo(){
        return true;
    }
    
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        triggerAutoDismiss();
        return super.dispatchTouchEvent(event);
    }



    private OnClickListener mDismissClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == SelectSourcePopup.this){
                dismiss();
            }
        }
    };
    
    private Runnable mAutoDismissRunner = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };
}
