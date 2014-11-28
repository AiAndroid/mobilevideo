package com.video.ui;

import android.app.Application;
import android.content.Context;
import android.os.StatFs;
import android.util.Log;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by liuhuadong on 11/18/14.
 */
public class MobileVideoApplication extends Application{
    private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final int MAX_DISK_CACHE_SIZE = 100 * 1024 * 1024; // 50MB
    private LruCache cache;

    @Override
    public void onCreate() {
        super.onCreate();
        File cacheFile = createDefaultCacheDir(getApplicationContext());

        cache = new LruCache(getApplicationContext());
        Log.d("MobileVideoApplication", " max siz="+cache.maxSize());
        Picasso picasso = new Picasso.Builder(getApplicationContext()).memoryCache(cache).downloader(new OkHttpDownloader(cacheFile, calculateDiskCacheSize(cacheFile))).build();
        Picasso.setSingletonInstance(picasso);

        Picasso.with(getApplicationContext()).setLoggingEnabled(true);
        Picasso.with(getApplicationContext()).setIndicatorsEnabled(true);
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
