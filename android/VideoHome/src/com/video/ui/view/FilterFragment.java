package com.video.ui.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.R;
import com.video.ui.view.subview.PortBlockView;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 12/8/14.
 */
public class FilterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.filter_layout, null);
        MetroLayout ml = (MetroLayout) root.findViewById(R.id.metrolayout);
        //create filter and insert it into UI
        {
            Block<VideoItem> item = new Block<VideoItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<VideoItem>>();


            Block<VideoItem> title = new Block<VideoItem>();
            title.title = "地区";
            title.ui_type = new DisplayItem.UI();
            title.ui_type.id = LayoutConstant.linearlayout_title;

            item.blocks.add(title);

            Block<VideoItem> filteritem = new Block<VideoItem>();
            filteritem.title = "episode";
            filteritem.ui_type = new DisplayItem.UI();
            filteritem.ui_type.id = LayoutConstant.linearlayout_filter_select;
            filteritem.ui_type.row_count     = 4;
            filteritem.ui_type.display_count = 11;
            filteritem.filters = new DisplayItem.Filter();
            filteritem.filters.put("filters", createAreaFilter());

            item.blocks.add(filteritem);

            PortBlockView view = new PortBlockView(getActivity(), item, new Integer(100));
            ml.addItemViewPort(view, LayoutConstant.block_sub_channel, 0, 0);
        }

        {
            Block<VideoItem> item = new Block<VideoItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<VideoItem>>();


            Block<VideoItem> title = new Block<VideoItem>();
            title.title = "类型";
            title.ui_type = new DisplayItem.UI();
            title.ui_type.id = LayoutConstant.linearlayout_title;

            item.blocks.add(title);

            Block<VideoItem> filteritem = new Block<VideoItem>();
            filteritem.title = "episode";
            filteritem.ui_type = new DisplayItem.UI();
            filteritem.ui_type.id = LayoutConstant.linearlayout_filter_select;
            filteritem.ui_type.row_count     = 4;
            filteritem.ui_type.display_count = 11;
            filteritem.filters = new DisplayItem.Filter();
            filteritem.filters.put("filters", createTypeFilter());

            item.blocks.add(filteritem);

            PortBlockView view = new PortBlockView(getActivity(), item, new Integer(100));
            ml.addItemViewPort(view, LayoutConstant.block_sub_channel, 0, 1);
        }

        {
            Block<VideoItem> item = new Block<VideoItem>();
            item.ui_type = new DisplayItem.UI();
            item.ui_type.id = LayoutConstant.block_sub_channel;
            item.blocks = new ArrayList<Block<VideoItem>>();


            Block<VideoItem> title = new Block<VideoItem>();
            title.title = "年代";
            title.ui_type = new DisplayItem.UI();
            title.ui_type.id = LayoutConstant.linearlayout_title;

            item.blocks.add(title);

            Block<VideoItem> filteritem = new Block<VideoItem>();
            filteritem.title = "episode";
            filteritem.ui_type = new DisplayItem.UI();
            filteritem.ui_type.id = LayoutConstant.linearlayout_filter_select;
            filteritem.ui_type.row_count     = 4;
            filteritem.ui_type.display_count = 11;
            filteritem.filters = new DisplayItem.Filter();
            filteritem.filters.put("filters", createYearFilter());

            item.blocks.add(filteritem);

            PortBlockView view = new PortBlockView(getActivity(), item, new Integer(100));
            ml.addItemViewPort(view, LayoutConstant.block_sub_channel, 0, 2);
        }

        return root;
    }

    String []area = new String[]{
            "华语","美国","欧洲","日韩","其他"
    };
    private ArrayList<DisplayItem.FilterItem> createAreaFilter(){
        ArrayList<DisplayItem.FilterItem> filterItems = new ArrayList<DisplayItem.FilterItem>();
        for(String name: area)
        {
            DisplayItem.FilterItem item = new DisplayItem.FilterItem();
            item.name = name;
            item.fid = "0";
            filterItems.add(item);
        }

        return filterItems;
    }

    String []types = new String[]{
        "古装","经僧","动作","戏剧","宴请","古装","经僧","动作","戏剧","宴请","古装","经僧","动作","戏剧","宴请"
    };
    private ArrayList<DisplayItem.FilterItem> createTypeFilter(){
        ArrayList<DisplayItem.FilterItem> filterItems = new ArrayList<DisplayItem.FilterItem>();
        for(String name: types)
        {
            DisplayItem.FilterItem item = new DisplayItem.FilterItem();
            item.name = name;
            item.fid = "0";
            filterItems.add(item);
        }
        return filterItems;
    }

    String []years = new String[]{
            "2014","2014","2012","2011","2010","2009","2008","2007","更早"
    };
    private ArrayList<DisplayItem.FilterItem> createYearFilter(){
        ArrayList<DisplayItem.FilterItem> filterItems = new ArrayList<DisplayItem.FilterItem>();
        for(String name: years)
        {
            DisplayItem.FilterItem item = new DisplayItem.FilterItem();
            item.name = name;
            item.fid = "0";
            filterItems.add(item);
        }
        return filterItems;
    }
}
