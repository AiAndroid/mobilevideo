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
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class AdsView extends RelativeLayout implements DimensHelper {
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
    private  TextView  page_indicator;
    private  ArrayList<DisplayItem> content;
    private  ArrayList<View> viewList = new ArrayList<View>();
    private void initUI(ArrayList<DisplayItem> items){
        content = items;
        View root = View.inflate(getContext(), R.layout.ads_viewflipper,  this);
        viewFlipper = (ViewPager) root.findViewById(R.id.image_flipper);
        page_indicator = (TextView) root.findViewById(R.id.page_indicator);

        for(DisplayItem item: items) {
            viewList.add(getImageView(item));
        }

        page_indicator.setText(String.format("%1$s/%2$s", 1 ,content.size()));
        viewFlipper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {}

            @Override
            public void onPageSelected(int i) {
                page_indicator.setText(String.format("%1$s/%2$s", i+1 ,content.size()));
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

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
        imageView.setClickable(true);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Picasso.with(getContext()).load(item.images.get("poster").url).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).fit().transform(new CategoryItemView.Round_Corners(getContext(), 4, 4)).into(imageView);
        return imageView;
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.media_banner_height);
        }
        return mDimens;
    }
}
