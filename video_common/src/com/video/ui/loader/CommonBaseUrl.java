package com.video.ui.loader;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public abstract class CommonBaseUrl {
	protected Context mAppContext;

    public static String BaseURL = "http://mv.mitvos.com/api/a1/";

    public static void setBaseURL(String url){
        BaseURL = url;
    }

	public static int fetchedBaseUrl = -1;
	public CommonBaseUrl(Context appContext) {
		mAppContext = appContext.getApplicationContext();
	}

	protected abstract void getBaseURLFromLoacalSetting();

	public String addCommonParams(String url) {
		CommonUrlBuilder urlBuilder = new CommonUrlBuilder(url);

		urlBuilder.put("locale", getLocale());
		urlBuilder.put("res", getResolution());
		urlBuilder.put("ptf", getProduct());
		urlBuilder.put("device_id", getDeviceId(mAppContext));
		urlBuilder.put("key", Constants.KEY);

		String tmpUrl = urlBuilder.toUrl();
		String path;
		try {
			path = new URL(tmpUrl).getPath();
		} catch (MalformedURLException e) {
			return tmpUrl;
		}

		int indexOfPath = tmpUrl.indexOf(path);
		String strForSign = tmpUrl.substring(indexOfPath);
		Log.d("xxx", "strForSign " + strForSign);
		String sign = genSignature(strForSign);
		Log.d("xxx", "sign " + sign);

		urlBuilder.put("opaque", sign);

		return urlBuilder.toUrl();
	}

	protected String getLocale() {
		Locale locale = mAppContext.getResources().getConfiguration().locale;
		return locale.getLanguage() + "_" + locale.getCountry();
	}

	protected String getResolution() {
		DisplayMetrics displaymetrics = mAppContext.getResources().getDisplayMetrics();
		if (displaymetrics.widthPixels == 720) {
			return "hd720";
		} else if (displaymetrics.widthPixels == 1080) {
			return "hd1080";
		}else if (displaymetrics.widthPixels == 1440) {
			return "hd1440";
		}
		else if (displaymetrics.widthPixels == 2160) {
			return "hd2160";
		} else {
			return displaymetrics.widthPixels + "x" + displaymetrics.heightPixels;
		}
	}

	protected String getProduct() {
		return "601";
	}

	protected static String sDeviceId;

	protected static String getDeviceId(Context con) {
		if (sDeviceId == null) {
            //TODO, add owner define
			if ((null == sDeviceId) || sDeviceId.isEmpty()) {
				sDeviceId = "81816e93dd774fa3a422825976434adf";
			}
		}
		return sDeviceId;
	}

	protected String genSignature(String str) {
		String tmpUrlStr;
		tmpUrlStr = str + "&token=" + Constants.TOKEN;
		Log.d("xxx", tmpUrlStr);

		String opaque = null;
		try {
			opaque = Utils.getSignature(tmpUrlStr.getBytes(), Constants.SSEC.getBytes());
		} catch (InvalidKeyException e) {
			Log.e("InvalidKeyException", "InvalidKeyException");
		} catch (NoSuchAlgorithmException e) {
			Log.e("NoSuchAlgorithmException", "NoSuchAlgorithmException");
		}

		return opaque;
	}
}
