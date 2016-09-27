package com.atsgg.customviewdemo02.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.atsgg.customviewdemo02.R;

/**
 * Created by Mr.W on 2016/9/22.
 * <p>
 * setColorFilter() 设置颜色过滤
 */

public class CustomView extends View {

    //  生成色彩矩阵
    ColorMatrix mColorMatrix = new ColorMatrix(new float[]{
            1.438F, -0.122F, -0.016F, 0, -0.03F,
            -0.062F, 1.378F, -0.016F, 0, 0.05F,
            -0.062F, -0.122F, 1.483F, 0, -0.02F,
            0, 0, 0, 1, 0,
//            0.33F, 0.59F, 0.11F, 0, 0,   //第一行表示的R（红色）的向量，
//            0.33F, 0.59F, 0.11F, 0, 0,   // 第二行表示的G（绿色）的向量，
//            0.33F, 0.59F, 0.11F, 0, 0,   // 第三行表示的B（蓝色）的向量，
//            0, 0, 0, 1, 0,               // 最后一行表示A（透明度）的向量
    });

    private Context mContext;
    private Paint mPaint;
    private int mWeight;
    private int mHeight;

    private Bitmap mBitmap; //位图

    private int x, y;    //  位图绘制时左上角的起点坐标


    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        initPaint();
        initRes(context);
    }

    private void initRes(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.genji);
//        x = mWeight / 2 - mBitmap.getWidth() / 2;
//        y = mHeight / 2 - mBitmap.getHeight() / 2;
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.argb(255, 255, 128, 103));
//        mPaint.setStrokeWidth(10);
        mPaint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
        initRes(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(mWeight / 2, mHeight / 2, 200, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWeight = w;
        mHeight = h;
    }
}
