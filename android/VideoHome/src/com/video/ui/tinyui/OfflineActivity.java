package com.video.ui.tinyui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.DisplayItemActivity;
import com.video.ui.R;
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.iDataORM;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.MetroLayout;
import com.video.ui.view.subview.BaseCardView;
import com.video.ui.view.subview.DownloadVideoItemView;

import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 2/2/15.
 */
public class OfflineActivity extends DisplayItemActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private MetroLayout mMetroLayout;
    private ListView    listView;
    int cursorLoaderID = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_ui);

        mMetroLayout = (MetroLayout)findViewById(R.id.metrolayout);
        mLoadingView = makeEmptyLoadingView(getBaseContext(), (RelativeLayout)findViewById(R.id.root_container));


        mMetroLayout.addItemViewPort(createListContentView(), LayoutConstant.single_view, 0, 0);
        adapter = new RelativeAdapter(getBaseContext(), null, true);

        setTitle(item.title);
        //update UI
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClicker);
        getSupportLoaderManager().initLoader(cursorLoaderID, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        Uri baseUri = iDataORM.DOWNLOAD_CONTENT_URI;
        return new CursorLoader(getBaseContext(), baseUri, iDataORM.downloadProject, null, null, "date_int desc");
    }

    private RelativeAdapter adapter;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLoadingView.stopLoading(true, false);

        adapter.swapCursor(cursor);
    }

    AdapterView.OnItemClickListener itemClicker = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Object tag = view.getTag();
            if(tag != null && tag instanceof DisplayItem){
                DisplayItem content = (DisplayItem)tag;
                BaseCardView.launcherAction(getBaseContext(), content);
            }

        }
    };

    private Gson gson = new Gson();
    private View createListContentView(){
        listView = (ListView) View.inflate(getBaseContext(), R.layout.list_content_layout, null);
        return listView;
    }

    public class RelativeAdapter extends CursorAdapter {
        public RelativeAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            DownloadVideoItemView root = new DownloadVideoItemView(context);
            return root;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            DownloadVideoItemView root = (DownloadVideoItemView) view;

            int size = cursor.getCount();
            if(size == 1) {
                root.setBackgroundResource(R.drawable.com_item_bg_full);
                root.line.setVisibility(View.INVISIBLE);
                root.padding.setVisibility(View.GONE);
            } else {
                if(cursor.getPosition() == 0) {
                    root.layout.setBackgroundResource(R.drawable.com_item_bg_up);
                    root.line.setVisibility(View.VISIBLE);
                    root.padding.setVisibility(View.GONE);
                } else if(cursor.getPosition() == size - 1) {
                    root.layout.setBackgroundResource(R.drawable.com_item_bg_down);
                    root.line.setVisibility(View.INVISIBLE);
                    root.padding.setVisibility(View.VISIBLE);
                } else {
                    root.layout.setBackgroundResource(R.drawable.com_item_bg_mid);
                    root.line.setVisibility(View.VISIBLE);
                    root.padding.setVisibility(View.GONE);
                }
            }

            iDataORM.ActionRecord ar = iDataORM.getInstance(context).formatActionRecord(cursor);
            root.bind(ar);
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
