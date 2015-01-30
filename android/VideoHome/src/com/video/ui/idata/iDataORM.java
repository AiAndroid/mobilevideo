package com.video.ui.idata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.google.gson.Gson;
import com.tv.ui.metro.model.DisplayItem;

import java.lang.reflect.Type;
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
    public static final Uri FAVOR_CONTENT_URI            = Uri.parse("content://" + AUTHORITY + "/favor");

    private static final String data_collect_interval     = "data_collect_interval";
    private static  String TAG = "iDataORM";

    public static String FavorAction   = "favor";
    public static String HistoryAction = "play_history";

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
            "date_time"
    };

    public static class FavorCol{
        public static final String ID         = "_id";
        public static final String RES_ID     = "res_id";
        public static final String NS         = "ns";
        public static final String VALUE      = "value";
        public static final String Action     = "action";
        public static final String Uploaded   = "uploaded";
        public static final String ChangeDate = "date_time";
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
        ct.put(FavorCol.RES_ID, res_id);
        ct.put(FavorCol.NS,     ns);
        ct.put(FavorCol.VALUE,  json);
        ct.put(FavorCol.Action,  action);
        ct.put(FavorCol.Uploaded,  0);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
        //if exist, update
        if(true == existFavor(context, ns, action, res_id)){
            updateFavor(context, action, ct);
        }else{
            ret = context.getContentResolver().insert(FAVOR_CONTENT_URI, ct);
        }
        return ret;
    }

    public static boolean updateFavor(Context context, String action, ContentValues ct) {
        boolean ret = false;
        String where = String.format(" ns = \'%1$s\' and res_id = \'%2$s\' and action=\'%3$s\'", ct.get(FavorCol.NS), ct.get(FavorCol.RES_ID), ct.get(FavorCol.Action));
        if(context.getContentResolver().update(FAVOR_CONTENT_URI, ct, where, null) > 0){
            ret = true;
        }
        return ret;
    }

    public static boolean existFavor(Context context, String ns, String action, String res_id){
        boolean exist = false;
        String where = FavorCol.NS +"='"+ns+"' and " + FavorCol.RES_ID + " ='" + res_id + "' and action='"+action + "'";
        Cursor cursor = context.getContentResolver().query(FAVOR_CONTENT_URI, new String[]{"_id"}, where, null, null);
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
        String where = FavorCol.NS +"='"+ns+"' and " + FavorCol.RES_ID + " ='" + res_id + "' and action='"+action + "'";
        int lens = context.getContentResolver().delete(FAVOR_CONTENT_URI, where, null);
        return lens;
    }

    public static int getFavoritesCount(Context context, String ns, String action){
        int count = 0;
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
        String where = FavorCol.NS +"='"+ns + "' and action='" + action + "'";
        Cursor cursor = context.getContentResolver().query(FAVOR_CONTENT_URI, actionProject, where, null, null);
        if(cursor != null ){
            count = cursor.getCount();
            cursor.close();
            cursor = null;
        }
        return count;
    }

    public static ArrayList<ActionRecord> getFavorites(Context context, String ns, String action){
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
        String where = FavorCol.NS +"='"+ns + "' and action='" + action + "'";
        Cursor cursor = context.getContentResolver().query(FAVOR_CONTENT_URI, actionProject, where, null, null);
        if(cursor != null ){
            while(cursor.moveToNext()){
                ActionRecord item = new ActionRecord();
                item.id     = cursor.getInt(cursor.getColumnIndex(FavorCol.ID));
                item.res_id = cursor.getString(cursor.getColumnIndex(FavorCol.RES_ID));
                item.ns     = cursor.getString(cursor.getColumnIndex(FavorCol.NS));
                item.json   = cursor.getString(cursor.getColumnIndex(FavorCol.VALUE));
                item.action = cursor.getString(cursor.getColumnIndex(FavorCol.Action));
                item.uploaded = cursor.getInt(cursor.getColumnIndex(FavorCol.Uploaded));
                item.date   = cursor.getString(cursor.getColumnIndex(FavorCol.ChangeDate));
                actionRecords.add(item);
            }
            cursor.close();
            cursor = null;
        }
        return actionRecords;
    }

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
