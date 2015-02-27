package com.video.ui.tinyui;

import android.app.ActionBar;
import android.os.Bundle;
import com.video.ui.R;
import miui.preference.PreferenceActivity;

/**
 * Created by liuhuadonbg on 2/26/15.
 */
public class BasePreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initStyle();
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }

    private void initStyle() {
        setTheme(miui.R.style.Theme_Light_Settings);
    }
}
