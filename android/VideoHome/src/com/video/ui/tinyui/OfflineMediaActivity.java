package com.video.ui.tinyui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.subview.ChannelVideoItemView;

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

		//only for test
		//check for current loading
		setLoadingCount(10);
		setLoadingProgress(100, 1000);

		adapter = new RelativeAdapter(getBaseContext(), null, true);
		mListView = (GridView) findViewById(R.id.offline_media_block_grids);

		//update UI
		mListView.setAdapter(adapter);
		mLoadingView = makeEmptyLoadingView(getBaseContext(), (RelativeLayout) findViewById(R.id.tabs_content));

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
			}else {
				Log.d(TAG, "no current download task");
				mLoadingBar.setVisibility(View.GONE);
			}
		}
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
			vi.hint = new DisplayItem.Hint();
			vi.hint.put("right", String.format(getString(R.string.total_episode), 4));
			//vi.title = gson.fromJson(ar.sub_value, DisplayItem.Media.Episode.class).name;
			root.setContent(vi, cursor.getPosition());

			root.setVideoClickListener(offlineClick);
		}

		//TODO
		private View.OnClickListener offlineClick = new View.OnClickListener(){

			@Override
			public void onClick(View v) {

			}
		};
	}

}
