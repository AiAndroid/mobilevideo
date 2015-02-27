/**
 *  Copyright(c) 2014 XiaoMi TV Group
 *    
 *  AnimatorFactory.java
 *
 *  @author tianli(tianli@xiaomi.com)
 *
 *  2014-11-27
 */
package com.video.ui.view.detail;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * @author tianli
 *
 */
public class AnimatorFactory {

    public static final int ANIMATE_DURATION = 450;

    public static final int AUTO_DISMISS_TIMER = 3000;
    public static final int AUTO_IMMEDIATELY_DISMISS_TIMER = 100;

    private static int getHeight(View view){
        int height = view.getHeight();
        if(height == 0){
            height = view.getContext().getResources().getDisplayMetrics().heightPixels;
        }
        return height;
    }

    
    public static  Animator animateInTopView(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 
                -getHeight(view), 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(ANIMATE_DURATION);
        animator.addUpdateListener(new AnimatorInvalidateUpdateListener(view));
        animator.start();
        view.setVisibility(View.VISIBLE);
        return animator;
    }


    
    public static Animator animateOutTopView(View view) {
        Animator animator = ObjectAnimator.ofFloat(view, "translationY", 
                 0, -view.getHeight());
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(ANIMATE_DURATION);
        animator.addListener(new ViewGoneAnimatorListener(view));
        animator.start();
        return animator;
    }

    public static  class ViewGoneAnimatorListener implements Animator.AnimatorListener {

        private View mView;

        public ViewGoneAnimatorListener(View view){
            mView = view;
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if(mView != null){
                mView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

        @Override
        public void onAnimationStart(Animator animator) {
        }

    }

    
    public static class AnimatorInvalidateUpdateListener implements AnimatorUpdateListener{

        private View mView;
        public AnimatorInvalidateUpdateListener(View view){
            mView = view;
        }
        @Override
        public void onAnimationUpdate(ValueAnimator animator) {
            if(mView != null){
                mView.invalidate();
            }
        }
    };
}
