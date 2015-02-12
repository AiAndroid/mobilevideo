package com.video.ui.view.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import android.widget.FrameLayout.LayoutParams;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.push.Util;

public class OfflineSelectEpisodeView extends PopupWindow {
	public static final String TAG = "OfflineSelectEpisodeView";

	private TextView             mTextView;
	private EpisodeContainerView mLoadingGridView;


	private VideoItem  mMedias;
	private ButtonPair mButtonPair;
	private RelativeLayout mRoot;
	private LinearLayout mContent;

	private Intent  mIntent;
	private Context mContext;

	@SuppressLint("InflateParams")
    public OfflineSelectEpisodeView(Context context, Intent intent){
		super(LayoutInflater.from(context).inflate(R.layout.offline_select_ep, null),
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
		lp.alpha = 0.5f;
		((Activity)context).getWindow().setAttributes(lp);
		setAnimationStyle(R.style.vertical_popup_anim_style);
		mContext = context;
		mIntent = intent;
		init();
	}
	
	private void init() {
		mMedias = (VideoItem) mIntent.getSerializableExtra("item");
		refreshUI();
	}
	
	private void refreshUI() {

		if(mRoot == null){
			mRoot = (RelativeLayout) getContentView().findViewById(R.id.offline_select_ep_root);
			mContent = (LinearLayout) View.inflate(mContext, R.layout.offline_select_ep_widget, null);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,	mContext.getResources().getDimensionPixelOffset(R.dimen.size_988));
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mContent.setLayoutParams(lp);
			mRoot.addView(mContent);

			mTextView = (TextView) mContent.findViewById(R.id.offline_select_ep_subtitle);
			mLoadingGridView = (EpisodeContainerView) mContent.findViewById(R.id.offline_select_ep_grids);
			mLoadingGridView.setVideo(mMedias, EpisodeContainerView.EPISODE_OFFLINE_UI_STYLE);
			refreshStorage();

			mButtonPair = (ButtonPair) mContent.findViewById(R.id.offline_select_ep_pair);
			mButtonPair.setOnPairClickListener(new ButtonPair.OnPairClickListener() {
				@Override
				public void onRightClick() {
				    if(true){
						Toast.makeText(mContext, R.string.download_add_success, Toast.LENGTH_SHORT).show();
				    }else{
						Toast.makeText(mContext, R.string.offline_no_selected_item_hint, Toast.LENGTH_SHORT).show();
				    }
				}
				@Override
				public void onLeftClick() {
					dismiss();
				}
			});
		}
	}
	
	private void refreshSelectedCount() {
		setRightButtonPair();
	}
	
	private void setRightButtonPair(){
		int mediaCount = getSelectedCount();
		if(mediaCount > 0){
			mButtonPair.setRightText(mContext.getResources().getString(R.string.offline_with,mediaCount));
	         mButtonPair.setRightButtonEnable(true);
		}else{
			mButtonPair.setRightText(R.string.offline);
			mButtonPair.setRightButtonEnable(false);
		}
	}

	private int getSelectedCount(){
		return 10;
	}
	
	private void refreshStorage() {
		StringBuilder sb = new StringBuilder();
		sb.append(mContext.getResources().getString(R.string.storage_remain)).
			append(Util.convertToFormateSize(Util.getSDAvailaleSize()));
		sb.append(" / ");
		sb.append(mContext.getResources().getString(R.string.storage_total)).
			append(Util.convertToFormateSize(Util.getSDAllSize()));
		mTextView.setText(sb.toString());
	}
}
