package com.video.ui.view.detail;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.VolleyHelper;
import com.video.ui.DisplayItemActivity;
import com.video.ui.R;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;
import com.video.ui.push.Util;
import com.video.ui.utils.VideoUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 *@author tangfuling
 *
 */

public class CommentEditActivity extends DisplayItemActivity {
	private static String TAG = CommentEditActivity.class.getName();
	
	public static final String KEY_MEDIA_INFO = "mediaInfo";
	
	//UI
	private TextView mBtnBack;
	private TextView mBtnSend;
	private TextView mTopTitle;
	private EditText mEditText;
	
	private ScoringView mScoringView;

	//data
	private String mCommentStr = "";
	private int mScore = 10;
	
	private int MIN_COMMENT_LEN;
	private int MAX_COMMENT_LEN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_edit);
		init();
		new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.showInputMethodWindow(CommentEditActivity.this, mEditText);
            }
        }, 300);
	}
	
	//init
	private void init() {
		initDimen();
		initTopTitle();
	}
	
	private void initDimen() {
		MIN_COMMENT_LEN = getResources().getInteger(R.integer.comment_min_length);
		MAX_COMMENT_LEN = getResources().getInteger(R.integer.comment_max_length);
	}
	
	private void initTopTitle() {
		mTopTitle = (TextView) findViewById(R.id.comment_edit_top_title);
		mEditText = (EditText) findViewById(R.id.comment_edit_et);
		mEditText.addTextChangedListener(mTextWatcher);
		mBtnBack = (TextView) findViewById(R.id.comment_edit_back);
		mBtnSend = (TextView) findViewById(R.id.comment_edit_send);
		mBtnBack.setOnClickListener(mOnClickListener);
		mBtnSend.setOnClickListener(mOnClickListener);
		mScoringView = (ScoringView) findViewById(R.id.comment_edit_score);
		String str = getResources().getString(R.string.media_name_comment);
		if(item != null) {
			str = String.format(str, item.title);
		}
		mTopTitle.setText(str);
	}
	
	//get data
	Account mAccount;
	private void startAuthAccount() {
		AccountManager mAccountManager = AccountManager.get(getBaseContext());
		Account[] account = mAccountManager.getAccountsByType("com.xiaomi");
		if(account.length == 0) {
			mAccountManager.addAccount("com.xiaomi", (String)null, (String[])null, (Bundle)null, this, null, (Handler)null);
		}else {
			Log.d(TAG, "xiaimi account: " + account[0].toString());
			mAccount = account[0];
			uploadComment();
		}
	}
	
	//packaged method
	private void sendComment() {
		mCommentStr = getEditTextUser();
		int length = mCommentStr.length();
		if(length < MIN_COMMENT_LEN) {
			String str = getString(R.string.comment_count_less_tip);
			str = String.format(str, MIN_COMMENT_LEN);
			Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
		} else if(length > MAX_COMMENT_LEN) {
			String str = getString(R.string.comment_count_much_tip);
			str = String.format(str, MAX_COMMENT_LEN);
			Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
		} else if(!mScoringView.isUserRated()) {
			Toast.makeText(getApplicationContext(), R.string.click_star_rate, Toast.LENGTH_SHORT).show();
		} else {
			startAuthAccount();
		}
	}
	
	private String getEditTextUser() {
		return mEditText.getText().toString().trim();
	}
	
	private void uploadComment() {
		mScore = mScoringView.geCurtScore();
		if(item != null) {
			String commentURL = CommonUrl.BaseURL + "action/comment";
			Response.Listener<Boolean> listener = new Response.Listener<Boolean>() {
				@Override
				public void onResponse(Boolean response) {
					//
					Log.d(TAG, "comment success!");
					Intent result = new Intent();
					DetailCommentView.VideoComments.VideoComment vcitem = new DetailCommentView.VideoComments.VideoComment();
					vcitem.comment = mCommentStr;
					vcitem.score   = mScore;
					vcitem.uid     = mAccount!=null?mAccount.name:getString(R.string.xiaomi_user) ;
					result.putExtra("comment", vcitem);
					setResult(RESULT_OK, result);
					finishActivity(100);
				}
			};

			Response.ErrorListener errorListener = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {Log.d(TAG, "fail to fetch comments:"+item);	}
			};

			JSONObject  sb = new JSONObject();
			try {
				sb.put("vid",     VideoUtils.getVideoID(item.id));
				sb.put("comment", mCommentStr);
				sb.put("score",   mScore);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			RequestQueue requestQueue = VolleyHelper.getInstance(getApplicationContext()).getAPIRequestQueue();
			BaseGsonLoader.PostRequest<Boolean> gsonRequest = null;
			try {
				gsonRequest = new BaseGsonLoader.PostRequest<Boolean>(commentURL, null, sb.toString().getBytes("utf-8"),  listener, errorListener);
				requestQueue.add(gsonRequest);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	//UI callback
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id == R.id.comment_edit_back) {
	             Util.closeInputMethodWindow(CommentEditActivity.this);
	             finish();
			} else if(id == R.id.comment_edit_send) {
				sendComment();
			}
		}
	};
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			if(text.length() == MAX_COMMENT_LEN) {
				String str = getString(R.string.comment_count_much_tip);
				str = String.format(str, MAX_COMMENT_LEN);
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			}
		}
	};

}
