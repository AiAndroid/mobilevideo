package com.video.ui.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by liuhuadong on 9/23/14.
 */
public class GenericItemList<T>  implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<T> data;
    public long         update_time;
    public Preload      preload;

    public class Preload implements Serializable {
        private static final long serialVersionUID = 1L;
        ArrayList<String>   images;
    }
}
