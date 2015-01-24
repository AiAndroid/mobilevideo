package com.video.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.video.ui.view.DetailFragment;

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

        View view = titlebar.findViewById(R.id.channel_filte_btn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("mvschema://video/filter?rid=" + item.id));
                    intent.putExtra("item", item);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception ne) {
                    ne.printStackTrace();
                }
            }
        });



        DetailFragment df = new DetailFragment();
        Bundle data = new Bundle();
        data.putSerializable("item", getIntent().getSerializableExtra("item"));
        df.setArguments(data);
        getSupportFragmentManager().beginTransaction().add(R.id.detail_view, df, "details").commit();
    }
}
