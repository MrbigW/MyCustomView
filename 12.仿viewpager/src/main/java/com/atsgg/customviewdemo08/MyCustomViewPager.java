package com.atsgg.customviewdemo08;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Mr.W on 2016/9/23.
 */

public class MyCustomViewPager extends ViewGroup {

    /**
     * 手势识别器  1.定义  2.实例化 3.接收触摸事件
     */
    private GestureDetector mDetector;

    //    private MyScroller mScroller;
    private Scroller mScroller;

    public MyCustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        initGesDetector(context);
        mScroller = new Scroller(context);
    }

    private int distanceTotal = 0;

    private void initGesDetector(final Context context) {
        mDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                if (getScrollX() + distanceX >= 0 && currentIndex < getChildCount() - 1) {
                    scrollBy((int) distanceX, 0);
                }
                return true;
            }
        });
    }

    private float startX;
    private int currentIndex;// 当前页面的位置

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);// 还会继续传递事件

        mDetector.onTouchEvent(event);// 手势识别器接收触摸事件

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();// 记录起始坐标
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();// 记录移动结束后坐标
                int tempIndex = currentIndex;// 获取当前的页面位置

                /**
                 * 判断该显示前一个还是后一个页面
                 */
                if ((startX - endX) > getWidth() / 2) {
                    tempIndex++;
                } else if ((endX - startX) > getWidth() / 2) {
                    tempIndex--;
                }
                /**
                 * 根据页面下标移动到对应的页面
                 */
                moveToPager(tempIndex);
                break;
        }
        return true;
    }


    public void moveToPager(int index) {
        /**
         * 屏蔽非法值
         */
        if (index < 0) {
            index = 0;
        }
        if (index > getChildCount() - 1) {
            index = getChildCount() - 1;
        }

        currentIndex = index;// 得到要移动到的页面的位置

        if (monPagerChangeListener != null) {
            monPagerChangeListener.onChangePager(currentIndex);
        }
        /**
         * 根据位置移动控件的内容
         */
//        scrollTo(getWidth() * currentIndex, 0);
        int distanceX = currentIndex * getWidth() - getScrollX();
//        mScroller.startScroll(getScrollX(), getScrollY(), distanceX, 0);
        mScroller.startScroll(getScrollX(), getScrollY(), distanceX, 0, Math.abs(distanceX));
        invalidate(); //执行该方法引起onDraw()方法与computeScroll()方法执行
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Log.e("TAG", getWidth() + "--------" + child.getWidth() + "-----------" + l + "-" + t + "-" + r + "-" + b);
            child.layout(i * getWidth(), 0, (i + 1) * getWidth(),
                    getHeight());
        }
    }

    private float downX;
    private float downY;

    /**
     * 是否中断事件的传递，默认返回false,意思为，不中断，按正常情况，传递事件
     * 如果为true，就将事件中断，直接执行自己的onTounchEvent方法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDetector.onTouchEvent(ev);// 手势识别器接收触摸事件
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = ev.getX();
                float newY = ev.getY();

                int distanceX = (int) Math.abs(newX - downX);
                int distanceY = (int) Math.abs(newY - downY);
                // 如果水平方向滑动的距离大于竖直方向滑动，就是左右滑动，中断事件；否则事件继续传递
                if (distanceX > distanceY && distanceX > 10) {
                    result = true;
                } else {
                    moveToPager(currentIndex);
                }
            case MotionEvent.ACTION_UP:

                break;
        }

        return result;
    }

    private onPagerChangeListener monPagerChangeListener;

    public interface onPagerChangeListener {
        void onChangePager(int index);
    }

    public void setOnPagerChangeListener(onPagerChangeListener listener) {
        this.monPagerChangeListener = listener;
    }
}
