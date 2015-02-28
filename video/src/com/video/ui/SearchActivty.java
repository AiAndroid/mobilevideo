package com.video.ui;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.GenericAlbumLoader;
import com.video.ui.view.LinearFrame;
import com.video.ui.view.SearchFragment;
import com.video.ui.view.subview.SelectItemsBlockView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by liuhuadong on 7/29/14.
 */
public class SearchActivty extends MainActivity implements SearchFragment.SearchResultListener{
    private static final String TAG = SearchActivty.class.getName();
    private EditText et;
    private int DEFAULT_MAX = 5;
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
        et.setOnFocusChangeListener(searchFocuseChange);
        et.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(TextUtils.isEmpty(et.getText()) && iDataORM.hasSearchHistory(getBaseContext())){
                removeNoResultView(true);
            }
        }
    };

    View.OnFocusChangeListener searchFocuseChange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus && TextUtils.isEmpty(et.getText()) && iDataORM.hasSearchHistory(getBaseContext())){
                removeNoResultView(true);
            }
        }
    };

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
            removeNoResultView(true);
        }
    }

    private void removeNoResultView(boolean showSearchHistory){
        RelativeLayout header_placeholder = (RelativeLayout) findViewById(R.id.header_placeholder);
        if(header_placeholder != null){
            header_placeholder.removeAllViews();
            if(showSearchHistory == false || iDataORM.hasSearchHistory(getBaseContext()) == false) {
                header_placeholder.setVisibility(View.GONE);
            }else {
                createHistoryView(header_placeholder);
            }
        }
    }

    private void createHistoryView(RelativeLayout  header_placeholder){
        int width   = getResources().getDimensionPixelSize(R.dimen.rank_banner_width);
        int height  = getResources().getDimensionPixelSize(R.dimen.rank_button_height);
        int padding = (getResources().getDisplayMetrics().widthPixels - width)/2;
        header_placeholder.setVisibility(View.VISIBLE);
        //add search history to header layout container
        ArrayList<iDataORM.SearchHistoryItem> historyItems = iDataORM.getSearchHistory(getBaseContext(), iDataORM.getIntValue(getBaseContext(), iDataORM.Max_Show_Search, DEFAULT_MAX));
        View listContainer = View.inflate(getBaseContext(), R.layout.linear_frame, null);
        RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flp.topMargin = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
        header_placeholder.addView(listContainer, flp);
        LinearFrame lf = (LinearFrame)listContainer.findViewById(R.id.list);

        int size = historyItems.size();
        for(int i=0;i<size;i++){
            iDataORM.SearchHistoryItem item = historyItems.get(i);
            View historyView = View.inflate(getBaseContext(), R.layout.search_history_item, null);
            TextView textView = (TextView) historyView.findViewById(R.id.search_item_name);
            textView.setText(item.key);
            historyView.setTag(item.key);

            if(i == 0) {
                historyView.setBackgroundResource(R.drawable.com_item_bg_up);
                if(size == 1){
                    historyView.findViewById(R.id.line).setVisibility(View.GONE);
                }
            }else if(i == size -1 ){
                historyView.setBackgroundResource(R.drawable.com_item_bg_mid);
                historyView.findViewById(R.id.line).setVisibility(View.GONE);
            }
            else {
                historyView.setBackgroundResource(R.drawable.com_item_bg_mid);
            }

            historyView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    searchKeyword((String)v.getTag(), null);
                }
            });

            lf.addItemViewPort(historyView, width, height, padding, 0);
        }

        //add one clean button
        View buttonContain = View.inflate(getBaseContext(), R.layout.button_enter, null);
        Button blockView = (Button) buttonContain.findViewById(R.id.enter_button);
        blockView.setText(R.string.clear_search_history);
        buttonContain.setBackgroundResource(R.drawable.com_item_bg_down);
        blockView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                iDataORM.removeSearchHistory(getBaseContext(), null);

                //remove the history view
                removeNoResultView(false);
            }
        });

        int span = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
        lf.addItemViewPort(buttonContain, width, height+span, padding, 0);
    }

    private void removeSearchResultFragment(){
        Fragment df = getSupportFragmentManager().findFragmentById(R.id.search_result);
        if(df!= null) {
            getSupportFragmentManager().beginTransaction().remove(df).commit();
        }
    }

    @Override
    public void onSearchResult(final boolean result, final GenericBlock<DisplayItem> searchResult) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(result == false){
                    removeSearchResultFragment();

                    //show no search result view
                    RelativeLayout header_placeholder = (RelativeLayout) findViewById(R.id.header_placeholder);
                    if(header_placeholder != null) {
                        header_placeholder.removeAllViews();

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
                    removeNoResultView(false);
                }
            }
        });
    }

    @Override public void setContentView(){
        setContentView(R.layout.search_layout);
    }

    @Override protected void afterUICreated(){
        ViewGroup fl = (ViewGroup) findViewById(R.id.root_container);
        SelectItemsBlockView fv = (SelectItemsBlockView) EpisodePlayAdapter.findFilterBlockView(fl);
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

    private void searchKeyword(String keyword, DisplayItem item) {
        if (TextUtils.isEmpty(keyword) == false) {
            //add to history
            iDataORM.addSearchHistory(getBaseContext(), keyword.trim());

            findViewById(R.id.search_result).setVisibility(View.VISIBLE);
            //
            //need define one search fragment
            SearchFragment df = new SearchFragment();
            Bundle data = new Bundle();

            Block<DisplayItem> searchItem = new Block<DisplayItem>();
            if (item == null) {
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
            } else {
                searchItem.target = item.target;
                searchItem.ns = "search";
                searchItem.type = "album";
                searchItem.id = item.id;
            }

            data.putSerializable("tab", searchItem);

            df.setArguments(data);
            df.setSearchResultListener(this);
            if (getSupportFragmentManager().findFragmentById(R.id.search_result) != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.search_result, df).commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.search_result, df, "search_result").commit();
            }
        }
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
