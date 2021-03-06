package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.tv.ui.metro.model.LayoutConstant;
import com.video.ui.R;
import com.video.ui.loader.GenericAlbumLoader;
import com.video.ui.loader.OnNextPageLoader;
import com.video.ui.view.block.BaseCardView;
import com.video.ui.view.block.ChannelVideoItemView;

import java.util.ArrayList;

public class SearchFragment extends LoadingFragment implements LoaderManager.LoaderCallbacks<GenericBlock<DisplayItem>>,OnNextPageLoader {
    private final String TAG = SearchFragment.class.getName();
	public    MetroLayout        mMetroLayout;
    protected Block<DisplayItem> tab;
    protected int                index;

    private   ListView           listView;
    private   EmptyLoadingView   mLoadingView;
    private   int                loaderID;
    private   boolean            haveBuildInData = false;

    public interface SearchResultListener{
        public void onSearchResult(boolean result, GenericBlock<DisplayItem> searchResult);
    }

    private SearchResultListener searchResultListener;
    public void setSearchResultListener(SearchResultListener listener){
        searchResultListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView ="+this);
        View v = inflater.inflate(R.layout.searchfragment, container, false);
        mMetroLayout = (MetroLayout)v.findViewById(R.id.metrolayout);
        tab = (Block<DisplayItem>) this.getArguments().getSerializable("tab");
        index     = getArguments().getInt("index", 0);

        mLoadingView = makeEmptyLoadingView(getActivity(), (RelativeLayout) v.findViewById(R.id.tabs_content));
        mLoadingView.setOnRetryListener(retryLoadListener);

        //if send data in, just use the sent data, then fetch new data in next page
        if(haveBuildInData == false) {
            loaderID = GenericAlbumLoader.VIDEO_ALBUM_LOADER_ID + (stepID++);
            getActivity().getSupportLoaderManager().initLoader(loaderID, savedInstanceState, this);
        }

        //we just have one list view
        return v;
    }



    static int stepID = 1000;

    private View createListContentView(Block<DisplayItem> preData){
        listView = (ListView) View.inflate(getActivity(), R.layout.list_content_layout, null);
        return listView;
    }

    @Override
    public Loader<GenericBlock<DisplayItem>> onCreateLoader(int id, Bundle bundle) {
        if(id == loaderID) {
            mLoader = GenericAlbumLoader.generateVideoAlbumLoader(getActivity(), tab, haveBuildInData==true?2:1);
            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        }

        return null;
    }

    private GenericBlock<DisplayItem> mVidoeInfo;
    RelativeAdapter adapter;
    @Override
    public void onLoadFinished(Loader<GenericBlock<DisplayItem>> genericBlockLoader, GenericBlock<DisplayItem> result) {
        if(result == null){
            if(mVidoeInfo == null){
                mLoadingView.stopLoading(false, false);
            }

            if(searchResultListener != null)
                searchResultListener.onSearchResult(false, null);
            return;
        }
        //for cache show
        if (result != null && mVidoeInfo != null &&
                mVidoeInfo.times != null && mVidoeInfo.times.updated == result.times.updated &&
                mLoader.getCurrentPage() == 1) {
            return;
        }

        //first page
        if (mVidoeInfo == null) {
            mVidoeInfo = result;

            for(int i=0;i<mVidoeInfo.blocks.get(0).blocks.size();i++) {
                Block<DisplayItem> block = mVidoeInfo.blocks.get(0).blocks.get(i);
                if(block.ui_type.id == LayoutConstant.channel_list_long_hot ||
                        block.ui_type.id == LayoutConstant.channel_list_long_rate ||
                        block.ui_type.id == LayoutConstant.channel_list_short ) {

                    if(block.items.size() > 0) {
                        //have data
                        //now we just get the data from server
                        addViewPort(createListContentView(tab), LayoutConstant.single_view, 0, 0);
                        adapter = new RelativeAdapter(block.items, block.ui_type.id);
                        //update UI
                        listView.setAdapter(adapter);
                        listView.setOnScrollListener(scrollListener);
                        listView.setOnItemClickListener(itemClicker);
                    }
                    break;
                }
            }

        }else {

            if(result.blocks.size() > 0) {

                //same page, no need update
                int currentPage = Integer.valueOf(mVidoeInfo.blocks.get(0).meta.page());
                int toBePage    = Integer.valueOf(result.blocks.get(0).meta.page());
                if (currentPage == toBePage) {
                    Log.d(TAG, "same page loader, may from cache page=" + currentPage);
                    return;
                }

                int content_step = 0;
                for(int i=0;i<result.blocks.get(0).blocks.size();i++) {
                    Block<DisplayItem> block = result.blocks.get(0).blocks.get(i);
                    if(block.ui_type.id == LayoutConstant.channel_list_long_hot ||
                            block.ui_type.id == LayoutConstant.channel_list_long_rate ||
                            block.ui_type.id == LayoutConstant.channel_list_short ) {

                        content_step = i;
                        mVidoeInfo.blocks.get(0).blocks.get(i).items.addAll(block.items);
                        break;
                    }
                }

                //to remember the page
                mVidoeInfo.blocks.get(0).media = result.blocks.get(0).media;

                adapter.changeContent(mVidoeInfo.blocks.get(0).blocks.get(content_step).items);
                adapter.notifyDataSetChanged();
            }
        }

        if(searchResultListener != null) {
            try {
                if (mVidoeInfo.blocks.get(0).blocks.get(0).items.size() > 0)
                    searchResultListener.onSearchResult(true, result);
                else
                    searchResultListener.onSearchResult(false, result);
            }catch (Exception ne){
                searchResultListener.onSearchResult(false, result);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<GenericBlock<DisplayItem>> genericBlockLoader) {

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
        if(mLoader != null 	){
            int nextPage = 0;
            if(mVidoeInfo != null && mVidoeInfo.blocks.get(0).meta.page() != null){
                nextPage = Integer.valueOf(mVidoeInfo.blocks.get(0).meta.page());
            }

            if((((GenericAlbumLoader)mLoader).hasMoreData() ||  nextPage > 0) && !((GenericAlbumLoader)mLoader).isLoading()) {
                ((GenericAlbumLoader) mLoader).nextPage(nextPage + 1);
                mLoader.forceLoad();
                return true;
            }
        }else{

            //
            //when enter this UI, use the send in data firstly, then fetch next page
            //
            loaderID = GenericAlbumLoader.VIDEO_ALBUM_LOADER_ID + (stepID++);
            getActivity().getSupportLoaderManager().initLoader(loaderID, null, this);
            return true;
        }

        return false;
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
        private int mui_type;
        public RelativeAdapter(ArrayList<DisplayItem> content, int ui_type){
            super();
            items = content;
            mui_type = ui_type;
        }

        public void changeContent(ArrayList<DisplayItem> content){
            if(content != null) {
                items = content;
                notifyDataSetChanged();
            }
        }
        private ArrayList<DisplayItem> items;

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
            return mui_type;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ChannelVideoItemView root = null;
            if(items == null) {
                root = new ChannelVideoItemView(getActivity(), getLeftBottomUIType(), true);
            }else {
                if(view == null) {
                    root = new ChannelVideoItemView(getActivity(), getLeftBottomUIType(), true);
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

}
