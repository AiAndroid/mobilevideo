package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.R;
import com.video.ui.utils.ViewUtils;

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<GenericBlock<DisplayItem>> {
    private final String TAG = ListFragment.class.getName();
	public    MetroLayout        mMetroLayout;
    protected Block<DisplayItem> tab;
    protected int                index;

    private ListView             listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView ="+this);
        View v = inflater.inflate(R.layout.listfragment, container, false);
        mMetroLayout = (MetroLayout)v.findViewById(R.id.metrolayout);
        mMetroLayout.setMetroCursorView((MetroCursorView)v.findViewById(R.id.metrocursor));
        tab = (Block<DisplayItem>) this.getArguments().getSerializable("tab");
        index     = getArguments().getInt("index", 0);

        //have data
        if(tab.items != null && tab.items.size() > 0){
            //construct UI directly
            listView = (ListView) View.inflate(getActivity(), R.layout.list_content_layout, null);
            mMetroLayout.addItemViewPort(listView, LayoutConstant.single_view, 0, 0);
        }else {
            createDataLoader();
        }

        return v;
    }

    @Override
    public Loader<GenericBlock<DisplayItem>> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    private void createDataLoader(){

    }

    @Override
    public void onLoadFinished(Loader<GenericBlock<DisplayItem>> genericBlockLoader, GenericBlock<DisplayItem> displayItemGenericBlock) {

    }

    @Override
    public void onLoaderReset(Loader<GenericBlock<DisplayItem>> genericBlockLoader) {

    }

    public View addViewPort(View view, int celltype, int x, int y){
        return mMetroLayout.addItemViewPort(view, celltype, x, y);
    }

    public void initViews(){

    }


    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume="+this);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause="+this);
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();

        Log.d(TAG, "onDestroy="+this);
        ViewUtils.unbindImageDrawables(getView());
    }
}
