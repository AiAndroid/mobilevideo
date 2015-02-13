package com.video.ui.idata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MediaUrlForPlayerUtil implements Html5PlayUrlRetriever.PlayUrlListener {
	private static final String TAG = MediaUrlForPlayerUtil.class.getName();
	
	private Context mContext;
	private WebView mWebView;
	Handler mHandler = null;

	private PlayUrlObserver mObserver;
    private Html5PlayUrlRetriever mUrlRetriever = null;

	public MediaUrlForPlayerUtil(Context context) {
		this.mContext = context.getApplicationContext();
		mHandler = new Handler(mContext.getMainLooper());
	}

	private String source;
    private synchronized void startUrlRetriever(){
        WebView webView = mWebView;
        if(webView != null){
    	    if(mUrlRetriever != null){
    	 	    mUrlRetriever.release();
    	    }
    	    mUrlRetriever = new Html5PlayUrlRetriever(webView, source);
    	    mUrlRetriever.setPlayUrlListener(this);
    	    mUrlRetriever.setSkipAd(true);
    	    mUrlRetriever.start();
        }
    }
	
	public synchronized void getMediaUrlForPlayer(String  h5_url, String source, PlayUrlObserver observer) {
		Log.d(TAG, "public get media url for player");
		tearDown();
		this.source = source;

		setObserver(observer);
		getPlayerUrl(h5_url);

		mHandler.removeCallbacks(mCancelGetUrlForPlayerRunnalbe);
		mHandler.postDelayed(mCancelGetUrlForPlayerRunnalbe, 35000);
	}
	
	public synchronized void getMediaUrlForPlayer(String mediaUrl, PlayUrlObserver observer) {
		setObserver(observer);
		getPlayerUrl(mediaUrl);
		mHandler.removeCallbacks(mCancelGetUrlForPlayerRunnalbe);
		mHandler.postDelayed(mCancelGetUrlForPlayerRunnalbe, 35000);
	}
	
	public synchronized void tearDown() {
		Log.d(TAG, "tear down");
		mHandler.removeCallbacks(mCancelGetUrlForPlayerRunnalbe);

		if(mUrlRetriever != null){
			mUrlRetriever.release();
		}
		if(mObserver != null) {
			mObserver.onReleaseLock();
		}
		if(mWebView != null) {
			final WebView webView = mWebView;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					webView.destroy();
				}
			}, 500);
			mWebView = null;		
		}
	}
	
	private Runnable mCancelGetUrlForPlayerRunnalbe = new Runnable() {
		
		@Override
		public void run() {
			onGetPlayerUrlCancel();
		}
	};
	
	private synchronized void onGetPlayerUrlFinish(String playUrl, String html5Url) {
		if(mObserver != null){
			mObserver.onUrlUpdate(playUrl, html5Url);
		}
		tearDown();
	}
	
	private synchronized void onGetPlayerUrlError() {
		if(mObserver != null){
			mObserver.onError();
		}
		tearDown();
	}
	
	private synchronized void onGetPlayerUrlCancel() {
		if(mObserver != null){
			mObserver.onError();
		}
		tearDown();
	}
	
	public synchronized void getPlayerUrl(String url) {
		Log.d(TAG, "get player url ");
		initWebView();
		mWebView.loadUrl(url);
		startUrlRetriever();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private synchronized void initWebView() {
		if(mWebView == null) {
			mWebView = new WebView(mContext);
		}
		mWebView.getSettings().setJavaScriptEnabled(true);
		String newUserAgentString = mWebView.getSettings().getUserAgentString() + " " + "MiuiVideo/1.0";
		mWebView.getSettings().setUserAgentString(newUserAgentString);
		mWebView.clearCache(false);
		{
			mWebView.setHttpAuthUsernamePassword("", "", "", "");
		}

		mWebView.setWebViewClient(new MyWebViewClient());
	}
	
	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			startUrlRetriever();
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d(TAG, "on page finish");
			if(source.equals("iqiyi")) {
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


	@Override
	public void onUrlUpdate(final String htmlUrl, final String playUrl) {
		Log.d(TAG, "url for player getting res: " + playUrl);
    	if (!TextUtils.isEmpty(playUrl)) {
			Log.d(TAG, "url for player done: " + playUrl);
			onGetPlayerUrlFinish(playUrl, htmlUrl);
		}
	}
	
	public void setObserver(PlayUrlObserver observer) {
		this.mObserver = observer;
	}

	public interface PlayUrlObserver {
		public void onUrlUpdate(String playUrl, String html5Url);
		public void onError();
		public void onReleaseLock();
	}
}
