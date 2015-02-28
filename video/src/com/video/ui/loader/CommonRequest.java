package com.video.ui.loader;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;

public abstract class CommonRequest<T> extends Request<T> {

	private CommonBaseUrl mCommonUrl;

	public CommonRequest(Context context, int method, String url, ErrorListener listener) {
		super(method, url, listener);

		mCommonUrl = new CommonUrl(context);
	}

	@Override
	public String getUrl() {
		String url = super.getUrl();
		return mCommonUrl.addCommonParams(url);
	}

}
