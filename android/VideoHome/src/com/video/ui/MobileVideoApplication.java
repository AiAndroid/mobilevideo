package com.video.ui;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.AppVersion;
import com.tv.ui.metro.model.PlaySource;
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.push.MiPushManager;
import com.video.ui.utils.VideoUtils;

import java.io.File;
import java.util.ArrayList;

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
                checkVerison();
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
                        android.app.DownloadManager dm = (android.app.DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

                        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(response.apk_url));
                        request.setMimeType("application/vnd.android.package-archive");
                        request.setMimeType(VideoUtils.getMimeType(response.apk_url));
                        request.setTitle(response.version_name);
                        request.setDescription(response.released_by);
                        request.setVisibleInDownloadsUi(true);
                        request.setShowRunningNotification(true);
                        int downloadFlag = DownloadManager.Request.NETWORK_WIFI;
                        if(iDataORM.isOpenCellularOfflineHint(getApplicationContext()) == false){
                            downloadFlag |=DownloadManager.Request.NETWORK_MOBILE;
                        }
                        request.setAllowedNetworkTypes(downloadFlag);
                        request.allowScanningByMediaScanner();

                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "." +MimeTypeMap.getFileExtensionFromUrl(response.apk_url);
                        request.setDestinationUri(Uri.fromFile(new File(path)));

                        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE|android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        download_id = dm.enqueue(request);
                        Log.d(TAG, "add to download apk :"+appversion + " down: "+download_id);

                        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
                        getApplicationContext().registerReceiver(receiver, filter);
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

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {

                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if(downloadId == download_id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            Intent actionIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            actionIntent.setDataAndType(Uri.parse(uriString), "application/vnd.android.package-archive");
                            actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(actionIntent);
                        }
                    }
                }
            }
        }
    };
    private long download_id;

    @Override
    public void onTerminate(){
        super.onTerminate();

        MVDownloadManager.getInstance(getBaseContext()).stop(getBaseContext());
        getApplicationContext().unregisterReceiver(receiver);
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
