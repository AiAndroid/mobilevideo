package com.video.ui.view.subview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        boolean ret = super.dispatchTouchEvent(ev);
        if(ret){
            ((ViewGroup)viewFlipper.getParent()).requestDisallowInterceptTouchEvent(true);
        }
        return ret;
    }

    private  ViewPager viewFlipper;
    private  ArrayList<DisplayItem> content;
    private  ArrayList<View> viewList = new ArrayList<View>();
    private void initUI(ArrayList<DisplayItem> items){
        content = items;
        View root = View.inflate(getContext(), R.layout.ads_viewflipper,  this);
        viewFlipper = (ViewPager) root.findViewById(R.id.image_flipper);

        for(DisplayItem item: items) {
            viewList.add(getImageView(item));
        }

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));

            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

        };
        viewFlipper.setAdapter(pagerAdapter);
    }

    private ImageView getImageView(DisplayItem item){
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.drawable.list_selector_bg);
        Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.icon_h_default).error(R.drawable.icon_h_default).fit().into(imageView);
        return imageView;
    }
}
