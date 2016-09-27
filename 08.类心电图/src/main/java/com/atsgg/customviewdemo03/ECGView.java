package com.atsgg.customviewdemo03;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 实现心电图
 * <p>
 * Created by Mr.W on 2016/9/22.
 */

public class ECGView extends View {

    private Paint mPaint;
    private Path mPath;

    private int screenW, screenH;// 屏幕宽高
    private float x, y;//    路径初始坐标
    private float initScreenW;//    屏幕初始宽度
    private float initX;    //  初始x轴坐标
    private float transX, moveX; //  画布移动距离

    private boolean isCancasMove;// 画布是否需要平移

    public ECGView(Context context) {
        this(context, null);
        initPaint();
    }

    public ECGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

        mPath = new Path();
        transX = 0;
        isCancasMove = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);
        mPath.lineTo(x, y);

        //向左平移画布
        canvas.translate(-transX, 0);
        //计算坐标
        calCoors();
        //绘制路径
        canvas.drawPath(mPath, mPaint);
        invalidate();
    }

    /**
     *  private int screenW, screenH;// 屏幕宽高
     *  private float x, y;//    路径初始坐标
     *  private float initScreenW;//    屏幕初始宽度
     *  private float initX;    //  初始x轴坐标
     *  private float transX, moveX; //  画布移动距离
     */
    private void calCoors() {
        if (isCancasMove == true) {
            transX += 4;
        }
        if (x < initX) {
            x += 8;
        } else {
            if (x < initX + moveX) {
                x += 2;
                y -= 8;
            } else {
                if (x < initX + (moveX * 2)) {
                    x += 2;
                    y += 14;
                } else {
                    if (x < initX + (moveX * 3)) {
                        x += 2;
                        y -= 12;
                    } else {
                        if (x < initX + (moveX * 4)) {
                            x += 2;
                            y += 6;
                        } else {
                            if (x < initScreenW) {
                                x += 8;
                            } else {
                                isCancasMove = true;
                                initX = initX + initScreenW;
                            }
                        }
                    }
                }
            }


        }
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setShadowLayer(7, 0, 0, Color.GREEN);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        screenH = h;
        screenW = w;

        x = 0;
        y = (screenH / 2) + (screenW / 4) + (screenH / 10);

        initScreenW = screenW;

        initX = (screenW / 2) + (screenW / 4);
        moveX = (screenW / 24);

        mPath.moveTo(x, y);
    }
}
