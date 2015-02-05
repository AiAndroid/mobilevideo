package com.video.ui.view.user;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.video.ui.R;
//import miui.accounts.ExtraAccountManager;

public class UserHeadView extends FrameLayout {

	private Context mContext;
	
	//UI
	private View mContentView;
	private UserHeadImageView mUserHeadIv;
	private TextView mUserHeadName;
	private TextView mUserHeadIdentity;

	public UserHeadView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UserHeadView(Context context) {
		this(context, null, 0);
	}

	public UserHeadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}
	
	//init
	private void init() {
		mContentView = View.inflate(mContext, R.layout.user_head, null);
		addView(mContentView);
		
		mUserHeadIv = (UserHeadImageView) mContentView.findViewById(R.id.user_head_iv);
		mUserHeadName = (TextView) mContentView.findViewById(R.id.user_head_name);
		mUserHeadIdentity = (TextView) mContentView.findViewById(R.id.user_head_identity);

		refresh();
	}

	private void refresh() {
		AccountManager mAccountManager = AccountManager.get(getContext());
		Account[] account = mAccountManager.getAccountsByType("com.xiaomi");
		if(account.length == 0) {
			showNotLogin();
		} else {
			showLogin(account[0]);
		}
	}
	
	private void showLogin(Account account) {
		mUserHeadName.setText(account.name);
		mUserHeadIdentity.setText(account.name);
		mUserHeadIv.setBackgroundResource(R.drawable.user_head_default);
		//fetchImage();

		//final Account xiaomiAccount = ExtraAccountManager.getXiaomiAccount(getContext());
	}
	
	private void showNotLogin() {
		mUserHeadName.setText(R.string.not_login);
		mUserHeadIdentity.setText(R.string.click_to_login);
		//fetchImage();
	}

}
