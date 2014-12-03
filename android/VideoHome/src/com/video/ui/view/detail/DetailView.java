package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.RetryView;

public class DetailView extends FrameLayout {

	public static final String TAG = DetailView.class.getName();
	
	private Context mContext;

    DisplayItem item;
	
	//UI
	private View mContentView;
	private View mDetailViewContent;
	private View mDetailViewLoad;
	private RetryView mDetailViewRetry;
	
	private DetailPosterView mPosterView;

	private DetailScrollView mScrollView;

	public DetailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public DetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public DetailView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public void playCurCi() {
		mScrollView.playCurCi();
	}
	
	public int getCurCi() {
		return mScrollView.getCurCi();
	}
	

	public void setRecommend() {
	}
	
	public void setData(DisplayItem _item) {
        item = _item;

	    refresh();
	}
	
	public void showLoadingView() {
		mDetailViewLoad.setVisibility(View.VISIBLE);
		mDetailViewContent.setVisibility(View.INVISIBLE);
		mDetailViewRetry.setVisibility(View.INVISIBLE);
	}
	
	public void showRetryView() {
		mDetailViewRetry.setVisibility(View.VISIBLE);
		mDetailViewLoad.setVisibility(View.INVISIBLE);
		mDetailViewContent.setVisibility(View.INVISIBLE);
	}
	
	public void showContentView() {
		mDetailViewContent.setVisibility(View.VISIBLE);
		mDetailViewRetry.setVisibility(View.INVISIBLE);
		mDetailViewLoad.setVisibility(View.INVISIBLE);
	}
	
	//init
	private void init() {
		initUi();
	}
	
	private void initUi() {
		mContentView = View.inflate(mContext, R.layout.detail_view, null);
		addView(mContentView);
		
		mDetailViewContent = mContentView.findViewById(R.id.detail_view_content);
		mDetailViewLoad = mContentView.findViewById(R.id.detail_view_load);
		mDetailViewRetry = (RetryView) mContentView.findViewById(R.id.detail_view_retry);
		
		mPosterView = (DetailPosterView) mContentView.findViewById(R.id.detail_poster_view);
		mScrollView = (DetailScrollView) mContentView.findViewById(R.id.detail_scroll_view);
		mScrollView.setOnScrollChangedListener(mOnScrollChangedListener);
	}
	
	//packaged method
	private void refresh() {
		refreshPosterView();
		refreshScrollView();
	}
	
	private void refreshPosterView() {
		if(item != null) {
			mPosterView.setImageUrlInfo(item.images.poster().url);
		}
	}
	
	private void refreshScrollView() {
		if(item != null) {
			mScrollView.setIntroduce("一堆描述将凡在这里");
		}
		//mScrollView.setData();
		//mScrollView.setPlayHistory();
	}

	private ObserverScrollView.OnScrollChangedListener mOnScrollChangedListener = new ObserverScrollView.OnScrollChangedListener() {
		
		@Override
		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			int posterViewTranslateSpeed = 2;
			int scrollViewTopPadding = mScrollView.getTopPadding();
			float scrollY = t;
			if(scrollY < 0) {
				scrollY = 0;
			} else if(scrollY > scrollViewTopPadding) {
				scrollY = scrollViewTopPadding;
			}
			float alpha = 1 - scrollY / scrollViewTopPadding;
			mPosterView.setPosterAlpha(alpha);
			mPosterView.setTranslationY(scrollViewTopPadding / posterViewTranslateSpeed * (alpha - 1));
		}
	};
}
