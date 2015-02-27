package com.video.ui.utils;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * Created by liuhuadonbg on 2/12/15.
 */
public class Utils {
    public static long getSDAvailaleSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        Log.d("test", "getSDAvailaleSize " + availableBlocks * blockSize);
        return availableBlocks * blockSize;
    }

    public static long getSDAllSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        Log.d("test", "getSDAllSize " + availableBlocks * blockSize);
        return availableBlocks * blockSize;
    }

    public static String convertToFormateSize(long size){
        if(size > 1024){
            long sizeInKB = size / 1024;
            if(sizeInKB > 1024){
                long sizeInMB = sizeInKB / 1024;
                if(sizeInMB > 1024){
                    long sizeInGB = sizeInMB / 1024;
                    sizeInMB = (long) (sizeInMB % 1024 / 102.4);
                    return sizeInGB + (sizeInMB == 0 ? "" : "." + sizeInMB) + "GB";
                }
                sizeInKB = (long) (sizeInKB % 1024 / 102.4);
                return sizeInMB + (sizeInKB == 0 ? "" : "." + sizeInKB) + "MB";
            }
            size = (long) (size % 1024 / 102.4);
            return sizeInKB + (size == 0 ? "" : "." + size) + "KB";
        }
        return size + "B";
    }
}
