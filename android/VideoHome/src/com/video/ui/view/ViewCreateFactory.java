package com.video.ui.view;

import android.content.Context;
import android.view.View;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class ViewCreateFactory {
    public static View CreateBlockView(Context context,  Block<DisplayItem> item){
        View view = null;
        switch (item.ui_type.id){
            case LayoutConstant.list_category_land:
                break;
            case LayoutConstant.imageswitcher:
                //view = new AdsView(context, item.items);
                break;
        }
        return view;
    }

    public static View CreateSingleView(Context context, DisplayItem item){
        switch (item.ui_type.id){
            case LayoutConstant.list_category_land:
                break;
        }
        return null;
    }
}
