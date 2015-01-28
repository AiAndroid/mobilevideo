package com.video.ui.view.subview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

/**
 * Created by liuhuadong on 11/19/14.
 */
public abstract class BaseCardView  extends RelativeLayout {
    public BaseCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(media_item_padding == -1){
            media_item_padding = getResources().getDimensionPixelSize(R.dimen.media_item_padding);
        }
    }

    protected DisplayItem item;
    public DisplayItem getMediaContent(){
        return item;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public java.lang.Object getTag(int key) {
        Object obj = super.getTag(key);
        if(obj == null){
            return new Integer(-1);
        }

        return obj;
    }

    protected static int media_item_padding = -1;
    public static void launcherAction(Context context, DisplayItem item){
        Log.d("click action", "item ="+item);
        //Toast.makeText(context, "prepare to launch="+item.title + "/" +item.id + "/" + item.target + "/"+item.ns+ item.ui_type, Toast.LENGTH_SHORT).show();

        if(item.target != null && item.target.entity != null) {
            if(item.target.entity.endsWith("pvideo")) {
                item.type = "item";
            }else if(item.target.entity.endsWith("svideo")) {
                item.type = "item";
            }else if(item.target.entity.endsWith("album_collection")) {
                item.type = "album";
            }else if(item.target.entity.endsWith("album")) {
                item.type = "album";
            }else{
                item.type = "item";
            }
        }else {
            item.type = "album";
        }

        if(item.id.endsWith("play_history") || item.id.endsWith("play_offline") || item.id.endsWith("play_favor")){
            item.type = "local_album";
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("mvschema://" + item.ns + "/" + item.type + "?rid=" + item.id));
            intent.putExtra("item", item);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception ne) {
            ne.printStackTrace();
        }
    }
}
