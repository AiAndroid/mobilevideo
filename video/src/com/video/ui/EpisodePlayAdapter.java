package com.video.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;
import com.video.ui.utils.VideoUtils;
import com.video.ui.view.subview.SelectItemsBlockView;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by liuhuadonbg on 1/28/15.
 */
public class EpisodePlayAdapter {
    private static String TAG = "PlayHelper";

    public static void playEpisode(final Context context, final TextView view, DisplayItem.Media.CP cp, final DisplayItem.Media.Episode episode,final DisplayItem.Media media, final DisplayItem item){
        String id = episode.id;
        String url = CommonUrl.BaseURL + "play?id=" + VideoUtils.getVideoID(episode.id) + "&cp="+cp.cp;
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

                Toast.makeText(context, "Server error: "+error, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "fail to fetch play source:"+error);
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<PlaySource> gsonRequest = new BaseGsonLoader.GsonRequest<PlaySource>(calledURL, new TypeToken<PlaySource>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(context.getCacheDir() + "/" + id + ".playsource.cache");
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);
    }

    private static Gson gson = new Gson();
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

        //cp code
        if(ps.cp_code != 0){
            intent.putExtra("media_source", ps.cp_code);
        }else {
            Log.e(TAG, "why come here, server should send the cp info");
            if (ps.cp.equals("sohu"))
                intent.putExtra("media_source", 3);
            else
                intent.putExtra("media_source", 8);
        }

        intent.putExtra("available_episode_count", media.items.size());
        intent.putExtra("media_poster_url", media.poster);
        intent.putExtra("media_set_name", episode.name);
        intent.putExtra("mediaTitle", media.name);

        //sdkinfo for cp video player
        if(ps.app_info.has("vid") ==  true){
            intent.putExtra("sdkinfo", ps.app_info.toString());
        }else {
            Log.e(TAG, "why come here, server should send the sdkinfo");
            if(ps.cp.equals("sohu")) {
                intent.putExtra("sdkinfo", String.format("{\"vid\":%1$s, \"resolutionmap\":1, \"site\":1}", ps.cp_id));
            }else if(ps.cp.equals("iqiyi")){
                intent.putExtra("sdkinfo", String.format("{\"vid\":\"%1$s\", \"resolutionmap\":1, \"site\":1}", ps.h5_url));
            }
        }
        intent.putExtra("sdkdisable", false);
        intent.putExtra("item", item);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        item.target.entity = "pvideo";
        iDataORM.getInstance(context).addFavor(context, item.ns, iDataORM.HistoryAction, item.id, item);
        MiPushClient.subscribe(context, item.id, null);
    }

    public static View findFilterBlockView(ViewGroup view){
        if(view == null)
            return null;

        if(SelectItemsBlockView.class.isInstance(view ))
            return view;

        int size = view.getChildCount();
        for (int i=0;i<size;i++){
            View item =  view.getChildAt(i);

            if(SelectItemsBlockView.class.isInstance(item ))
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
