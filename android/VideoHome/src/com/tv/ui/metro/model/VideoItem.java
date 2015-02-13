package com.tv.ui.metro.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liuhuadong on 12/1/14.
 */
public class VideoItem extends DisplayItem {
    private static final long serialVersionUID = 1L;

    public ArrayList<Block<DisplayItem>>  blocks;
    public int download_trys = 0;
}
