package com.video.ui.view.detail;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;

public class DetailInfoView extends LinearLayout {

	private TextView mScoreView;
	private TextView mAreaTimeView;
	private TextView mTypeView;
	private ActorsView mActorsView;
	
	public DetailInfoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DetailInfoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DetailInfoView(Context context) {
        this(context, null, 0);
	}
	
	private void init(){
	    setGravity(Gravity.BOTTOM);
	}
	

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mScoreView = (TextView) findViewById(R.id.detail_info_score);
        mAreaTimeView = (TextView) findViewById(R.id.detail_info_area_time);
        mTypeView = (TextView) findViewById(R.id.detail_info_type);
        mActorsView = (ActorsView) findViewById(R.id.detail_info_actors);
    }

	public void setVideo(VideoItem mItem) {
		//score
		String score = "%.1f";
		score = String.format(score, mItem.media.score);
		mScoreView.setText(score);

		//actors
		StringBuilder actors = new StringBuilder();
		if(mItem.media.stuff.director() != null) {
			for (DisplayItem.Media.Stuff.Star item : mItem.media.stuff.director()) {
				if (actors.length() > 0) {
					actors.append(" ");
				}
				actors.append(item.name);
			}
		}

		if(mItem.media.stuff.writer() != null) {
			for (DisplayItem.Media.Stuff.Star item : mItem.media.stuff.writer()) {
				if (actors.length() > 0) {
					actors.append(" ");
				}
				actors.append(item.name);
			}
		}

		if(mItem.media.stuff.actor() != null) {
			for(DisplayItem.Media.Stuff.Star item: mItem.media.stuff.actor()){
				if(actors.length() > 0){
					actors.append(" ");
				}
				actors.append(item.name);
			}
		}

		//type
		StringBuffer type = new StringBuffer();
		if(TextUtils.isEmpty(mItem.media.category_name) == false) {
			type.append(mItem.media.category_name);
			mTypeView.setText(type);
		}else {
			mTypeView.setVisibility(GONE);
		}

		//time and area
		StringBuffer areaAndTimeSb = new StringBuffer();
		if(mItem.media.tags.genre() != null) {
			for (String item : mItem.media.tags.genre()) {
				if (areaAndTimeSb.length() > 0) {
					areaAndTimeSb.append(" | ");
				}
				areaAndTimeSb.append(item);
			}
		}

		if(mItem.media.tags.area() != null) {
			for (String item : mItem.media.tags.area()) {
				if (areaAndTimeSb.length() > 0) {
					areaAndTimeSb.append(" | ");
				}
				areaAndTimeSb.append(item);
			}
		}
		if(mItem.media.tags.language() != null) {
			for (String item : mItem.media.tags.language()) {
				if (areaAndTimeSb.length() > 0) {
					areaAndTimeSb.append(" | ");
				}
				areaAndTimeSb.append(item);
			}
		}

		if(mItem.media.tags.year() != null){
			for(String item: mItem.media.tags.year()){
				if(areaAndTimeSb.length() > 0){
					areaAndTimeSb.append(" | ");
				}
				areaAndTimeSb.append(item);
			}
		}

		if(areaAndTimeSb.length() > 0) {
			areaAndTimeSb.append(" | ");
		}
		areaAndTimeSb.append(mItem.media.date);
		mAreaTimeView.setText(areaAndTimeSb);


		if(actors.length() == 0) {
			mActorsView.setVisibility(View.GONE);
		} else {
			mActorsView.setVisibility(View.VISIBLE);
			mActorsView.setActors(actors.toString());
		}
	}
}
