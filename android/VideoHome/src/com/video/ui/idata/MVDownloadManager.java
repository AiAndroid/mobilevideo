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
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;

import com.video.ui.utils.VideoUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liuhuadonbg on 1/31/15.
 */
public class MVDownloadManager {
    private static String TAG = "MVDownloadManager";
    private static MVDownloadManager _instacne;
    private Context mContext;
    private DownloadManager dm;
    private ArrayList<iDataORM.ActionRecord> currentDownloadRecords;

    public ArrayList<iDataORM.ActionRecord> getCurrentCacheDownload(){
        return currentDownloadRecords;
    }

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

    private Cursor downloadCursor = null;
    private void addObserver(DownloadManager dm){
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_RUNNING
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
                    if(mListener.size() == 0){
                        Log.d(TAG, "no ui observer, just ignore");
                        return;
                    }
                    ArrayList<DownloadTablePojo> donws = loadDownloadStatusFromDM(downloadCursor);
                    updateUIAndLocalDB(donws);
                    break;
            }
        }
    };

    private void updateUIAndLocalDB(final ArrayList<DownloadTablePojo> donws){

        //TODO
        //should do it in next thread
        //update databases
        for(DownloadTablePojo item: donws){
            if(item.status == DownloadTablePojo.DownloadSuccess){
                iDataORM.downloadFinished(mContext, Integer.valueOf(item.downloadId));
            }
        }

        for(int i=0;i<currentDownloadRecords.size();i++){
            iDataORM.ActionRecord ar = currentDownloadRecords.get(i);

            for(DownloadTablePojo down: donws){
                if(down.downloadId.equals(String.valueOf(ar.download_id))){
                    ar.download_status = down.status;
                    ar.totalsizebytes  = down.total;
                    ar.downloadbytes   = down.recv;
                }
            }
        }

        Set<String> sets = mListener.keySet();
        Iterator<String> its = sets.iterator();
        while (its.hasNext()){
            String key = its.next();
            WeakReference<DownloadListner> value = mListener.get(key);
            if(value.get() != null){
                value.get().downloadUpdate(donws);
            }
        }

    }

    private ArrayList<DownloadTablePojo> loadDownloadStatusFromDM(Cursor cursor){
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
        public void downloadUpdate(ArrayList<DownloadTablePojo> downloadTablePojos);
    }

    private HashMap<String, WeakReference<DownloadListner>> mListener = new HashMap<String , WeakReference<DownloadListner>>();
    public void addDownloadListener(String key, DownloadListner listner){
        mListener.put(key, new WeakReference<DownloadListner>(listner));
    }

    public void removeListener(String key){
        mListener.remove(key);
    }



    public void start(Context con){
        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO){
            con.registerReceiver(receiver, new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            con.registerReceiver(receiver, new IntentFilter(android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED));

            //load data from local table
            currentDownloadRecords = iDataORM.getInstance(con).getDownloads(con, 0);
            addObserver(dm);
        }
    }

    public void stop(Context con){
        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO)
        {
            con.unregisterReceiver(receiver);
        }
    }

    public static final int DOWNLOAD_IN = -100;
    public long requestDownload(Context con, VideoItem video, DisplayItem.Media.Episode episode){
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

            //String url = ramdonvideo[((int) (Math.random() * 100)) % ramdonvideo.length];
            String url = video.media.poster;
            android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(url));
            //request.setMimeType("application/vnd.android.package-archive");
            request.setMimeType(VideoUtils.getMimeType(url));

            request.setTitle(video.title);
            request.setVisibleInDownloadsUi(true);
            request.setShowRunningNotification(true);
            int downloadFlag = DownloadManager.Request.NETWORK_WIFI;
            if(iDataORM.getBooleanValue(mContext, iDataORM.mobile_offline_hint, true) == false){
                downloadFlag |=DownloadManager.Request.NETWORK_MOBILE;
            }
            request.setAllowedNetworkTypes(downloadFlag);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "video");
            request.allowScanningByMediaScanner();

            request.setDestinationUri(Uri.fromFile(Environment.getDownloadCacheDirectory()));

            if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB){
                try{
                    request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE|android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }catch(Exception ne){}
            }
            //request.setNotificationVisibility(true);
            download_id = dm.enqueue(request);
            iDataORM.getInstance(con).addDownload(con, video.id, download_id, "", video, episode);


            Log.d(TAG, "new download=" + download_id);
        }
        else{
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
            if(!dm.getClass().getName().equalsIgnoreCase("android.app.MiuiDownloadManager")){
                return;
            }
            Class<?> miuidownlaod = Class.forName("android.app.MiuiDownloadManager");
            java.lang.reflect.Method pauseDownload = miuidownlaod.getMethod("pauseDownload", ids.getClass());
            pauseDownload.setAccessible(true);
            pauseDownload.invoke(dm, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void resumeDownload(DownloadManager dm ,long[] ids){
        try {
            if(!dm.getClass().getName().equalsIgnoreCase("android.app.MiuiDownloadManager")){
                return;
            }
            Class<?> miuidownlaod = Class.forName("android.app.MiuiDownloadManager");
            java.lang.reflect.Method resumeDownload = miuidownlaod.getMethod("resumeDownload", ids.getClass());
            resumeDownload.setAccessible(true);
            resumeDownload.invoke(dm, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isInDownloading(Context con, String res_id, String sub_id)
    {
        int download_id = iDataORM.getInstance(con).getDowndloadID(con, res_id, sub_id);
        if(download_id != -1){

            android.app.DownloadManager dm = (android.app.DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);
            android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
            query.setFilterById(download_id);
            Cursor c = dm.query(query);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS));
                if(DownloadManager.STATUS_RUNNING == status || DownloadManager.STATUS_PENDING == status || DownloadManager.STATUS_SUCCESSFUL == status){
                    return true;
                }
            }
        }
        return false;
    }

    //http://www.apkbus.com/forum.php?mod=viewthread&tid=144845

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context con, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "action=" + intent);

            if (android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO){
                    android.app.DownloadManager dm = (android.app.DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);

                    long downloadId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    //update download status
                    //TODO


                    Log.d(TAG, "new download complete=" + downloadId);
                    android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS);
                        if (android.app.DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            String uriString = c.getString(c.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_FILENAME));

                            if(uriString.startsWith("file://"))
                            {
                                uriString = uriString.substring(7);
                            }

                            if(uriString.endsWith(".apk")){
                                ApkFileManager.installApk(con, uriString, "", "", false);
                            }
                        }
                        else if(android.app.DownloadManager.STATUS_FAILED == c.getInt(columnIndex)){
                            Log.d(TAG, "new download fail="+downloadId);
                        }
                    }

                }
            }
            else if (android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)){
                if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO){
                    android.app.DownloadManager dm = (android.app.DownloadManager) con.getSystemService(Context.DOWNLOAD_SERVICE);

                    long downloadId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Log.d(TAG, "new download complete="+downloadId);
                    android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS);
                        if (android.app.DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            String uriString = c.getString(c.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_FILENAME));
                            if(uriString.startsWith("file://"))
                            {
                                uriString = uriString.substring(7);
                            }

                            Log.d(TAG, "new downloaded"+uriString);
                            if(uriString.endsWith(".apk")){
                                ApkFileManager.installApk(con, uriString, "", "", false);
                            }else
                            {
                                openDownloadsPage(con);
                            }
                        }
                        else if(android.app.DownloadManager.STATUS_FAILED == c.getInt(columnIndex)){
                            Log.d(TAG, "new download fail="+downloadId);
                            openDownloadsPage(con);
                        }
                    }
                }else{
                    openDownloadsPage(con);
                }
            }
        }
    };


    private void openDownloadsPage(Context context) {
        Intent pageView = new Intent(android.app.DownloadManager.ACTION_VIEW_DOWNLOADS);
        pageView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(pageView);
    }


    public static class DownloadTablePojo {


        public static final int    DownloadQueue   = 5; //排对中
        public static final int    Downloading     = 4;//下载中
        public static final int    DownloadPause   = 3;//下载暂停
        public static final int    DownloadFail    = 2;
        public static final int    DownloadSuccess = 1;//下载完成


        String downloadId;
        long   recv;
        long   total;
        long   lastUpdate;
        int    reason;
        int    status;


        public DownloadTablePojo(Cursor sor)
        {
            downloadId = sor.getString(sor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));
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
    }
}
