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
import com.video.ui.R;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;
import com.video.ui.utils.VideoUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.HashMap;

/**
 * Created by liuhuadonbg on 2/13/15.
 */
public class OfflineDownload {
    private static final String TAG = "html5-download";
    Context      mContext;
    VideoItem    mItem;
    DisplayItem.Media.Episode mEpisode;
    DisplayItem.Media.CP      mCP;

    static HashMap<String, OfflineDownload> tasks = new HashMap<String, OfflineDownload>();

    private void releaseDownload(String key){
        tasks.remove(key);
    }
    private OfflineDownload(Context context, VideoItem item, DisplayItem.Media.CP cp, DisplayItem.Media.Episode episode){
        mContext = context.getApplicationContext();
        mItem    = item;
        mCP      = cp;
        mEpisode = episode;
    }

    public static void startDownload(final Context context, final TextView view, final VideoItem item, DisplayItem.Media.CP cp, final com.tv.ui.metro.model.DisplayItem.Media.Episode episode){
        OfflineDownload offlineDownload = new OfflineDownload(context, item, cp, episode);
        tasks.put(item.id, offlineDownload);
        offlineDownload.startDownloadTask(view, offlineDownload.createSourceLister(context));
    }

    public void startDownloadTask(final TextView view, final EpisodeSourceListener el){
        mItem.download_trys++;
        Log.d(TAG, "start download retry times: "+mItem.download_trys);

        String id = mEpisode.id;
        String url = CommonUrl.BaseURL + "play?id=" + VideoUtils.getVideoID(mEpisode.id) + "&cp="+mCP.cp;
        String calledURL = new CommonUrl(mContext).addCommonParams(url);

        String showText = "";
        if(view != null) {
            showText = (String) view.getText();
            view.setText(mContext.getString(R.string.connecting));
        }

        final String preText = showText;
        Response.Listener<PlaySource> listener = new Response.Listener<PlaySource>() {
            @Override
            public void onResponse(PlaySource response) {
                if(view != null) {
                    view.setText(preText==null?"":preText);
                }
                Log.d(TAG, "play source:" + response);

                el.playSource(true, response, mItem, mEpisode);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(view != null) {
                    view.setText(preText);
                }

                Toast.makeText(mContext, "Server error: " + error, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "fail to fetch play source:"+error);
                el.playSource(false, null, mItem, mEpisode);
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(mContext).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<PlaySource> gsonRequest = new BaseGsonLoader.GsonRequest<PlaySource>(calledURL, new TypeToken<PlaySource>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(mContext.getCacheDir() + "/" + id + ".playsource.cache");
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);
    }

    public interface EpisodeSourceListener{
        public void playSource(boolean result, PlaySource ps, VideoItem item, DisplayItem.Media.Episode episode);
    }

    public EpisodeSourceListener createSourceLister(final Context context){
        EpisodeSourceListener playSouceFetchListener = new EpisodeSourceListener() {
            @Override
            public void playSource(boolean result, final PlaySource ps, final  VideoItem item, final DisplayItem.Media.Episode episode) {
                if(result == false) {
                    releaseDownload(item.id);
                    return;
                }

                Log.d(TAG, "play source returned : "+ps);
                //ui thread do the task
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        MediaUrlForPlayerUtil mediaUrlForPlayerUtil = new MediaUrlForPlayerUtil(context.getApplicationContext());
                        mediaUrlForPlayerUtil.getMediaUrlForPlayer(ps.h5_url+"1000|0001", ps.cp, item, episode,  createPlayUrlObserver(context));
                    }
                });

                //PlayUrlLoader mUrlLoader = new PlayUrlLoader(mContext.getApplicationContext(), ps.h5_url, ps.cp);
                //mUrlLoader.get(30000, item, episode, createUrlLoader());
            }
        };

        return playSouceFetchListener;
    }

    private MediaUrlForPlayerUtil.PlayUrlObserver createPlayUrlObserver(final Context context){
        MediaUrlForPlayerUtil.PlayUrlObserver playUrlObserver = new MediaUrlForPlayerUtil.PlayUrlObserver() {
            @Override
            public void onUrlUpdate(String playUrl, String html5Url, VideoItem item, DisplayItem.Media.Episode episode) {
                Log.d(TAG, "************************** onUrlUpdate: "+html5Url);

                releaseDownload(item.id);
                appendDownload(context, playUrl, item, episode);
            }

            @Override
            public void onError(VideoItem item, DisplayItem.Media.Episode episode) {
                Log.d(TAG,  "onError try times:"+item.download_trys);
                if(item.download_trys < 5){
                    //relaunch the fetch
                    OfflineDownload od = tasks.get(item.id);

                    //restart the task
                    if(od != null) {
                        od.startDownloadTask(null, od.createSourceLister(context));
                    }
                    releaseDownload(item.id);
                }
            }

            @Override
            public void onReleaseLock() {
                Log.d(TAG, "onReleaseLock");
            }
        };
        return playUrlObserver;
    }

    private PlayUrlLoader.H5OnloadListener createUrlLoader(final Context context) {

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

    private void appendDownload(Context context, String playurl, VideoItem item, DisplayItem.Media.Episode episode){
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
