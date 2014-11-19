package com.video.ui.view;

import android.content.Context;
import android.view.View;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.view.subview.*;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class ViewCreateFactory {
    public static View CreateBlockView(Context context,  Block<DisplayItem> item){
        View view = null;
        switch (item.ui_type.id){
            case LayoutConstant.imageswitcher:
                view = new AdsView(context, item.items);
                break;
            case LayoutConstant.linearlayout_top:
                view = new QuickNavigationView(context, item.items);
                break;
            case LayoutConstant.linearlayout_left:
                view = new QuickLocalNavigateView(context, item.items);
                break;
            case LayoutConstant.list_category_land:
                view = new CategoryItemView(context, item);
                break;
        }
        return view;
    }

    public static View CreateSingleView(Context context, DisplayItem item){
        View view = null;
        switch (item.ui_type.id){
            case LayoutConstant.list_category_land:
                view = new SelectItemView(context, item);
                break;
        }
        return null;
    }
}
