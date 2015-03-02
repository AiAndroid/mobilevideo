package com.tv.ui.metro.model;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by liuhuadonbg on 3/2/15.
 */
public class Constants {
    public static final String Local_Video = "local";
    public static final String Favor_Video = "favor";
    public static final String History_Video = "history";
    public static final String Offline_Video = "offline";

    public static final String Video_ID_Favor   = "play_favor";
    public static final String Video_ID_History = "play_history";
    public static final String Video_ID_Offline = "play_offline";
    public static final String Video_ID_Local   = "play_local";

    public static final String Entiry_Long_Video  = "pvideo";
    public static final String Entity_Short_Video = "svideo";

    public static final String Entity_App_Video           = "app";
    public static final String Entity_App_Market_Video    = "app_market";
    public static final String Entity_Intent_Video        = "intent";
    public static final String Entity_Search_Video        = "search";
    public static final String Entity_Search_Result_Video = "search_result";
    public static final String Entity_Album               = "album";
    public static final String Entity_Album_Collection    = "album_collection";

    public static class  TestData{
        public static Block<DisplayItem> createTitleBlock(Context context, String title){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.linearlayout_title;
            return item;
        }

        public static Block<DisplayItem> createLineBlock(Context context, String title) {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = title;
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.linearlayout_none;

            item.target = new DisplayItem.Target();
            item.target.entity = "app_market";

            return item;
        }

        public static Block<DisplayItem> createGamesBlock() {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.list_small_icon;
            item.ui_type.row_count = 1;

            item.items = new ArrayList<DisplayItem>();
            {
                DisplayItem child = new DisplayItem();
                child.title = "《全民奇迹》";
                child.sub_title = "公测庆典，一款以《奇迹MU》为蓝本的动作手游，游戏以Unity3D引擎打造.";
                child.target = new DisplayItem.Target();
                child.target.entity = "app";
                child.target.url = "http://app.mi.com/download/79047";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0dd1b652ee178445210c95093ceb434a15b2b8983";
                child.images.put("icon", image);

                item.items.add(child);
            }
            return  item;
        }

        public static Block<DisplayItem> createAppsBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.grid_small_icon;
            item.ui_type.row_count = 5;

