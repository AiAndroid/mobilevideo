package com.video.ui.utils;

import android.content.Context;
import android.webkit.MimeTypeMap;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;

import java.io.*;

/**
 * Created by liuhuadonbg on 2/2/15.
 */
public class VideoUtils {
    public static VideoItem getVideoItem(Context context, String id){
        try {
            FileInputStream fis = new FileInputStream(context.getCacheDir().getAbsolutePath() + "/videoinfo/" + id);
            ObjectInputStream ois = new ObjectInputStream(fis);

            VideoItem item = (VideoItem) ois.readObject();
            return  item;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getVideoID(DisplayItem item){
        return item.id.substring(item.id.indexOf('/', 0) + 1);
    }

    public static void saveVideoInfo(Context context, VideoItem item){
        FileOutputStream fos = null;
        try {
            if(new File(context.getCacheDir().getAbsolutePath() + "/videoinfo/").exists() == false)
                new File(context.getCacheDir().getAbsolutePath() + "/videoinfo/").mkdirs();

            fos = new FileOutputStream(context.getCacheDir().getAbsolutePath() + "/videoinfo/" + item.id);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(item);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
