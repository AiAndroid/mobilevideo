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
import com.tv.ui.metro.model.LayoutConstant;
import com.video.ui.view.block.GridMediaBlockView;

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
        setContentView(R.layout.favor_ui);
        initActionMode();

        showSearch(false);
        showFilter(false);
        showEdit(true);


        if(getIntent().getBooleanExtra(Constants.Favor_Video, false) || (item != null && item.id.endsWith(Constants.Video_ID_Favor) )){
            setTitle(getString(R.string.my_favorite));
            getIntent().putExtra(Constants.Favor_Video, true);
        }else if(getIntent().getBooleanExtra(Constants.History_Video, false) || (item != null && item.id.endsWith(Constants.Video_ID_History) )){
            setTitle(getString(R.string.play_history));
            getIntent().putExtra(Constants.History_Video, true);
        }else if(getIntent().getBooleanExtra(Constants.Local_Video, false) || (item != null && item.id.endsWith(Constants.Video_ID_Local) )){
            getIntent().putExtra(Constants.Local_Video, true);
            setTitle(getString(R.string.local_video));
        }

        //TODO change to CursorLoader, because we need load the data from server
        bcv = (BlockContainerView) findViewById(R.id.container_view);
        bcv.setOnItemSelectListener(itemSelectListener);
        bcv.setOnItemLongClickLitener(itemLongClick);
        new LoadAsyncTask().execute();
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
    }

    private ActionDeleteView.Callback mDeleteCallback = new ActionDeleteView.Callback(){
        @Override
        public void onActionDeleteClick() {

            for(DisplayItem item: willDelSelects){
                iDataORM.removeFavor(getApplicationContext(), "video", getIntent().getBooleanExtra(Constants.Favor_Video, false) == true ? iDataORM.FavorAction : iDataORM.HistoryAction, item.id);
            }
            willDelSelects.clear();

            exitActionMode();
            new LoadAsyncTask().execute();
        }
        @Override
        public void onActionSelectAll() {
            bcv.selectAll(true);
        }

        @Override
        public void onActionUnSelectAll() {
            bcv.selectAll(false);
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

        if(bcv != null){
            bcv.setInEditMode(false);
        }
    }

    private View.OnLongClickListener itemLongClick = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            if(isInEditMode()){
                exitActionMode();
            }else{
                startActionMode();
            }
            return true;
        }
    };

    private ArrayList<DisplayItem> willDelSelects = new ArrayList<DisplayItem>();
    private GridMediaBlockView.MediaItemView.OnItemSelectListener itemSelectListener = new GridMediaBlockView.MediaItemView.OnItemSelectListener() {
        @Override
        public void onSelected(View view, DisplayItem item,  boolean selected, int flag) {
            Log.d(TAG, "selected item:"+selected + " item:"+item);

            switch (flag){
                case GridMediaBlockView.MediaItemView.FLAG_SELECT_SINGLE: {
                    if (selected) {
                        willDelSelects.add(item);
                    } else {
                        willDelSelects.remove(item);
                    }
                    mDeleteActionMode.setSelectCount(willDelSelects.size());
                    break;
                }
                case  GridMediaBlockView.MediaItemView.FLAG_SELECT_ALL: {
                    if(selected == false){
                        willDelSelects.clear();
                    }else {
                        int size = 0;
                        Block<DisplayItem> block = bcv.getContent();
                        for(Block<DisplayItem> bitem: block.blocks){
                            for(Block<DisplayItem> subitem:bitem.blocks){
                                if(subitem.items != null){
                                    willDelSelects.addAll(subitem.items);
                                }
                            }
                        }
                    }

                    mDeleteActionMode.setSelectCount(willDelSelects.size());
                    break;
                }
            }
        }
    };

    ArrayList<iDataORM.ActionRecord> records;
    public class LoadAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            if(getIntent().getBooleanExtra("offline", false) == true){
                records = iDataORM.getInstance(getBaseContext()).getDownloads(getBaseContext());
            }else {
                records = iDataORM.getInstance(getBaseContext()).getFavorites(getBaseContext(), "video", getIntent().getBooleanExtra(Constants.Favor_Video, false) == true ? iDataORM.FavorAction : iDataORM.HistoryAction);
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
                                getIntent().getBooleanExtra(Constants.Favor_Video, false) == true?R.string.local_favorite_empty_title:R.string.play_his_empty_title,
                                getIntent().getBooleanExtra(Constants.Favor_Video, false) == true?R.drawable.empty_icon_favorite:R.drawable.empty_icon_play_his);
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

        String action = getIntent().getBooleanExtra(Constants.Favor_Video, false) == true ? iDataORM.FavorAction : iDataORM.HistoryAction;

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
