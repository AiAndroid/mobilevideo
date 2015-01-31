package com.video.ui.view.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.video.ui.R;
import com.video.ui.idata.iDataORM;
import com.video.ui.tinyui.AlbumActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 *@author tangfuling
 *
 */

public class MyVideoFragment extends Fragment {
	
	private Activity mContext;
	
	public static final int TAG_LOCAL_MEDIA = 0;
	public static final int TAG_MY_FAVORITE = 1;
	public static final int TAG_MY_OFFLINE = 2;
	public static final int TAG_PLAY_HIS = 3;
	public static final int TAG_SHARE_DEVICE = 4;
	public static final int TAG_ADDON = 5;
	public static final int TAG_SETTING = 6;

	public static class MyVideoItem {
		public int itemIconResId;
		public String itemName = "";
		public String mDesc = "";
		public int tag;
	}

	//UI
	private View mContentView;
	private UserHeadView mUserHeadView;
	private ListView mListView;
	private MyVideoAdapter mAdapter;
	
	private List<MyVideoItem> mMyVideoItems = new LinkedList<MyVideoItem>();
	private MyVideoItem mLocalMediaItem;
	private MyVideoItem mMyFavoriteItem;
	private MyVideoItem mMyOfflineItem;
	private MyVideoItem mPlayHistoryItem;
	private MyVideoItem mShareDeviceItem;
	private MyVideoItem mAddonItem;
	private MyVideoItem mSettingItem;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.my_video, container, false);
		return mContentView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = (Activity) getActivity();
		init();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	//init
	private void init() {
		initDataSupply();
		initUI();
	}
	
	private void initDataSupply() {

	}
	
	private void initUI() {
		initHeadView();
		initMyVideoItems();
		initListView();
		refreshListView();
	}
	
	private void initHeadView() {
	    mUserHeadView = new UserHeadView(getActivity());
	    mUserHeadView.setPadding(0, getResources().getDimensionPixelOffset(R.dimen.my_video_list_margin_top),
	            0, getResources().getDimensionPixelSize(R.dimen.my_video_avatar_padding_bottom));
		mUserHeadView.setOnClickListener(mOnClickListener);
	}

	private String getCountDesc(int count){
		String str = mContext.getResources().getString(R.string.gong_count_ge);
		str = formatString(str, count);
		return str;
	}

	public static String formatString(String format, Object... args){
		try{
			return String.format(format, args);
		}catch(Exception e){
		}
		return "";
	}
	
	private void initMyVideoItems() {
		mLocalMediaItem = new MyVideoItem();
		mLocalMediaItem.itemName = mContext.getResources().getString(R.string.local_video);
		mLocalMediaItem.itemIconResId = R.drawable.icon_my_video_local;
		mLocalMediaItem.tag = TAG_LOCAL_MEDIA;
		mMyVideoItems.add(mLocalMediaItem);

		mMyFavoriteItem = new MyVideoItem();
		mMyFavoriteItem.itemName = mContext.getResources().getString(R.string.my_favorite);
		mMyFavoriteItem.itemIconResId = R.drawable.icon_my_video_favorite;
		mMyFavoriteItem.tag = TAG_MY_FAVORITE;
		mMyFavoriteItem.mDesc = getCountDesc(iDataORM.getInstance(getActivity()).getFavoritesCount(getActivity(), "video", iDataORM.FavorAction));
		mMyVideoItems.add(mMyFavoriteItem);
		
		mMyOfflineItem = new MyVideoItem();
		mMyOfflineItem.itemName = mContext.getResources().getString(R.string.my_offline);
		mMyOfflineItem.itemIconResId = R.drawable.icon_my_video_offline;
		mMyOfflineItem.tag = TAG_MY_OFFLINE;
		mMyVideoItems.add(mMyOfflineItem);
		
		mPlayHistoryItem = new MyVideoItem();
		mPlayHistoryItem.itemName = mContext.getResources().getString(R.string.play_history);
		mPlayHistoryItem.itemIconResId = R.drawable.icon_my_video_play_his;
		mPlayHistoryItem.mDesc = getCountDesc(iDataORM.getInstance(getActivity()).getFavoritesCount(getActivity(), "video", iDataORM.HistoryAction));
		mPlayHistoryItem.tag = TAG_PLAY_HIS;
		mMyVideoItems.add(mPlayHistoryItem);
		
		mShareDeviceItem = new MyVideoItem();
		mShareDeviceItem.itemName = mContext.getResources().getString(R.string.share_device);
		mShareDeviceItem.itemIconResId = R.drawable.icon_my_video_share_device;
		mShareDeviceItem.tag = TAG_SHARE_DEVICE;
		mMyVideoItems.add(mShareDeviceItem);
		
		mAddonItem = new MyVideoItem();
		mAddonItem.itemName = mContext.getResources().getString(R.string.addon);
		mAddonItem.itemIconResId = R.drawable.icon_my_video_addon;
		mAddonItem.tag = TAG_ADDON;
		mMyVideoItems.add(mAddonItem);
	    
		mSettingItem = new MyVideoItem();
		mSettingItem.itemName = mContext.getResources().getString(R.string.setting);
		mSettingItem.itemIconResId = R.drawable.icon_my_set_up;
		mSettingItem.tag = TAG_SETTING;
		mMyVideoItems.add(mSettingItem);
	}
	
	private void initListView() {
	    mListView = (ListView) mContentView.findViewById(R.id.my_video_list);
		mAdapter = new MyVideoAdapter(mContext);
		mAdapter.setOnMyVideoClickListener(mOnMyVideoClickListener);
		mListView.setSelector(R.drawable.transparent);
		mListView.setDivider(null);
		mListView.setAdapter(mAdapter);
		mListView.addHeaderView(mUserHeadView);
	}
	
	private void refreshListView() {
	    if(!isAdded()){
            return;
        }
		refreshMyVideoItems();
		mAdapter.setGroup(mMyVideoItems);
	}
	
	private void refreshMyVideoItems() {

	}

	//UI callback
	private MyVideoView.OnMyVideoClickListener mOnMyVideoClickListener = new MyVideoView.OnMyVideoClickListener() {
		
		@Override
		public void onMyVideoClick(MyVideoView view, MyVideoItem myVideoItem) {
			int tag = myVideoItem.tag;
			switch (tag){
				case TAG_SETTING:
					break;
				case TAG_MY_FAVORITE:
					Intent favorIntent = new Intent(getActivity(), AlbumActivity.class);
					favorIntent.putExtra("favor", true);
					getActivity().startActivity(favorIntent);
					break;
				case TAG_PLAY_HIS:
					Intent history = new Intent(getActivity(), AlbumActivity.class);
					history.putExtra("history", true);
					getActivity().startActivity(history);
					break;
			}
			Toast.makeText(getActivity(), "not support", Toast.LENGTH_LONG).show();
		}
	};
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == mUserHeadView) {
				AccountManager mAccountManager = AccountManager.get(getActivity());
				Account[] account = mAccountManager.getAccountsByType("com.xiaomi");
				if(account.length == 0) {
					mAccountManager.addAccount("com.xiaomi", (String)null, (String[])null, (Bundle)null, getActivity(), null, (Handler)null);
				}else {
					Intent intent = new Intent("android.settings.XIAOMI_ACCOUNT_SYNC_SETTINGS");
					mContext.startActivity(intent);
				}
			}
		}
	};

	public abstract class BaseGroupAdapter<T> extends BaseAdapter {

		protected Context mContext;
		protected List<T> mGroup = new ArrayList<T>();

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		@Override
		public boolean isEnabled(int position) {
			return true;
		}

		public BaseGroupAdapter(Context context) {
			this.mContext = context;
		}

		public int getCount() {
			return mGroup.size();
		}

		public T getItem(int position) {
			if (position < 0 || position >= mGroup.size()) {
				return null;
			}
			return mGroup.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public boolean isEmpty() {
			return mGroup.isEmpty();
		}

		public void clear() {
			mGroup.clear();
			notifyDataSetChanged();
		}

		public void setGroup(List<T> list) {
			mGroup.clear();
			if (list != null && list.size() > 0) {
				mGroup.addAll(list);
			}
			notifyDataSetChanged();
		}

		public void setGroup(T[] array) {
			if(array != null) {
				setGroup(Arrays.asList(array));
			} else {
				List<T> list = null;
				setGroup(list);
			}
		}

		public void addGroup(List<T> list) {
			if (list != null) {
				for (T item : list) {
					if (item != null) {
						mGroup.add(item);
					}
				}
				notifyDataSetChanged();
			}
		}

		public void addGroup(T[] array) {
			if(array != null) {
				addGroup(Arrays.asList(array));
			} else {
				List<T> list = null;
				addGroup(list);
			}
		}

		public void refresh() {
			notifyDataSetChanged();
		}

	}

	public class MyVideoAdapter extends BaseGroupAdapter<MyVideoFragment.MyVideoItem> {

		private Context mContext;
		private MyVideoView.OnMyVideoClickListener mListener;

		public MyVideoAdapter(Context context) {
			super(context);
			this.mContext = context;
		}

		public void setOnMyVideoClickListener(MyVideoView.OnMyVideoClickListener listener) {
			this.mListener = listener;
		}

		private class ViewHolder {
			MyVideoView myVideoView1;
			MyVideoView myVideoView2;
		}

		@Override
		public int getCount() {
			int rows = (int) Math.ceil(mGroup.size() / 2f);
			return rows;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView == null) {
				View view = View.inflate(mContext, R.layout.my_video_item, null);
				viewHolder = new ViewHolder();
				viewHolder.myVideoView1 = (MyVideoView) view.findViewById(R.id.my_video_item_1);
				viewHolder.myVideoView2 = (MyVideoView) view.findViewById(R.id.my_video_item_2);
				viewHolder.myVideoView1.setOnMyVideoClickListener(mListener);
				viewHolder.myVideoView2.setOnMyVideoClickListener(mListener);
				convertView = view;
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			MyVideoFragment.MyVideoItem myVideoItem1 = null;
			MyVideoFragment.MyVideoItem myVideoItem2 = null;
			if(position * 2 < mGroup.size()) {
				myVideoItem1 = mGroup.get(position * 2);
			}
			if(position * 2 + 1 < mGroup.size()) {
				myVideoItem2 = mGroup.get(position * 2 + 1);
			}
			viewHolder.myVideoView1.setItem(myVideoItem1);
			viewHolder.myVideoView2.setItem(myVideoItem2);

			return convertView;
		}
	}
}
