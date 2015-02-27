package com.video.ui.loader;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.tv.ui.metro.model.VideoItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by liuhuadong on 9/16/14.
 */
public abstract class GenericAlbumLoader<T> extends BaseGsonLoader<GenericBlock<T>>{
    public static int VIDEO_ALBUM_LOADER_ID   = 0x901;
    public static int VIDEO_SUBJECT_SEARCH_LOADER_ID   = 0x902;
    public GenericAlbumLoader(Context con, DisplayItem item, int setPage){
        super(con, item, setPage);
    }

    public GenericAlbumLoader(Context con, DisplayItem item){
        super(con, item, 1);
    }

    public static GenericAlbumLoader<DisplayItem> generateTabsLoader(final Context con, DisplayItem item){
        GenericAlbumLoader<DisplayItem> loader = new GenericAlbumLoader<DisplayItem>(con, item){

            @Override
            public void setCacheFileName() {
                cacheFileName = "tabs_album_";
            }

            @Override
            public void setLoaderURL(DisplayItem _item) {
                mItem = _item;

                String url = "";
                if(_item.ns.equals("home")) {
                    url = CommonUrl.BaseURL + "c/home";
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }else if(_item.ns.equals("search")) {
                    if(_item.id.endsWith("search.choice")) {
                        url = CommonUrl.BaseURL + "c/search";
                        //url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/mobile_search_choice.json";
                    }else {
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_one_list.json";
                    }
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
                else {
                    url = CommonUrl.BaseURL + encode(_item.target.url);
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<DisplayItem>> gsonRequest = new GsonRequest<GenericBlock<DisplayItem>>(calledURL, new TypeToken<GenericBlock<DisplayItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");

                //if for search no cache
                if(mItem.ns.equals("search") || calledURL.contains("search?kw=")) {
                    gsonRequest.setShouldCache(false);
                }
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }


    public static GenericAlbumLoader<VideoItem> generateVideoAlbumLoader(Context con, DisplayItem item, int setPage){
        GenericAlbumLoader<VideoItem> loader = new GenericAlbumLoader<VideoItem>(con, item, setPage){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_album_";
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<VideoItem>> gsonRequest = new GsonRequest<GenericBlock<VideoItem>>(calledURL, new TypeToken<GenericBlock<VideoItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }

    public static GenericAlbumLoader<VideoItem> generateVideoSearchLoader(Context con, final String keyword){
        String mSearchword = keyword;
        GenericAlbumLoader<VideoItem> loader = new GenericAlbumLoader<VideoItem>(con, null){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_search_";
            }

            @Override
            public void setLoaderURL(DisplayItem _item) {
                if(TextUtils.isEmpty(mKeyword) == false) {
                    String url = CommonUrl.BaseURL + "search?kw=" + URLEncoder.encode(mKeyword) ;
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
            }

            @Override
            public void setSearchKeyword(String key) {
                super.setSearchKeyword(key);

                if(TextUtils.isEmpty(key) == false) {
                    String url = CommonUrl.BaseURL + "search?kw=" + URLEncoder.encode(key) ;
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<VideoItem>> gsonRequest = new GsonRequest<GenericBlock<VideoItem>>(calledURL, new TypeToken<GenericBlock<VideoItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setShouldCache(false);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mKeyword + ".search.cache");
                requestQueue.add(gsonRequest);
            }
        };
        loader.setSearchKeyword(mSearchword);
        return  loader;
    }

    @Override
    public void setLoaderURL(DisplayItem _item) {
        mItem = _item;

        String url = getURL(mItem.target.url, page);
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }

    private String getURL(String target_url, int page){
        String url = CommonUrl.BaseURL + encode(target_url) + "?page=" + page;
        try {
            if (Uri.parse(target_url).getQueryParameterNames() != null && Uri.parse(target_url).getQueryParameterNames().size() > 0) {
                url = CommonUrl.BaseURL + encode(target_url) + "&page=" + (page);
            }
        }catch (Exception ne){}

        return url;
    }

    protected static String encode(String name){
       return name;
    }

    public boolean hasMoreData() {
        //TODO
        if(mResult != null && mResult.blocks != null && mResult.blocks.size() > 0 //tab
                && mResult.blocks.get(0).blocks != null && mResult.blocks.get(0).blocks.size() > 0 && //blocks
                mResult.blocks.get(0).blocks.get(0).items != null && mResult.blocks.get(0).blocks.get(0).items.size() >= 5/*page_size*/){
            return  true;
        }
        return false;
    }

    public boolean isLoading() {
        return mIsLoading;
    }


    public void nextPage() {
        nextPage(++page);
    }

    public void nextPage(int specificPage) {
        page = specificPage;
        //load from server
        mIsLoading = true;
        String url = getURL(mItem.target.url, page);


        //TODO, just return test data list
        //String url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_one_list.json";
        Log.d("nextpage", "page="+(page));
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
        loadDataByGson();
    }
}
