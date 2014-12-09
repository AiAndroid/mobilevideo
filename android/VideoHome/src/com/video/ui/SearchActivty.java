package com.video.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.loader.GenericAlbumLoader;
import com.video.ui.view.ListFragment;
import com.video.ui.view.MetroFragment;

/**
 * Created by liuhuadong on 7/29/14.
 */
public class SearchActivty extends MainActivity{
    private static final String TAG = SearchActivty.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);
        tv.setText(R.string.search);

        ImageView back_imageview = (ImageView) titlebar.findViewById(R.id.title_top_back);
        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivty.this.finish();
            }
        });
    }

    @Override
    protected Class getFragmentClass(Block<DisplayItem> block){
        if(block.id.endsWith(".choice"))
            return MetroFragment.class;

        return ListFragment.class;
    }

    //please override this fun
    protected void createTabsLoader() {
        if(albumItem == null){
            albumItem = new DisplayItem();
            albumItem.ns    = "search";
            albumItem.type  = "album";
            albumItem.id    = "choice";
        }

        mLoader = GenericAlbumLoader.generateTabsLoader(getBaseContext(), albumItem);
    }
}
