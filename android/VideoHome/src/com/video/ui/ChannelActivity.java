package com.video.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.loader.GenericAlbumLoader;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.ListFragment;
import com.video.ui.view.MetroFragment;

/**
 * Created by liuhuadong on 12/1/14.
 */
public class ChannelActivity extends  MainActivity {
    private static final String TAG = ChannelActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);
        tv.setText(albumItem.title);

        final View view = titlebar.findViewById(R.id.channel_filte_btn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(_contents != null && (_contents.blocks.get(0).id.endsWith(".choice") || _contents.blocks.get(0).id.endsWith(".r") )) {
                        Block<DisplayItem> block = _contents.blocks.get(0);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("mvschema://video/filter?rid=" + block.id));
                        for(Block<DisplayItem> eachB :block.blocks) {
                            if (eachB.ui_type != null && eachB.ui_type.id == LayoutConstant.linearlayout_filter) {
                                intent.putExtra("item", eachB);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                break;
                            }
                        }
                    }
                } catch (Exception ne) {
                    ne.printStackTrace();
                }
            }
        });

        View search = titlebar.findViewById(R.id.channel_search_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("mvschema://video/search?rid=" + "choice"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception ne) {
                    ne.printStackTrace();
                }
            }
        });


        ImageView back_imageview = (ImageView) titlebar.findViewById(R.id.title_top_back);
        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelActivity.this.finish();
            }
        });
    }

    @Override
    public void setContentView(){
        setContentView(R.layout.channel_layout);
    }

    private boolean isExistFilter(Block<DisplayItem> block){
        for(Block<DisplayItem> item:block.blocks){
            if(item.ui_type != null && item.ui_type.id == LayoutConstant.linearlayout_filter){
                return true;
            }
        }
        return false;
    }
    protected Class getFragmentClass(Block<DisplayItem> block){
        if(block.id.endsWith(".choice") || block.id.endsWith(".r")) {
            if(isExistFilter(block)){
                showFilter(true);
                showSearch(true);
            }
            return MetroFragment.class;
        }

        showSearch(true);
        return ListFragment.class;
    }

    @Override
    protected void addVideoTestData(GenericBlock<DisplayItem> _content) {
    }

    //please override this fun
    protected void createTabsLoader() {
        if(albumItem == null){
            albumItem = new DisplayItem();
            albumItem.ns    = "home";
            albumItem.type  = "album";
            albumItem.title = "home";
        }

        mLoader = GenericAlbumLoader.generateTabsLoader(getBaseContext(), albumItem);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void setUserFragmentClass() {
        isNeedUserTab = false;
    }
}