package com.video.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.video.ui.view.user.MyVideoFragment;

/**
 * Created by liuhuadonbg on 1/24/15.
 */
public class UserActivity extends DisplayItemActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_ui);

        showSearch(false);
        showFilter(false);

        setTitle(getString(R.string.personal_center));

        Fragment fg = getSupportFragmentManager().findFragmentById(R.id.detail_view);
        if(fg == null) {
            MyVideoFragment df = new MyVideoFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.user_fragment, df, "user_fragment").commit();
        }
    }
}