            item.items = new ArrayList<DisplayItem>();
            //sohu
            {
                DisplayItem child = new DisplayItem();
                child.title = "搜狐";
                child.target = new DisplayItem.Target();
                child.target.entity = "app";
                child.target.url    = "http://upgrade.m.tv.sohu.com/channels/hdv/4.6.3/SohuTV_4.6.3_1309_201502022041.apk";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01AUSPmZ9K9/bIyRiiudsqkTsG.png";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //pptv
            {
                DisplayItem child = new DisplayItem();
                child.title = "PPTV";
                child.target = new DisplayItem.Target();
                child.target.entity = "app";
                child.target.url    = "http://download.pplive.com/PPTV_aPhone_4.0.4_775_20140916.apk";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01ycTc2CWZ3/eSgjAsS58aTeqF.png";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "爱奇艺";
                child.target = new DisplayItem.Target();
                child.target.entity = "app";
                child.target.url    = "http://mbdapp.iqiyi.com/j/ap/iqiyi_xmshpmk.apk";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p01d5PtLAhv6/yPF76vKE70eA1x.png";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //fengxing
            {
                DisplayItem child = new DisplayItem();
                child.title = "风行";
                child.target = new DisplayItem.Target();
                child.target.entity = "app";
                child.target.url    = "http://m.app.mi.com/download/3581";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p013utylDQ9D/5tqyB2BfGQ1vEg.png";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //phenix
            {
                DisplayItem child = new DisplayItem();
                child.title = "凤凰";
                child.target = new DisplayItem.Target();
                child.target.entity = "app";
                child.target.url    = "http://m.app.mi.com/download/2889";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://image.box.xiaomi.com/mfsv2/download/s010/p016LLWvD4DD/83MBZ9LtZmueev.png";
                child.images.put("icon", image);

                item.items.add(child);
            }

            return item;
        }

        public static Block<DisplayItem> createCaiPiaoBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.grid_small_icon;
            item.ui_type.row_count = 6;

            item.items = new ArrayList<DisplayItem>();
            //sohu
            {
                DisplayItem child = new DisplayItem();
                child.title = "双色球";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.action = Intent.ACTION_VIEW;
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://p7.sinaimg.cn/1641447470/180/23591251173452";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //pptv
            {
                DisplayItem child = new DisplayItem();
                child.title = "大乐透";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.action = Intent.ACTION_VIEW;
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://photocdn.sohu.com/20120119/Img332675933.jpg";
                //image.url = "http://pic.baike.soso.com/p/20100629/bki-20100629110833-109286196.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "竞彩足球";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.action = Intent.ACTION_VIEW;
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://img4.imgtn.bdimg.com/it/u=4131549034,2694083374&fm=11&gp=0.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //fengxing
            {
                DisplayItem child = new DisplayItem();
                child.title = "竞彩篮球";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.action = Intent.ACTION_VIEW;
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://www.v2gg.com/uploads/allimg/140419/0949131193-0.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            //phenix
            {
                DisplayItem child = new DisplayItem();
                child.title = "11选5";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.action = Intent.ACTION_VIEW;
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://img26.nipic.com/20110803/457269_094456472149_1.jpg";
                child.images.put("icon", image);

                item.items.add(child);
            }
            {
                DisplayItem child = new DisplayItem();
                child.title = "更多推荐";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.action = Intent.ACTION_VIEW;
                child.target.url    = "http://cp.mi.com";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://pic.paopaoche.net/up/2014-4/201442594839.png";
                child.images.put("icon", image);

                item.items.add(child);
            }

            return item;
        }


        public static Block<DisplayItem> createMovieSinglePosterBlock() {
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "Sale Off 50%";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.linearlayout_single_poster;
            item.target = new DisplayItem.Target();
            item.target.entity = "intent";
            item.target.url    = "o2o://movies/";

            item.images = new ImageGroup();
            Image image = new Image();
            image.url = "http://t12.baidu.com/it/u=4075131680,1827553139&fm=58";
            image.url = "http://img.2258.com/d/file/yule/mingxing/tuwen/2015-01-21/41c36e08a00b46473f33e62dc91f1bcd.jpg";
            item.images.put("poster", image);
            item.hint = new DisplayItem.Hint();
            item.hint.put("center", "狼图腾(选座): 3折");
            return item;
        }
        public static Block<DisplayItem> createMovieBlock(){
            Block<DisplayItem> item = new Block<DisplayItem>();
            item.title = "";
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.grid_media_port;
            item.ui_type.row_count = 3;

            item.items = new ArrayList<DisplayItem>();
            //sohu
            {
                DisplayItem child = new DisplayItem();
                child.title = "狼图腾";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.url    = "o2o://movies/";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=2411093926,2475191373&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "3 折");

                item.items.add(child);
            }
            //pptv
            {
                DisplayItem child = new DisplayItem();
                child.title = "天将雄师 国语 2015";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.url    = "o2o://movies/";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=753869764,2801031141&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "9.9元");

                item.items.add(child);
            }
            //iqiyi
            {
                DisplayItem child = new DisplayItem();
                child.title = "澳门风云2";
                child.target = new DisplayItem.Target();
                child.target.entity = "intent";
                child.target.url    = "o2o://movies/";
                child.images = new ImageGroup();
                Image image = new Image();
                image.url = "http://t2.baidu.com/it/u=514158127,293282070&fm=20";
                child.images.put("poster", image);
                child.hint = new DisplayItem.Hint();
                child.hint.put("center", "3D 9.9元");

                item.items.add(child);
            }

            return item;
        }
    }
}
