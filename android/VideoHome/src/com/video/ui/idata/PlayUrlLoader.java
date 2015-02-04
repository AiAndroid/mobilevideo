/**
 *   Copyright(c) 2014 XiaoMi TV Group
 *   
 *   PlayUrlLoader.java
 *  
 *   @author tianli(tianli@xiaomi.com)
 * 
 *   @date 2014-7-8
 */

package com.video.ui.idata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * @author tianli
 *
 */
public class PlayUrlLoader {
	
    private final static String TAG = "PlayUrlLoader";
    
	private String mHtml5;
	private int mSource;
	private Context mContext;
	private WebView mWebView = null;
	private String mPlayUrl;
    private Html5PlayUrlRetriever mUrlRetriever = null;
    private final Object mSyncLock = new Object();
    private Handler mHandler = new Handler(Looper.getMainLooper());

	private boolean isQIYI = true;
    
	public PlayUrlLoader(Context context, String html5, int source){
		mHtml5 = html5;
		mSource = source;
		mContext = context.getApplicationContext();
	} 

	public static interface H5OnloadListener{
		public void playUrlFetched(boolean result, String playurl, WebView webView);
	}

	private H5OnloadListener urlLoaderListener;
	public void get(int timeout, H5OnloadListener listener){
		urlLoaderListener = listener;
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				initWebView();
				mWebView.loadUrl(mHtml5);
				startUrlRetriever();
			}
		});
	}
	
	private void release(){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mWebView.destroy();
				mWebView = null;
			}
		}, 500);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		mWebView = new WebView(mContext);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setJavaScriptEnabled(true);

	    mWebView.setHttpAuthUsernamePassword("", "", "", "");

		String newUserAgentString = mWebView.getSettings().getUserAgentString() + " " + "MiuiVideo/1.0";
		mWebView.getSettings().setUserAgentString(newUserAgentString);
		mWebView.clearCache(false);
		mWebView.setWebViewClient(new MyWebViewClient());
	}
	
    private void startUrlRetriever(){
		Log.d(TAG, "startUrlRetriever");
    	WebView webView = mWebView;
    	if(webView != null){
    		if(mUrlRetriever != null){
    			mUrlRetriever.release();
    		}
    		mUrlRetriever = new Html5PlayUrlRetriever(webView, mSource);
    		mUrlRetriever.setPlayUrlListener(mInnerUrlListener);
    		mUrlRetriever.setSkipAd(true);
    		mUrlRetriever.start();
    	}
    }
    
    private Html5PlayUrlRetriever.PlayUrlListener mInnerUrlListener = new Html5PlayUrlRetriever.PlayUrlListener(){
		@Override
		public void onUrlUpdate(String htmlUrl, String url) {
			mPlayUrl = url;

			urlLoaderListener.playUrlFetched(true, mPlayUrl, mWebView);
		}
    };

	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			startUrlRetriever();
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d(TAG, "on page finish: " + url);
			if(isQIYI) {
				mUrlRetriever.startQiyiLoop();
	    	} 
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			return super.shouldInterceptRequest(view, url);
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			return super.shouldOverrideKeyEvent(view, event);
		}
	}

}
