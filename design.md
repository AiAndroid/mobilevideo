#数据结构
每个UI大块是由Block组成，Block会包含Blocks，或者具体的展示Items

##元素表达：
###文本
主标题
副标题
一句话描述
###UI
UI_Type
背景图片



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
#UI类型
viewflipper------------101-------广告可以-------------给广告切换image<br>
linearlayout_top-------201-------单行button----icon---图标在文本上边<br>
linearlayout_left------202-------单行Button----icon---图标在文本左边<br>
linearlayout_poster----203-------单行Button----海报---缺省,一行多个，以海报形式展示,电影：“韩剧专场”<br>
linearlayout_none------204-------单行Button----文本， 缺省<br>
<br>
block_land------------300------------展示块容器------------没有什么用 landscape layout<br>
block_port------------301------------展示块容器------------没有什么用 portrait layout<br>
<br>
tabs_horizontal-------302------------水平tabs------------水平展示tab的父Block      <br>
tabs_vertical---------303------------垂直tabs------------垂直展示tab类型的Block，用于tablet，TV<br>
grid_land-------------304------------gridview------------水平展示媒体图片<br>
grid_port-------------305------------gridview------------垂直展示媒体图片<br>
list_land-------------306------------listview------------水平展示媒体图片<br>
list_port-------------307------------listview------------垂直展示媒体图片<br>
<br>
list_width_full-------308------------listview------------填充整个宽度  category的port layout<br>
list_height_full------309------------listview------------填充整个高度  category land layout<br>


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

