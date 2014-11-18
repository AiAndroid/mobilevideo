package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.video.ui.R;

/**
 *@author tangfuling
 *
 */

public class CornerUpImageView extends ImageView {
	
	private Context mContext;
	private int mRadius;
	
	public CornerUpImageView(Context context) {
		super(context);
		mContext = context;
		initRadius();
	}
	
	public CornerUpImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initRadius();
	}
	
	public void setRadius(int radius) {
		this.mRadius = radius;
	}
	
	private void initRadius() {
		mRadius = mContext.getResources().getDimensionPixelSize(R.dimen.video_common_radius_18);
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(toRoundCorner(bm));
	}
	
	private Bitmap toRoundCorner(Bitmap bitmap) {  
		if(bitmap == null || mRadius == 0) {
			return bitmap;
		}

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
        final Paint paint = new Paint();  
        final Rect rect1 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF1 = new RectF(rect1);
        final Rect rect2 = new Rect(0, mRadius, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        canvas.drawRoundRect(rectF1, mRadius, mRadius, paint);
        canvas.drawRect(rect2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect1, rect1, paint);
        return output;  
    }
}
