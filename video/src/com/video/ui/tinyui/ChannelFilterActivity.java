package com.video.ui.tinyui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.DisplayItemActivity;
import com.video.ui.R;
import com.video.ui.view.FilterFragment;
import com.video.ui.view.subview.BaseCardView;
import com.video.ui.view.subview.SelectItemsBlockView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by liuhuadong on 12/8/14.
 */
public class ChannelFilterActivity extends DisplayItemActivity {

    FilterFragment df;
    Block<DisplayItem> block;
    String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_ui_layout);

        View titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);

        block = (Block<DisplayItem>) item;
        if(block != null && block.filters != null && block.filters.filters != null){
            for(DisplayItem.FilterItem item: block.filters.filters){
                if(item.target.url.equals(DisplayItem.FilterItem.custom_filter)){
                    title = item.title;
                    tv.setText(item.title);
                    break;
                }
            }
        }

        showFilter(false);
        showSearch(false);

        df = new FilterFragment();
        Bundle data = new Bundle();
        data.putSerializable("item", getIntent().getSerializableExtra("item"));
        df.setArguments(data);
        getSupportFragmentManager().beginTransaction().add(R.id.filter_view, df, "filters").commit();


        TextView okButton = (TextView) findViewById(R.id.detail_play);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilters.clear();
                getSelectFilters(findViewById(R.id.filter_view));

                Log.d(ChannelFilterActivity.class.getName(), selectedFilters.toString());

                if(selectedFilters.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<selectedFilters.size();i++){
                        if(sb.length() > 0){
                            sb.append("-");
                        }

                        sb.append(selectedFilters.get(i));
                    }
                    try {
                        String action_url = String.format(block.filters.custom_filter_id_format, URLEncoder.encode(sb.toString(), "utf-8"));

                        DisplayItem launchItem = new DisplayItem();
                        launchItem.target = new DisplayItem.Target();
                        launchItem.target.url = action_url;
                        launchItem.target.entity = "album";

                        launchItem.title = title;
                        launchItem.ns    = "video";
                        launchItem.id    = "filter select";

                        Log.d(ChannelFilterActivity.class.getName(), "filter select:"+action_url);
                        BaseCardView.launcherAction(getBaseContext(), launchItem);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    ArrayList<String> selectedFilters = new ArrayList<String>();

    private void getSelectFilters(View view){
        if (view instanceof SelectItemsBlockView) {
            selectedFilters.addAll(((SelectItemsBlockView) view).getSelectedItems());
        }else if(view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View item = ((ViewGroup) view).getChildAt(i);
                if (item instanceof ViewGroup) {
                    getSelectFilters(item);
                }
            }
        }
    }
}
