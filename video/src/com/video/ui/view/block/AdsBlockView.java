package com.video.ui.view.block;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.utils.ViewUtils;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class AdsBlockView extends BaseCardView implements DimensHelper, AdsAnimationListener {
    public AdsBlockView(Context context) {
        this(context, null, 0);
    }
    public AdsBlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public AdsBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AdsBlockView(Context context, ArrayList<DisplayItem> items, Object tag) {
        super(context, null, 0);

        setTag(R.integer.picasso_tag, tag);
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
    private  TextView  page_title;
    private  ArrayList<DisplayItem> content;
    private  ArrayList<View> viewList = new ArrayList<View>();
    private void initUI(ArrayList<DisplayItem> items){
        content = items;
        View root = View.inflate(getContext(), R.layout.ads_viewflipper,  this);
        viewFlipper = (ViewPager) root.findViewById(R.id.image_flipper);
        page_indicator = (TextView) root.findViewById(R.id.page_indicator);
        page_title     = (TextView) root.findViewById(R.id.page_title);

        //addView(root);

        viewList.add(getImageView(items.get(items.size()-1)));
        for(DisplayItem item: items) {
            viewList.add(getImageView(item));
        }
        viewList.add(getImageView(items.get(0)));

        page_indicator.setText(String.format("%1$s/%2$s", 1 ,content.size()));
        viewFlipper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //Log.d("ads", "onPageScrolled");
            }

            @Override
            public void onPageSelected(int i) {
                int contentPos = (content.size()+ (i-1))%content.size();
                ImageView iv = (ImageView)( viewList.get(i).findViewById(R.id.image_ads));
                if(iv == null){
                    Log.d("ads", "maybe finished activity");
                    return;
                }

                Picasso.with(getContext()).
                        load(content.get(contentPos).images.get("poster").url)
                        .priority(Picasso.Priority.HIGH)
                        .fit()
                        .transform(new CategoryBlockView.Round_Corners(getContext(), 4, 4, false))
                        .into(iv);

                page_indicator.setText(String.format("%1$s/%2$s ", i ,content.size()));
                mHander.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAnimation();
                    }
                }, 200);

                //when move to last, move to first
                if(i == content.size()+1){
                    viewFlipper.setCurrentItem(1, false);
                    page_title.setText(content.get(0).title);
                }else if(i == 0){
                    viewFlipper.setCurrentItem(content.size(), false);
                    page_title.setText(content.get(content.size()-1).title);
                }else {
                    page_title.setText(content.get(i-1).title);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == 1) {
                    stopAnimation();
                } else {
                    startAnimation();
                }
            }
        });

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {return arg0 == arg1; }

            @Override
            public int getCount() { return viewList.size(); }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
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
                View view = viewList.get(position);
                if (view != null && view.getParent() == null) {
                    container.addView(view);
                }

                return view;
            }
        };
        viewFlipper.setAdapter(pagerAdapter);
        viewFlipper.setCurrentItem(1, false);
    }

    private View getImageView(final DisplayItem item){
        View view  = View.inflate(getContext(), R.layout.ads_imageview_container, null);

        view.findViewById(R.id.ads_media_click).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                launcherAction(getContext(), item);
            }
        });
        return view;
    }

    private Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.media_banner_height);
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {
        for(int i=0;i<content.size();i++) {
            ImageView view = (ImageView) viewList.get(i).findViewById(R.id.image_ads);
            DisplayItem item = content.get(i);
            Picasso.with(getContext()).load(item.images.get("poster").url).resize(getDimens().width, getDimens().height).tag(getTag(R.integer.picasso_tag)).priority(Picasso.Priority.HIGH).transform(new CategoryBlockView.Round_Corners(getContext(), 4, 4, false)).into(view);
        }
    }

    @Override
    public void unbindDrawables(View view) {
        ViewUtils.unbindDrawables(this);
    }

    private Handler mHander = new Handler();
    private boolean stoped = true;
    @Override
    public void startAnimation() {
        if(stoped == true) {
            stoped = false;
            mHander.postDelayed(swipe, 5000);
        }
    }

    private Runnable swipe = new Runnable() {
        @Override
        public void run() {
            if(stoped == false) {
                int index = viewFlipper.getCurrentItem();
                viewFlipper.setCurrentItem((index + 1) % viewList.size());
                mHander.postDelayed(this, 5000);
            }
        }
    };

    @Override
    public void stopAnimation() {
        stoped = true;
        mHander.removeCallbacks(swipe);
    }

    @Override
    public AdsAnimationListener getAnimationListener() {
        return this;
    }
}
