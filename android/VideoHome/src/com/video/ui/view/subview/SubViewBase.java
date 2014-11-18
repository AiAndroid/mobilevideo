package com.video.ui.view.subview;

/**
 * Created by liuhuadong on 11/18/14.
 */
public interface SubViewBase {
    public Dimens getDimens();

    public static class Dimens{
        public int width;
        public int height;
        public int h_padding;
        public int v_padding;
    }
}
