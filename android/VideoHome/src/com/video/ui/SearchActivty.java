package com.video.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

        ImageView back_imageview = (ImageView) titlebar.findViewById(R.id.title_top_back);
        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivty.this.finish();
            }
        });

        titlebar.findViewById(R.id.channel_search_btn).setOnClickListener(searchClickLister);
    }

    @Override public void setContentView(){
        setContentView(R.layout.search_layout);
    }

    View.OnClickListener searchClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText et = (EditText) findViewById(R.id.search_name);
            String keyword = et.getText().toString();
            if(TextUtils.isEmpty(keyword) == false) {
                findViewById(R.id.search_result).setVisibility(View.VISIBLE);
                ListFragment df = new ListFragment();
                Bundle data = new Bundle();
                Block<DisplayItem> searchItem = new Block<DisplayItem>();
                searchItem.ns   = "search";
                searchItem.type = "album";
                searchItem.id   = keyword;

                data.putSerializable("tab", searchItem);
                df.setArguments(data);
                getSupportFragmentManager().beginTransaction().add(R.id.search_result, df, "search_result").commit();
            }
        }
    };

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
            albumItem.id    = "search.choice";
        }

        mLoader = GenericAlbumLoader.generateTabsLoader(getBaseContext(), albumItem);
    }
}
