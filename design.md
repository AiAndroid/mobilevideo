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

#Code in Android

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

<br>

<br>
sample 例子：
Block{<br>
    ui_type: {name: block_port, id:301}<br>
    name: 热点<br>
    media:{}<br>
    xxx<br>
    Block{<br>
        ui_type: {name: viewflipper, id:101}<br><br>

        items:[
            {
                ui_type: {name: item_landscape_ads, id:403}
                title： xxx
                subtitle:xxx
                desc:xxx
                media:{id, type, ns，poster images, hot images,xxx} //和以前类似,可以把明星，电影分成不同的ns
                action{target:xxxx, params:xxxx} //params可以承载k-v 数据，里面会保留有关键字，intent, path,
                xx
                xx
             }
        ]
    }

    Block:{<br>
            ui_type: {name: block, id:300}<br>
            xxx<br>
            Block:{<br>
                  ui_type: {name: tabs_horizontal, id:401}<br>
                  电视剧<br>
                  Block{<br>
                        ui_type: {name: gridview, id:303}      <br>
                        热播剧<br>
                        items:[<br>
                            {<br>
                                    ui_type: {name: item_landscape, id:401}<br>
                                    media:<br>
                            }<br>
                        ]            <br>
                  }<br>
                  Block{<br>
                        ui_type: {name: gridview, id:303}                  <br>
                  }<br>
                  Block{ <br>
                        ui_type: {name: gridview, id:303}                  <br>
                 }<br>
            }<br>
            Block:{}<br>
            Block:{}<br>
            Block:{}<br>
    }<br>
}<br>
<br>
<br>
class Item{<br>
    String  title       //主标题<br>
    String  sub_title   //副标题<br>
    String  desc        //一句话描述<br><br>
    
    UI_Type ui_type;<br><br>
    
    Poster  poster;//海报,放Media里面，还是Media外面<br>
    Media   media;<br><br>
    
    class Media{<br>
        String id;   //resource id<br>
        String type; //资源类型<br>
        String ns;   //资源空间<br><br><br>
        
        
        ImageGroup<br>
    }<br><br>

}<br>
<br>
class Block extends Item<br>
{<br>
      public ArrayList<Block>         blocks<br>
      public ArrayList<Item>          items<br>
}<br>


ImageSwitcher:
文本，图
