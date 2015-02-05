package com.video.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.loader.GenericAlbumLoader;
import com.video.ui.view.ListFragment;
import com.video.ui.view.MetroFragment;
import com.video.ui.view.subview.FilterBlockView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by liuhuadong on 7/29/14.
 */
public class SearchActivty extends MainActivity{
    private static final String TAG = SearchActivty.class.getName();
    private EditText et;
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

        et = (EditText) findViewById(R.id.search_name);
        if(item != null){
            if(TextUtils.isEmpty(item.id) == false) {
                try {
                    String keyword = Uri.parse(item.id).getQueryParameter("kw");
                    et.setText(keyword);
                }catch (Exception ne){}
            }
        }

        et.addTextChangedListener(tw);
    }

    boolean filtMode = false;
    TextWatcher tw = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            filtMode = charSequence.length() > 0;
            filtVideoContent(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void filtVideoContent(String keyword){
        if(filtMode == false){
            Fragment df = getSupportFragmentManager().findFragmentById(R.id.search_result);
            if(df!= null) {
                getSupportFragmentManager().beginTransaction().remove(df).commit();
            }
        }
    }

    @Override public void setContentView(){
        setContentView(R.layout.search_layout);
    }

    @Override protected void afterUICreated(){
        ViewGroup fl = (ViewGroup) findViewById(R.id.root_container);
        FilterBlockView fv = (FilterBlockView) EpisodePlayAdapter.findFilterBlockView(fl);
        fv.setOnPlayClickListener(keywordClick, null);
    }

    private View.OnClickListener keywordClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Object obj = v.getTag();
            if(obj != null){
                DisplayItem item = (DisplayItem)obj;
                String keyword = Uri.parse(item.id).getQueryParameter("kw");
                et.setText(keyword);
                searchKeyword(keyword, item);
            }
        }
    };

    View.OnClickListener searchClickLister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText et = (EditText) findViewById(R.id.search_name);
            String keyword = et.getText().toString();
            searchKeyword(keyword, null);
        }
    };

    private void searchKeyword(String keyword, DisplayItem item){
        if(TextUtils.isEmpty(keyword) == false) {
            findViewById(R.id.search_result).setVisibility(View.VISIBLE);
            //
            //need define one search fragment
            ListFragment df = new ListFragment();
            Bundle data = new Bundle();

            Block<DisplayItem> searchItem = new Block<DisplayItem>();
            if(item == null) {
                searchItem.target = new DisplayItem.Target();
                searchItem.target.entity = "search_result";
                try {
                    searchItem.target.url = String.format("search?key=%1$s", URLEncoder.encode(keyword, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                searchItem.ns = "video";
                searchItem.type = "album";
                searchItem.id = String.format("search?key=%1$s", keyword);
            }else {
                searchItem.target = item.target;
                searchItem.ns = "video";
                searchItem.type = "album";
                searchItem.id = item.id;
            }

            data.putSerializable("tab", searchItem);
            df.setArguments(data);
            if(getSupportFragmentManager().findFragmentById(R.id.search_result) != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.search_result, df).commit();
            }else {
                getSupportFragmentManager().beginTransaction().add(R.id.search_result, df, "search_result").commit();
            }
        }
    }

    @Override
    protected Class getFragmentClass(Block<DisplayItem> block){
        if(block.id.endsWith(".choice") || block.id.endsWith(".r"))
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
