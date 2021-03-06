package com.video.ui.idata;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;

import com.video.ui.utils.VideoUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liuhuadonbg on 1/31/15.
 */
public class MVDownloadManager {
    private static String TAG = "MVDownloadManager";
    private static MVDownloadManager _instacne;
    private Context mContext;
    private DownloadManager dm;


    private MVDownloadManager(Context context){
        mContext = context.getApplicationContext();
        dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static MVDownloadManager getInstance(Context context){
        if(_instacne == null){
            _instacne = new MVDownloadManager(context);
            _instacne.start(context);
        }
        return _instacne;
    }

    public DownloadManager getDownloadManger(){
        return dm;
    }

    HandlerThread backThread = new  HandlerThread("download-thread");
    Handler backThreadHandler;

    public void start(final Context con){
        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO){
            backThread.start();
            backThreadHandler = new Handler(backThread.getLooper());

            addObserver(dm);
        }
    }

    public void stop(Context con){}

    private Cursor downloadCursor = null;
    private void addObserver(DownloadManager dm){
        DownloadManager.Query query = new DownloadManager.Query();
        query = query.setFilterByStatus(DownloadManager.STATUS_RUNNING
                | DownloadManager.STATUS_PENDING
                | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_FAILED
                | DownloadManager.STATUS_SUCCESSFUL);
        downloadCursor = dm.query(query);

        downloadCursor.registerContentObserver(mDownloadObserver);
    }

