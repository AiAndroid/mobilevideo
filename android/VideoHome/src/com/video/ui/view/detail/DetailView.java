package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.view.RetryView;

public class DetailView extends FrameLayout {

	public static final String TAG = DetailView.class.getName();
	
	private Context mContext;

    VideoItem item;
	
	//UI
	private View mContentView;
	private View mDetailViewContent;

	private DetailPosterView mPosterView;

	private DetailScrollView mScrollView;

	public DetailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initUI();
	}

	public DetailView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DetailView(Context context) {
		this(context, null, 0);
	}

	
	public void setData(VideoItem _item) {
		item = _item;
		refresh();
	}
	
	private void initUI() {
		mContentView = View.inflate(mContext, R.layout.detail_view, null);
		addView(mContentView);
		
		mDetailViewContent = mContentView.findViewById(R.id.detail_view_content);
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
			mPosterView.setImageUrlInfo(item.media.poster);
		}
	}
	
	private void refreshScrollView() {
		if(item != null) {
			mScrollView.setIntroduce("一堆描述将凡在这里");
		}
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
