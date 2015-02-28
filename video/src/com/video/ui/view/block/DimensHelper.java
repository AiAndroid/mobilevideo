package com.video.ui.view.block;

import android.view.View;

/**
 * Created by liuhuadong on 11/18/14.
 */
public interface DimensHelper {
    public Dimens getDimens();

    public void invalidateUI();
    public void unbindDrawables(View view);

    public static class Dimens{
        public int width;
        public int height;
        public int h_padding;
        public int v_padding;
    }
}
