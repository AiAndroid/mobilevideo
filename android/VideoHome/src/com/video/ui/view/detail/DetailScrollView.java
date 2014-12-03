package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import com.video.ui.R;

public class DetailScrollView extends ObserverScrollView {

	private Context mContext;
	
//	private View mContentView;
	private DetailInfoView mInfoView;
	private DetailIntroduceView mIntroduceView;
	private DetailCommentView mCommentView;
	private DetailRecommendView mRecommendView;
	
	private int mScrollViewTopPadding;
	
	public DetailScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
//		init();
	}

	public DetailScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
//		init();
	}

	public DetailScrollView(Context context) {
		super(context);
		this.mContext = context;
	}
	
	protected int getTopPadding() {
		return mScrollViewTopPadding;
	}
	
	protected int getCurCi() {
		return 1;
	}
	
	protected void playCurCi() {
	}
	
	protected void setIntroduce(String introduce) {
		mIntroduceView.setIntroduce(introduce);
	}
	
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mScrollViewTopPadding = mContext.getResources().getDimensionPixelSize(R.dimen.detail_scroll_top_padding);
        mInfoView = (DetailInfoView) findViewById(R.id.detail_info_view);
        mIntroduceView = (DetailIntroduceView) findViewById(R.id.detail_introduce_view);
        mCommentView = (DetailCommentView) findViewById(R.id.detail_comment_view);
        mRecommendView = (DetailRecommendView) findViewById(R.id.detail_recommend_view);
    }
}