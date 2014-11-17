package com.video.ui.view;

import android.view.View;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class ViewCreateFactory {
    public static View CreateBlockView(DisplayItem item){
        switch (item.ui_type.id){
            case LayoutConstant.list_category_land:
                break;
        }
        return null;
    }

    public static View CreateSingleView(DisplayItem item){
        switch (item.ui_type.id){
            case LayoutConstant.list_category_land:
                break;
        }
        return null;
    }
}
