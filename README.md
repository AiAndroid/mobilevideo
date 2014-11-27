#android framework for mobilevideo

===========
framework for multi-cp for android mobile video app

#数据结构
每个UI大块是由Block组成，Block会包含Blocks，或者具体的展示Items

##元素表达：
###文本
主标题<br>
副标题<br>
一句话描述<br><br>
###media 数据, 可以为空，问题：数据增加，缓存失效<br>
id<br>
poster<br>
type  //类型<br>
ns    //空间，人物，视频，其他<br>
cp    //内容提供方<br>
duration<br>
rate<br>
play_times<br>
likes<br>
unlikes<br>
comments<br>
label{"4更新"，postion:left_top}<br>
episode(更新至24集)<br>

###action
target   HTML5，需要在这里定义
params   K-V类型

###UI
UI_Type<br>
背景图(keep Z order)<br> 
--------text_color<br>
--------bg_color<br>
--------icon<br>
--------back 顺序index 1开始往上画<br>
------------image 1<br>
------------image .<br>
------------image n<br>
--------<br>
<br>
<br>
<br>



#Block
 _______________________________<br>
| Block&nbsp;&nbsp;</br>
|&nbsp;&nbsp;&nbsp;_____________________<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;Block  ____     |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|item|    |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|item|    |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|item|    |<br>
|&nbsp;&nbsp;&nbsp;|____________________|<br>
|<br>
|    _____________________<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;Block  _____    |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;________________|<br>
|_______________________________<br>

</br>
#UI类型(UI_TYPE)
block_port(tab)------------------------------------block_port(tab)<br>
---------tabs_vertical(fragment)[0*1]<br>
---------------block_channel<br>
---------------block_channel<br>
---------------------block<br>
---------------------block<br>
-------------------------item<br>
-------------------------item<br><br>

    public final static int imageswitcher      = 101;//-------广告可以-------------给广告切换image
    public final static int linearlayout_top   = 201;//-------单行button----icon---图标在文本上边
    public final static int linearlayout_left  = 202;//-------单行Button----icon---图标在文本左边
    public final static int linearlayout_poster= 203;//-------单行Button----缺省,一行多个，以海报形式展示,电视剧：“韩剧专场”
    public final static int linearlayout_none  = 204;//-------单行Button----文本， 缺省
    public final static int linearlayout_land  = 205;//-------单行海报---一行多个，以海报形式展示,电影：“韩剧专场”
    public final static int linearlayout_title = 206;//-------标题
    public final static int linearlayout_single_poster = 207;//-------单行海报

    public final static int block_land         = 300;//------------展示块容器------------没有什么用 landscape layout
    public final static int block_port         = 301;//------------展示块容器------------没有什么用 portrait layout

    public final static int tabs_horizontal    = 311;//------------水平tabs------------水平展示tab的父Block
    public final static int tabs_vertical      = 312;//------------垂直tabs------------垂直展示tab类型的Block，用于tablet，TV

    public final static int block_channel      = 400;//------------多个频道块
    public final static int block_sub_channel  = 401;//------------一个频道块

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

<br>

<br>
sample 例子：

public class Block<T>  extends DisplayItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public String              sort;
    public ArrayList<String>   filters;
    public String              pagi;

    public ArrayList<T>        items;
    public ArrayList<Block<T>> blocks;

    public String toString(){
        return "\n\nBlock: " + super.toString() +" \n\titems:"+items + "\n\tend\n\n\n";
    }
}

public class DisplayItem implements Serializable{
	private static final long serialVersionUID = 2L;

	public static class UI implements Serializable {
		private static final long serialVersionUID = 2L;
        public String name;
        public int    id;
        public int    row_count;
        public UI clone(){
            UI item = new UI();
            item.name = name;
            item.id   = id;
            item.row_count = row_count;
            return item;
        }
		public String toString() {
			return " type:" + name + "  id:" + id;
		}
	}

    public static class Media implements Serializable {
        private static final long serialVersionUID = 1L;
        public long  likes;
        public long  unlikes;
        public long  comments;
        public long  play_times;
        public float duration;
        public float  rate;
        public String episode; //update to 24
        public String label;   //corner;
        public CP      cp;
        public Fee     fee;
        public Version version;


        public static class CP implements Serializable{
            private static final long serialVersionUID = 1L;
            String name;
            String versrion_code;
        }

        public static class Fee implements Serializable{
            private static final long serialVersionUID = 1L;
            String name;
            String pay_load;
        }

        public static class Version implements Serializable{
            private static final long serialVersionUID = 1L;
            String latest_version;
            String min_version;
        }

        public Media clone(){
            //TODO
            return new Media();
        }
    }

	public static class Times implements Serializable {
		private static final long serialVersionUID = 1L;
		public long updated;
		public long created;

        public Times clone(){
            Times item = new Times();
            item.created = created;
            item.updated = updated;
            return item;
        }
	}
    public static class Target implements Serializable{
        private static final long serialVersionUID = 1L;
        public String type;
        public String params;


        public Target clone(){
            Target item = new Target();
            item.type = type;
            item.params = params;
            return  item;
        }
    }


    public String id;
    public String title;
    public String sub_title;
    public String desc;
	public String ns;
	public String type;
    public Target     action;
	public ImageGroup images;
	public UI         ui_type;
	public Times      times;
    public Media      media;

    public DisplayItem clone(){
        DisplayItem item = new DisplayItem();

        if(action != null)item.action = action.clone();

        item.ns   = this.ns;
        item.type = this.type;
        item.id   = this.id;
        item.title = this.title;
        item.sub_title = this.sub_title;
        item.desc      = this.desc;
        if(images != null)item.images = this.images.clone();
        if(ui_type != null)item.ui_type = this.ui_type.clone();
        if(times!= null)item.times = times.clone();
        if(media!= null)item.media = media.clone();

        return  item;
    }
	public String toString() {
		return " ns:" + ns + " type:" + type + " target=" + action + " id:" + id + " name:" + title + "images:"
				+ images + " _ui:" + ui_type;
	}

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(obj instanceof DisplayItem){
            return  ((DisplayItem)obj).id.equals(id);
        }

        return  false;
    }
}

ImageSwitcher:
文本，图

