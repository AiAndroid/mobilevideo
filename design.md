#数据结构
每个UI大块是由Block组成，Block会包含Blocks，或者具体的展示Items

#Block
 _______________________________<br>
| Block&nbsp;&nbsp</br>
|&nbsp;&nbsp;&nbsp;_____________________<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;Block  ____&nbsp;&nbsp |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|item|&nbsp;&nbsp|<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|item|&nbsp;&nbsp|<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|item|&nbsp;&nbsp|<br>
|&nbsp;&nbsp;&nbsp;|____________________|<br>
|<br>
|&nbsp;&nbsp_____________________<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;Block  _____&nbsp;&nbsp|<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|Block|   |<br>
|&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;________________|<br>
|_______________________________<br>

</br>
#UI类型
viewflipper&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp101&nbsp;&nbsp   广告可以&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp 给广告切换image<br>
linearlayout_top&nbsp;&nbsp   201&nbsp;&nbsp   单行button&nbsp;&nbspicon   图标在文本上边<br>
linearlayout_left&nbsp;&nbsp  202&nbsp;&nbsp   单行Button,   icon， 图标在文本左边<br>
linearlayout_poster&nbsp;&nbsp203&nbsp;&nbsp   单行Button&nbsp;&nbsp海报   缺省,一行多个，以海报形式展示,电影：“韩剧专场”<br>
linearlayout_none&nbsp;&nbsp  204&nbsp;&nbsp   单行Button&nbsp;&nbsp文本， 缺省<br>
<br>
block_land&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp300&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp展示块容器&nbsp;&nbsp   没有什么用 landscape layout<br>
block_port&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp301&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp展示块容器&nbsp;&nbsp   没有什么用 portrait layout<br>
<br>
tabs_horizontal&nbsp;&nbsp   302&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp水平tabs&nbsp;&nbsp&nbsp;&nbsp 水平展示tab的父Block&nbsp;&nbsp  <br>
tabs_vertical&nbsp;&nbsp&nbsp;&nbsp 303&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp垂直tabs&nbsp;&nbsp&nbsp;&nbsp 垂直展示tab类型的Block，用于tablet，TV<br>
grid_land&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp 304&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspgridview&nbsp;&nbsp&nbsp;&nbsp 水平展示媒体图片<br>
grid_port&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp 305&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspgridview&nbsp;&nbsp&nbsp;&nbsp 垂直展示媒体图片<br>
list_land&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp 306&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsplistview&nbsp;&nbsp&nbsp;&nbsp 水平展示媒体图片<br>
list_port&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp 307&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsplistview&nbsp;&nbsp&nbsp;&nbsp 垂直展示媒体图片<br>
<br>
list_width_full&nbsp;&nbsp   308&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsplistview&nbsp;&nbsp&nbsp;&nbsp 填充整个宽度  category的port layout<br>
list_height_full&nbsp;&nbsp  309&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsplistview&nbsp;&nbsp&nbsp;&nbsp 填充整个高度  category land layout<br>

<br>
sample 例子：
Block{
&nbsp;&nbspui_type: {name: block_port, id:301}
&nbsp;&nbspname: 热点
&nbsp;&nbspmedia:{}
&nbsp;&nbspxxx
&nbsp;&nbspBlock{
&nbsp;&nbsp&nbsp;&nbspui_type: {name: viewflipper, id:101}

&nbsp;&nbsp&nbsp;&nbspitems:[
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp{
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspui_type: {name: item_landscape_ads, id:403}
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsptitle： xxx
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspsubtitle:xxx
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspdesc:xxx
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspmedia:{id, type, ns，poster images, hot images,xxx} //和以前类似,可以把明星，电影分成不同的ns
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspaction{target:xxxx, params:xxxx} //params可以承载k-v 数据，里面会保留有关键字，intent, path,
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspxx
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspxx
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp }
&nbsp;&nbsp&nbsp;&nbsp]
&nbsp;&nbsp}

&nbsp;&nbspBlock:{
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspui_type: {name: block, id:300}
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspxxx
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspBlock:{
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  ui_type: {name: tabs_horizontal, id:401}
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  电视剧
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  Block{
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspui_type: {name: gridview, id:303}&nbsp;&nbsp  
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp热播剧
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspitems:[
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp{
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspui_type: {name: item_landscape, id:401}&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp   
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspmedia:
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp}
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp]&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  }
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  Block{
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspui_type: {name: gridview, id:303}&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  }
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  Block{ 
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspui_type: {name: gridview, id:303}&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp  
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp }
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp}
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspBlock:{}
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspBlock:{}
&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbspBlock:{}
&nbsp;&nbsp}
}

class Item{
&nbsp;&nbspString  title&nbsp;&nbsp   //主标题
&nbsp;&nbspString  sub_title   //副标题
&nbsp;&nbspString  desc&nbsp;&nbsp&nbsp;&nbsp//一句话描述
&nbsp;&nbsp
&nbsp;&nbspUI_Type ui_type;
&nbsp;&nbsp
&nbsp;&nbspPoster  poster;//海报,放Media里面，还是Media外面
&nbsp;&nbspMedia   media;
&nbsp;&nbsp
&nbsp;&nbspclass Media{
&nbsp;&nbsp&nbsp;&nbspString id;   //resource id
&nbsp;&nbsp&nbsp;&nbspString type; //资源类型
&nbsp;&nbsp&nbsp;&nbspString ns;   //资源空间
&nbsp;&nbsp&nbsp;&nbsp
&nbsp;&nbsp&nbsp;&nbsp
&nbsp;&nbsp&nbsp;&nbspImageGroup
&nbsp;&nbsp}

}

class Block extends Item
{
&nbsp;&nbsp  public ArrayList<Block>&nbsp;&nbsp&nbsp;&nbsp blocks
&nbsp;&nbsp  public ArrayList<Item>&nbsp;&nbsp&nbsp;&nbsp  items
}

