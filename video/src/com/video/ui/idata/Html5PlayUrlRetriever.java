/**
 * 
 */
package com.video.ui.idata;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tianli
 *
 */
public class Html5PlayUrlRetriever {

	public interface JSObject {

	}

	private static final String TAG = "html5";
	
	private static final int URL_LOOP_INTERVAL_QIYI = 3000;
	private static final int URL_LOOP_INTERVAL = 2000;
	
	private String mSource ;
	private String mPlayUrl = null;
	
	private boolean mAutoPlay = false;
	
	private boolean mVideoReady = false;
	private boolean mReleased = false;
	
	private LogCatMonitor mLogCat = null;
	private Handler mHandler;
	private PlayUrlListener mPlayUrlListener = null;
	private WebView mWebView = null;
	
	private boolean mSkipAd = false;

	
	public Html5PlayUrlRetriever(WebView webView, String source){
		init(webView, source);
	}
	
	public void setSkipAd(boolean skip){
		mSkipAd = skip;
	}

	private int step=0;
	private void init(WebView webView, String source){
		mSource = source;
		mWebView = webView;
		Context context = webView.getContext();
		mHandler = new Handler(context.getMainLooper());
		mWebView.addJavascriptInterface(new H5Object(), "Methods");
		mLogCat = new LogCatMonitor();
	}
	
	public synchronized void start(){
		if(mSource.equals("iqiyi")){//iqiyi
			mLogCat.start();
		}else{
			step++;
        	getVideoUrlLoop(2000);

			Log.d(TAG, "times step:"+step + " to fetch url:"+mSource);
		}

		mAutoPlay = true;
	}
	
	public synchronized void startQiyiLoop(){
		getVideoUrlQiyiLoop(3000);
	}
	
    private synchronized void getVideoUrlLoop(int delay) {
    	if(!mReleased){
    		Log.i(TAG, "get video url");
    		mHandler.removeCallbacks(mGetVideoUrlRunnale);
			if(step <= 60) {
				mHandler.postDelayed(mGetVideoUrlRunnale, delay);
			}else {
				Log.d(TAG, "not find result for 60 times");
			}
    	}
    }
    
    private void onUrlFound(final String pageUrl, final String url){
    	mHandler.post(new Runnable() {
			@Override
			public void run() {
				if(!TextUtils.isEmpty(url) && !url.equals(mPlayUrl)){
					WebView webView = mWebView;
					String webUrl = pageUrl;
					if(TextUtils.isEmpty(webUrl)){
						webUrl = webView.getUrl();
					}
					mPlayUrl = url;
					notifyUrlReady(webUrl, mPlayUrl);
				}
			}
		});
    }
    
    private void getVideoUrlQiyiLoop(int delay) {
    	if(!mReleased){
    		Log.i(TAG, "get video url qiyi");
    		mHandler.removeCallbacks(mGetVideoUrlQiyiRunnale);
    		mHandler.postDelayed(mGetVideoUrlQiyiRunnale, delay);
    	}
    }
    
    private Runnable mGetVideoUrlQiyiRunnale = new Runnable() {
		@Override
		public void run() {
			Log.i(TAG, "get video url qiyi run...");
			if(!mReleased && TextUtils.isEmpty(mPlayUrl)) {
				exeJs(JS_GET_URL);
				getVideoUrlQiyiLoop(URL_LOOP_INTERVAL_QIYI);
			}
		};
    };
    
    private Runnable mGetVideoUrlRunnale = new Runnable() {
		@Override
		public void run() {
			Log.i(TAG, "get video url run...");
			if(!mReleased && !mVideoReady && TextUtils.isEmpty(mPlayUrl)) {
				exeJs(JS_GET_URL);
				getVideoUrlLoop(URL_LOOP_INTERVAL);
			}
		};
    };

    
    public boolean isAutoPlay(){
    	return mAutoPlay;
    }
    
    private void exeJs(String js){
    	try {
    		mWebView.loadUrl(js);
		} catch (Exception e) {
		}
    }
	
	public synchronized void release(){
		mReleased = true;
		mVideoReady = false;
		mPlayUrl = null;
		setPlayUrlListener(null);
    	mHandler.removeCallbacks(mGetVideoUrlQiyiRunnale);
    	mHandler.removeCallbacks(mGetVideoUrlRunnale);
		if(mSource.equals("iqiyi")){
			mLogCat.interrupt();
		}

		Log.d(TAG, "I am just released: "+this);
	}
	
	private synchronized void notifyUrlReady(String pageUrl, String playUrl){
		Log.d(TAG, "find url:"+playUrl + " this:"+this + " mReleased:"+mReleased );
		if(mPlayUrlListener != null){
			mPlayUrlListener.onUrlUpdate(pageUrl, playUrl);
		}
	}
	
	public synchronized void setPlayUrlListener(PlayUrlListener playUrlListener) {
		this.mPlayUrlListener = playUrlListener;
	}

	public class H5Object implements JSObject{
		@JavascriptInterface
        public boolean isVideoReady() {
			Log.d(TAG, "isVideoReady " + mVideoReady);
        	return mVideoReady;
        }

		@JavascriptInterface
        public void onVideoReady() {
			Log.d(TAG, "onVideoReady.");
        	mVideoReady = true;
        }

		@JavascriptInterface
		public boolean canAutoPlay(){
			return true;
		}

		@JavascriptInterface
		public boolean canSkipAd(){
			return mSkipAd;
		}
		
		private String mLastUrl = null;

