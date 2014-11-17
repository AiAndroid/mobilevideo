package com.tv.ui.metro.model;

import com.video.ui.utils.DateFormate;

import java.io.Serializable;
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
            sb.append("update_time=" + DateFormate.dateToString(new Date(times.updated)));

        sb.append("\n");
        return  sb.toString();
    }
}

