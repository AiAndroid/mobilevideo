package com.aimashi.mobile.video.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.video.ui.R;

/**
 * Created by TV Metro on 9/1/14.
 */
public class TitleBar extends LinearLayout {

    private TextView mTitleView;
    public TitleBar(Context context) {
        this(context, null, 0);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int UIStyle) {
        super(context, attrs, UIStyle);
        init(context);
    }

    public  void enableSeach(boolean enable){
        if(findViewById(R.id.album_search) != null)
            findViewById(R.id.album_search).setEnabled(enable);
        if(findViewById(R.id.title_bar_search) != null)
            findViewById(R.id.title_bar_search).setEnabled(enable);
    }

    public void setTitle(int resid){
        mTitleView.setText(resid);
    }

    public void setTitle(CharSequence text){
        mTitleView.setText(text);
    }

    private View.OnClickListener mClickListner;
    public void setBackPressListner(View.OnClickListener clickListener){
        mClickListner = clickListener;
        View view = findViewById(R.id.back_container);
        if(view != null)view.setOnClickListener(mClickListner);
    }

    public static interface OnCastClick{
        public void OnCaseContentToTV(View view);
    }
    private OnCastClick mCastClick;
    public void setOnCastClick(OnCastClick castClick){
        mCastClick = castClick;
    }

    private OnSearchClick mSearchClick;
    public void setOnSearchClick(OnSearchClick searchClick){
        mSearchClick = searchClick;
    }
    public static interface OnSearchClick{
        public void onClick(View view);
    }

    private OnShareClick mShareClick;
    public void setOnShareClick(OnShareClick searchClick){
        mShareClick = searchClick;
    }
    public static interface OnShareClick{
        public void onClick(View view);
    }


    public void showTitle(boolean show){
        mTitleView.setVisibility(show?VISIBLE:GONE);
    }
    private void init(Context context){
        LayoutInflater.from(getContext()).inflate(R.layout.title_bar_layout, this);
        mTitleView = (TextView) this.findViewById(R.id.titlebar_title);

        findViewById(R.id.title_bar_search).setOnClickListener(inLineSearchClick);

        View view = findViewById(R.id.title_bar_tv_connected);
        if(view != null)view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mCastClick != null)
                mCastClick.OnCaseContentToTV(v);
            }
        });
        view = findViewById(R.id.title_bar_share);
        if(view != null)view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mShareClick != null)
                mShareClick.onClick(v);
            }
        });

    }

    View.OnClickListener inLineSearchClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final EditText ev = (EditText) findViewById(R.id.album_search);
            if(mSearchClick != null){
                if(TextUtils.isEmpty(ev.getText().toString()) && (ev.getVisibility() == VISIBLE)) {
                    ev.setVisibility(INVISIBLE);
                    final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ev.getWindowToken(), 0);
                }else {
                    ev.setVisibility(VISIBLE);
                    final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ev.requestFocus();
                            imm.showSoftInput(ev, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }, 200);

                    mSearchClick.onClick(ev);
                }
            }else {
                ev.setVisibility(ev.getVisibility() == View.VISIBLE ? INVISIBLE : VISIBLE);
            }
        }
    };
}
