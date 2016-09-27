package com.atsgg.customviewdemo09;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mr.W on 2016/9/23.
 */

public class WaveView extends View {

    private Paint mPaint;// 画笔对象
    private Path mPath;// 路径对象

    private int vWidth, vHeight;// 控件宽高
    private float ctrX, ctrY;// 控制点的坐标
    private float waveY;// 整个Wave顶部两端点的Y坐标，该坐标与控制点的Y坐标增减幅一致,以及
    private float startX, endX;// 整个Wave的左端点,与右端点

    private boolean isInc; // 判断控制点是该坐移还是右移,true为右移

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 实例化画笔并设置参数
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0xFF00796B);

        // 实例化路径
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 获取控件的宽高
        vWidth = w;
        vHeight = h;

        // 计算端点Y坐标
        waveY = 1 / 8F * vHeight;
        // 计算控制点Y的坐标
        ctrY = -1 / 16F * vHeight;

        startX = -1 / 4F * vWidth;
        endX = 1 / 4F * vWidth + vWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         *  设置Path起点
         * 注意我将Path的起点设置在了控件的外部看不到的区域
         * 如果我们将起点设置在控件左端x=0的位置会使得贝塞尔曲线变得生硬
         * 至于为什么刚才我已经说了
         * 所以我们稍微让起点往“外”走点
         */
        mPath.moveTo(startX, waveY);

        /**
         *  以二阶曲线的方式通过控制点连接位于控件右边的终点
         * 终点的位置也是在控件外部
         * 我们只需不断让ctrX的大小变化即可实现“浪”的效果
         */
        mPath.quadTo(ctrX, ctrY, endX, waveY);
//        mPath.cubicTo(ctrX, ctrY,
//                (vWidth + 1 / 4F * vWidth) / 2, waveY - 1 / 8F * vHeight,
//                vWidth + 1 / 4F * vWidth, waveY);

        // 围绕控件闭合曲线
        mPath.lineTo(endX, vHeight);
        mPath.lineTo(startX, vHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);

        /**
         *  当控制点的x坐标大于或等于终点x坐标时更改标识值
         */
        if (ctrX >= endX) {
            isInc = false;
        }
        /**
         *  当控制点的x坐标小于或等于起点x坐标时更改标识值
         */
        else if (ctrX <= startX) {
            isInc = true;
        }
        // 根据标识值判断当前的控制点x坐标是该加还是减
        ctrX = isInc ? ctrX + 20 : ctrX - 20;

        if (ctrY <= vHeight) {
            ctrY += 2;
            waveY += 2;
        }
        mPath.reset();
        invalidate();
    }
}
