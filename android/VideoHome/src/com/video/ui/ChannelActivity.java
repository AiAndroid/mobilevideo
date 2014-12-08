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

        View view = titlebar.findViewById(R.id.channel_filte_btn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(_contents != null && _contents.blocks.get(0).id.endsWith(".choice")) {
                        Block<DisplayItem> block = _contents.blocks.get(0);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("mvschema://video/filter?rid=" + block.id));
                        intent.putExtra("item", block);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
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
    protected Class getFragmentClass(Block<DisplayItem> block){
        if(block.id.endsWith(".choice"))
            return MetroFragment.class;

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