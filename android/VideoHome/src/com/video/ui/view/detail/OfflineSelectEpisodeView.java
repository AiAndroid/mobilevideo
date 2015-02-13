package com.video.ui.view.detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.MediaUrlForPlayerUtil;
import com.video.ui.idata.PlayUrlLoader;
import com.video.ui.idata.iDataORM;
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

		for(DisplayItem.Media.Episode episode: episodes) {
			EpisodePlayAdapter.fetchOfflineEpisodeSource(mContext.getApplicationContext(), null, mMedias, cp, episode, createSourceLister());
		}
	}

	private EpisodePlayAdapter.EpisodeSourceListener createSourceLister(){
		EpisodePlayAdapter.EpisodeSourceListener playSouceFetchListener = new EpisodePlayAdapter.EpisodeSourceListener() {
			@Override
			public void playSource(boolean result, final PlaySource ps, final  VideoItem item, final DisplayItem.Media.Episode episode) {
				if(result == false)
					return;

				MediaUrlForPlayerUtil mediaUrlForPlayerUtil = new MediaUrlForPlayerUtil(mContext.getApplicationContext());
				mediaUrlForPlayerUtil.getMediaUrlForPlayer(ps.h5_url, ps.cp, createPlayUrlObserver());

				//PlayUrlLoader mUrlLoader = new PlayUrlLoader(mContext.getApplicationContext(), ps.h5_url, ps.cp);
				//mUrlLoader.get(30000, item, episode, createUrlLoader());
			}
		};

		return playSouceFetchListener;
	}

	private MediaUrlForPlayerUtil.PlayUrlObserver createPlayUrlObserver(){
		MediaUrlForPlayerUtil.PlayUrlObserver playUrlObserver = new MediaUrlForPlayerUtil.PlayUrlObserver() {
			@Override
			public void onUrlUpdate(String playUrl, String html5Url) {
				Log.d("html5", "onUrlUpdate: "+html5Url);
			}

			@Override
			public void onError() {
				Log.d("html5", "onError");
			}

			@Override
			public void onReleaseLock() {
				Log.d("html5", "onReleaseLock");
			}
		};
		return playUrlObserver;
	}
	private PlayUrlLoader.H5OnloadListener createUrlLoader() {

		PlayUrlLoader.H5OnloadListener h5LoadListener = new PlayUrlLoader.H5OnloadListener() {
			@Override
			public void playUrlFetched(PlayUrlLoader mLoader, boolean result, String playurl, WebView webView, VideoItem item, DisplayItem.Media.Episode episode) {
				mLoader.release();

				Log.d("download", "qiyi url:" + playurl);
				if (TextUtils.isEmpty(playurl) == true)
					return;

				long download_id = MVDownloadManager.getInstance(mContext).requestDownload(mContext, item, episode, playurl);
				if (download_id == MVDownloadManager.DOWNLOAD_IN) {
					Toast.makeText(mContext, "已经添加到队列，下载中", Toast.LENGTH_LONG).show();
				} else if (download_id != -1) {
					iDataORM.getInstance(mContext).addDownload(mContext, item.id, download_id, playurl, item, episode);
					MiPushClient.subscribe(mContext, item.id, null);

					Toast.makeText(mContext, "已经添加到队列，download id:" + download_id, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(mContext, "add download fail", Toast.LENGTH_LONG).show();
				}
			}
		};

		return h5LoadListener;
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
