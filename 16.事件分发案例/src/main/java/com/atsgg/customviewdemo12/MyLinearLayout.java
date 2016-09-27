package com.atsgg.customviewdemo12;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Mr.W on 2016/9/24.
 */

public class MyLinearLayout extends LinearLayout {

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 该方法拦截触摸事件
     * 1.如果返回的是true,将会触发当前View的onTouchEvent();
     * 2.如果返回的是false,事件将会传给孩子
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 得到listview的个数
        int count = getChildCount();
        // 得到每个listview的宽度
        int childWidth = getWidth() / count;
        // 得到每个listview的高度
        int heigh = getHeight();

        // 得到触摸的X坐标
        float eventX = event.getX();

        // 滑动最左边的listview
        if (eventX < childWidth) {
            event.setLocation(childWidth / 2, event.getY());
            getChildAt(0).dispatchTouchEvent(event);
            // 消费掉事件
            return true;
        } else if (eventX > childWidth / 2 && eventX < 2 * childWidth) {// 滑动中间的listview
            float eventY = event.getY();
            // 判断是上半部分还是下半部分，来确定是此listview下拉还是部listvie上拉
            if (eventY <= heigh / 2) {
                event.setLocation(childWidth / 2, event.getY());
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    child.dispatchTouchEvent(event);
                }
                return true;
            } else if (eventY > heigh / 2) {
                event.setLocation(childWidth / 2, event.getY());
                getChildAt(1).dispatchTouchEvent(event);
                return true;
            }

        } else if (eventX > 2 * childWidth) {// 滑动第三个listview
            event.setLocation(childWidth / 2, event.getY());
            getChildAt(2).dispatchTouchEvent(event);
            return true;
        }

        return true;
    }
}






























