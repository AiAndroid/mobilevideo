package com.video.ui.view.detail;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
	
	protected void setActors(String actors) {
		if(TextUtils.isEmpty(actors)) {
			mActorsView.setVisibility(View.GONE);
		} else {
			mActorsView.setVisibility(View.VISIBLE);
			mActorsView.setActors(actors);
		}
	}
	
	protected void setMediaInfo(VideoItem mediaInfo) {
		String score = "%.1f";
		score = String.format(score, 9.9);
		String actors = "陈学通 王爱军 朱辉 王维 白鹏 高荣欣";
		StringBuffer type = new StringBuffer();
		type.append("电影 电视剧");
		StringBuffer areaAndTimeSb = new StringBuffer();
		{
			areaAndTimeSb.append("大陆");
			areaAndTimeSb.append(" | ");
		}
		areaAndTimeSb.append("2014-09-09");
		
		mScoreView.setText(score);
		mAreaTimeView.setText(areaAndTimeSb);
		mTypeView.setText(type);
		mActorsView.setActors(actors);
	}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mScoreView = (TextView) findViewById(R.id.detail_info_score);
        mAreaTimeView = (TextView) findViewById(R.id.detail_info_area_time);
        mTypeView = (TextView) findViewById(R.id.detail_info_type);
        mActorsView = (ActorsView) findViewById(R.id.detail_info_actors);

        setActors("actor actor actor actor actor actor actor actor");
        setMediaInfo(null);
    }
}
