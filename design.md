#数据结构
每个UI大块是由Block组成，Block会包含Blocks，或者具体的展示Items

#Block
 _______________________________</br>
| Block    </br>
|    _____________________</br>
|    |    Block  ____     |</br>
|    |          |item|    |</br>
|    |          |item|    |</br>
|    |          |item|    |</br>
|    |____________________|</br>
|</br>
|    _____________________</br>
|    |    Block  _____    |</br>
|    |          |Block|   |</br>
|    |          |Block|   |</br>
|    |          |Block|   |</br>
|    |          |Block|   |</br>
|    |____________________|</br>
|_______________________________</br>

</br>
#UI类型
viewflipper            101       广告可以             给广告切换image</br>
linearlayout_top       201       单行button    icon   图标在文本上边</br>
linearlayout_left      202       单行Button,   icon， 图标在文本左边</br>
linearlayout_poster    203       单行Button    海报   缺省,一行多个，以海报形式展示,电影：“韩剧专场”</br>
linearlayout_none      204       单行Button    文本， 缺省</br>
</br>
block_land            300            展示块容器       没有什么用 landscape layout</br>
block_port            301            展示块容器       没有什么用 portrait layout</br>
</br>
tabs_horizontal       302            水平tabs         水平展示tab的父Block      </br>
tabs_vertical         303            垂直tabs         垂直展示tab类型的Block，用于tablet，TV</br>
grid_land             304            gridview         水平展示媒体图片</br>
grid_port             305            gridview         垂直展示媒体图片</br>
list_land             306            listview         水平展示媒体图片</br>
list_port             307            listview         垂直展示媒体图片</br>
</br>
list_width_full       308            listview         填充整个宽度  category的port layout</br>
list_height_full      309            listview         填充整个高度  category land layout</br>

</br>
sample 例子：
Block{
    ui_type: {name: block_port, id:301}
    name: 热点
    media:{}
    xxx
    Block{
        ui_type: {name: viewflipper, id:101}

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

    Block:{
            ui_type: {name: block, id:300}
            xxx
            Block:{
                  ui_type: {name: tabs_horizontal, id:401}
                  电视剧
                  Block{
                        ui_type: {name: gridview, id:303}      
                        热播剧
                        items:[
                            {
                                    ui_type: {name: item_landscape, id:401}                                                       
                                    media:
                            }
                        ]            
                  }
                  Block{
                        ui_type: {name: gridview, id:303}                  
                  }
                  Block{ 
                        ui_type: {name: gridview, id:303}                  
                 }
            }
            Block:{}
            Block:{}
            Block:{}
    }
}

class Item{
    String  title       //主标题
    String  sub_title   //副标题
    String  desc        //一句话描述
    
    UI_Type ui_type;
    
    Poster  poster;//海报,放Media里面，还是Media外面
    Media   media;
    
    class Media{
        String id;   //resource id
        String type; //资源类型
        String ns;   //资源空间
        
        
        ImageGroup
    }

}

class Block extends Item
{
      public ArrayList<Block>         blocks
      public ArrayList<Item>          items
}

