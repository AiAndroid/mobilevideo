package com.video.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by liuhuadonbg on 1/24/15.
 */
public class UserActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mobile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                break;
            case R.id.menu_share:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "欢迎试用\"新视频UI\", 点击链接 https://github.com/AiAndroid/mobilevideo/blob/master/dev.md 下载");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享 \"新视频UI\"");
                startActivity(Intent.createChooser(intent, "分享 \"小米视频UI框架\""));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
