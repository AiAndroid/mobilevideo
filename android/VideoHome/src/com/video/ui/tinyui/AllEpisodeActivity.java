package com.video.ui.tinyui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.DisplayItemActivity;
import com.video.ui.EpisodePlayAdapter;
import com.video.ui.R;
import com.video.ui.view.detail.EpisodeContainerView;
import com.video.ui.view.subview.FilterBlockView;

/**
 * Created by liuhuadonbg on 1/26/15.
 */
public class AllEpisodeActivity extends DisplayItemActivity {

    private String TAG = AllEpisodeActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.linear_container);

        currentCP = (DisplayItem.Media.CP) getIntent().getSerializableExtra("cp");

        showFilter(false);
        showSearch(false);
        setTitle(getString(R.string.all_episode));
        ViewGroup vg = (ViewGroup) findViewById(R.id.episode_container);

        EpisodeContainerView.createEpisodeView(getBaseContext(), (VideoItem) item, vg);

        FilterBlockView fv = (FilterBlockView) EpisodePlayAdapter.findFilterBlockView(vg);
        fv.setOnPlayClickListener(episodeClick, null);

    }

    private DisplayItem.Media.CP currentCP;
    View.OnClickListener episodeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DisplayItem.Media.Episode ps = (DisplayItem.Media.Episode) view.getTag();
            if(view instanceof FilterBlockView.VarietyEpisode ){
                view = view.findViewById(R.id.detail_variety_item_name);
            }
            EpisodePlayAdapter.playEpisode(getBaseContext(), (TextView) view, currentCP, ps, item.media, item);
            Log.d(TAG, "click episode:" + view.getTag());
        }
    };
}
