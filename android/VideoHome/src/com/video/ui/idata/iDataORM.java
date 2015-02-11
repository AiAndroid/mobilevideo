package com.video.ui.idata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.android.volley.toolbox.ClearCacheRequest;
import com.google.gson.Gson;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.SettingActivity;

import java.lang.reflect.Type;
import java.net.PortUnreachableException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tv metro on 7/7/14.
 *
 */
public class iDataORM {
    public static final String AUTHORITY                 = "com.video.ui.mobile";
    public static final Uri SETTINGS_CONTENT_URI         = Uri.parse("content://" + AUTHORITY + "/settings");
    public static final Uri ALBUM_CONTENT_URI            = Uri.parse("content://" + AUTHORITY + "/local_album");
    public static final Uri DOWNLOAD_CONTENT_URI         = Uri.parse("content://" + AUTHORITY + "/download");
    public static final Uri SEARCH_CONTENT_URI           = Uri.parse("content://" + AUTHORITY + "/search");

    private static final String data_collect_interval     = "data_collect_interval";
    private static  String TAG = "iDataORM";

    public static String FavorAction   = "favor";
    public static String HistoryAction = "play_history";
    public static String Max_Show_Search = "Max_Show_Search";

    public static String mobile_offline_hint = "mobile_offline_hint";
    public static String KEY_PREFERENCE_SOURCE = "prefer_source_cp";

    private static iDataORM _instance;
    public static iDataORM getInstance(Context con){
        if(_instance == null){
            _instance = new iDataORM(con);
        }

        return _instance;
    }

    private Context mContext;
    private iDataORM(Context con){
        mContext = con.getApplicationContext();
    }

    public static String[]settingsProject =  new String[]{
            "_id",
            "name",
            "application",
            "value",
    };

    public static String[] actionProject =  new String[]{
            "_id",
            "res_id",
            "ns",
            "value",
            "action",
            "uploaded",
            "date_time",
            "date_int"
    };

    public static String[] downloadProject =  new String[]{
            "_id",
            "res_id",
            "ns",
            "value",

            "download_url",

            "sub_id",
            "sub_value",

            "uploaded",

            "date_time",
            "date_int",

            "download_status",
            "download_path",

            "totalsizebytes",
            "downloadbytes",

            "download_id"
    };

    public static String[]searchProject =  new String[]{
            "_id",
            "key",
            "date_time",
            "date_int",
    };

    public static boolean isPriorityStorage(Context baseContext) {
        return false;
    }

    public static boolean isOpenCellularPlayHint(Context context) {
        return false;
    }

    public static boolean isOpenCellularOfflineHint(Context context) {
        return false;
    }

    public static boolean isMiPushOn() {
        return true;
    }

    public static void setPriorityStorage(Context context, boolean priorityStorage) {

    }

    public static void setOpenCellularPlayHint(Context context, boolean checked) {
    }

    public static void setOpenCellularOfflineHint(Context context, boolean checked) {
    }

    public static void setMiPushOn(Context context, boolean miPushOn) {

    }

    public static class ColumsCol {
        public static final String ID         = "_id";
        public static final String RES_ID     = "res_id";
        public static final String NS         = "ns";
        public static final String VALUE      = "value";
        public static final String KEY        = "key";
        public static final String Action     = "action";
        public static final String Uploaded   = "uploaded";
        public static final String ChangeDate = "date_time";
        public static final String ChangeLong = "date_int";

        public static final String SUB_ID           = "sub_id";
        public static final String SUB_VALUE        = "sub_value";
        public static final String DOWNLOAD_ID      = "download_id";
        public static final String DOWNLOAD_STATUS  = "download_status";
        public static final String DOWNLOAD_PATH    = "download_path";
        public static final String DOWNLOAD_URL     = "download_url";

        public static final String TOTAL_SIZE       = "totalsizebytes";
        public static final String DOWNLOADED_SIZE  = "downloadbytes";
    }

