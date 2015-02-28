package com.video.ui.idata;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.video.ui.utils.VideoUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by liuhuadonbg on 1/31/15.
 */
public class BackgroundService extends IntentService {
    static final String TAG = "apk-download-BackgroundService";
    public BackgroundService() {
        super("video_background");
    }

    private static boolean registed_download = false;
    private static HashMap<Long, Long> downloadingTask = new HashMap<Long, Long>();

    public static void startDownloadAPK(Context context, String apk_url, String title, String desc){
        Intent intent = new Intent(REQUEST_APK_DOWNLOAD);
        intent.putExtra("apk_url", apk_url);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);

        context.startService(intent);
    }

    private static final String apk_mime = "application/vnd.android.package-archive";
    private static long requestDownload(Context context, String apk_url, String title, String desc){
        android.app.DownloadManager dm = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(Uri.parse(apk_url));
        request.setMimeType(apk_mime);
        request.setMimeType(VideoUtils.getMimeType(apk_url));
        request.setTitle(title);
        request.setDescription(desc);
        request.setVisibleInDownloadsUi(true);
        request.setShowRunningNotification(true);
        int downloadFlag = DownloadManager.Request.NETWORK_WIFI;
        if(iDataORM.isOpenCellularOfflineHint(context) == false){
            downloadFlag |=DownloadManager.Request.NETWORK_MOBILE;
        }
        request.setAllowedNetworkTypes(downloadFlag);
        request.allowScanningByMediaScanner();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "." + MimeTypeMap.getFileExtensionFromUrl(apk_url);
        request.setDestinationUri(Uri.fromFile(new File(path)));

        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE|android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        long download_id = dm.enqueue(request);
        registerDownloadMoniter(context);
        downloadingTask.put(download_id, download_id);
        return download_id;
    }

    public static void registerDownloadMoniter(Context context){
        if(registed_download == false) {
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
            context.registerReceiver(receiver, filter);
            registed_download = true;
        }
    }
    public static void unRegisterDownloadApkMonitor(Context context){
        try {
            registed_download = false;
            context.unregisterReceiver(receiver);
        }catch (Exception ne){}
    }
    public static final String REQUEST_APK_DOWNLOAD = "com.miui.video_apk_download";
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null )
            return;

        if(REQUEST_APK_DOWNLOAD.equals(intent.getAction())){
            String apk_url = intent.getStringExtra("apk_url");
            String title   = intent.getStringExtra("title");
            String desc    = intent.getStringExtra("desc");

            requestDownload(getApplicationContext(), apk_url, title, desc);
        }
    }

    static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        String mime      = c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                        if(apk_mime.equals(mime)) {
                            installApk(context, uriString);
                            downloadingTask.remove(downloadId);
                        }
                    }
                }
            }else if (android.app.DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)){
                if(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.FROYO){
                    long downloadId = intent.getLongExtra(android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Log.d(TAG, "new download complete=" + downloadId);
                    android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS);
                        if (android.app.DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            String uriString = c.getString(c.getColumnIndex(android.app.DownloadManager.COLUMN_LOCAL_FILENAME));
                            if(uriString.startsWith("file://")){
                                uriString = uriString.substring(7);
                            }

                            Log.d(TAG, "new downloaded"+uriString);
                            if(uriString.endsWith(".apk")){
                                BackgroundService.installApk(context, uriString);
                            }else{
                                openDownloadsPage(context);
                            }
                        }
                        else if(android.app.DownloadManager.STATUS_FAILED == c.getInt(columnIndex)){
                            Log.d(TAG, "new download fail="+downloadId);
                            openDownloadsPage(context);
                        }
                    }
                }else{
                    openDownloadsPage(context);
                }
            }
        }
    };

    private static void openDownloadsPage(Context context) {
        Intent pageView = new Intent(android.app.DownloadManager.ACTION_VIEW_DOWNLOADS);
        pageView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(pageView);
    }

    public static void installApk(Context context, String uriString){
        try {
            Intent actionIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            actionIntent.setDataAndType(Uri.parse(uriString), "application/vnd.android.package-archive");
            actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(actionIntent);
        }catch (Exception ne){
            ne.printStackTrace();
        }
    }
}
