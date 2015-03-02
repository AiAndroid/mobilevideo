package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.LayoutConstant;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.view.block.PortBlockView;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 12/8/14.
 */
public class FilterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Block<DisplayItem> block =  (Block<DisplayItem>) this.getArguments().getSerializable("item");
        View root = inflater.inflate(R.layout.filter_layout, null);
        MetroLayout ml = (MetroLayout) root.findViewById(R.id.metrolayout);
        //create filter and insert it into UI
        DisplayItem.Filter filter = block.filters;
        if (filter.all != null) {
            for (int i = 0; i < filter.all.size(); i++) {
                DisplayItem.Filter.FilterType ft = filter.all.get(i);
                Block<VideoItem> item = new Block<VideoItem>();
                item.ui_type = new DisplayItem.UI();
                item.ui_type.id = LayoutConstant.block_sub_channel;
                item.blocks = new ArrayList<Block<VideoItem>>();


                Block<VideoItem> title = new Block<VideoItem>();
                title.title = ft.title;
                title.ui_type = new DisplayItem.UI();
                title.ui_type.id = LayoutConstant.linearlayout_title;
                item.blocks.add(title);

                Block<VideoItem> filteritem = new Block<VideoItem>();
                filteritem.title = "filters";
                filteritem.ui_type = new DisplayItem.UI();
                filteritem.ui_type.id = LayoutConstant.linearlayout_filter_select;
                filteritem.ui_type.row_count = 4;
                filteritem.ui_type.display_count = ft.tags.size();
                filteritem.filters = new DisplayItem.Filter();
                filteritem.filters.all = new ArrayList<DisplayItem.Filter.FilterType>();
                filteritem.filters.all.add(ft);
                item.blocks.add(filteritem);

                PortBlockView view = new PortBlockView(getActivity(), item, new Integer(100));
                ml.addItemViewPort(view, LayoutConstant.block_sub_channel, 0, i);
            }
        }

        return root;
    }
}
