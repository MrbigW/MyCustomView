package com.atsgg.customviewdemo04;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Shader.TileMode里有三种模式：
 * CLAMP:边缘拉伸
 * MIRROR:镜像
 * REPETA:重复
 * 注意：BitmapShader是先应用了Y轴的模式而X轴是后应用的
 * Created by Mr.W on 2016/9/22.
 */

public class BrickView extends View {

    private Paint mFillPaint, mStrokePaint;// 填充和描边的画笔
    private BitmapShader mBitmapShader;//   Bitmap着色器

    private float posX, posY;// 触摸点的X,Y坐标
    private Bitmap mBitmap;


    public BrickView(Context context) {
        this(context, null);
    }

    public BrickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景颜色
        canvas.drawColor(Color.DKGRAY);
        // 绘制边和填充
        if (posX != 0 && posY != 0) {
            canvas.drawCircle(posX, posY, 100, mStrokePaint);
            canvas.drawCircle(posX, posY, 100, mFillPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 手指移动时获取触摸点坐标并刷新视图
         */
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posX = event.getX();
            posY = event.getY();

            invalidate();
        }
        return true;
    }

    private void initPaint() {
        // 实例化描边画笔并设置参数
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaint.setColor(0XFF000000);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);
        mStrokePaint.setStrokeWidth(5);

        // 实例化填充画笔
        mFillPaint = new Paint();

        // 生成BitmapShader
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mFillPaint.setShader(mBitmapShader);
    }
}
