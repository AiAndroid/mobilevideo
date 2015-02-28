package com.video.ui;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.AppVersion;
import com.video.ui.idata.BackgroundService;
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.push.MiPushManager;

import java.io.File;

/**
 * Created by liuhuadong on 11/18/14.
 */
public class MobileVideoApplication extends Application{
    private static final String TAG = "MobileVideoApplication";
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 50MB
    private LruCache cache;

    @Override
    public void onCreate() {
        super.onCreate();

        MVDownloadManager.getInstance(getBaseContext());
        BackgroundService.registerDownloadMoniter(getApplicationContext());
        MiPushManager.getInstance(getBaseContext());
        File cacheFile = createDefaultCacheDir(getApplicationContext());

        cache = new LruCache(getApplicationContext());
        Log.d("MobileVideoApplication", " max siz="+cache.maxSize());
        Picasso picasso = new Picasso.Builder(getApplicationContext()).memoryCache(cache).downloader(new OkHttpDownloader(cacheFile, calculateDiskCacheSize(cacheFile))).build();
        Picasso.setSingletonInstance(picasso);

        Picasso.with(getApplicationContext()).setLoggingEnabled(false);
        Picasso.with(getApplicationContext()).setIndicatorsEnabled(false);

        //check whether have new version
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(iDataORM.getBooleanValue(getApplicationContext(), "development_app", true) == true) {
                    checkVerison();
                }
            }
        }, 10000);
    }
    String appversion = "https://github.com/AiAndroid/mobilevideo/raw/master/appupgrade.json";
    private void checkVerison(){
        Response.Listener<AppVersion> listener = new Response.Listener<AppVersion>() {
            @Override
            public void onResponse(AppVersion response) {
                PackageManager pm = getApplicationContext().getPackageManager();
                try {
                    int verionCode = pm.getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode;
                    if(response.version_code > verionCode){
                        Log.d(TAG, "add to download apk :"+appversion);
                        BackgroundService.startDownloadAPK(getApplicationContext(), response.apk_url, response.version_name, response.released_by + " @" + response.release_date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(getApplicationContext()).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<AppVersion> gsonRequest = new BaseGsonLoader.GsonRequest<AppVersion>(appversion, new TypeToken<AppVersion>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(getApplicationContext().getCacheDir() + "/app_version.cache");
        gsonRequest.setShouldCache(true);
        requestQueue.add(gsonRequest);
    }

    @Override
    public void onTerminate(){
        super.onTerminate();

        MVDownloadManager.getInstance(getBaseContext()).stop(getBaseContext());
        BackgroundService.unRegisterDownloadApkMonitor(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        Log.d("MobileVideoApplication", "onLowMemory");
        cache.clear();
    }

    static File createDefaultCacheDir(Context context) {
        File cache = new File(context.getApplicationContext().getCacheDir(), "picasso-cache");
        if (!cache.exists()) {
            //noinspection ResultOfMethodCallIgnored
            cache.mkdirs();
        }
        return cache;
    }

    static long calculateDiskCacheSize(File dir) {
        long size = MIN_DISK_CACHE_SIZE;

        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            // Target 2% of the total space.
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }

        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
    }
}
