package com.video.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.video.ui.view.FilterFragment;

/**
 * Created by liuhuadong on 12/8/14.
 */
public class ChannelFilterActivity extends DisplayItemActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_ui_layout);

        View titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);
        tv.setText(item.title);

        titlebar.findViewById(R.id.channel_filte_btn).setVisibility(View.GONE);
        titlebar.findViewById(R.id.channel_search_btn).setVisibility(View.GONE);

        ImageView back_imageview = (ImageView) titlebar.findViewById(R.id.title_top_back);
        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChannelFilterActivity.this.finish();
            }
        });

        FilterFragment df = new FilterFragment();
        Bundle data = new Bundle();
        data.putSerializable("item", getIntent().getSerializableExtra("item"));
        df.setArguments(data);
        getSupportFragmentManager().beginTransaction().add(R.id.filter_view, df, "filters").commit();
    }
}
