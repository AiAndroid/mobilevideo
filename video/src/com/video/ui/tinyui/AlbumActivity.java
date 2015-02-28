package com.video.ui.tinyui;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.tv.ui.metro.model.*;
import com.video.ui.DisplayItemActivity;
import com.video.ui.R;
import com.video.ui.idata.iDataORM;
import com.video.ui.view.ActionDeleteView;
import com.video.ui.view.BlockContainerView;
import com.video.ui.view.EmptyView;
import com.video.ui.view.LayoutConstant;

import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 1/30/15.
 * if less than 30 items, we can use the good performance ui,
 * else
 *    large than 30, use gridview and cusorLoader
 *
 */
public class AlbumActivity extends DisplayItemActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG=AlbumActivity.class.getName();
    BlockContainerView bcv;
    private int cursorFinishedLoaderID = 211;
    private ActionDeleteView mDeleteActionMode;
    private Button mBtnAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionMode();

        setContentView(R.layout.favor_ui);

        showSearch(false);
        showFilter(false);
        showEdit(true);
        mBtnAction = (Button) findViewById(R.id.channel_edit_btn);
        mBtnAction.setText(R.string.edit);
        mBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInEditMode()){
                    exitActionMode();
                }else{
                    startActionMode();
                }
            }
        });

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
        //getSupportLoaderManager().initLoader(cursorFinishedLoaderID, null, this);
    }

    public boolean isInEditMode(){
        return mDeleteActionMode.isInEditMode();
    }

    private void initActionMode() {
        mDeleteActionMode = new ActionDeleteView(this, mDeleteCallback);
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.MATCH_PARENT);
        mDeleteActionMode.setLayoutParams(params);
        viewGroup.addView(mDeleteActionMode);

        mDeleteActionMode.setVisibility(View.GONE);
    }

    private ActionDeleteView.Callback mDeleteCallback = new ActionDeleteView.Callback(){
        @Override
        public void onActionDeleteClick() {
            //TODO
            //do real delete
            //onDeleteClick();
            exitActionMode();
            loadFavor.execute();
        }
        @Override
        public void onActionSelectAll() {
            selectAll();
        }

        @Override
        public void onActionUnSelectAll() {
            unSelectAll();
        }
    };

    protected void startActionMode() {
        if(!mDeleteActionMode.isInEditMode()) {
            mDeleteActionMode.startActionMode();
        }
        mBtnAction.setText(android.R.string.cancel);
        if(bcv != null){
            bcv.setInEditMode(true);
        }
    }

    protected void exitActionMode() {
        if(mDeleteActionMode.isInEditMode()) {
            mDeleteActionMode.exitActionMode();
        }
        mBtnAction.setText(R.string.edit);
        clearAllSelected();
        if(bcv != null){
            bcv.setInEditMode(false);
        }
    }

    private void selectAll(){
        bcv.selectAll(true);
    }

    private void unSelectAll(){
        bcv.selectAll(false);
    }

    private void clearAllSelected(){

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
                    if(vi != null && vi.blocks.size() > 0 && vi.blocks.get(1).items != null && vi.blocks.get(1).items.size() > 0) {
                        top.blocks.add(vi);
                    }

                    vi = createLatestVideos(false);
                    if(vi != null && vi.blocks.size() > 0 && vi.blocks.get(1).items != null && vi.blocks.get(1).items.size() > 0) {
                        top.blocks.add(vi);
                    }

                    if(top.blocks.size() == 0){
                        bcv.setVisibility(View.GONE);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.tabs_content);

                        RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        flp.addRule(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.CENTER_VERTICAL);

                        View emptyView = new EmptyView(getApplicationContext(),
                                getIntent().getBooleanExtra("favor", false) == true?R.string.local_favorite_empty_title:R.string.play_his_empty_title,
                                getIntent().getBooleanExtra("favor", false) == true?R.drawable.empty_icon_favorite:R.drawable.empty_icon_play_his);
                        rl.addView(emptyView, flp);
                        rl.setVisibility(View.VISIBLE);
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
                grid_port.items.add(di);
            }
        }
        vi.blocks.add(grid_port);

        return vi;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = iDataORM.ALBUM_CONTENT_URI;
        mLoadingView.startLoading(true);

        String action = getIntent().getBooleanExtra("favor", false) == true ? iDataORM.FavorAction : iDataORM.HistoryAction;

        String where = iDataORM.ColumsCol.NS +"='video' and action='" + action + "' and date_int >= 0";
        return new CursorLoader(getBaseContext(), baseUri, iDataORM.actionProject, where, null, "date_int desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
