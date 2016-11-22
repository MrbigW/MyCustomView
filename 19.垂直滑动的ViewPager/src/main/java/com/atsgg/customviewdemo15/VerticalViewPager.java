package com.atsgg.customviewdemo15;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by MrbigW on 2016/11/22.
 * weChat:1024057635
 * GitHub:MrbigW
 * Usage: -.-
 * -------------------=.=------------------------
 */

public class VerticalViewPager extends ViewPager {

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 设置Viewpager的切换动画，这里设置才能真正实现垂直滑动的viewpager
        setPageTransformer(true, new VerticalTransformer());

    }

    /**
     * 拦截事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean intercept = super.onInterceptTouchEvent(swapEvent(ev));

        swapEvent(ev);

        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapEvent(ev));
    }

    private MotionEvent swapEvent(MotionEvent ev) {
        // 获取宽高
        float width = getWidth();
        float height = getHeight();

        // 将Y轴的移动距离转变成X轴的移动距离
        float swappedX = (ev.getY() / height) * width;
        // 将X轴的移动距离转变成Y轴的移动距离
        float swappedY = (ev.getX() / width) * height;

        // 重设event的位置
        ev.setLocation(swappedX, swappedY);

        return ev;
    }

    /**
     * viewpager的垂直切换动画
     */
    public class VerticalTransformer implements PageTransformer {

        public static final String TAG = "simple";

        @Override
        public void transformPage(View page, float position) {
//            float alpha = 0;
//
//            if (0 <= position && position <= 1) {
//                alpha = 1 - position;
//            } else if (-1 < position && position < 0) {
//                alpha = position + 1;
//            }
//
//            page.setAlpha(alpha);
            float transX = page.getWidth() * -position;
            page.setTranslationX(transX);
            float transY = position * page.getHeight();
            page.setTranslationY(transY);
        }
    }
}


















