package com.atsgg.customviewdemo06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Mr.W on 2016/9/23.
 */

public class MyToggleButton extends View implements View.OnClickListener {

    private Bitmap bgBitmap;
    private Bitmap btnBitmap;
    private Paint mPaint;
    private int slidingMax;
    private int sliding;

    public MyToggleButton(Context context) {
        this(context, null);
    }

    public MyToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(bgBitmap.getWidth(), bgBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bgBitmap, 0, 0, mPaint);
        canvas.drawBitmap(btnBitmap, sliding, 0, mPaint);
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
       bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        btnBitmap = BitmapFactory.decodeResource(getResources(),  R.drawable.slide_button);
        slidingMax = bgBitmap.getWidth() - btnBitmap.getWidth();
        setOnClickListener(this);
    }

    // 判断是否可点击
    private boolean isCheckEnable = true;
    // 记录点击是的坐标
    private float firstX;
    private float startX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = startX = event.getX();
                isCheckEnable = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float endX, distanceX;
                endX = event.getX();
                distanceX = endX - startX;
                if (Math.abs(endX - firstX) > 5) {
                    isCheckEnable = false;
                }
                sliding += distanceX;
                if (sliding < 0) {
                    sliding = 0;
                }
                if (sliding > slidingMax) {
                    sliding = slidingMax;
                }
                invalidate();
                startX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (!isCheckEnable) {
                    if (sliding > slidingMax / 2) {
                        isOpen = true;
                    } else {
                        isOpen = false;
                    }
                    flushView();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    // 判断按钮状态
    private boolean isOpen = false;

    @Override
    public void onClick(View v) {
        if (isCheckEnable) {
            isOpen = !isOpen;
            flushView();
        }
    }

    private void flushView() {
        if (isOpen) {
            sliding = slidingMax;
        } else {
            sliding = 0;
        }
        invalidate();
    }
}
