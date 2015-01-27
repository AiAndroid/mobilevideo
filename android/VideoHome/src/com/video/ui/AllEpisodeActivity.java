package com.video.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.view.detail.EpisodeContainerView;

/**
 * Created by liuhuadonbg on 1/26/15.
 */
public class AllEpisodeActivity extends DisplayItemActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.linear_container);

        showFilter(false);
        showSearch(false);
        setTitle(getString(R.string.all_episode));
        ViewGroup vg = (ViewGroup) findViewById(R.id.episode_container);

        EpisodeContainerView.createEpisodeView(getBaseContext(), (VideoItem) item, vg);
    }
}
