package com.video.ui.tinyui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.DisplayItemActivity;
import com.video.ui.R;
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.iDataORM;
import com.video.ui.utils.Strings;
import com.tv.ui.metro.model.LayoutConstant;
import com.video.ui.view.block.ChannelVideoItemView;

import java.util.ArrayList;


public class OfflineMediaActivity extends DisplayItemActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static String TAG = "download-OfflineMediaActivity";
	//UI
	private View mLoadingBar;
	private TextView mLoadingCountTextView;
	private TextView mLoadingProgressTextView;
	
	private int mLoadingCount = 10;
	private GridView          mListView;
	private int cursorFinishedLoaderID = 201;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_media);
		setTitle(getString(R.string.offline));

		mLoadingBar = findViewById(R.id.offline_loading_bar);
		mLoadingBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLoadingCount > 0) {
					Intent intent = new Intent(OfflineMediaActivity.this, OfflineActivity.class);
					startActivity(intent);
				}
			}
		});
		mLoadingCountTextView = (TextView) mLoadingBar.findViewById(R.id.offline_media_bar_title);
		mLoadingProgressTextView = (TextView) mLoadingBar.findViewById(R.id.offline_media_bar_subtitle);

		setLoadingCount(0);
		setLoadingProgress(0, 0);

		adapter = new RelativeAdapter(getBaseContext(), null, true);
		mListView = (GridView) findViewById(R.id.offline_media_block_grids);

		//update UI
		mListView.setAdapter(adapter);
		mLoadingView = makeEmptyLoadingView(getBaseContext(), (RelativeLayout) findViewById(R.id.tabs_content));

		findViewById(R.id.offline_media_block).setVisibility(View.GONE);
		//after check then load
		getSupportLoaderManager().initLoader(cursorFinishedLoaderID, null, this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		initData();
	}

	private DownloadManager dm ;
	private void initData(){
		if(iDataORM.getDownloadCount(getBaseContext()) == 0) {
			findViewById(R.id.offline_media_scrollview).setVisibility(View.GONE);
		}else {
			//no need to show downloaded
			dm = (DownloadManager) getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);
			ArrayList<Long> ids = iDataORM.getDownloadIDs(getBaseContext(), 0);
			if (ids.size() > 0) {
				DownloadManager.Query query = new DownloadManager.Query();
				long[] inids = new long[ids.size()];
				for (int i = 0; i < ids.size(); i++) {
					inids[i] = ids.get(i);
				}
				query = query.setFilterById(inids);
				Cursor currentUI = dm.query(query);
				if (currentUI != null && currentUI.getCount() > 0 && currentUI.moveToFirst()) {
					do {
						MVDownloadManager.DownloadTablePojo dp = new MVDownloadManager.DownloadTablePojo(currentUI);

						if (dp.status == MVDownloadManager.DownloadTablePojo.DownloadSuccess) {
							iDataORM.downloadFinished(getBaseContext(), Integer.valueOf(dp.downloadId));
						}
					} while (currentUI.moveToNext());

					currentUI.close();
				}

				//check for downloading status
				checkForDownloadingStatus();

			}else {
				Log.d(TAG, "no current download task");
				//mLoadingBar.setVisibility(View.GONE);
			}
		}
	}

	private void checkForDownloadingStatus(){
		DownloadManager.Query query = new DownloadManager.Query();
		query = query.setFilterByStatus(DownloadManager.STATUS_RUNNING
				| DownloadManager.STATUS_PENDING
				| DownloadManager.STATUS_PAUSED
				| DownloadManager.STATUS_FAILED);
		downloadCursor = dm.query(query);
		//do one time for init data
		handler.sendEmptyMessage(EVENT_RELOAD_DOWNLOAD);

		downloadCursor.registerContentObserver(mDownloadObserver);
	}
	private Cursor          downloadCursor;
	private final int EVENT_RELOAD_DOWNLOAD = 100;
	private ContentObserver mDownloadObserver = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			if (handler.hasMessages(EVENT_RELOAD_DOWNLOAD)) {
				handler.removeMessages(EVENT_RELOAD_DOWNLOAD);
			}
			handler.sendEmptyMessage(EVENT_RELOAD_DOWNLOAD);
		}
	};

	Handler handler = new Handler(){
		public void dispatchMessage(Message msg){
			switch (msg.what){
				case EVENT_RELOAD_DOWNLOAD:
					//update UI
					ArrayList<MVDownloadManager.DownloadTablePojo> donws = MVDownloadManager.loadDownloadStatusFromDM(downloadCursor);
					setLoadingCount(donws.size());
					long downloaded=0, totalsize=0;
					for(MVDownloadManager.DownloadTablePojo item:donws){
						downloaded += item.recv;
						totalsize  += item.total;
					}
					setLoadingProgress(downloaded, totalsize);
					break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			downloadCursor.unregisterContentObserver(mDownloadObserver);
		}catch (Exception ne){}
	}



	private void setLoadingCount(int count) {
		mLoadingCountTextView.setText(getResources().getString(R.string.download_task_with, count));
	}
	
	private void setLoadingProgress(long completed, long total) {
	    if(mLoadingCountTextView != null){
	        mLoadingProgressTextView.setText(getResources().getString(R.string.download_with, Strings.formatSize(completed), Strings.formatSize(total)));
	    }
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		Uri baseUri = iDataORM.DOWNLOAD_GROUP_CONTENT_URI;
		mLoadingView.startLoading(true);

		//remove not exist downloads
        int lens = iDataORM.clearDownloadNotInSystemDowndoad(getBaseContext());
		Log.d(TAG, "remove not exist download in system download:"+lens);
		return new CursorLoader(getBaseContext(), baseUri, iDataORM.downloadProject, "download_status == 1", null, "date_int desc");
	}

	private RelativeAdapter adapter;
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mLoadingView.stopLoading(true, false);

		if(cursor != null && cursor.getCount() > 0){
			findViewById(R.id.offline_media_block).setVisibility(View.VISIBLE);
		}
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {}

	Gson gson = new Gson();
	public class RelativeAdapter extends CursorAdapter {
		public RelativeAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
			ChannelVideoItemView root = new ChannelVideoItemView(context, LayoutConstant.channel_grid_long, false);
			return root;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ChannelVideoItemView root = (ChannelVideoItemView) view;
			iDataORM.ActionRecord ar = iDataORM.getInstance(context).formatActionRecord(cursor);
			VideoItem vi = gson.fromJson(ar.json, VideoItem.class);
			//
			//TODO performance
			//update ui in back thread
			//
			vi.hint = new DisplayItem.Hint();
			vi.hint.put("right", String.format(getString(R.string.total_episode), iDataORM.getFinishedEpisodeCount(getBaseContext(), vi.id)));
			root.setContent(vi, cursor.getPosition());

			root.setVideoClickListener(offlineClick);
		}

		//TODO
		private View.OnClickListener offlineClick = new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				//show downloaded videos
				Intent downloaedIntent = new Intent(getBaseContext(), AllEpisodeActivity.class);
				VideoItem vi = getOfflinedVideoItem(v);
				downloaedIntent.putExtra("item", vi);
				startActivity(downloaedIntent);
			}
		};

		VideoItem getOfflinedVideoItem(View view){

			VideoItem vi = (VideoItem) view.getTag();
			vi.media.display_layout = new DisplayItem.Media.DisplayLayout();
			vi.media.display_layout.type = DisplayItem.Media.DisplayLayout.TYPE_OFFLINE;
			vi.media.display_layout.max_display = 1000;

			vi.media.items = new ArrayList<DisplayItem.Media.Episode>();
			//get finished download resources
			ArrayList<iDataORM.ActionRecord>  episode = iDataORM.getFinishedDownloads(getBaseContext(), vi.id);
			for (iDataORM.ActionRecord item: episode){
				vi.media.items.add(gson.fromJson(item.sub_value, DisplayItem.Media.Episode.class));
			}

			return vi;
		}
	}

}
