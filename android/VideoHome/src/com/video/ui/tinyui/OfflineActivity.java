package com.video.ui.tinyui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RelativeLayout;
import com.video.ui.DisplayItemActivity;
import com.video.ui.R;
import com.video.ui.idata.iDataORM;

/**
 * Created by liuhuadonbg on 2/2/15.
 */
public class OfflineActivity extends DisplayItemActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    int cursorLoaderID = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listfragment);

        mLoadingView = makeEmptyLoadingView(getBaseContext(), (RelativeLayout)findViewById(R.id.tabs_content));
        //getLoaderManager().initLoader(cursorLoaderID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        Uri baseUri = iDataORM.DOWNLOAD_CONTENT_URI;
        return new CursorLoader(getBaseContext(), baseUri, iDataORM.downloadProject, null, null, "date_int ASC");
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLoadingView.stopLoading(true, false);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
