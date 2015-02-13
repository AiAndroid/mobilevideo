package com.video.ui.idata;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.EpisodePlayAdapter;
import com.video.ui.R;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;
import com.video.ui.utils.VideoUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by liuhuadonbg on 2/13/15.
 */
public class OfflineDownload {
    private static final String TAG = "html5-download";

    public static void startDownloadTask(final Context context, final TextView view, final VideoItem item, DisplayItem.Media.CP cp, final com.tv.ui.metro.model.DisplayItem.Media.Episode episode, final EpisodeSourceListener el){
        String id = episode.id;
        String url = CommonUrl.BaseURL + "play?id=" + VideoUtils.getVideoID(episode.id) + "&cp="+cp.cp;
        String calledURL = new CommonUrl(context).addCommonParams(url);

        String showText = "";
        if(view != null) {
            showText = (String) view.getText();
            view.setText(context.getString(R.string.connecting));
        }

        final String preText = showText;
        Response.Listener<PlaySource> listener = new Response.Listener<PlaySource>() {
            @Override
            public void onResponse(PlaySource response) {
                if(view != null) {
                    view.setText(preText==null?"":preText);
                }
                Log.d(TAG, "play source:" + response);

                el.playSource(true, response, item, episode);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(view != null) {
                    view.setText(preText);
                }

                Toast.makeText(context, "Server error: " + error, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "fail to fetch play source:"+error);
                el.playSource(false, null, item, episode);
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<PlaySource> gsonRequest = new BaseGsonLoader.GsonRequest<PlaySource>(calledURL, new TypeToken<PlaySource>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(context.getCacheDir() + "/" + id + ".playsource.cache");
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);
    }

    public interface EpisodeSourceListener{
        public void playSource(boolean result, PlaySource ps, VideoItem item, DisplayItem.Media.Episode episode);
    }

    public static EpisodeSourceListener createSourceLister(final Context context){
        EpisodeSourceListener playSouceFetchListener = new EpisodeSourceListener() {
            @Override
            public void playSource(boolean result, final PlaySource ps, final  VideoItem item, final DisplayItem.Media.Episode episode) {
                if(result == false)
                    return;

                Log.d(TAG, "play source returned : "+ps);
                //ui thread do the task
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        MediaUrlForPlayerUtil mediaUrlForPlayerUtil = new MediaUrlForPlayerUtil(context.getApplicationContext());
                        mediaUrlForPlayerUtil.getMediaUrlForPlayer(ps.h5_url, ps.cp, item, episode,  createPlayUrlObserver(context));
                    }
                });

                //PlayUrlLoader mUrlLoader = new PlayUrlLoader(mContext.getApplicationContext(), ps.h5_url, ps.cp);
                //mUrlLoader.get(30000, item, episode, createUrlLoader());
            }
        };

        return playSouceFetchListener;
    }

    private static MediaUrlForPlayerUtil.PlayUrlObserver createPlayUrlObserver(final Context context){
        MediaUrlForPlayerUtil.PlayUrlObserver playUrlObserver = new MediaUrlForPlayerUtil.PlayUrlObserver() {
            @Override
            public void onUrlUpdate(String playUrl, String html5Url, VideoItem item, DisplayItem.Media.Episode episode) {
                Log.d(TAG, "onUrlUpdate: "+html5Url);

                appendDownload(context, playUrl, item, episode);
            }

            @Override
            public void onError() {
                Log.d(TAG,  "onError");
            }

            @Override
            public void onReleaseLock() {
                Log.d(TAG, "onReleaseLock");
            }
        };
        return playUrlObserver;
    }

    private static PlayUrlLoader.H5OnloadListener createUrlLoader(final Context context) {

        PlayUrlLoader.H5OnloadListener h5LoadListener = new PlayUrlLoader.H5OnloadListener() {
            @Override
            public void playUrlFetched(PlayUrlLoader mLoader, boolean result, String playurl, WebView webView, VideoItem item, DisplayItem.Media.Episode episode) {
                mLoader.release();

                Log.d(TAG, "playUrlFetched url:" + playurl);
                appendDownload(context, playurl, item, episode);
            }
        };

        return h5LoadListener;
    }

    private static void appendDownload(Context context, String playurl, VideoItem item, DisplayItem.Media.Episode episode){
        Log.d(TAG,  "qiyi url:" + playurl);
        if (TextUtils.isEmpty(playurl) == true)
            return;

        long download_id = MVDownloadManager.getInstance(context).requestDownload(context, item, episode, playurl);
        if (download_id == MVDownloadManager.DOWNLOAD_IN) {
            Toast.makeText(context, "已经添加到队列，下载中", Toast.LENGTH_LONG).show();
        } else if (download_id != -1) {
            iDataORM.getInstance(context).addDownload(context, item.id, download_id, playurl, item, episode);
            MiPushClient.subscribe(context, item.id, null);

            Toast.makeText(context, "已经添加到队列，download id:" + download_id, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "add download fail", Toast.LENGTH_LONG).show();
        }
    }
}
