package com.tv.ui.metro.model;

import java.io.Serializable;

/**
 * Created by liuhuadonbg on 2/14/15.
 */
public class AppVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    public int    version_code;
    public String recent_change;
    public String version_name;
    public String apk_url;
    public String release_date;
    public String released_by;
}
