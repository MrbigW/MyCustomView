package com.atsgg.youku;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Mr.W on 2016/9/21.
 */
public class Tools {

    public static void hideView(View view) {
      hideView(view,0);
    }


    public static void showView(View view) {
      showView(view,0);
    }

    public static void hideView(View view, int delay) {
        //  view.setRotation();
        ObjectAnimator oba = ObjectAnimator.ofFloat(view,"rotation",0,180);
        oba.setDuration(500);
        oba.setStartDelay(delay);
        oba.start();
        //  设置轴心点
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight());
    }
    public static void showView(View view, int delay){
        //  view.setRotation();
        ObjectAnimator oba = ObjectAnimator.ofFloat(view,"rotation",180,360);
        oba.setDuration(500);
        oba.setStartDelay(delay);
        oba.start();
        //  设置轴心点
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight());
    }
}
