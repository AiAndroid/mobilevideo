package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.video.ui.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailCommentView extends FrameLayout {

	private Context mContext;
	private View mContentView;
	private TextView mCommentReviewBtn;
	private TextView mCommentWriteBtn;
	private DetailCommentReviewView mCommentReviewView;
	
	private View mEmptyView;
	private TextView mEmptyWriteBtn;
	
	private int mPageNo = 1;
	private int mPageSize = 3;
	
	//data from net
	private int mTotalCommentCount;
	
	public DetailCommentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public DetailCommentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DetailCommentView(Context context) {
		this(context, null, 0);
	}

	
	//init
	private void init() {
		initDataSupply();
		initUI();
	}
	
	private void initUI() {
		mContentView = View.inflate(mContext, R.layout.detail_comment, null);
		addView(mContentView);
		mCommentWriteBtn = (TextView) mContentView.findViewById(R.id.detail_comment_write);
		mCommentWriteBtn.setOnClickListener(mOnClickListener);
		mCommentReviewBtn = (TextView) mContentView.findViewById(R.id.detail_comment_btn);
		mCommentReviewBtn.setOnClickListener(mOnClickListener);
		mCommentReviewView = (DetailCommentReviewView) findViewById(R.id.detail_comment_review);
		
		mEmptyView = View.inflate(mContext, R.layout.detail_comment_empty, null);
		mEmptyWriteBtn = (TextView) mEmptyView.findViewById(R.id.detail_comment_empty_write);
		mEmptyWriteBtn.setOnClickListener(mOnClickListener);
		addView(mEmptyView);
		
		refresh();
	}
	
	private void initDataSupply() {

	}

    private MediaReview makeTestData(){
        MediaReview item = new MediaReview();
        item.choice = 1;
        item.score  = 5;
        item.userid = "刘华东";
        item.createtime = new Date(System.currentTimeMillis()).toGMTString();
        item.filmreview = "乱七八糟的东西新";
        return item;
    }
	//packaged method
	private void refresh() {
		List<MediaReview> reviews = new ArrayList<MediaReview>();
        reviews.add(makeTestData());
        reviews.add(makeTestData());

        mTotalCommentCount = 100;

		mCommentReviewView.setMediaReviews(reviews);
		String str = mContext.getResources().getString(R.string.see_all_count_comment);
		str = String.format(str, mTotalCommentCount);
		mCommentReviewBtn.setText(str);
		if(mTotalCommentCount == 0) {
			mContentView.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
		} else {
			mContentView.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);
		}
	}
	
	private void startCommentEditActivity() {

	}
	
	private void startCommentReviewActivity() {

	}
	
	//UI callback
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == mCommentWriteBtn || v == mEmptyWriteBtn) {
				startCommentEditActivity();
			} else if(v == mCommentReviewBtn){
				startCommentReviewActivity();
			}
		}
	};
}
