package com.video.ui.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.tv.ui.metro.model.VideoItem;

import java.net.URLEncoder;

/**
 * Created by liuhuadong on 9/16/14.
 */
public abstract class GenericAlbumLoader<T> extends BaseGsonLoader<GenericBlock<T>>{
    public static int VIDEO_ALBUM_LOADER_ID   = 0x901;
    public static int VIDEO_SUBJECT_SEARCH_LOADER_ID   = 0x902;
    public GenericAlbumLoader(Context con, DisplayItem item){
        super(con, item);
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

                String url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_movie.json";
                if(_item.ns.equals("home")) {
                    url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/mi_mobile_port.json";
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }else if(_item.ns.equals("search")) {
                    if(_item.id.endsWith("search.choice")) {
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/mobile_search_choice.json";
                    }else {
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_one_list.json";
                    }
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
                else {
                    if(mItem.id.equals("movie.channel")) {
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_movie.json";
                    }
                    else if(mItem.id.equals("tv.channel")){
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_tv.json";
                    }else if(mItem.id.equals("variety.channel")){
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_variety.json";
                    }else if(mItem.id.equals("documentary.channel")){
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_documentary.json";
                    }else if(mItem.id.equals("cartoon.channel")){
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_cartoon.json";
                    }else if(mItem.id.equals("channel_movie_usa")){
                        url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_movie_usa.json";
                    }

                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                    //
                    //TODO, not defined
                    //super.setLoaderURL(_item);
                }
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<DisplayItem>> gsonRequest = new GsonRequest<GenericBlock<DisplayItem>>(calledURL, new TypeToken<GenericBlock<DisplayItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                //TODO
                //if for search no cache
                if(mItem.ns.equals("search")) {
                    gsonRequest.setShouldCache(true);
                }
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }


    public static GenericAlbumLoader<VideoItem> generateVideoAlbumLoader(Context con, DisplayItem item){
        GenericAlbumLoader<VideoItem> loader = new GenericAlbumLoader<VideoItem>(con, item){
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
                    String url = CommonUrl.BaseURL + "video/search?q=" + URLEncoder.encode(mKeyword) /*+ "&page="+page*/;
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
            }

            @Override
            public void setSearchKeyword(String key) {
                super.setSearchKeyword(key);

                if(TextUtils.isEmpty(key) == false) {
                    String url = CommonUrl.BaseURL + "video/search?q=" + URLEncoder.encode(key) /*+ "&page="+page*/;
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
        //String url = CommonUrl.BaseURL + mItem.ns + "/" + mItem.type + "?id=" + mItem.id + "&page="+page;
        //TODO, just return test data list
        String url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_one_list.json";
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }

    public boolean hasMoreData() {
        //TODO
        if(mResult != null && mResult.blocks != null && mResult.blocks.size() > 0 && mResult.blocks.get(0).items.size() == page_size){
            return  true;
        }
        return false;
    }

    public boolean isLoading() {
        return mIsLoading;
    }


    public void nextPage() {
        page++;
        //load from server
        mIsLoading = true;
        //
        //tring url = CommonUrl.BaseURL + mItem.ns + "/" + mItem.type + "?id=" + mItem.id + "&page="+(page);
        //TODO, just return test data list
        String url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_one_list.json";
        Log.d("nextpage", "page="+(page));
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
        loadDataByGson();
    }
}
