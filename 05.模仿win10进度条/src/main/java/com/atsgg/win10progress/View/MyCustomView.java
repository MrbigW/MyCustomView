package com.atsgg.win10progress.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 流程
 * 1.一个匀速圆周运动的点
 * 先用path画一个圆
 * ValueAnimator设置为0f-1f的平滑
 * 用PathMeasure根据ValueAnimator返回的值截取path上的一个点
 * 2.多个匀速圆周运动的点
 * 我们设置让t每间隔0.05就画一个点，总共画4个点，注意这里getSegment()的最后一个要设置为true来保证画出来的是多个点而不是一条线
 * 3.多个速度由快到慢圆周运动的点
 * 函数为y = -x*x + 2*x，当x=1时，y=mPathMeasure.getLength();
 * 4.点与点之间的间距线性减少，动画的最后合为一个点
 *      虽然流程3中点与点的间距已经开始减少，不过这只是因为速度不同间距才改变的，我们的目的是让这些点到最后合并为1个点，
 *      也就是说开始的时候每个点的X间距0.05，结束的时候要让他们的X相同
 * 5.为了让动画看起来更加流畅，需要在动画即将结束的时候手动绘制点
 *        if(t>=0.95){
 *              canvas.drawPoint(0,-150,mPaint);
 *        }
 * <p>
 * 核心控件
 * PathMeasure：截取Path中的一部分并显示
 * ValueAnimator：完成动画从初始值平滑的过度到结束值的效果，同时还负责管理动画的播放次数、播放模式、以及对动画设置监听器等
 */

public class MyCustomView extends View {

    private Paint mPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;
    private int mWidth, mHeight;
    private ValueAnimator mValueAnimator;

    //  用来接收ValueAnimator的返回值，代表整个动画的进度
    private float t;
    private Path mDst;


    public MyCustomView(Context context) {
        this(context, null);
        initPaint();

        initPath();

        initVAnimation();
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initPaint();

        initPath();

        initVAnimation();
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();

        initPath();

        initVAnimation();
    }

    // 
    private void initVAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                t = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mValueAnimator.start();
    }

    private void initPath() {
        mPath = new Path();
        RectF rect = new RectF(-100, -100, 100, 100);
        mPath.addArc(rect, -90, 359.9f);    //这里角度不能选360，否则会测量失误，具体原因和android的内部优化有关
        mPathMeasure = new PathMeasure(mPath, false);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(13);
        mPaint.setColor(Color.WHITE);
        //  设置画笔为圆笔
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //  抗锯齿
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        mDst = new Path();
//        mPathMeasure.getSegment(mPathMeasure.getLength() * t, mPathMeasure.getLength() * t + 1, mDst, true);
        int num = (int) (t / 0.05);
        float s, y, x;
        switch (num) {
            default:
            case 3:
                x = t - 0.15f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
            case 2:
                x = t - 0.10f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
            case 1:
                x = t - 0.05f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
            case 0:
                x = t;
                s = mPathMeasure.getLength();
                y = -s * x * x + 2 * s * x;
                mPathMeasure.getSegment(y, y + 1, mDst, true);
                break;
        }
        if(t>=0.95){
            canvas.drawPoint(0,-100,mPaint);
        }else {
            canvas.drawPath(mDst, mPaint);
        }
        canvas.drawPath(mDst, mPaint);
        mDst.reset();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}

























