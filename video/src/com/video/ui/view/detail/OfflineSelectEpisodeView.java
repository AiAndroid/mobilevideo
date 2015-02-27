package com.video.ui.view.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.*;
import android.widget.FrameLayout.LayoutParams;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.EpisodePlayAdapter;
import com.video.ui.R;
import com.video.ui.idata.*;
import com.video.ui.push.Util;
import com.video.ui.view.subview.SelectItemsBlockView;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;

public class OfflineSelectEpisodeView extends PopupWindow {
	public static final String TAG = "OfflineSelectEpisodeView";

	private TextView             mTextView;
	private EpisodeContainerView mLoadingGridView;


	private VideoItem  mMedias;
	private DisplayItem.Media.CP cp;
	private ButtonPair mButtonPair;
	private RelativeLayout mRoot;
	private LinearLayout mContent;
	private SelectItemsBlockView episodeView;

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
		cp      = (DisplayItem.Media.CP) mIntent.getSerializableExtra("cp");
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
			//we just ignore variety type
			mMedias.media.display_layout.type = DisplayItem.Media.DisplayLayout.TYPE_TV;
			mLoadingGridView.setVideo(mMedias, EpisodeContainerView.EPISODE_OFFLINE_UI_STYLE);

			episodeView = (SelectItemsBlockView) EpisodePlayAdapter.findFilterBlockView(mLoadingGridView);
			if(episodeView != null) {
				episodeView.setOnPlayClickListener(episodeClick, null);
			}

			refreshStorage();

			mButtonPair = (ButtonPair) mContent.findViewById(R.id.offline_select_ep_pair);
			mButtonPair.setOnPairClickListener(new ButtonPair.OnPairClickListener() {
				@Override
				public void onRightClick() {
				    if(episodeView.getSelectedEpisodeItems().size() > 0){
						addToDownloadInBG.execute();
						Toast.makeText(mContext, R.string.download_add_success, Toast.LENGTH_SHORT).show();

						resetMainWindowAlpha();
						dismiss();
				    }else{
						Toast.makeText(mContext, R.string.offline_no_selected_item_hint, Toast.LENGTH_SHORT).show();
				    }
				}
				@Override
				public void onLeftClick() {
					resetMainWindowAlpha();
					dismiss();
				}
			});

			mButtonPair.setRightText(R.string.offline);
		}
	}

	private void resetMainWindowAlpha(){
		WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
		lp.alpha = 1.0f;
		((Activity)mContext).getWindow().setAttributes(lp);
	}

	AsyncTask  addToDownloadInBG = new AsyncTask() {
		@Override
		protected Object doInBackground(Object[] params) {
			addToDownloadList(episodeView.getSelectedEpisodeItems());
			return null;
		}
	};
	//TODO
	//we should use queue to add the list, do it one by one
	private void addToDownloadList(ArrayList<DisplayItem.Media.Episode> episodes){
		//add task to queue
		for(DisplayItem.Media.Episode episode: episodes) {
			episode.download_trys = 0;
			OfflineDownload.startDownload(mContext.getApplicationContext(), null, mMedias, cp, episode);
		}
	}

	View.OnClickListener episodeClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			DisplayItem.Media.Episode ps = (DisplayItem.Media.Episode) view.getTag();
			if(view instanceof SelectItemsBlockView.VarietyEpisode){
				view = view.findViewById(R.id.detail_variety_item_name);
			}

			refreshSelectedCount();
			Log.d(TAG, "click episode:" + view.getTag());
		}
	};
	
	private void refreshSelectedCount() {
		int mediaCount = episodeView.getSelectedEpisodeItems().size();
		if(mediaCount > 0){
			mButtonPair.setRightText(mContext.getResources().getString(R.string.offline_with,mediaCount));
	         mButtonPair.setRightButtonEnable(true);
		}else{
			mButtonPair.setRightText(R.string.offline);
			mButtonPair.setRightButtonEnable(false);
		}
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
