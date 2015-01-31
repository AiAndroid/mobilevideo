package com.video.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PlaySource;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;
import com.video.ui.view.subview.FilterBlockView;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by liuhuadonbg on 1/28/15.
 */
public class EpisodePlayAdapter {
    private static String TAG = "PlayHelper";

    public static void playEpisode(final Context context, final TextView view, DisplayItem.Media.CP cp, final DisplayItem.Media.Episode episode,final DisplayItem.Media media, final DisplayItem item){
        String id = episode.id;
        String url = CommonUrl.BaseURL + "play?id=" +id.substring(id.indexOf('/', 0) + 1) + "&cp="+cp.cp;
        String calledURL = new CommonUrl(context).addCommonParams(url);

        final String preText = (String) view.getText();
        view.setText(context.getString(R.string.connecting));
        Response.Listener<PlaySource> listener = new Response.Listener<PlaySource>() {
            @Override
            public void onResponse(PlaySource response) {
                view.setText(preText);
                Log.d(TAG, "play source:" + response);

                setPostToOldAPI(context, response, episode, media, item);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                view.setText(preText);

                Log.d(TAG, "fail to fetch play source");
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<PlaySource> gsonRequest = new BaseGsonLoader.GsonRequest<PlaySource>(calledURL, new TypeToken<PlaySource>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(context.getCacheDir() + "/" + id + ".playsource.cache");
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);
    }

    /*
     * http://wiki.n.miui.com/pages/viewpage.action?pageId=8559511
     */
    public static void setPostToOldAPI(Context context, PlaySource ps, DisplayItem.Media.Episode episode, DisplayItem.Media media, DisplayItem item){
        Intent intent = new Intent("duokan.intent.action.VIDEO_PLAY", Uri.parse(ps.h5_url));
        intent.putExtra("video_type", 0);
        intent.putExtra("current_episode", episode.episode);
        intent.putExtra("multi_set", media.items.size() > 0);
        intent.putExtra("media_id", Integer.valueOf(ps.cp_id));
        intent.putExtra("media_clarity", 1);
        intent.putExtra("media_h5_url", ps.h5_url);
        intent.putExtra("media_source", 8);
        intent.putExtra("available_episode_count", media.items.size());
        intent.putExtra("media_poster_url", media.poster);
        intent.putExtra("media_set_name", episode.name);
        intent.putExtra("mediaTitle", media.name);
        intent.putExtra("sdkinfo", String.format("{\"vid\":\"%1$s\"}", ps.h5_url));
        intent.putExtra("sdkdisable", false);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        iDataORM.getInstance(context).addFavor(context, item.ns, iDataORM.HistoryAction, item.id, item);
        MiPushClient.subscribe(context, item.id, null);
    }

    public static View findFilterBlockView(ViewGroup view){
        if(view == null)
            return null;

        if(FilterBlockView.class.isInstance(view ))
            return view;

        int size = view.getChildCount();
        for (int i=0;i<size;i++){
            View item =  view.getChildAt(i);

            if(FilterBlockView.class.isInstance(item ))
                return item;

            if(item instanceof ViewGroup){
                View result = findFilterBlockView((ViewGroup) item);
                if(result != null){
                    return result;
                }
            }
        }

        return null;
    }
}
