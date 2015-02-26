package com.tv.ui.metro.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by liuhuadong on 9/23/14.
 */
public class GenericBlock<T> implements Serializable {
    private static final long serialVersionUID = 2L;
    public ArrayList<Block<T>>  blocks;
    public DisplayItem.Times    times;
    public String               base_url;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" GenericBlock: ");

        if(times != null)
            sb.append("update_time=" + dateToString(new Date(times.updated)));

        sb.append("\n");
        return  sb.toString();
    }

    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);

        return ctime;
    }
}

