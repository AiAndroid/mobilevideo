package com.video.ui.view.block;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.Image;
import com.tv.ui.metro.model.ImageGroup;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.idata.MVDownloadManager;
import com.video.ui.idata.iDataORM;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class DownloadVideoItemView extends RelativeLayout {

    private static final String TAG = "download-DownloadVideoItemView";
    public DownloadVideoItemView(Context context) {
        super(context);
        mainHandler = new Handler();
        initViews(context);
    }


    private int width  = -1;
    private int height = -1;
    iDataORM.ActionRecord content;
    Gson gson = new Gson();
    VideoItem displayItem;
    DisplayItem.Media.Episode episode;
    Handler mainHandler;

    public void bind(iDataORM.ActionRecord item){

        content = item;
        if(item.object == null){
            item.object = gson.fromJson(item.json, VideoItem.class);
        }

        if(item.object != null){
            displayItem = (VideoItem) item.object;

            setTag(displayItem);

            if(displayItem.images == null || displayItem.images.poster() == null) {
                displayItem.images = new ImageGroup();
                Image image = new Image();
                image.url = displayItem.media.poster;
                displayItem.images.put("poster", image);
            }
        }

        episode = gson.fromJson(item.sub_value, DisplayItem.Media.Episode.class);
        if(width == -1){
            width = getResources().getDimensionPixelSize(R.dimen.media_list_cover_v_width);
            height = getResources().getDimensionPixelSize(R.dimen.media_list_cover_v_height);
        }

        String logo = "";
        if(displayItem.images != null) {
            if (displayItem.images.poster() != null)
                logo = displayItem.images.poster().url;

            if(logo != null && logo.length() > 0) {
                Picasso.with(getContext()).load(logo).tag(getContext()).centerCrop().resize(width, height).into(poster);
            }
        }

        // title
        title.setText(episode.name);
        // subtitle

        MVDownloadManager.getInstance(getContext()).addDownloadListener(String.valueOf(item.download_id), new MVDownloadManager.DownloadListner() {
            @Override
            public void downloadUpdate(final MVDownloadManager.DownloadTablePojo itemData) {

                if(itemData.downloadId == content.download_id) {
                    updateUI(itemData);
                    Log.d("download", "update view:" + itemData);
                }else {
                    Log.d(TAG, "download id is changed: "+content.download_id + " in id:"+itemData.downloadId);
                }
            }
        });
    }

    public int getDowbnloadID(){
        return content.download_id;
    }
    public void updateUI(MVDownloadManager.DownloadTablePojo downloadTablePojo){
        downloadStatus = downloadTablePojo;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                download_reason.setText("");
                switch (downloadStatus.status){
                    case MVDownloadManager.DownloadTablePojo.DownloadFail:
                        place.setBackground(getResources().getDrawable(R.drawable.btn_offline_fail));
                        //download_reason.setText("fail");
                        break;
                    case MVDownloadManager.DownloadTablePojo.Downloading:
                        place.setBackground(getResources().getDrawable(R.drawable.btn_offline_loading));
                        break;
                    case MVDownloadManager.DownloadTablePojo.DownloadQueue:
                        place.setBackground(getResources().getDrawable(R.drawable.btn_offline_waiting));
                        break;
                    case MVDownloadManager.DownloadTablePojo.DownloadPause:
                        place.setBackground(getResources().getDrawable(R.drawable.btn_offline_pause));
                        //download_reason.setText("pause");
                        break;
                    case MVDownloadManager.DownloadTablePojo.DownloadSuccess:
                        //place.setText("finished");
                        break;
                }

                subtitle.setText(String.format("%.1fMB/%.1fMB", downloadStatus.recv/(1024.0*1024.0), downloadStatus.total/(1024.0*1024.0)));
                int progress = (int) ((downloadStatus.recv*100)/downloadStatus.total);
                download_per.setText(String.format("%1$s", String.valueOf(progress)+"%"));
                offline_loading_item_progress.setProgress(progress);
            }
        });
    }

    private View      convertView;
    private ImageView poster;
    private TextView title;
    private TextView download_per;
    private ProgressBar offline_loading_item_progress;

    private MVDownloadManager.DownloadTablePojo downloadStatus;

    public TextView subtitle;
    public TextView place;
    public TextView download_reason;
    public RelativeLayout layout;
    public View     line;
    public View     padding;

    private void initViews(Context ctx){
        int res_id = R.layout.download_item;
        convertView = LayoutInflater.from(ctx).inflate(res_id, this);
        layout = (RelativeLayout) convertView.findViewById(R.id.channel_rank_item_layout);

        poster = (ImageView) convertView.findViewById(R.id.channel_rank_item_poster);
        title = (TextView) convertView.findViewById(R.id.channel_rank_item_title);
        subtitle = (TextView) convertView.findViewById(R.id.channel_rank_item_subtitle);


        place = (TextView) convertView.findViewById(R.id.download_status);
        download_reason = (TextView) convertView.findViewById(R.id.download_reason);
        line = convertView.findViewById(R.id.channel_rank_item_line);
        padding = convertView.findViewById(R.id.channel_rank_item_padding);
        download_per = (TextView) convertView.findViewById(R.id.channel_rank_item_hot);

        offline_loading_item_progress = (ProgressBar) convertView.findViewById(R.id.offline_loading_item_progress);

        place.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(downloadStatus != null){
                    switch (downloadStatus.status){
                        case MVDownloadManager.DownloadTablePojo.DownloadFail:
                            MVDownloadManager.resumeDownload(MVDownloadManager.getInstance(getContext()).getDownloadManger(), new long[]{downloadStatus.downloadId});
                            break;
                        case MVDownloadManager.DownloadTablePojo.Downloading:
                            MVDownloadManager.pauseDownload(MVDownloadManager.getInstance(getContext()).getDownloadManger(), new long[]{downloadStatus.downloadId});
                            place.setBackground(getResources().getDrawable(R.drawable.btn_offline_pause));
                            break;
                        case MVDownloadManager.DownloadTablePojo.DownloadQueue:
                            MVDownloadManager.pauseDownload(MVDownloadManager.getInstance(getContext()).getDownloadManger(), new long[]{downloadStatus.downloadId});
                            break;
                        case MVDownloadManager.DownloadTablePojo.DownloadPause:
                            MVDownloadManager.resumeDownload(MVDownloadManager.getInstance(getContext()).getDownloadManger(), new long[]{downloadStatus.downloadId});
                            place.setBackground(getResources().getDrawable(R.drawable.btn_offline_loading));
                            break;
                        case MVDownloadManager.DownloadTablePojo.DownloadSuccess:
                            place.setText("finished");
                            break;
                    }
                }
            }
        });
    }
}
