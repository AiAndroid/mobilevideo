package com.video.ui.tinyui;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.iDataORM;
import com.video.ui.utils.VideoUtils;
import com.video.ui.view.detail.EpisodeContainerView;
import com.video.ui.view.subview.SelectItemsBlockView;

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

        EpisodeContainerView.createEpisodeView(getBaseContext(), (VideoItem) item, vg, EpisodeContainerView.EPISODE_BUTTON_UI_STYLE);

        SelectItemsBlockView fv = (SelectItemsBlockView) EpisodePlayAdapter.findFilterBlockView(vg);
        fv.setOnPlayClickListener(episodeClick, null);

    }

    private DisplayItem.Media.CP currentCP;
    View.OnClickListener episodeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DisplayItem.Media.Episode ps = (DisplayItem.Media.Episode) view.getTag();
            if(view instanceof SelectItemsBlockView.VarietyEpisode ){
                view = view.findViewById(R.id.detail_variety_item_name);
            }
            if(item.media != null && item.media.display_layout != null && DisplayItem.Media.DisplayLayout.TYPE_OFFLINE.equals(item.media.display_layout.type)){
                DownloadManager dm = (DownloadManager) getBaseContext().getSystemService(Context.DOWNLOAD_SERVICE);
                int down_id = iDataORM.getDowndloadID(getBaseContext(), item.id, ps.id);
                DownloadManager.Query query = new DownloadManager.Query();

                query = query.setFilterById(new long[]{down_id});
                Cursor currentUI = dm.query(query);
                if (currentUI != null && currentUI.getCount() > 0 && currentUI.moveToFirst()) {
                    String local_uri = currentUI.getString(currentUI.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                    currentUI.close();

                    Intent showIntent = new Intent(Intent.ACTION_VIEW);
                    showIntent.setDataAndType(Uri.parse(local_uri), VideoUtils.getMimeType(local_uri));
                    showIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(showIntent);
                }
            }else {
                EpisodePlayAdapter.playEpisode(getBaseContext(), (TextView) view, currentCP, ps, item.media, item);
            }
            Log.d(TAG, "click episode:" + view.getTag());
        }
    };
}
