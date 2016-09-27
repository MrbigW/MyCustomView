package com.atsgg.customviewdemo14;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by 那位程序猿Mr.W on 2016/9/26.
 * 微信:1024057635
 */

public class MySlideLayout extends FrameLayout {

    private View contentView, deleteView, topView;

    private int contentWidth, deleteWidth, topWidth, ViewHeight;

    private Scroller mScroller;

    public MySlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化Scroller
        mScroller = new Scroller(context);

    }

    /**
     * 该方法在布局加载完成之后回调
     * 1. 得到子View对象（ContentView,deleteView,topView）-->onFinishInflate()
     * 2. 此方法在onMeasure(),onLayout()方法之前执行
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
        topView = getChildAt(2);
    }

    /**
     * 得到子View的宽和高-->onMeasure()
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        contentWidth = contentView.getMeasuredWidth();
        deleteWidth = deleteView.getMeasuredWidth();
        topWidth = topView.getMeasuredWidth();

        ViewHeight = getMeasuredHeight();
    }

    /**
     * 对子View进行重新布局-->onLayout()
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        topView.layout(contentWidth, 0, contentWidth + topWidth, ViewHeight);
        deleteView.layout(contentWidth + topWidth, 0, contentWidth + deleteWidth + topWidth, ViewHeight);

    }


    /**
     * 第一次按下的值
     */
    private int lastX;
    private int lastY;
    private int downX;
    private int downY;

    /**
     * 处理触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 1.记录起始坐标
                lastX = downX = eventX;
                lastY = downY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                // 2.计算X轴偏移量
                int distanceX = eventX - lastX;

                int toScrollX = getScrollX() - distanceX;

                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX >= deleteWidth + topWidth) {
                    toScrollX = deleteWidth + topWidth;
                }

                scrollTo(toScrollX, 0);

                // 3.重新赋值
                lastX = eventX;

                /**
                 * 让menuView滑动的途中，不让ListView滑动
                 */
                // 4.得到在水平和垂直方向滑动的距离
                int dX = Math.abs(eventX - downX);
                int dY = Math.abs(eventY - downY);


                // 5.反拦截
                if (dX > dY && dX > 5) {
                    // 1.让父层视图不拦截子View事件； 2.把事件传给当前控件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;
            case MotionEvent.ACTION_UP:
                // 4.当up的时候，计算总的偏移量，判断是平滑的关闭还是打开
                int totalScrollX = getScrollX();

                if (totalScrollX < (topWidth + deleteWidth) / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
        }
        return true;
    }

    /**
     * 为了让点击事件和触摸事件互不影响
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下回传
                if (mOnStateChangeListener != null) {
                    mOnStateChangeListener.onDrown(this);
                }
                // 1.记录其实X轴坐标
                lastX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                // 2.获得滑动时X轴的坐标
                float endX = ev.getX();

                // 3.得到在X轴方向滑动的距离
                float dX = Math.abs(endX - lastX);

                if (dX > 5) {
                    // 1.让父视图不拦截子View事件； 2.把事件传给当前控件
                    isIntercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return isIntercept;
    }

    /**
     * 显示菜单
     */
    public void openMenu() {
        mScroller.startScroll(getScrollX(), getScrollY(),
                topWidth + deleteWidth - getScrollX(), getScrollY());
        invalidate(); // 重绘并去执行computeScroll
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onOpen(this);
        }
    }

    /**
     * 收起菜单
     */
    public void closeMenu() {
        mScroller.startScroll(getScrollX(), getScrollY(),
                0 - getScrollX(), getScrollY());
        invalidate(); // 重绘并去执行computeScroll
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onClose(this);
        }
    }

    /**
     * 固定写法
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    private onStateChangeListener mOnStateChangeListener;

    public void setOnStateChangeListener(onStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
    }

    /**
     * 监听状态变化
     */
    public interface onStateChangeListener {

        /**
         * 当按下的时候回调
         *
         * @param layout
         */
        void onDrown(MySlideLayout layout);

        /**
         * 当menu打开的时候回调这个方法
         *
         * @param layout
         */
        void onOpen(MySlideLayout layout);

        /**
         * 当关闭的时候回调这个方法
         *
         * @param layout
         */
        void onClose(MySlideLayout layout);

        /**
         * Move的时候调用此方法
         */

    }

}
