    public static class ActionRecord<T>{
        public int    id;
        public String res_id;
        public String ns;
        public String json;
        public String action;
        public int    uploaded;
        public Object object;
        public String date;
        public long   dateInt;

        //just for download
        public int    download_id;
        public String download_url;
        public String download_path;
        public int    download_status;
        public String sub_id;
        public String sub_value;
        public long   downloadbytes;
        public long   totalsizebytes;

        public static <T> T parseJson(Gson gson, String json, Type type){
            return gson.fromJson(json, type);
        }
    }

    public static String toJson(Gson gson, DisplayItem item){
        return gson.toJson(item);
    }

    static Gson gson = new Gson();
    public static Uri addFavor(Context context, String ns, String action,String res_id,  DisplayItem item){
        return addFavor(context, ns, action, res_id, gson.toJson(item));
    }
    /**
     * add history and favor
     * @param context
     * @param ns     video
     * @param action favor/history
     * @param res_id media id
     * @param json   whole video info json
     * @return
     */
    public static Uri addFavor(Context context, String ns, String action,String res_id,  String json){
        Uri ret = null;
        ContentValues ct = new ContentValues();
        ct.put(ColumsCol.RES_ID, res_id);
        ct.put(ColumsCol.NS,     ns);
        ct.put(ColumsCol.VALUE,  json);
        ct.put(ColumsCol.Action,  action);
        ct.put(ColumsCol.Uploaded,  0);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
        ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());
        //if exist, update
        if(true == existFavor(context, ns, action, res_id)){
            updateFavor(context, action, ct);
        }else{
            ret = context.getContentResolver().insert(ALBUM_CONTENT_URI, ct);
        }
        return ret;
    }

    public static boolean updateFavor(Context context, String action, ContentValues ct) {
        boolean ret = false;
        String where = String.format(" ns = \'%1$s\' and res_id = \'%2$s\' and action=\'%3$s\'", ct.get(ColumsCol.NS), ct.get(ColumsCol.RES_ID), ct.get(ColumsCol.Action));
        if(context.getContentResolver().update(ALBUM_CONTENT_URI, ct, where, null) > 0){
            ret = true;
        }
        return ret;
    }

    public static boolean existFavor(Context context, String ns, String action, String res_id){
        boolean exist = false;
        String where = ColumsCol.NS +"='"+ns+"' and " + ColumsCol.RES_ID + " ='" + res_id + "' and action='"+action + "'";
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, new String[]{"_id"}, where, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
            cursor = null;
        }
        return exist;
    }

    public static int removeFavor(Context context, String ns, String action, String res_id){
        String where = ColumsCol.NS +"='"+ns+"' and " + ColumsCol.RES_ID + " ='" + res_id + "' and action='"+action + "'";
        int lens = context.getContentResolver().delete(ALBUM_CONTENT_URI, where, null);
        return lens;
    }

    public static int getFavoritesCount(Context context, String ns, String action){
        int count = 0;
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
        String where = ColumsCol.NS +"='"+ns + "' and action='" + action + "'";
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, new String[]{"_id"}, where, null, null);
        if(cursor != null ){
            count = cursor.getCount();
            cursor.close();
            cursor = null;
        }
        return count;
    }

    public static ArrayList<ActionRecord> getFavorites(Context context, String ns, String action, int before_date){
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
        String where = ColumsCol.NS +"='"+ns + "' and action='" + action + "' and date_int >= "+before_date;
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, actionProject, where, null, " date_int desc");
        if(cursor != null ){
            while(cursor.moveToNext()){
                ActionRecord item = new ActionRecord();
                item.id     = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                item.ns     = cursor.getString(cursor.getColumnIndex(ColumsCol.NS));
                item.json   = cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE));
                item.action = cursor.getString(cursor.getColumnIndex(ColumsCol.Action));
                item.uploaded = cursor.getInt(cursor.getColumnIndex(ColumsCol.Uploaded));
                item.date   = cursor.getString(cursor.getColumnIndex(ColumsCol.ChangeDate));
                item.dateInt = cursor.getLong(cursor.getColumnIndex(ColumsCol.ChangeLong));

                actionRecords.add(item);
            }
            cursor.close();
            cursor = null;
        }
        return actionRecords;
    }

    public static ArrayList<ActionRecord> getFavorites(Context context, String ns, String action){
        return getFavorites(context, ns, action, 0);
    }

    /*
    * download begin
    */
    public static Uri addDownload(Context context, String res_id, long download_id, String download_url, DisplayItem item, DisplayItem.Media.Episode episode){
        String json = gson.toJson(item);
        Uri ret = null;
        ContentValues ct = new ContentValues();
        ct.put(ColumsCol.RES_ID, res_id);
        ct.put(ColumsCol.VALUE,  json);
        ct.put(ColumsCol.SUB_ID,    episode.id);
        ct.put(ColumsCol.SUB_VALUE, gson.toJson(episode));
        ct.put(ColumsCol.NS,     "video"); //TODO need add ns to download apk
        ct.put(ColumsCol.DOWNLOAD_URL, download_url);
        ct.put(ColumsCol.DOWNLOAD_ID, download_id);
        ct.put(ColumsCol.Uploaded,  0);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
        ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());
        //if exist, update
        if(true == existDowndload(context, res_id, episode.id)){
            updateDownload(context, ct);
        }else{
            ret = context.getContentResolver().insert(DOWNLOAD_CONTENT_URI, ct);
        }
        return ret;
    }

    private static final int FINISHED    = 1;
    private static final int NOT_FINISHED=0;
    public static void downloadFinished(Context context, int download_id){
        ActionRecord ar = getDowndloadByDID(context, download_id);
        ar.download_status = FINISHED;
        updateDownload(context, actionRecordToContentValues(ar));
    }

    public static ActionRecord getDowndloadByDID(Context context, int download_id){
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, " download_id = "+download_id, null, " date_int desc");
        if(cursor != null ){
            while(cursor.moveToNext()){
                return formatActionRecord(cursor);
            }
            cursor.close();
            cursor = null;
        }
        return null;
    }

    public static ActionRecord formatActionRecord(Cursor cursor){
        ActionRecord item = new ActionRecord();
        item.id     = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
        item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));

        item.json    = cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE));
        item.sub_id  = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
        item.sub_value  = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_VALUE));

        item.uploaded = cursor.getInt(cursor.getColumnIndex(ColumsCol.Uploaded));
        item.date   = cursor.getString(cursor.getColumnIndex(ColumsCol.ChangeDate));
        item.dateInt = cursor.getLong(cursor.getColumnIndex(ColumsCol.ChangeLong));
        item.download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
        item.download_url = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
        item.download_status = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_STATUS));
        item.download_path   = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_PATH));
        return  item;
    }

    private static ContentValues actionRecordToContentValues(ActionRecord ar){
        ContentValues ct = new ContentValues();
        ct.put(ColumsCol.ID,        ar.id);
        ct.put(ColumsCol.RES_ID,    ar.res_id);
        ct.put(ColumsCol.VALUE,     ar.json );
        ct.put(ColumsCol.SUB_ID,    ar.sub_id);
        ct.put(ColumsCol.SUB_VALUE, ar.sub_value);
        ct.put(ColumsCol.NS,        ar.ns); //TODO need add ns to download apk
        ct.put(ColumsCol.DOWNLOAD_URL, ar.download_url);
        ct.put(ColumsCol.DOWNLOAD_ID,  ar.download_id);
        ct.put(ColumsCol.Uploaded,     ar.uploaded);
        ct.put(SettingsCol.ChangeDate, ar.date);
        ct.put(ColumsCol.ChangeLong,   ar.dateInt);

        ct.put(ColumsCol.DOWNLOAD_PATH,   ar.download_path);
        ct.put(ColumsCol.DOWNLOAD_STATUS, ar.download_status);

        return  ct;
    }

    public static boolean updateDownload(Context context, ContentValues ct) {
        boolean ret = false;
        String where = String.format(" res_id = \'%1$s\' ", ct.get(ColumsCol.RES_ID));
        if(context.getContentResolver().update(DOWNLOAD_CONTENT_URI, ct, where, null) > 0){
            ret = true;
        }
        return ret;
    }

    public static int getDowndloadID(Context context, String res_id, String sub_id){
        int download_id = -1;
        String where = ColumsCol.RES_ID + " ='" + res_id +  "' and " + ColumsCol.SUB_ID + " ='"+sub_id + "'";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_ID}, where, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0 && cursor.moveToFirst()){
                download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
            }
            cursor.close();
            cursor = null;
        }
        return download_id;
    }

    public static boolean existDowndload(Context context, String res_id, String sub_id){
        boolean exist = false;
        String where = ColumsCol.RES_ID + " ='" + res_id +  "' and " + ColumsCol.SUB_ID + " ='"+sub_id + "'";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
            cursor = null;
        }
        return exist;
    }

    public static int removeDownload(Context context, String res_id){
        String where = ColumsCol.RES_ID + " ='" + res_id + "'";
        int lens = context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, null);
        return lens;
    }

    public static int getDownloadCount(Context context){
        int count = 0;
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{"_id"}, null, null, null);
        if(cursor != null ){
            count = cursor.getCount();
            cursor.close();
            cursor = null;
        }
        return count;
    }

    public static ArrayList<ActionRecord> getDownloads(Context context){
       return getDownloads(context, 0);
    }

    public static ArrayList<ActionRecord> getDownloads(Context context, int before_date){
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, " date_int >= "+before_date + " and download_status != 1", null, " date_int desc");
        if(cursor != null ){
            while(cursor.moveToNext()){
                ActionRecord item = new ActionRecord();
                item.id     = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));

                item.json    = cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE));
                item.sub_id  = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
                item.sub_value  = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_VALUE));

                item.uploaded = cursor.getInt(cursor.getColumnIndex(ColumsCol.Uploaded));
                item.date   = cursor.getString(cursor.getColumnIndex(ColumsCol.ChangeDate));
                item.dateInt = cursor.getLong(cursor.getColumnIndex(ColumsCol.ChangeLong));
                item.download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
                item.download_url = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));

                actionRecords.add(item);
            }
            cursor.close();
            cursor = null;
        }
        return actionRecords;
    }
    /*
    *download end
    */

    /*
     * begin search history
     */
    public static class SearchHistoryItem{
        public int    id         ;
        public String key        ;
        public String date       ;
        public long   date_int   ;
    }

    public static boolean hasSearchHistory(Context context) {
        boolean exist = false;
        Cursor cursor = context.getContentResolver().query(SEARCH_CONTENT_URI, new String[]{ColumsCol.ID}, null, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
            cursor = null;
        }
        return exist;
    }

    public static int removeSearchHistory(Context context, String key) {
        int lines = 0;
        //remove all
        if(TextUtils.isEmpty(key)){
            lines = context.getContentResolver().delete(SEARCH_CONTENT_URI, null, null);
        }else{
            String where = ColumsCol.KEY + " ='" + key + "'";
            lines = context.getContentResolver().delete(SEARCH_CONTENT_URI, where, null);
        }

        return lines;
    }

    public static Uri addSearchHistory(Context context, String key) {

        Uri ret = null;
        ContentValues ct = new ContentValues();
        ct.put(ColumsCol.KEY, key);
        ct.put(ColumsCol.ChangeDate, dateToString(new Date()));
        ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());
        //if exist, update
        if(true == existSearch(context, key)){
            updateSearch(context, ct);
        }else{
            ret = context.getContentResolver().insert(SEARCH_CONTENT_URI, ct);
        }
        return ret;

    }

    public static boolean updateSearch(Context context, ContentValues ct) {
        boolean ret = false;
        String where = String.format(" key = \'%1$s\' ", ct.get(ColumsCol.KEY));
        if(context.getContentResolver().update(SEARCH_CONTENT_URI, ct, where, null) > 0){
            ret = true;
        }
        return ret;
    }

    public static boolean existSearch(Context context, String key){
        boolean exist = false;
        String where = ColumsCol.KEY + " ='" + key +  "' " ;
        Cursor cursor = context.getContentResolver().query(SEARCH_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
            cursor = null;
        }
        return exist;
    }

    public static ArrayList<SearchHistoryItem> getSearchHistory(Context con, int count){
        ArrayList<SearchHistoryItem> actionRecords = new ArrayList<SearchHistoryItem>();
        Cursor cursor = con.getContentResolver().query(SEARCH_CONTENT_URI, searchProject, null, null, " date_int desc");
        if(cursor != null ){
            while(cursor.moveToNext() && actionRecords.size() <count){
                SearchHistoryItem item = new SearchHistoryItem();
                item.id     = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                item.key    = cursor.getString(cursor.getColumnIndex(ColumsCol.KEY));
                item.date   = cursor.getString(cursor.getColumnIndex(ColumsCol.ChangeDate));
                item.date_int = cursor.getLong(cursor.getColumnIndex(ColumsCol.ChangeLong));

                actionRecords.add(item);
            }
            cursor.close();
        }

        return actionRecords;
    }
    /*
     * end search history
     */

    public int getDataCollectionInterval(int defaultValue) {
        return 120;
    }


    public static int getIntValue(Context con, String name, int defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        int valueB = defaultValue;
        try{
            if(value != null){
                valueB = Integer.valueOf(value);
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }

    public static boolean getBooleanValue(Context con, String name, boolean defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        boolean valueB = defaultValue;
        try{
            if(value != null){
                valueB = value.equals("1")?true:false;
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }


    public static String getStringValue(Context con, String name, String defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        String valueB = defaultValue;
        try{
            if(value != null && value.length() > 0){
                valueB = value;
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }

    public static class SettingsCol{
        public static final String ID         = "_id";
        public static final String Name       = "name";
        public static final String Value      = "value";
        public static final String Application= "application";
        public static final String ChangeDate = "date_time";
        public static final String ChangeLong = "date_long";
    }



    //settings
    public String getSettingValue(String name) {
        String va = null;
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
        }
        return va;
    }

    public long getLongValue(String name, long defaultV) {
        String va = String.valueOf(defaultV);
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
        }
        return  Long.valueOf(va);
    }

    public boolean getBooleanValue(String name, boolean defaultV) {
        Boolean va = defaultV;
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value)).equals("0")?false:true;
            }
            cursor.close();
        }
        return  va;
    }

    public Uri addSetting(String name, String value) {
        return addSetting(mContext, name, value);
    }

    public static Uri addSetting(Context context, String name, String value) {
        return addSetting(context, SETTINGS_CONTENT_URI, name, value);
    }

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static String getSettingValue(Context con, Uri settingUri, String name) {
        String va = null;
        String where = SettingsCol.Name +"='"+name+"'";
        Cursor cursor = con.getContentResolver().query(settingUri, settingsProject,where, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
            cursor = null;
        }
        return va;
    }

    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);

        return ctime;
    }

    private static Uri addSetting(Context context, Uri settingUri, String name, String value) {
        Uri ret = null;
        ContentValues ct = new ContentValues();
        ct.put(SettingsCol.Name, name);
        ct.put(SettingsCol.Value, value);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
        //if exist, update
        if(null != getSettingValue(context, settingUri, name))
        {
            updateSetting(context, settingUri, name, value);
        }
        else
        {
            ret = context.getContentResolver().insert(settingUri, ct);
        }

        return ret;
    }

    public static boolean updateSetting(Context context, Uri settingUri, String name, String value) {
        boolean ret = false;
        String where = String.format(" name = \"%1$s\" ", name);
        ContentValues ct = new ContentValues();
        ct.put(SettingsCol.Value, value);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));

        if(context.getContentResolver().update(settingUri, ct, where, null) > 0)
        {
            ret = true;
        }
        return ret;
    }

}
