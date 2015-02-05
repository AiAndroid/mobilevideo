package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class QuickNavigationBlockView extends BaseCardView implements DimensHelper {
    public QuickNavigationBlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        itemWidth = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_width);
        colorsNormal  =new int[itemWidth * getDimens().height];
        colorsPressed =new int[itemWidth * getDimens().height];
    }

    private  int[] colorsNormal;
    private  int[] colorsPressed;

    private int []draws = {
        R.drawable.quick_entry_tv_series_bg,
        R.drawable.quick_entry_film_bg,
        R.drawable.quick_entry_variety_bg,
        R.drawable.quick_entry_all_bg
    };

    private static String []color = {
            "#f18f5a",
            "#e36565",
            "#7790ec",
            "#43ca7f",
            "#f7698d",
            "#b477da",
            "#51beec",
            "#e7a551"
    };
    private static String []colorpress = {
            "#db7e4c",
            "#c75959",
            "#687ed1",
            "#3da86c",
            "#cf5474",
            "#9b69ba",
            "#44aad5",
            "#cd9144"
    };
    private int []hodlerdraws = {
            R.drawable.quick_entry_tv_series,
            R.drawable.quick_entry_film,
            R.drawable.quick_entry_variety,
            R.drawable.quick_entry_all
    };

    static int step = 0;
    private View                   root;
    private ArrayList<DisplayItem> content;
    public QuickNavigationBlockView(Context context, ArrayList<DisplayItem> items, Object tag) {
        this(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        content = items;

        root = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);

        int width = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_width);
        int padding = (getDimens().width-4*width)/3;
        int leftPadding = 0;
        for (int i=0;i<items.size();i++) {
            final DisplayItem item = items.get(i);
            View view =  View.inflate(getContext(), R.layout.qucik_entry_textview, null);
            view.setClickable(true);

            if(item.images.normal() != null || true){
                try {

                    int normalColor = -1;
                    int pressColor  = -1;
                    try {
                        normalColor = Color.parseColor(item.images.normal().bgcolor);
                        pressColor  = Color.parseColor(item.images.pressed().bgcolor);
                    }catch (Exception ne){
                        normalColor = Color.parseColor(color[step%color.length]);
                        pressColor  = Color.parseColor(colorpress[step%colorpress.length]);
                    }

                    StateListDrawable stalistDrawable = new StateListDrawable();
                    int r, g,b,a;
                    for (int y = 0; y < getDimens().height; y++) {//use of x,y is legible then the use of i,j
                        for (int x = 0; x < itemWidth; x++) {
                            if(normalColor == -1) {
                                r = (x * 255 / (itemWidth - 1));
                                g = y * 255 / (getDimens().height - 1);
                                b = 255 - Math.min(r, g);
                                a = Math.max(r, g);
                                colorsNormal[y * itemWidth + x] = (a << 24) | (r << 16) | (g << 8) | (b);//the shift operation generates the color ARGB
                            }else{
                                colorsNormal[y * itemWidth + x] = normalColor;//is java hava C memset to enhance the performace
                            }
                        }
                    }

                    for (int y = 0; y < getDimens().height; y++) {//use of x,y is legible then the use of i,j
                        for (int x = 0; x < itemWidth; x++) {
                            if(pressColor == -1) {
                                r = (x * 255 / (itemWidth - 1));
                                g = y * 255 / (getDimens().height - 1);
                                b = 255 - Math.min(r, g);
                                a = Math.max(r, g)/2;
                                colorsPressed[y * itemWidth + x] = (a << 24) | (r << 16) | (g << 8) | (b);//the shift operation generates the color ARGB
                            }else{
                                colorsPressed[y * itemWidth + x] = pressColor;//is java hava C memset to enhance the performace
                            }
                        }
                    }
                    Drawable normalDrawable = new BitmapDrawable(getResources(), getRoundedCornerBitmap(Bitmap.createBitmap(colorsNormal, itemWidth, getDimens().height, Bitmap.Config.ARGB_8888), 8));
                    Drawable pressedDrawable = new BitmapDrawable(getResources(), getRoundedCornerBitmap(Bitmap.createBitmap(colorsPressed, itemWidth, getDimens().height, Bitmap.Config.ARGB_8888), 8));

                    int pressed = android.R.attr.state_pressed;
                    int window_focused = android.R.attr.state_window_focused;
                    int focused = android.R.attr.state_focused;
                    int selected = android.R.attr.state_selected;

                    stalistDrawable.addState(new int []{pressed , window_focused}, pressedDrawable);
                    stalistDrawable.addState(new int []{pressed , -focused},       normalDrawable);
                    stalistDrawable.addState(new int []{selected }, pressedDrawable);
                    stalistDrawable.addState(new int []{focused }, pressedDrawable);
                    stalistDrawable.addState(new int []{}, normalDrawable);
                    view.setBackground(stalistDrawable);

                }catch (Exception ne){
                    view.setBackgroundResource(draws[i%4]);
                }
            }else {
                view.setBackgroundResource(draws[i%4]);
            }


            TextView tv = (TextView) view.findViewById(R.id.quick_enter);
            tv.setText(item.title);

            ImageView iv = (ImageView) view.findViewById(R.id.enter_image_indicator);
            Picasso.with(getContext()).load(item.images.icon().url).tag(getTag(R.integer.picasso_tag)).placeholder(hodlerdraws[i]).priority(Picasso.Priority.HIGH).into(iv);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    launcherAction(getContext(), item);
                }
            });
            mMetroLayout.addItemView(view,width , getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_height), leftPadding, padding);

            step++;
        }
    }

    private int itemWidth;
    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_height) + 2*getResources().getDimensionPixelSize(R.dimen.detail_info_type_top_margin);
        }
        return mDimens;
    }

    @Override
    public void invalidateUI() {
        LinearFrame mMetroLayout = (LinearFrame)root.findViewById(R.id.metrolayout);
        for (int i=0;i<content.size();i++) {
            final DisplayItem item = content.get(i);
            View view =  mMetroLayout.getChildAt(i);

            ImageView iv = (ImageView) view.findViewById(R.id.enter_image_indicator);
            if(iv != null) {
                Picasso.with(getContext()).load(item.images.icon().url).tag(getTag(R.integer.picasso_tag)).placeholder(R.drawable.quick_entry_default).priority(Picasso.Priority.HIGH).fit().into(iv);
            }
        }
    }

    @Override
    public void unbindDrawables(View view) {

    }
}
