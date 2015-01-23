package com.video.ui.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.GenericAlbumLoader;
import com.video.ui.loader.OnNextPageLoader;
import com.video.ui.utils.ViewUtils;
import com.video.ui.view.metro.MetroCursorView;
import com.video.ui.view.subview.BaseCardView;
import com.video.ui.view.subview.ChannelVideoItemView;

import java.util.ArrayList;

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<GenericBlock<VideoItem>>,OnNextPageLoader {
    private final String TAG = ListFragment.class.getName();
	public    MetroLayout        mMetroLayout;
    protected Block<DisplayItem> tab;
    protected int                index;

    private   ListView           listView;
    protected BaseGsonLoader     mLoader;
    private   EmptyLoadingView   mLoadingView;
    private   int                loaderID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView ="+this);
        View v = inflater.inflate(R.layout.listfragment, container, false);
        mMetroLayout = (MetroLayout)v.findViewById(R.id.metrolayout);
        mMetroLayout.setMetroCursorView((MetroCursorView)v.findViewById(R.id.metrocursor));
        tab = (Block<DisplayItem>) this.getArguments().getSerializable("tab");
        index     = getArguments().getInt("index", 0);

        mLoadingView = makeEmptyLoadingView(getActivity(), (RelativeLayout) v.findViewById(R.id.tabs_content));
        mLoadingView.setVisibility(View.GONE);

        //have data

        //now we just get the data from server
        addViewPort(createListContentView(tab), LayoutConstant.single_view, 0, 0);


        if(tab.blocks != null && tab.blocks.size() > 0){

            for(int i=0;i<tab.blocks.size();i++){
                Block<DisplayItem> block = tab.blocks.get(i);
                if(block.ui_type.id == LayoutConstant.linearlayout_single_desc){
                    TextView textView = new TextView(getActivity());
                    textView.setText(block.desc);
                    textView.setBackgroundResource(R.drawable.com_block_n);
                    listView.addHeaderView(textView, null, false);

                    //no need filter and search
                    if(getActivity().findViewById(R.id.channel_filte_btn) != null) {
                        getActivity().findViewById(R.id.channel_filte_btn).setVisibility(View.GONE);
                    }
                    if(getActivity().findViewById(R.id.channel_search_btn) != null) {
                        getActivity().findViewById(R.id.channel_search_btn).setVisibility(View.GONE);
                    }

                    break;
                }
            }
        }

        loaderID = GenericAlbumLoader.VIDEO_ALBUM_LOADER_ID + (stepID++);
        getActivity().getSupportLoaderManager().initLoader(loaderID, savedInstanceState, this);

        //we just have one list view
        return v;
    }

    static int stepID = 1000;

    private View createListContentView(Block<DisplayItem> block){
        listView = (ListView) View.inflate(getActivity(), R.layout.list_content_layout, null);
        return listView;
    }


    @Override
    public Loader<GenericBlock<VideoItem>> onCreateLoader(int id, Bundle bundle) {
        if(id == loaderID) {
            mLoader = GenericAlbumLoader.generateVideoAlbumLoader(getActivity(), tab);
            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        }

        return null;
    }

    RetryView.OnRetryLoadListener retryLoadListener = new RetryView.OnRetryLoadListener() {
        @Override
        public void OnRetryLoad(View vClicked) {
            if(mLoader != null){
                mLoader.forceLoad();
            }
        }
    };

    int currentPage = -1;
    private GenericBlock<VideoItem> mVidoeInfo;
    RelativeAdapter adapter;
    @Override
    public void onLoadFinished(Loader<GenericBlock<VideoItem>> genericBlockLoader, GenericBlock<VideoItem> result) {
        if(result == null){
            if(mVidoeInfo == null){
                mLoadingView.stopLoading(false, true);
            }
            return;
        }
        //for cache show
        if (result != null && mVidoeInfo != null &&
                mVidoeInfo.times != null && mVidoeInfo.times.updated == result.times.updated &&
                mLoader.getCurrentPage() == 1) {
            return;
        }

        if (currentPage == mLoader.getCurrentPage()) {
            Log.d(TAG, "same page loader, may from cache page=" + currentPage);
            return;
        }

        currentPage = mLoader.getCurrentPage();
        //first page
        if (mVidoeInfo == null) {
            mVidoeInfo = result;
            for(int i=0;i<mVidoeInfo.blocks.get(0).blocks.size();i++) {
                Block<VideoItem> block = mVidoeInfo.blocks.get(0).blocks.get(i);
                if(block.ui_type.id == LayoutConstant.channel_list_long_hot ||
                        block.ui_type.id == LayoutConstant.channel_list_long_rate ||
                        block.ui_type.id == LayoutConstant.channel_list_short ) {
                    adapter = new RelativeAdapter(block.items);
                    break;
                }
            }

            //update UI
            listView.setAdapter(adapter);
            listView.setOnScrollListener(scrollListener);
            listView.setOnItemClickListener(itemClicker);
        }else {

            if(result.blocks.size() > 0) {

                int content_step = 0;
                for(int i=0;i<result.blocks.get(0).blocks.size();i++) {
                    Block<VideoItem> block = result.blocks.get(0).blocks.get(i);
                    if(block.ui_type.id == LayoutConstant.channel_list_long_hot ||
                            block.ui_type.id == LayoutConstant.channel_list_long_rate ||
                            block.ui_type.id == LayoutConstant.channel_list_short ) {

                        content_step = i;
                        mVidoeInfo.blocks.get(0).blocks.get(i).items.addAll(block.items);
                        break;
                    }
                }


                adapter.changeContent(mVidoeInfo.blocks.get(0).blocks.get(content_step).items);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<GenericBlock<VideoItem>> genericBlockLoader) {

    }

    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
            if(firstVisibleItem + 5 >= totalItemCount){
                nextPage();
            }
        }
    };

    @Override
    public boolean nextPage() {
        if(mLoader != null 	&& ((GenericAlbumLoader)mLoader).hasMoreData() && !((GenericAlbumLoader)mLoader).isLoading()){
            ((GenericAlbumLoader)mLoader).nextPage();
            mLoader.forceLoad();
            return true;
        }else{
            return false;
        }
    }

    AdapterView.OnItemClickListener itemClicker = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Object tag = view.getTag();
            if(tag != null && tag instanceof DisplayItem){
                DisplayItem content = (DisplayItem)tag;
                BaseCardView.launcherAction(getActivity(), content);
            }

        }
    };

    public class RelativeAdapter extends BaseAdapter {
        public RelativeAdapter(ArrayList<VideoItem> content){
            super();
            items = content;
        }

        public void changeContent(ArrayList<VideoItem> content){
            if(content != null) {
                items = content;
                notifyDataSetChanged();
            }
        }
        private ArrayList<VideoItem> items;

        @Override
        public int getCount() {
            return items==null?40:items.size();
        }

        @Override
        public Object getItem(int i) {
            return items==null?new Object():items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        private int getLeftBottomUIType(){
            return mVidoeInfo.blocks.get(0).blocks.get(0).ui_type.id;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ChannelVideoItemView root = null;
            if(items == null) {
                root = new ChannelVideoItemView(getActivity());
                root.setUIType(getLeftBottomUIType());
            }else {
                if(view == null) {
                    root = new ChannelVideoItemView(getActivity());
                }else{
                    root = (ChannelVideoItemView)view;
                }


                root.setTag(getItem(i));
                root.setUIType(getLeftBottomUIType());
                root.setContent((DisplayItem) getItem(i), i);
            }

            int size = getCount();
            if(size == 1) {
                root.setBackgroundResource(R.drawable.com_item_bg_full);
                root.line.setVisibility(View.INVISIBLE);
                root.padding.setVisibility(View.GONE);
            } else {
                if(i == 0) {
                    root.layout.setBackgroundResource(R.drawable.com_item_bg_up);
                    root.line.setVisibility(View.VISIBLE);
                    root.padding.setVisibility(View.GONE);
                } else if(i == size - 1) {
                    root.layout.setBackgroundResource(R.drawable.com_item_bg_down);
                    root.line.setVisibility(View.INVISIBLE);
                    root.padding.setVisibility(View.VISIBLE);
                } else {
                    root.layout.setBackgroundResource(R.drawable.com_item_bg_mid);
                    root.line.setVisibility(View.VISIBLE);
                    root.padding.setVisibility(View.GONE);
                }
            }
            return root;
        }
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

    public EmptyLoadingView makeEmptyLoadingView(Context context,  RelativeLayout parentView){
        return makeEmptyLoadingView(context, parentView,  RelativeLayout.CENTER_IN_PARENT);
    }

    public EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView, int rule){
        EmptyLoadingView loadingView = new EmptyLoadingView(context);
        loadingView.setOnRetryListener(retryLoadListener);
        loadingView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(rule);
        parentView.addView(loadingView, rlp);
        return loadingView;
    }
}