		@JavascriptInterface
        public void getUrl(final String pageUrl, final String playUrl, boolean neverAd) {
			Log.d(TAG, "getUrl pageUrl = " + pageUrl + ", playUrl = " + playUrl);
			if(playUrl.contains(".html")){
				return;
			}
			if(neverAd){
				onUrlFound(pageUrl, playUrl);
				return;
			}
			if(mLastUrl != null && mLastUrl.equals(playUrl)){
				return;
			}
			mLastUrl = playUrl;
			onUrlFound(pageUrl, playUrl);

			Log.d(TAG, "i am released: "+mReleased);
        }
    }
	
	private class LogCatMonitor extends Thread{
		@Override
		public void run() {
			try{
				Pattern pattern = Pattern.compile("MediaPlayer.* [uU]ri is .*(http\\:.*)");
				Process logProc = Runtime.getRuntime().exec("logcat -c");
				logProc.waitFor();
				logProc = Runtime.getRuntime().exec("logcat");
				BufferedReader br = new BufferedReader(new InputStreamReader(
						logProc.getInputStream()));
				do{
					String line = br.readLine();
					Matcher matcher = pattern.matcher(line);
					if(matcher.find()){
						String url = matcher.group(1).trim();
						matcher.group();
						Log.d(TAG, "onUrlFound " + url);
						onUrlFound(null, url);
						break;
					}
				}while(!isInterrupted());
			}catch(Exception e){
			}
		}
	}

	
    private static final String JS_GET_URL = "javascript:(function() {" +
			"if(window.Methods == undefined || window.Methods.isVideoReady == undefined || window.Methods.isVideoReady()){" +
			"	return;" +
			"}" +
		    "var pageUrl = window.location.href;" +
		    "var videoTags = document.getElementsByTagName('video');" +
		    "if(videoTags == null || videoTags == undefined || videoTags.length == 0){" +
		    	"return;" +
		    "}" +
	    	"window.Methods.onVideoReady();" +
	    	"var url = videoTags[0].getAttribute('src');" +
	    	"var sources = videoTags[0].getElementsByTagName('source');" +
	    	"if(sources != null && sources != undefined){" +
	    	"    for(var i = 0; i < sources.length; i++){" +
	    	"        var source = sources[i].getAttribute('src');" +
	    	"        if(source != null || source != undefined || source.length > 0){" +
	    	"            url = source;" +
	    	"	         break;"+
	    	"        }"+
	    	"    }"+
	    	"}"+
	    	"if(window.Methods != undefined && window.Methods.canAutoPlay != undefined" +
	    	"   && window.Methods.canAutoPlay()){" +
	    	"   videoTags[0].play();" +
			"}" +
	    	"if(url != null && url != undefined && url.length > 0) {" +
				"var adover = null;" + 
				"if(videoTags != null && videoTags != undefined && videoTags.length > 0){" +
				"    adover = videoTags[0].getAttribute('data-adover');" +
				"}" +
	            "if(adover == undefined || adover == null){" +
	    	        "window.Methods.getUrl(pageUrl, url, false);" +
	            "}else if(adover){" +
    	            "window.Methods.getUrl(pageUrl, url, false);" +
                "}" +
	    	"}" + 
		    "videoTags[0].addEventListener('durationchange',function() {" + 
		    	"var videoTags = document.getElementsByTagName('video');" +
		    	"if(videoTags == null || videoTags == undefined || videoTags.length == 0){" +
		    	"    return;" +
		    	"}" +
		    	"var url = videoTags[0].getAttribute('src');" +
		    	"if(window.Methods != undefined && window.Methods.canSkipAd != undefined &&" +
				"	 window.Methods.canSkipAd()){" +
		    	"    if(videoTags[0].duration >= 5 && videoTags[0].duration <= 30){" +
				"        videoTags[0].currentTime = videoTags[0].duration - 1;" +
				"        videoTags[0].play();" +
				"    }" +
				"}" +
		    	"var sources = videoTags[0].getElementsByTagName('source');" +
		    	"    if(sources != null && sources != undefined){" +
		    	"        for(var i = 0; i < sources.length; i++){" +
		    	"            var source = sources[i].src;" +
		    	"            if(source != null || source != undefined || source.length > 0){" +
		    	"                url = source;" +
		    	"				 break;"+
		    	"            }"+
		    	"        }"+
		    	"    }"+
		    	"if(url == null || url == undefined || url.length == 0){" +
		    	"    return; " +
		    	"}" + 
		    	"if(url == null || url == undefined || url.length == 0){" +
		    	"    return; " +
		    	"}" + 
			    "var video = document.getElementsByTagName('video');" +
			    "var adover = null;" + 
			    "if(video != null && video != undefined && video.length > 0){" +
			    "    adover = video[0].getAttribute('data-adover');" +
			    "}" +
			    "if(adover == undefined || adover == null){" +
    	            "window.Methods.getUrl(pageUrl, url, false);" +
                "}else if(adover){" +
	                "window.Methods.getUrl(pageUrl, url, false);" + // set false temporarily.
                "}" +		    
	        "}, false); " +
//		    "videoTags[0].addEventListener('durationchange',function() {" + 
//			"	var videoTags = document.getElementsByTagName('video');" +
//			"	if(videoTags == null || videoTags == undefined || videoTags.length == 0){" +
//			"  		return;" +
//			"	}" +
//			"	if(videoTags[0].duration > 5 && videoTags[0].duration < 60){" +
//			"	    videoTags[0].currentTime = videoTags[0].duration - 1;" +
//			"	    videoTags[0].play();" +
//			"   }" +
//			"}, false); " +
        "})()";
	
	public interface PlayUrlListener {
		public void onUrlUpdate(String htmlUrl, String url);
	}
}
