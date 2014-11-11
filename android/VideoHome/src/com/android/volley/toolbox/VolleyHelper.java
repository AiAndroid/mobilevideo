package com.android.volley.toolbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VolleyHelper {
    private final static String TAG="VolleyHelper";
	private static final int IMAGE_CACHE_SIZE = (int) (0.3f * Runtime.getRuntime().maxMemory());
	private static VolleyHelper mInstance;
	private RequestQueue mAPIRequestQueue, mImageLoaderRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;

	public static synchronized VolleyHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new VolleyHelper(context);
		}
		return mInstance;
	}

	private VolleyHelper(Context context) {
		mCtx = context.getApplicationContext();
        mAPIRequestQueue = getAPIRequestQueue();
        mImageLoaderRequestQueue = getImageLoaderRequestQueue();

		mImageLoader = new ImageLoader(mImageLoaderRequestQueue, new ImageLoader.ImageCache() {
            int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
            int cacheSize = (int) ((maxMemory/8));

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount()/1024;
                }
            };

			@Override
			public Bitmap getBitmap(String url) {
                Bitmap tmp =  cache.get(url);
                return tmp;
			}

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				cache.put(url, bitmap);
            }
		});
	}

	public RequestQueue getAPIRequestQueue() {
		if (mAPIRequestQueue == null) {
			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
            mAPIRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mAPIRequestQueue;
	}

    public RequestQueue getImageLoaderRequestQueue() {
        if (mImageLoaderRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mImageLoaderRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), null, "images", 100*1014*1024);
        }
        return mImageLoaderRequestQueue;
    }

	public <T> void addToAPIRequestQueue(Request<T> req) {
		getAPIRequestQueue().add(req);
	}
    public <T> void addToImageLoaderRequestQueue(Request<T> req) {
        getAPIRequestQueue().add(req);
    }

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}
