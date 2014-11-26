package com.video.ui.view;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class LayoutConstant {
    public final static int imageswitcher      = 101;//-------广告可以-------------给广告切换image
    public final static int linearlayout_top   = 201;//-------单行button----icon---图标在文本上边
    public final static int linearlayout_left  = 202;//-------单行Button----icon---图标在文本左边
    public final static int linearlayout_poster= 203;//-------单行Button----缺省,一行多个，以海报形式展示,电视剧：“韩剧专场”
    public final static int linearlayout_none  = 204;//-------单行Button----文本， 缺省
    public final static int linearlayout_land  = 205;//-------单行海报---一行多个，以海报形式展示,电影：“韩剧专场”

    public final static int block_land         = 300;//------------展示块容器------------没有什么用 landscape layout
    public final static int block_port         = 301;//------------展示块容器------------没有什么用 portrait layout

    public final static int tabs_horizontal    = 311;//------------水平tabs------------水平展示tab的父Block
    public final static int tabs_vertical      = 312;//------------垂直tabs------------垂直展示tab类型的Block，用于tablet，TV

    public final static int block_channel      = 400;//------------一个频道块
    public final static int block_tabs         = 410;//------------
    public final static int grid_media_land    = 411;//------------gridview------------水平展示媒体图片
    public final static int grid_media_port    = 412;//------------gridview------------垂直展示媒体图片
    public final static int list_media_land    = 413;//------------listview------------水平展示媒体图片
    public final static int list_media_port    = 414;//------------listview------------垂直展示媒体图片

    public final static int list_category_land = 421;//------------listview------------category的land layout
    public final static int list_category_port = 422;//------------listview------------category port layout
    public final static int grid_item_selection  = 431;//------------grid----------------精选
    public final static int grid_block_selection = 432;//------------grid----------------精选
    public final static int list_rich_header   = 441;//--------------------------------排行
}
