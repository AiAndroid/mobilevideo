package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class AdsView extends RelativeLayout{
    public AdsView(Context context) {
        this(context, null, 0);
    }
    public AdsView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    public AdsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AdsView(Context context, ArrayList<DisplayItem> items) {
        super(context, null, 0);

        initUI(items);
    }

    private void initUI(ArrayList<DisplayItem> items){
        View root = View.inflate(getContext(), R.layout.ads_viewflipper,  this);
        ViewFlipper viewFlipper = (ViewFlipper) root.findViewById(R.id.image_flipper);
        for(DisplayItem item: items) {
            viewFlipper.addView(getImageView(item));
        }
    }

    private ImageView getImageView(DisplayItem item){
        ImageView imageView = new ImageView(getContext());
        Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.icon_h_default).error(R.drawable.icon_h_default).fit().into(imageView);
        return imageView;
    }
}
