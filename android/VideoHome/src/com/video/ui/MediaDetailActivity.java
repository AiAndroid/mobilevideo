package com.video.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by liuhuadong on 12/2/14.
 */
public class MediaDetailActivity extends DisplayItemActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_detail_layout);

        View titlebar = this.findViewById(R.id.title_bar);
        TextView tv = (TextView) titlebar.findViewById(R.id.title_top_name);
        tv.setText(item.title);

        titlebar.findViewById(R.id.channel_filte_btn).setVisibility(View.GONE);
        titlebar.findViewById(R.id.channel_search_btn).setVisibility(View.GONE);

        ImageView back_imageview = (ImageView) titlebar.findViewById(R.id.title_top_back);
        back_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaDetailActivity.this.finish();
            }
        });
    }
}
