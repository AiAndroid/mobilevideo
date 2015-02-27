package com.video.ui.view.user;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by liuhuadonbg on 1/30/15.
 */
public class UserHeadImageView extends ImageView {

    public UserHeadImageView(Context context) {
        super(context);
    }

    public UserHeadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(toRoundCorner(bm));
    }

    private Bitmap toRoundCorner(Bitmap bitmap) {
        if(bitmap == null) {
            return null;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        int radius = bitmap.getWidth() / 2;
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}