    private final int EVENT_RELOAD_DOWNLOAD = 100;
    private ContentObserver mDownloadObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            if (handler.hasMessages(EVENT_RELOAD_DOWNLOAD)) {
                handler.removeMessages(EVENT_RELOAD_DOWNLOAD);
            }
            handler.sendEmptyMessage(EVENT_RELOAD_DOWNLOAD);
        }
    };

    Handler handler = new Handler(){
        public void dispatchMessage(Message msg){
            switch (msg.what){
                case EVENT_RELOAD_DOWNLOAD:
                    backThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<DownloadTablePojo> donws = loadDownloadStatusFromDM(downloadCursor);
                            updateUIAndLocalDB(donws);
                        }
                    });

                    break;
            }
        }
    };

    private void updateUIAndLocalDB(final ArrayList<DownloadTablePojo> donws){
        //should do it in next thread
        //update databases
        for(DownloadTablePojo item: donws){
            if(item.status == DownloadTablePojo.DownloadSuccess){
                iDataORM.downloadFinished(mContext, Integer.valueOf(item.downloadId));
            }
        }

        //update observer
        for(DownloadTablePojo down: donws){
            if(mListener.get(String.valueOf(down.downloadId)) != null){
                DownloadListner dl = mListener.get(String.valueOf(down.downloadId)).get();
                if(dl != null) {
                    dl.downloadUpdate(down);
                }
            }
        }
    }

    public static ArrayList<DownloadTablePojo> loadDownloadStatusFromDM(Cursor cursor){
        ArrayList<DownloadTablePojo> ret = new ArrayList<DownloadTablePojo>();
        if (null == cursor) {
            return ret;
        }
        cursor.requery();

        if (!cursor.moveToFirst()) {
            return ret;
        }

        do {
            DownloadTablePojo dp = new DownloadTablePojo(cursor);
            ret.add(dp);
        } while (cursor.moveToNext());

        return ret;
    }


    public static interface DownloadListner{
        public void downloadUpdate(DownloadTablePojo downloadTablePojos);
    }

    private HashMap<String, WeakReference<DownloadListner>> mListener = new HashMap<String , WeakReference<DownloadListner>>();
    public void addDownloadListener(final String key, DownloadListner listner){
        /*
         * no need add monitor here, we monitor it in UI activity
        synchronized (mListener) {
            mListener.put(key, new WeakReference<DownloadListner>(listner));
        }*/

        //TODO performance issue
        //update current result
        DownloadManager.Query query = new DownloadManager.Query();
        query = query.setFilterById(new long[]{Integer.valueOf(key)});
        Cursor currentUI = dm.query(query);
        if(currentUI != null && currentUI.getCount() > 0 && currentUI.moveToFirst()){
            DownloadTablePojo dp = new DownloadTablePojo(currentUI);

            //
            //update database and will not show in this UI
            if(dp.status == MVDownloadManager.DownloadTablePojo.DownloadSuccess){
                backThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDataORM.downloadFinished(mContext, Integer.valueOf(key));
                    }
                });
            }
            listner.downloadUpdate(dp);
            currentUI.close();
        }else {
            //no record, so remove or ?
            Log.d(TAG, "no download task:"+key);
        }
    }


    public static final int DOWNLOAD_IN = -100;
    public long requestDownload(Context con, VideoItem video, DisplayItem.Media.Episode episode, String downloadurl){
        long download_id = -1;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) == false){
            Toast.makeText(con, R.string.no_sdcard_no_download, Toast.LENGTH_SHORT).show();
            return -1;
        }

        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO){
            if (isInDownloading(con, video.id, episode.id)) {
                Log.i(TAG, "download, ongoing item: " + video);
                return DOWNLOAD_IN;
            }

            android.app.DownloadManager dm = (android.app.DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);

            String url = downloadurl;
            android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url));
            //request.setMimeType("application/vnd.android.package-archive");
            request.setMimeType(VideoUtils.getMimeType(url));

            request.setTitle(episode.name);
            request.setDescription(episode.date);
            request.setVisibleInDownloadsUi(true);
            request.setShowRunningNotification(true);
            int downloadFlag = DownloadManager.Request.NETWORK_WIFI;
            if(iDataORM.isOpenCellularOfflineHint(con) == false){
                downloadFlag |=DownloadManager.Request.NETWORK_MOBILE;
            }
            request.setAllowedNetworkTypes(downloadFlag);

            //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "video");
            request.allowScanningByMediaScanner();

            request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "." + MimeTypeMap.getFileExtensionFromUrl(url))));

            if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB){
                try{
                    request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE|android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }catch(Exception ne){}
            }
            //request.setNotificationVisibility(true);
            download_id = dm.enqueue(request);
            iDataORM.getInstance(con).addDownload(con, video.id, download_id, url, video, episode);

            Log.d(TAG, "new download=" + download_id);
        }else{
            //download directly
            Intent downIntent = new Intent(Intent.ACTION_VIEW);
            downIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            downIntent.setData(Uri.parse(video.media.poster));
            try {
                con.startActivity(downIntent);
            }catch(Exception ne){
                ne.printStackTrace();
            }
        }
        return download_id;
    }

    //TODO refer to aimashi download code, SuperMarket and GameCenter
    //private static final Method RESUME_DOWNLOAD = Method.of(DownloadManager.class, "resumeDownload", "([J)V");
    //private static final Method PAUSE_DOWNLOAD  = Method.of(DownloadManager.class, "pauseDownload", "([J)V");

    public static void pauseDownload(DownloadManager dm ,long[] ids){
        try {
            if(!dm.getClass().getName().equalsIgnoreCase("android.app.DownloadManager")){
                return;
            }
            Class<?> miuidownlaod = Class.forName("android.app.DownloadManager");
            java.lang.reflect.Method pauseDownload = miuidownlaod.getMethod("pauseDownload", ids.getClass());
            pauseDownload.setAccessible(true);
            pauseDownload.invoke(dm, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void resumeDownload(DownloadManager dm ,long[] ids){
        try {
            if(!dm.getClass().getName().equalsIgnoreCase("android.app.DownloadManager")){
                return;
            }
            Class<?> miuidownlaod = Class.forName("android.app.DownloadManager");
            java.lang.reflect.Method resumeDownload = miuidownlaod.getMethod("resumeDownload", ids.getClass());
            resumeDownload.setAccessible(true);
            resumeDownload.invoke(dm, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isInDownloading(Context con, String res_id, String sub_id){
        int download_id = iDataORM.getInstance(con).getDowndloadID(con, res_id, sub_id);
        if(download_id != -1){

            android.app.DownloadManager dm = (android.app.DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);
            android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
            query.setFilterById(download_id);
            Cursor c = dm.query(query);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
                if(DownloadManager.STATUS_RUNNING == status ||
                        DownloadManager.STATUS_PENDING == status ||
                        DownloadManager.STATUS_PAUSED  == status ||
                        DownloadManager.STATUS_SUCCESSFUL == status){
                    return true;
                }
            }
        }
        return false;
    }



    public static class DownloadTablePojo {
        public static final int    DownloadQueue   = 5; //排对中
        public static final int    Downloading     = 4;//下载中
        public static final int    DownloadPause   = 3;//下载暂停
        public static final int    DownloadFail    = 2;
        public static final int    DownloadSuccess = 1;//下载完成

        public int downloadId;
        public long   recv;
        public long   total;
        public long   lastUpdate;
        public int    reason;
        public int    status;


        public DownloadTablePojo(Cursor sor) {
            downloadId = Integer.valueOf(sor.getString(sor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID)));
            reason = sor.getInt(sor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON));
            recv = sor.getLong(sor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            total = sor.getLong(sor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int _stat = sor.getInt(sor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));

            reason+=50000;

            if(_stat == DownloadManager.STATUS_PENDING || _stat == DownloadManager.STATUS_RUNNING){
                status = Downloading;
            }
            else if(_stat == DownloadManager.STATUS_PAUSED){
                status = DownloadPause;
            }
            else if(_stat == DownloadManager.STATUS_FAILED){
                status = DownloadFail;
            }
            else if(_stat == DownloadManager.STATUS_SUCCESSFUL)         {
                status = DownloadSuccess;
            }
        }

        @Override
        public String toString() {
            return "SystemDownload: DownloadID:"+downloadId+ "Status:"+status;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null)
                return false;

            if(obj instanceof DownloadTablePojo){
                return  ((DownloadTablePojo)obj).downloadId == (downloadId);
            }

            return  false;
        }
    }
}
