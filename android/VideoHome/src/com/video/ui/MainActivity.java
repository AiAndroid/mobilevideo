package com.video.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.*;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.aimashi.mobile.video.view.UserView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.tv.ui.metro.model.ImageGroup;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.TabsGsonLoader;
import com.video.ui.utils.ViewUtils;
import com.video.ui.view.EmptyLoadingView;
import com.video.ui.view.MetroFragment;
import com.video.ui.view.RecommendCardViewClickListenerFactory;
import com.video.ui.view.UserViewFactory;
import com.video.ui.view.subview.AdsAnimationListener;
import com.video.ui.view.subview.DimensHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<GenericBlock<DisplayItem>> {
    private final static String TAG = "TVMetro-MainActivity";

    protected BaseGsonLoader mLoader;
    TabHost    mTabHost;
    TabWidget  mTabs;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    EmptyLoadingView mLoadingView;
    GenericBlock<DisplayItem> _contents;
    boolean mTabChanging;
    int mPrePagerPosition = 0;

    protected DisplayItem albumItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this instanceof ChannelActivity) {
            setContentView(R.layout.channel_layout);
        }else {
            setContentView(R.layout.activity_main);
        }

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabs    = (TabWidget)findViewById(android.R.id.tabs);

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mLoadingView = makeEmptyLoadingView(this, (RelativeLayout)findViewById(R.id.tabs_content));

        albumItem = (DisplayItem) getIntent().getSerializableExtra("item");
        setUserFragmentClass();
        getSupportLoaderManager().initLoader(TabsGsonLoader.LOADER_ID, null, this);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewUtils.unbindDrawables(findViewById(R.id.main_tabs_container));
    }

    //please override this fun
    protected void createTabsLoader(){
        mLoader = new TabsGsonLoader(this, albumItem);
    }
    
    @Override
    public Loader<GenericBlock<DisplayItem>> onCreateLoader(int loaderId, Bundle bundle) {
        if(loaderId == TabsGsonLoader.LOADER_ID){
        	createTabsLoader();
            mLoader.setProgressNotifiable(mLoadingView);
            return mLoader;
        }else{
            return null;
        }
    }

    final static String buildInData="{\"data\":[{\"items\":[{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p017VHRusz5g/R2BoGcjC9rNir1.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"高德地图\",\"times\":{\"updated\":1404466152,\"created\":1404454443},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":1,\"w\":1,\"h\":2}},\"id\":\"180\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"album\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{\"translate\":{\"duration\":500,\"y_delta\":-15,\"interpolator\":0,\"startDelay\":0,\"x_delta\":0}},\"pos\":{\"y\":90,\"x\":342}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01rl7EaZ0XN/doB3Y9Zx35fa4W.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{\"y\":0,\"x\":48}}},\"name\":\"铁皮人儿童馆\",\"times\":{\"updated\":1401849669,\"created\":0},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":2,\"w\":2,\"h\":1}},\"id\":\"576\",\"type\":\"album\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01OiJJiUlpr/gmj9vgPhOureuX.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"不可思议的妈妈\",\"times\":{\"updated\":1401850237,\"created\":1378090812},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":5,\"w\":1,\"h\":1}},\"id\":\"176\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01shnaa7gKl/98UnssjDNwXMAZ.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"WPS Office\",\"times\":{\"updated\":1387526511,\"created\":1376617877},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":3,\"w\":1,\"h\":1}},\"id\":\"109\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCW8r0R2l/fkBbsIN6I5wI35.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCKbqwnYo/KqHUcCcSmzBm0O.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"大众点评\",\"times\":{\"updated\":1401344334,\"created\":1392277260},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":8,\"w\":1,\"h\":1}},\"id\":\"354\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"album\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{\"y\":75,\"x\":291}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p019cDUtcXZj/jy0zdtgegX3SUl.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{\"y\":0,\"x\":0}}},\"name\":\"铁皮人儿童馆\",\"times\":{\"updated\":1401849669,\"created\":0},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":4,\"w\":2,\"h\":1}},\"id\":\"576\",\"type\":\"album\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01yDxlUKG1s/bLtew5tzXRluVv.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"布丁酒店\",\"times\":{\"updated\":1399978027,\"created\":1392964753},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":2,\"w\":1,\"h\":1}},\"id\":\"359\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01lvwJ5nO0g/NoUqlnpclsuxRs.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"不可思议的妈妈\",\"times\":{\"updated\":1401850237,\"created\":1378090812},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":8,\"w\":1,\"h\":1}},\"id\":\"176\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01av5Akioaq/rqODKssj5sWyKj.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"大姨吗\",\"times\":{\"updated\":1399544098,\"created\":1378294032},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":4,\"w\":1,\"h\":1}},\"id\":\"183\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01YGuGzzMWm/rV3uxv8scGgVd1.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"旅游攻略\",\"times\":{\"updated\":1410854752,\"created\":1395399765},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":6,\"w\":1,\"h\":1}},\"id\":\"387\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01C3W0LR82K/HxLBL7NTIuLWya.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"万花筒相册\",\"times\":{\"updated\":1401950825,\"created\":1401882640},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":6,\"w\":1,\"h\":1}},\"id\":\"241\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01JiLwbqnZS/voMex4XV9dXvqm.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"万年历\",\"times\":{\"updated\":1399189032,\"created\":1398650342},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":2,\"x\":7,\"w\":1,\"h\":1}},\"id\":\"272\",\"type\":\"item\",\"ns\":\"game\"},{\"target\":{\"type\":\"item\"},\"images\":{\"text\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"icon\":{\"url\":\"\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p014VjmdJ2gi/aO2VnUOhGU2nP9.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"\",\"ani\":{},\"pos\":{}}},\"name\":\"驾考宝典\",\"times\":{\"updated\":1404181894,\"created\":1403775113},\"_ui\":{\"type\":\"metro_cell_banner\",\"layout\":{\"y\":1,\"x\":7,\"w\":1,\"h\":1}},\"id\":\"681\",\"type\":\"item\",\"ns\":\"game\"}],\"images\":{},\"name\":\"推荐\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"type\":\"metro\"},\"id\":\"recommend\",\"type\":\"album\",\"ns\":\"game\"},{\"items\":[{\"target\":{\"type\":\"billboard\"},\"images\":{\"text\":{},\"icon\":{},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p0126XVsd5Gq/2GgC8oaZghOWFd.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"图书资讯\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":1,\"w\":1,\"h\":2},\"type\":\"metro_cell\"},\"id\":\"7\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01d28NYdNuC/5shzAQ1yH28uLU.png\",\"ani\":{},\"pos\":{}},\"spirit\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01hama7b3dq/qob3Q6urR3JDdd.png\",\"ani\":{\"translate\":{\"duration\":500,\"y_delta\":0,\"startDelay\":0,\"interpolator\":0,\"x_delta\":10}},\"pos\":{\"y\":0,\"x\":0}}},\"name\":\"娱乐休闲\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":2,\"w\":2,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"1\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01HPE9quenU/4v76OTHjvor8pN.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01nIJvWYrk4/uL0jexuFYZHVK9.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"实用生活\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":2,\"x\":5,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"80\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01gT26uzM0J/USFXpIjwUIW62q.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"教育学习\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":4,\"w\":1,\"h\":2},\"type\":\"metro_cell\"},\"id\":\"22\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01LzZgiJgql/XmyB1EeSl4TYXg.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01dU2NZjNAv/nPZrDmqU4JkAgs.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"影音视听\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":2,\"x\":2,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"19\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p0106nqgJeV9/g3m1kLf0QB3PyV.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01FYEhauQdZ/UuKDRarFievkqn.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"图书资讯\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":2,\"x\":3,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"7\",\"type\":\"category\",\"ns\":\"game\"},{\"target\":{\"type\":\"category\"},\"images\":{\"text\":{},\"icon\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01b4nagMOxj/9rJs162KwT0N7O.png\",\"ani\":{},\"pos\":{}},\"back\":{\"url\":\"http://image.box.xiaomi.com/mfsv2/download/s010/p01h405dti3t/skYNEt5pNJJeOK.png\",\"ani\":{},\"pos\":{}},\"spirit\":{}},\"name\":\"健康健美\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"layout\":{\"y\":1,\"x\":5,\"w\":1,\"h\":1},\"type\":\"metro_cell\"},\"id\":\"81\",\"type\":\"category\",\"ns\":\"game\"}],\"images\":{},\"name\":\"分类\",\"times\":{\"updated\":0,\"created\":0},\"_ui\":{\"type\":\"metro\"},\"id\":\"categories\",\"type\":\"album\",\"ns\":\"game\"}],\"preload\":{\"images\":[\"\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p017VHRusz5g/R2BoGcjC9rNir1.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01rl7EaZ0XN/doB3Y9Zx35fa4W.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01OiJJiUlpr/gmj9vgPhOureuX.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01shnaa7gKl/98UnssjDNwXMAZ.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCW8r0R2l/fkBbsIN6I5wI35.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01XCKbqwnYo/KqHUcCcSmzBm0O.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p019cDUtcXZj/jy0zdtgegX3SUl.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01yDxlUKG1s/bLtew5tzXRluVv.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01lvwJ5nO0g/NoUqlnpclsuxRs.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01av5Akioaq/rqODKssj5sWyKj.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01YGuGzzMWm/rV3uxv8scGgVd1.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01C3W0LR82K/HxLBL7NTIuLWya.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01JiLwbqnZS/voMex4XV9dXvqm.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p014VjmdJ2gi/aO2VnUOhGU2nP9.png\",null,\"http://image.box.xiaomi.com/mfsv2/download/s010/p0126XVsd5Gq/2GgC8oaZghOWFd.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01d28NYdNuC/5shzAQ1yH28uLU.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01hama7b3dq/qob3Q6urR3JDdd.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01HPE9quenU/4v76OTHjvor8pN.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01nIJvWYrk4/uL0jexuFYZHVK9.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01gT26uzM0J/USFXpIjwUIW62q.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01LzZgiJgql/XmyB1EeSl4TYXg.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01dU2NZjNAv/nPZrDmqU4JkAgs.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p0106nqgJeV9/g3m1kLf0QB3PyV.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01FYEhauQdZ/UuKDRarFievkqn.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01b4nagMOxj/9rJs162KwT0N7O.png\",\"http://image.box.xiaomi.com/mfsv2/download/s010/p01h405dti3t/skYNEt5pNJJeOK.png\"]},\"update_time\":0}";
    @Override
    public void onLoadFinished(Loader<GenericBlock<DisplayItem>> tabsLoader, final GenericBlock<DisplayItem> tabs) {
        if(tabs != null && tabs.blocks != null && tabs.blocks.size() > 0){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateTabsAndMetroUI(tabs);
                    mTabHost.requestLayout();
                }
            });
        }else {
            mLoadingView.stopLoading(true, false);
        }
    }

    @Override
    public void onLoaderReset(Loader<GenericBlock<DisplayItem>> tabsLoader) {

    }

    protected void addVideoTestData(GenericBlock<DisplayItem> _content){
        Log.d(TAG, "addVideoTestData");
    }

    protected void updateTabsAndMetroUI(GenericBlock<DisplayItem> content){
        if(content == null || content.blocks == null ){
            return;
        }

        if(_contents != null ){
            if(_contents.times.updated == content.times.updated) {
                Log.d(TAG, "same content, no need to update UI");
                return;
            }
        }

        mTabs.removeAllViews();
        mViewPager.removeAllViews();
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        addVideoTestData(content);
        _contents = content;
        
       for(int i=0;i<content.blocks.size();i++) {
            Bundle args = new Bundle();
            args.putSerializable("tab",     content.blocks.get(i));
            args.putInt("index",            mTabs.getTabCount());
            args.putInt("tab_count",        content.blocks.size()+(isNeedUserTab?1:0));
            
            mTabsAdapter.addTab(mTabHost.newTabSpec(content.blocks.get(i).title).setIndicator(newTabIndicator(content.blocks.get(i).title, mTabs.getTabCount() == 0)),
                    getFragmentClass(content.blocks.get(i)), args);

        }

        //for user fragment
        if(isNeedUserTab){
            Bundle args = new Bundle();
            args.putInt("index",               mTabs.getTabCount());
            args.putInt("tab_count",           content.blocks.size()+1);
            args.putBoolean("user_fragment", true);
            mTabsAdapter.addTab(mTabHost.newTabSpec(mUserTabName).setIndicator(newTabIndicator(mUserTabName, mTabs.getTabCount() == 0)), mUserFragmentClass, args);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabs.focusCurrentTab(0);
            }
        }, 200);
    }

    protected Class getFragmentClass(Block<DisplayItem> block){
        return MetroFragment.class;
    }

    protected boolean isNeedUserTab = true;
    protected String mUserTabName = "";
    protected Class  mUserFragmentClass = null;
    protected void setUserFragmentClass(){  
        isNeedUserTab      = true;
    	mUserTabName       = getResources().getString(R.string.user_tab); 
    	mUserFragmentClass = MetroFragment.class;

        //please call this
        UserViewFactory.getInstance().setFactory(new UserViewFactory.ViewCreatorFactory(){
            @Override
            public ArrayList<View> create(Context context) {
                ArrayList<View> views = new ArrayList<View>();
                views.add(new UserView(context, "title 1"));
                views.add(new UserView(context, "title 2"));
                views.add(new UserView(context, "title 2"));
                return  views;
            }

            @Override
            public int getPadding(Context context) {
                return getResources().getDimensionPixelSize(R.dimen.user_view_padding);
            }
        });

        RecommendCardViewClickListenerFactory.getInstance().setFactory(new RecommendCardViewClickListenerFactory.ClickCreatorFactory() {
            @Override
            public View.OnClickListener getRecommendCardViewClickListener() {
                return null;
            }
        });
    }

    private View newTabIndicator(String tabName, boolean focused){
        final String name = tabName;
        View viewC  = View.inflate(this, R.layout.tab_view_indicator_item, null);

        TextView view = (TextView)viewC.findViewById(R.id.tv_tab_indicator);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
        }
        view.setText(name);
        mTabs.setPadding(getResources().getDimensionPixelSize(R.dimen.tab_left_offset), 0, 0, 0);

        if(focused == true){
            Resources res = getResources();
            view.setTextColor(res.getColor(R.color.orange));
            view.requestFocus();
        }

        return viewC;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    @Override
    protected void onResume(){
        super.onResume();

        View view = mViewPager.getChildAt(mViewPager.getCurrentItem());
        if(view instanceof AdsAnimationListener) {
            AdsAnimationListener ap = ((AdsAnimationListener) view).getAnimationListener();
            if (ap != null) {
                ap.startAnimation();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        View view = mViewPager.getChildAt(mViewPager.getCurrentItem());
        if(view instanceof AdsAnimationListener) {
            AdsAnimationListener ap = ((AdsAnimationListener) view).getAnimationListener();
            if (ap != null) {
                ap.stopAnimation();
            }
        }
    }

    protected String dataSchemaForSearchString = "tvschema://video/search";
    protected void setSeachSchema(String schema){
    	dataSchemaForSearchString = schema;
    }

    private AdsAnimationListener preAdsAnimation;
    public class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        private final FragmentManager fm;

        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            fm = activity.getSupportFragmentManager();
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            if(ViewUtils.LargerMemoryMode() == false) {
                final View view = fragments.get(new Integer(position)).getView();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewUtils.unbindImageDrawables(view);
                    }
                }, 500);

                container.removeView(view);
                fragments.remove(new Integer(position));
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            Fragment fragment = this.getItem(position);

            if (!fragment.isAdded()) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(fragment, fragment.getClass().getSimpleName());

                ft.commit();

                fm.executePendingTransactions();
            }

            if (fragment.getView() != null && fragment.getView().getParent() == null) {
                container.addView(fragment.getView());
            }

            return fragment;
        }

        HashMap<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();
        @Override
        public Fragment getItem(int position) {
            Fragment fg = fragments.get(new Integer(position));
            if(fg == null) {
                TabInfo info = mTabs.get(position);
                fg = Fragment.instantiate(mContext, info.clss.getName(), info.args);
                fragments.put(new Integer(position), fg);
            }            
            return fg;
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mTabChanging = true;
            mViewPager.setCurrentItem(position);
            mTabChanging = false;
            switchTabView(position);

            if(position < _contents.blocks.size()) {
                ImageGroup ig = _contents.blocks.get(position).images;
                if (ig != null) {
                    if (ig.back() != null && ig.back().url != null) {
                        //VolleyHelper.getInstance(MainActivity.this).getImageLoader().get(ig.back.url, ImageLoader.getCommonViewImageListener(findViewById(R.id.main_tabs_container), 0, 0));
                    }
                }
            }
        }

        private void switchTabView(final int index){
            Fragment fg = fragments.get(new Integer(index));
            if(fg instanceof AdsAnimationListener){
                AdsAnimationListener ap = ((AdsAnimationListener) fg).getAnimationListener();
                if(ap != null){
                    ap.startAnimation();
                    preAdsAnimation = ap;
                }else {
                    if(preAdsAnimation != null){
                        preAdsAnimation.stopAnimation();
                    }
                }
            }

            //TODO
            //We need pay much attention to memory usage
            if(false) {
                final int size = mViewPager.getChildCount();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //remove show image
                        for (int i = index + 2; i < size; i++) {
                            ViewUtils.unbindImageDrawables(mViewPager.getChildAt(i));
                            mViewPager.getChildAt(i).setTag(R.integer.removed_bitmap, true);
                        }
                        for (int i = index - 2; i >= 0; i--) {
                            ViewUtils.unbindImageDrawables(mViewPager.getChildAt(i));
                            mViewPager.getChildAt(i).setTag(R.integer.removed_bitmap, true);
                        }
                    }
                }, 500);

                //show picture
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = index + 1; i < size; i++) {
                            final int showIndex = i;
                            Object tag = mViewPager.getChildAt(i).getTag(R.integer.removed_bitmap);
                            if ((tag != null) && ((Boolean) tag == true)) {

                                invalidateUI(mViewPager.getChildAt(showIndex));
                                mViewPager.getChildAt(showIndex).setTag(R.integer.removed_bitmap, false);
                            }
                        }

                        for (int i = index - 1; i >= 0; i--) {
                            final int showIndex = i;
                            Object tag = mViewPager.getChildAt(i).getTag(R.integer.removed_bitmap);
                            if ((tag != null) && ((Boolean) tag == true)) {
                                invalidateUI(mViewPager.getChildAt(showIndex));
                                mViewPager.getChildAt(showIndex).setTag(R.integer.removed_bitmap, false);
                            }
                        }
                    }
                }, 500);
            }

            switchTab(index);
        }

        private void invalidateUI(View view){
            if (null == view) {
                return;
            }

            if(view instanceof DimensHelper){
                DimensHelper imageView = (DimensHelper)view;
                imageView.invalidateUI();
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    invalidateUI(((ViewGroup) view).getChildAt(i));
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
            mPrePagerPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    public void switchTab(int index){
        TabWidget tw = mTabHost.getTabWidget();
        for(int i=0;i<tw.getChildCount();i++) {
            View viewC = tw.getChildTabViewAt(i);
            //Log.d(TAG, "tab width="+viewC.getWidth() + " left="+viewC.getLeft());
            if(i == index) {
                TextView view = (TextView) viewC.findViewById(R.id.tv_tab_indicator);
                Resources res = view.getResources();
                view.setTextColor(res.getColor(R.color.orange));
            }else{
                TextView view = (TextView) viewC.findViewById(R.id.tv_tab_indicator);
                Resources res = view.getResources();
                view.setTextColor(res.getColor(R.color.tab));
            }
        }
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context,  RelativeLayout parentView){
        return makeEmptyLoadingView(context, parentView,  RelativeLayout.CENTER_IN_PARENT);
    }

    public static EmptyLoadingView makeEmptyLoadingView(Context context, RelativeLayout parentView, int rule){
        EmptyLoadingView loadingView = new EmptyLoadingView(context);
        loadingView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(rule);
        parentView.addView(loadingView, rlp);
        return loadingView;
    }
}
