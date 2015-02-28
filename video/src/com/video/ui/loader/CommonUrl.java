package com.video.ui.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.video.ui.idata.iDataORM;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class CommonUrl extends CommonBaseUrl{

    public static String BaseURL = "http://mv.mitvos.com/api/a1/";

    public static void setBaseURL(String url){
        BaseURL = url;
    }

	public static int fetchedBaseUrl = -1;
	public CommonUrl(Context appContext) {
		super(appContext);
		getBaseURLFromLoacalSetting();

	}

	@Override
	protected void getBaseURLFromLoacalSetting() {
		if(fetchedBaseUrl == -1) {
			String base_url = iDataORM.getInstance(mAppContext).getSettingValue("base_url");
			if (TextUtils.isEmpty(base_url) == false) {
				BaseURL = base_url;
			}

			fetchedBaseUrl = 1;
		}
	}

}
