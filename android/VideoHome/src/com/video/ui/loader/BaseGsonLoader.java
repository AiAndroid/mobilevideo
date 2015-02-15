package com.video.ui.loader;

import android.content.Context;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tv.ui.metro.model.DisplayItem;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by tv metro on 9/1/14.
 */
public abstract  class BaseGsonLoader<T> extends Loader<T> {
    private final  static String TAG = "BaseGsonLoader";

    protected       int page      = 1;
    protected final int page_size = 20;
    protected          T       mResult;
    protected volatile boolean mIsLoading;
    private ProgressNotifiable mProgressNotifiable;
    private boolean            mHasDeliveredResult;

    protected String cacheFileName = "";
    public abstract void setCacheFileName();

    private static boolean mEnableCache = true;
    public static void enableCache(boolean _enable){
        mEnableCache = _enable;
    }
    
    protected String calledURL = "";
    public abstract void setLoaderURL(DisplayItem obj);

    public BaseGsonLoader(Context context) {
        super(context);
        init(null);
    }

    protected DisplayItem mItem;
    private void init(DisplayItem item){
        mIsLoading = false;
        mHasDeliveredResult = false;
        mItem = item; 
        setCacheFileName();
        setLoaderURL(item);
    }

    public BaseGsonLoader(Context context, DisplayItem item, int setPage) {
        super(context);
        page = setPage;
        init(item);
    }

    public void setCurrentPage(int setPage){
        page = setPage;
    }

    public int getCurrentPage(){
        return page;
    }
    public void setProgressNotifiable(ProgressNotifiable progressNotifiable) {
        this.mProgressNotifiable = progressNotifiable;
        if (progressNotifiable != null) {
            progressNotifiable.init(dataExists(), mIsLoading);
        }
    }

    protected boolean dataExists() {
        // data exist and delivered to UI
        return mResult != null && mHasDeliveredResult;
    }

    @Override
    protected void onStartLoading() {
        if(mResult != null){
            deliverResult(mResult);
        }

        if (!mIsLoading && (mResult == null || takeContentChanged())) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {

        //load from server
        mIsLoading = true;
        if (mProgressNotifiable != null && page == 1) {
            mProgressNotifiable.startLoading(dataExists());
        }
        loadDataByGson();
    }


    protected Response.Listener<T> listener = new Response.Listener<T>() {
        @Override
        public void onResponse(T response) {
            mResult = response;
            deliverResult(response);
            mHasDeliveredResult = true;
            mIsLoading = false;

            if (mProgressNotifiable != null) {
                mProgressNotifiable.stopLoading(dataExists(), false);
            }
        }
    };

    protected Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse error:" + error.toString());
            //means load from next page
            if(page > 1){
                page--;
            }

            mIsLoading = false;
            if (mProgressNotifiable != null) {
                mProgressNotifiable.stopLoading(dataExists(), false);
            }

            deliverResult(null);
        }
    };

    protected abstract void loadDataByGson();

    protected String mKeyword;
    public void setSearchKeyword(String key) {
        mKeyword = key;
    }
    public static class GsonRequest<TV> extends Request<TV> {
        private final Gson gson = new Gson();
        private final Type type;
        private final Map<String, String> headers;
        private final Response.Listener<TV> listener;
        private String cacheFile;

        public void setCacheNeed(String _cacheFile){
            cacheFile = _cacheFile;
        }

        public GsonRequest(String url, Type type, Map<String, String> headers,
                           Response.Listener<TV> listener, Response.ErrorListener errorListener) {
            super(Method.GET, url, errorListener);
            this.type = type;
            this.headers = headers;
            this.listener = listener;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected void deliverResponse(TV response) {
            listener.onResponse(response);
        }

        @Override
        protected Response<TV> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                Log.d(TAG, "response json:" + json);
                long timeStart = System.currentTimeMillis();
                TV fromJson = gson.fromJson(json, type);
                long timeEnd = System.currentTimeMillis();
                Log.d(TAG, "fromJson take time in ms: " + (timeEnd - timeStart));
                Response<TV> res =  Response.success(fromJson, HttpHeaderParser.parseCacheHeaders(response));

                if(mEnableCache && cacheFile != null && cacheFile.length() > 0){
                    //save to files
                    updateToFile(cacheFile, json);
                }
                return  res;
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }

    public static class PostRequest<TV> extends Request<TV> {
        private final Map<String, String> headers;
        private final byte []             body;
        private final Response.Listener<TV> listener;

        public PostRequest(String url, Map<String, String> headers, byte[] body,
                           Response.Listener<TV> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            this.headers = headers;
            this.listener = listener;
            this.body = body;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            return body != null ? body : super.getBody();
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected void deliverResponse(TV response) {
            listener.onResponse(response);
        }

        @Override
        protected Response<TV> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                Log.d(TAG, "response json:" + json);
                Response<TV> res =  Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                return  res;
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }



    public static void createDir(String filename){
        new File(filename).mkdirs();
    }

    public static void updateToFile(String fileName, String response){
        if(null == response || TextUtils.isEmpty(fileName)){
            return;
        }
        createDir(fileName);

        File f = new File(fileName);
        if(f.exists()){
            f.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(response.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static StringBuilder readCacheFromFile(String filePath){
        StringBuilder sb = new StringBuilder();
        File f = new File(filePath);
        if(f.exists() == false){
            return  sb;
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(filePath));
            byte []buffer = new byte[4096*2];
            int len = -1;
            while((len = fin.read(buffer, 0, 4096*2)) > 0){
                sb.append(buffer);
            }
            buffer = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fin != null){
                try {
                    fin.close();
                    fin = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb;
    }
}
