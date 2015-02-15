package com.video.ui.view.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.video.ui.R;

import java.util.List;

public class DetailCommentReviewView extends LinearLayout {

	private Context mContext;

	public DetailCommentReviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public DetailCommentReviewView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public void setMediaReviews(List<DetailCommentView.VideoComments.VideoComment> mediaReviews) {
		removeAllViews();
		if(mediaReviews == null || mediaReviews.size() == 0) {
			return;
		}
		int size = mediaReviews.size()>4?4:mediaReviews.size();
		for(int i = 0; i < size; i++) {
			View contentView = View.inflate(mContext, R.layout.detail_review_list_item, null);
			addView(contentView);

			View divider = contentView.findViewById(R.id.detail_review_list_divider);
			TextView comment = (TextView) contentView.findViewById(R.id.detail_review_list_comment);
			TextView user = (TextView) contentView.findViewById(R.id.detail_review_list_user);
			RatingView ratingView = (RatingView) contentView.findViewById(R.id.detail_review_list_rating);
			DetailCommentView.VideoComments.VideoComment review = mediaReviews.get(i);
			if(review != null) {
				comment.setText(review.comment);
				user.setText(mContext.getString(R.string.xiaomi_user) + " "
						+ review.uid);
				ratingView.setScore(review.score);
			}
			if(i == mediaReviews.size() - 1) {
				divider.setVisibility(View.INVISIBLE);
			}
		}
	}

	//init
	private void init() {
		setOrientation(LinearLayout.VERTICAL);
	}
}
