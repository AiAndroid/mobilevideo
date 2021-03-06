package com.video.ui.view.block;

import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;
import com.squareup.picasso.Transformation;
import com.tv.ui.metro.model.Constants;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.idata.BackgroundService;


/**
 * Created by liuhuadong on 11/19/14.
 */
public abstract class BaseCardView  extends RelativeLayout {
    public BaseCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(media_item_padding == -1){
            media_item_padding = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
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
            if(Constants.Entiry_Long_Video.equals(item.target.entity)) {

                item.type = "item";
            }else if(Constants.Entity_Short_Video.equals(item.target.entity) ||
                     Constants.Entity_Album_Collection.equals(item.target.entity) ||
                     Constants.Entity_Album.equals(item.target.entity)) {

                item.type = "album";
            }else if(Constants.Entity_Search_Video.equals(item.target.entity) ||
                     Constants.Entity_Search_Result_Video.equals(item.target.entity)) {

                item.type = "search";
            }else if(Constants.Entity_App_Video.equals(item.target.entity)){

                BackgroundService.startDownloadAPK(context, item.target.url, item.title, context.getString(R.string.app_name));
                return;
            }else if(Constants.Entity_App_Market_Video.equals(item.target.entity)){
                try {
                    Intent appMarket = new Intent(Intent.ACTION_VIEW);
                    appMarket.setClassName("com.xiaomi.market", "com.xiaomi.market.ui.MarketTabActivity");
                    appMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(appMarket);
                }catch (Exception ne){}
                return;
            }else if(Constants.Entity_Intent_Video.equals(item.target.entity)){
                launchDefault(context, item);
                return;
            }
            else{
                launchDefault(context, item);
                return;
            }
        }else {
            item.type = "album";
            //launchDefault(context, item);
            //return;
        }

        if(item.id.endsWith(Constants.Video_ID_History) || item.id.endsWith(Constants.Video_ID_Offline) || item.id.endsWith(Constants.Video_ID_Favor)){
            item.type = "local_album";
        }

        if(item.id.endsWith(Constants.Video_ID_Offline) ){
            item.type = "play_offline";
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
    private static void launchDefault(Context context, DisplayItem item){
        try {
            Intent intent = null;
            if (item.target != null && TextUtils.isEmpty(item.target.action) == false)
                intent = new Intent(item.target.action);
            else
                intent = new Intent(Intent.ACTION_VIEW);


            intent.setData(Uri.parse(item.target.url));
            if (TextUtils.isEmpty(item.target.mime) == false)
               intent.setType(item.target.mime);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception ne){ne.printStackTrace();}
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float round) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float Px = round;

        final Rect bottomRect = new Rect(0, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, Px, Px, paint);
        // Fill in upper right corner
        // canvas.drawRect(topRightRect, paint);
        // Fill in bottom corners
        canvas.drawRect(bottomRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bitmap != output) {
            bitmap.recycle();
        }
        return output;
    }

    public static class BlendImageWithCover implements Transformation {
        static Bitmap frontBitmp ;
        static NinePatchDrawable vdrawable;
        static NinePatchDrawable hdrawable;
        private boolean mVertical;
        BlendImageWithCover(Context context, boolean vertical) {
            mVertical = vertical;
            if(frontBitmp == null){
                frontBitmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.poster_mask_v);
                byte[] chunk = frontBitmp.getNinePatchChunk();
                if(NinePatch.isNinePatchChunk(chunk)) {
                    vdrawable = new NinePatchDrawable(context.getResources(), frontBitmp, chunk, new Rect(), null);
                }

                frontBitmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.poster_mask_h);
                byte[] hchunk = frontBitmp.getNinePatchChunk();
                if(NinePatch.isNinePatchChunk(hchunk)) {
                    hdrawable = new NinePatchDrawable(context.getResources(), frontBitmp, hchunk, new Rect(), null);
                }
            }

        }

        @Override
        public String key() {
            return "blend bitmap";
        }

        @Override
        public Bitmap transform(Bitmap arg0) {
            return getRoundedTopLeftCornerBitmap(arg0);
        }

        public Bitmap getRoundedTopLeftCornerBitmap(Bitmap bitmap) {

            //TODO
            //bug for crash
            if(bitmap != null){
                return bitmap;
            }

            Canvas canvas = new Canvas(bitmap);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);

            canvas.drawBitmap(bitmap, rect, rect, paint);
            if(mVertical) {
                vdrawable.setBounds(rect);
                vdrawable.draw(canvas);
            }else {
                hdrawable.setBounds(rect);
                hdrawable.draw(canvas);
            }
            return bitmap;
        }
    }
}
