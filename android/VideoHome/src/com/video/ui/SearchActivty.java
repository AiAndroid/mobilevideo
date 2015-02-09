package com.video.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.loader.GenericAlbumLoader;
import com.video.ui.view.ListFragment;
import com.video.ui.view.MetroFragment;
import com.video.ui.view.SearchFragment;
import com.video.ui.view.subview.BaseCardView;
import com.video.ui.view.subview.FilterBlockView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by liuhuadong on 7/29/14.
 */
public class SearchActivty extends MainActivity implements SearchFragment.SearchResultListener{
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

        et.setOnEditorActionListener(searchActionIME);

        et.addTextChangedListener(tw);
    }

    TextView.OnEditorActionListener searchActionIME = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                EditText et = (EditText) findViewById(R.id.search_name);
                String keyword = et.getText().toString();
                searchKeyword(keyword, null);
                return true;
            }
            return false;
        }
    };

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
            removeSearchResultFragment();

            //remove
            removeNoResultView();
        }
    }

    private void removeNoResultView(){
        RelativeLayout header_placeholder = (RelativeLayout) findViewById(R.id.header_placeholder);
        if(header_placeholder != null){
            header_placeholder.removeAllViews();
            header_placeholder.setVisibility(View.GONE);
        }
    }

    private void removeSearchResultFragment(){
        Fragment df = getSupportFragmentManager().findFragmentById(R.id.search_result);
        if(df!= null) {
            getSupportFragmentManager().beginTransaction().remove(df).commit();
        }
    }

    @Override
    public void onResult(final boolean result, final GenericBlock<DisplayItem> searchResult) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(result == false){
                    removeSearchResultFragment();

                    //show no search result view
                    RelativeLayout header_placeholder = (RelativeLayout) findViewById(R.id.header_placeholder);
                    if(header_placeholder != null) {
                        RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        flp.addRule(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.CENTER_VERTICAL);

                        View emptyView = View.inflate(getBaseContext(), R.layout.search_empty_view, null);
                        header_placeholder.addView(emptyView, flp);

                        header_placeholder.setVisibility(View.VISIBLE);
                    }else {

                        //TODO
                        //we also need placeholder to place empty hint
                    }
                }else {
                    removeNoResultView();
                }
            }
        });
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
                et.setSelection(keyword.length(), keyword.length());
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
            SearchFragment df = new SearchFragment();
            Bundle data = new Bundle();

            Block<DisplayItem> searchItem = new Block<DisplayItem>();
            if(item == null) {
                searchItem.target = new DisplayItem.Target();
                searchItem.target.entity = "search_result";
                try {
                    searchItem.target.url = String.format("search?kw=%1$s", URLEncoder.encode(keyword, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                searchItem.ns = "search";
                searchItem.type = "album";
                searchItem.id = String.format("search?kw=%1$s", keyword);
            }else {
                searchItem.target = item.target;
                searchItem.ns = "search";
                searchItem.type = "album";
                searchItem.id = item.id;
            }

            data.putSerializable("tab", searchItem);

            df.setArguments(data);
            df.setSearchResultListener(this);
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
