package com.video.ui.loader;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.VideoBlocks;
import com.tv.ui.metro.model.VideoItem;

/**
 * Created by liuhuadong on 9/10/14.
 */
public abstract class GenericDetailLoader<T> extends BaseGsonLoader<VideoBlocks<T>> {
    public static int VIDEO_LOADER_ID = 0x702;
    public static int VIDEO_LOADER_PLAY_ID=0x703;

    public GenericDetailLoader(Context con, DisplayItem item){
        super(con, item, 1);
    }

    public static GenericDetailLoader<VideoItem> generateVideotLoader(Context con, DisplayItem item){
        GenericDetailLoader<VideoItem> loader = new GenericDetailLoader<VideoItem>(con, item){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_item_";
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<VideoBlocks<VideoItem>> gsonRequest = new GsonRequest<VideoBlocks<VideoItem>>(calledURL, new TypeToken<VideoBlocks<VideoItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }

    public static BaseGsonLoader<PlaySource> generateVideoPlayerSourceLoader(Context con, DisplayItem item){
        BaseGsonLoader<PlaySource> loader = new BaseGsonLoader<PlaySource>(con, item, 1){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_source_";
            }

            @Override
            public void setLoaderURL(DisplayItem _item) {
                String id = _item.media.items.get(0).id();
                String url = CommonUrl.BaseURL + "play?id=" +id.substring(id.indexOf('/', 0) + 1) + "&cp="+_item.media.cps.get(0).cp();
                calledURL = new CommonUrl(getContext()).addCommonParams(url);
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<PlaySource> gsonRequest = new GsonRequest<PlaySource>(calledURL, new TypeToken<PlaySource>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }
    
    @Override
    public void setLoaderURL(DisplayItem _item) {
        String url = CommonUrl.BaseURL + _item.target.url;
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }

    public GenericDetailLoader(Context context) {
        super(context);
    }
}
