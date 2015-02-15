package com.video.ui.view.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;
import com.video.ui.utils.VideoUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailCommentView extends FrameLayout {
    private static final String TAG = "DetailCommentView";
	private Context mContext;
	private View mContentView;
	private TextView mCommentReviewBtn;
	private TextView mCommentWriteBtn;
	private DetailCommentReviewView mCommentReviewView;
	
	private View mEmptyView;
	private TextView mEmptyWriteBtn;

	private VideoItem   mItem;
	private int mPageNo = 1;
	private int mPageSize = 3;
	public final static int MAX_SHOW_COUNT=3;
	
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
	}

	private Activity mActivity;
	public void setVideoContent(VideoItem item, Activity activity){
		mItem     = item;
		mActivity = activity;
		initDataSupply();
	}
	private VideoComments mComments;
	private void initDataSupply() {
		String commentURL = CommonUrl.BaseURL + "comment/"+ VideoUtils.getVideoID(mItem.id) + "?page=1";
		Response.Listener<VideoComments> listener = new Response.Listener<VideoComments>() {
			@Override
			public void onResponse(VideoComments response) {
				Log.d(TAG, "comments data: "+response);
				mComments = response;
				refresh(response);
			}
		};

		Response.ErrorListener errorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, "fail to fetch comments:"+mItem);
			}
		};

		RequestQueue requestQueue = VolleyHelper.getInstance(getContext()).getAPIRequestQueue();
		BaseGsonLoader.GsonRequest<VideoComments> gsonRequest = new BaseGsonLoader.GsonRequest<VideoComments>(commentURL, new TypeToken<VideoComments>(){}.getType(), null, listener, errorListener);
		gsonRequest.setCacheNeed(getContext().getCacheDir() + "/comment_"+VideoUtils.getVideoID(mItem.id));
		gsonRequest.setShouldCache(true);
		requestQueue.add(gsonRequest);
	}

	public void addNewComment(VideoComments.VideoComment comment) {
		VideoComments.VideoComment vc = new VideoComments.VideoComment() ;
		vc.comment = comment.comment;
		vc.score   = comment.score;
		mComments.data.add(0, vc);
		mCommentReviewView.setMediaReviews(mComments.data);

		mContentView.setVisibility(View.VISIBLE);
		mEmptyView.setVisibility(View.GONE);
		mCommentReviewBtn.setVisibility(mComments.data.size() > MAX_SHOW_COUNT?VISIBLE:GONE);
	}


	public static class VideoComments implements Serializable{
		private static final long serialVersionUID = 1L;
        public Meta                    meta;
		public ArrayList<VideoComment> data;

		public static class Meta implements Serializable{
			private static final long serialVersionUID = 1L;
			public int    comment_count;
			public int    page;
			public String vid;
			public String toString(){
				return "vid:"+vid + " page:"+page + " count:"+comment_count;
			}
		}

		public static class VideoComment implements Serializable{
			private static final long serialVersionUID = 1L;
			public String comment;
			public float  score;
			public String uid;
			public String user;
			public DisplayItem.Times time;
		}

		public String toString(){
			return "meta: "+meta;
		}
	}


    private VideoComments.VideoComment makeTestData(){
		VideoComments.VideoComment item = new VideoComments.VideoComment();
        item.score  = 5;
        item.uid = "刘华东";
		item.time = new DisplayItem.Times();
        item.time.created = new Date(System.currentTimeMillis()).getTime();//.toGMTString();
        item.comment = "乱七八糟的东西新";
        return item;
    }

	//packaged method
	private void refresh(VideoComments comments) {

		List<VideoComments.VideoComment> reviews = comments.data;
		if(comments.data == null || comments.data.size() == 0){
			reviews = new ArrayList<VideoComments.VideoComment>();
			//reviews.add(makeTestData());
			//reviews.add(makeTestData());
		}

        mTotalCommentCount = comments.meta.comment_count;

		mCommentReviewView.setMediaReviews(reviews);
		String str = mContext.getResources().getString(R.string.see_all_count_comment);
		str = String.format(str, mTotalCommentCount);
		mCommentReviewBtn.setText(str);
		if(mTotalCommentCount == 0) {
			mContentView.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
			mCommentReviewBtn.setVisibility(GONE);
		} else {
			mContentView.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);

			mCommentReviewBtn.setVisibility(mTotalCommentCount > MAX_SHOW_COUNT?VISIBLE:GONE);
		}
	}

	private void startCommentEditActivity() {
		if(mItem != null) {
			Intent intent = new Intent(getContext(), CommentEditActivity.class);
			intent.putExtra("item", mItem);
			mActivity.startActivityForResult(intent, 100);
		}else {
			Toast.makeText(getContext(), "not fetch video info", Toast.LENGTH_SHORT).show();
		}
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
