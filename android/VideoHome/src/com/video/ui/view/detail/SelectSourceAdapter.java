package com.video.ui.view.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

import java.util.ArrayList;

public class SelectSourceAdapter extends BaseAdapter {

	private ArrayList<DisplayItem.Media.CP> videoSource;

	private DisplayItem.Media.CP mCurrentSource;
	private Context mContext;
	public SelectSourceAdapter(Context context, ArrayList<DisplayItem.Media.CP> source) {
		super();
		mContext = context;
		videoSource = source;
	}

	public void setCurrentSource(DisplayItem.Media.CP source) {
		mCurrentSource = source;
		notifyDataSetChanged();
	}
	
	public DisplayItem.Media.CP getCurrentSource() {
		return mCurrentSource;
	}
	
	private static class ViewHolder {
		private TextView mTextView;
		private ImageView mImageView;
	}

	@Override
	public int getCount() {
		return videoSource.size();
	}

	@Override
	public Object getItem(int position) {
		return videoSource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return videoSource.get(position).cp.hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView != null && convertView.getTag() instanceof ViewHolder) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.source_item_select, parent, false);
			holder = new ViewHolder();
			holder.mTextView = (TextView) convertView.findViewById(R.id.source_item_text);
			holder.mImageView = (ImageView) convertView.findViewById(R.id.source_item_image);
			convertView.setTag(holder);
		}
		
		DisplayItem.Media.CP sourceid = (DisplayItem.Media.CP) getItem(position);
		if (mCurrentSource.cp.equals(sourceid.cp)) {
			holder.mTextView.setSelected(true);
			holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.orange));
		} else {
			holder.mTextView.setSelected(false);
			holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.black));
		}
		final String text = sourceid.name;
		holder.mTextView.setText(text);
		Picasso.with(mContext).load(sourceid.icon).fit().into(holder.mImageView);

		return convertView;
	}

}
