package com.video.ui.tinyui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.gson.Gson;
import com.tv.ui.metro.model.*;
import com.video.ui.DisplayItemActivity;
import com.video.ui.R;
import com.video.ui.idata.iDataORM;
import com.video.ui.view.BlockContainerView;
import com.video.ui.view.LayoutConstant;

import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 1/30/15.
 * if less than 30 items, we can use the good performance ui,
 * else
 *    large than 30, use gridview and cusorLoader
 *
 */
public class AlbumActivity extends DisplayItemActivity{

    BlockContainerView bcv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favor_ui);

        showSearch(false);
        showFilter(false);

        if(getIntent().getBooleanExtra("favor", false) || (item != null && item.id.endsWith("play_favor") )){
            setTitle(getString(R.string.my_favorite));
            getIntent().putExtra("favor", true);
        }else if(getIntent().getBooleanExtra("history", false) || (item != null && item.id.endsWith("play_history") )){
            setTitle(getString(R.string.play_history));
            getIntent().putExtra("history", true);
        }else if(getIntent().getBooleanExtra("offline", false) || (item != null && item.id.endsWith("play_offline") )){
            getIntent().putExtra("offline", true);
            setTitle(getString(R.string.my_offline));
        }

        //TODO change to CursorLoader, because we need load the data from server
        bcv = (BlockContainerView) findViewById(R.id.container_view);

        loadFavor.execute();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    ArrayList<iDataORM.ActionRecord> records;
    public AsyncTask loadFavor = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            if(getIntent().getBooleanExtra("offline", false) == true){
                records = iDataORM.getInstance(getBaseContext()).getDownloads(getBaseContext());
            }else {
                records = iDataORM.getInstance(getBaseContext()).getFavorites(getBaseContext(), "video", getIntent().getBooleanExtra("favor", false) == true ? iDataORM.FavorAction : iDataORM.HistoryAction);
            }
            mHandler.obtainMessage(SHOW_UI).sendToTarget();
            return null;
        }
    };

    private final int SHOW_UI=100;
    private Gson gson = new Gson();
    Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg){
            switch (msg.what){
                case SHOW_UI:
                    Block<DisplayItem> top = new Block<DisplayItem>();
                    top.ui_type = new DisplayItem.UI();
                    top.ui_type.id = LayoutConstant.block_port;

                    top.blocks = new  ArrayList<Block<DisplayItem>>();
                    Block<DisplayItem> vi = createLatestVideos(true);
                    if(vi != null) {
                        top.blocks.add(vi);
                    }

                    vi = createLatestVideos(false);
                    if(vi != null && vi.blocks.get(0).items != null && vi.blocks.get(0).items.size() > 0) {
                        top.blocks.add(vi);
                    }


                    bcv.setBlocks(top);
                    break;
            }
        }
    };

    private Block<DisplayItem> createLatestVideos(boolean lastweek){
        Block<DisplayItem> vi = new Block<DisplayItem>();
        vi.ui_type = new com.tv.ui.metro.model.DisplayItem.UI();
        vi.ui_type.id = LayoutConstant.block_sub_channel;

        vi.blocks = new  ArrayList<Block<DisplayItem>>();

        Block<DisplayItem> title = new Block<DisplayItem>();
        title.title = getString(R.string.online_video_oneweek);
        if(lastweek == false){
            title.title = getString(R.string.online_video_oneweek_ago);
        }
        title.ui_type = new com.tv.ui.metro.model.DisplayItem.UI();
        title.ui_type.id = LayoutConstant.linearlayout_title;
        vi.blocks.add(title);

        Block<DisplayItem> grid_port = new Block<DisplayItem>();
        grid_port.title = getString(R.string.online_video);
        grid_port.ui_type = new com.tv.ui.metro.model.DisplayItem.UI();
        grid_port.ui_type.id = LayoutConstant.grid_media_port;
        grid_port.ui_type.row_count = 3;

        long lastweekpointer = System.currentTimeMillis() - 7*24*60*60*1000l;
        grid_port.items = new ArrayList<DisplayItem>();
        for(iDataORM.ActionRecord ar :records){
            if((lastweek && ar.dateInt >= lastweekpointer) ||
                    (lastweek == false && ar.dateInt < lastweekpointer )) {
                VideoItem di = iDataORM.ActionRecord.parseJson(gson, ar.json, VideoItem.class);
                di.images = new ImageGroup();
                Image image = new Image();
                image.url = di.media.poster;
                di.images.put("poster", image);

                grid_port.items.add(di);
            }
        }
        vi.blocks.add(grid_port);

        return vi;
    }
}
