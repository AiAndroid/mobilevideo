package com.video.ui.view;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.video.ui.R;
import com.video.ui.loader.ProgressNotifiable;


public class EmptyLoadingView extends LinearLayout implements ProgressNotifiable, RetryView.OnRetryLoadListener {

    public EmptyLoadingView(Context context) {
        super(context);
        init(context);
    }

    public void setOnRetryListener(RetryView.OnRetryLoadListener retryListener){
        mRetryListener = retryListener;
    }

    private RetryView.OnRetryLoadListener mRetryListener;

    private View        loading_layout;
    private TextView    mTextView;
    private ProgressBar progress;
    private RetryView   view_retry;
    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.load_view, this);
        loading_layout = this.findViewById(R.id.loading_layout);
    	progress = (ProgressBar) this.findViewById(R.id.progress);
        mTextView = (TextView) this.findViewById(R.id.hint_text);
        view_retry = (RetryView)this.findViewById(R.id.view_retry);
        view_retry.setOnRetryLoadListener(this);
    }

    private void updateStyle(boolean hasData) {
        if (hasData) {
            getLayoutParams().height = LayoutParams.WRAP_CONTENT;
        } else {
            getLayoutParams().height = LayoutParams.MATCH_PARENT;
            if(Build.VERSION.SDK_INT >= 16)
                setBackground(null);
            else
                setBackgroundDrawable(null);
        }
    }

    @Override
    public void startLoading(boolean hasData) {
        updateStyle(hasData);
        loading_layout.setVisibility(VISIBLE);
        progress.setIndeterminate(true);
        view_retry.setVisibility(GONE);
    }

    @Override
    public void stopLoading(boolean hasData, boolean hasNext) {
        if (!hasNext) {
            updateStyle(hasData);
            if (hasData) {
                hideView(loading_layout);
                progress.setIndeterminate(false);
                view_retry.setVisibility(GONE);
            } else {
                view_retry.setVisibility(VISIBLE);
                progress.setIndeterminate(false);
                hideView(loading_layout);
            }
        }
    }

    private void showView(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == GONE) {
            view.setVisibility(VISIBLE);
        }
    }

    private void hideView(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == VISIBLE) {
            view.setVisibility(GONE);
        }
    }

    @Override
    public void init(boolean hasData, boolean isLoading) {
        updateStyle(hasData);
        if (isLoading) {
            loading_layout.setVisibility(VISIBLE);
            progress.setIndeterminate(true);
            view_retry.setVisibility(GONE);
        } else {

            if (hasData) {
                loading_layout.setVisibility(GONE);
                progress.setIndeterminate(false);
                view_retry.setVisibility(VISIBLE);
            } else {
                progress.setIndeterminate(true);
                loading_layout.setVisibility(VISIBLE);
                view_retry.setVisibility(GONE);
            }
        }
    }

    @Override
    public void OnRetryLoad(View vClicked) {
        progress.setIndeterminate(true);
        showView(this);
        if(mRetryListener != null){
            mRetryListener.OnRetryLoad(vClicked);
        }
    }
}