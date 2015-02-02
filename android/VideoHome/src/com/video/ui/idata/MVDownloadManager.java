package com.video.ui.idata;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;

import com.video.ui.utils.VideoUtils;
import miui.reflect.Method;

/**
 * Created by liuhuadonbg on 1/31/15.
 */
public class MVDownloadManager {
    private static String TAG = "MVDownloadManager";
    private static MVDownloadManager _instacne;
    private Context mContext;
    private MVDownloadManager(Context context){
        mContext = context.getApplicationContext();
    }
    public static MVDownloadManager getInstance(Context context){
        if(_instacne == null){
            _instacne = new MVDownloadManager(context);
            _instacne.start(context);
        }

        return _instacne;
    }


    //TODO refer to aimashi download code, SuperMarket and GameCenter
    private static final Method RESUME_DOWNLOAD = Method.of(DownloadManager.class, "resumeDownload", "([J)V");
    private static final Method PAUSE_DOWNLOAD  = Method.of(DownloadManager.class, "pauseDownload", "([J)V");


    public static boolean isInDownloading(Context con, String res_id)
    {
        int download_id = iDataORM.getInstance(con).getDowndloadID(con, res_id);
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


    public void start(Context con){
        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO)
        {
            con.registerReceiver(receiver, new IntentFilter(android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            con.registerReceiver(receiver, new IntentFilter(android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED));
        }
    }

    public void stop(Context con){
        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO)
        {
            con.unregisterReceiver(receiver);
        }
    }

    public static String[] ramdonvideo= {
        "http://vod01.media.ysten.com/media/new/2011/10/21/sd_dy_xskdjs_20111021.ts",
        "http://payment.icntvcdn.com/media/new/2013/icntv2/media/2014/06/18/1986d2fb6faa41ba9bf9a6bb2c085b48.ts",
        "http://vod01.media.ysten.com/media/new/2011/11/14/sd_dy_qyqx_20111114.ts",
        "http://vod01.media.ysten.com/media/new/2011/11/01/sd_dy_ygdx_20111101.ts",
        "http://vod01.media.ysten.com/media/new/2013/04/16/hd_dy_zjdbre_20130416.ts",
        "http://vod01.media.ysten.com/media/new/2011/10/31/sd_dy_zzx1_20111031.ts"
    };
    public static final int DOWNLOAD_IN = -100;
    public long requestDownload(Context con, VideoItem video, DisplayItem.Media.Episode episode){
        long download_id = -1;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) == false){
            Toast.makeText(con, R.string.no_sdcard_no_download, Toast.LENGTH_SHORT).show();
            return -1;
        }

        if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO){
            if (isInDownloading(con, video.id)) {
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
            iDataORM.getInstance(con).addDownload(con, video.id, download_id, video, episode);


            Log.d(TAG, "new download="+download_id);
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

}
