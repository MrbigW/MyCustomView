package com.atsgg.customviewdemo07;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Mr.W on 2016/9/23.
 */

public class MyattributeView extends View {

    private String nameStr;
    private int ageInt;
    private Bitmap bitmap;
    private int textSize;
    private Paint mPaint;

    public MyattributeView(Context context) {
        this(context, null);
    }

    public MyattributeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyattributeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        String my_name = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_name");
        String my_age = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_age");
        String my_bg = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "my_bg");


        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            System.out.println(attrs.getAttributeValue(i) + ":"
                    + attrs.getAttributeName(i));
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyattributeView,defStyleAttr,0);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int index = array.getIndex(i);
            switch (index) {
                case R.styleable.MyattributeView_my_name:
                    nameStr = array.getString(index);
                    break;
                case R.styleable.MyattributeView_my_age:
                    ageInt = array.getInt(index, 0);
                    break;
                case R.styleable.MyattributeView_my_bg:
                    Drawable draw = array.getDrawable(index);
                    BitmapDrawable bitDraw = (BitmapDrawable) draw;
                    bitmap = bitDraw.getBitmap();
                    break;
                case R.styleable.MyattributeView_text_size:
                    int defValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                            , 16, getResources().getDisplayMetrics());
                    textSize = array.getDimensionPixelSize(index, defValue);
                    Log.e("TAG", textSize + "");
                    break;
                default:
                    break;
            }
        }
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(nameStr + "--------" + ageInt, 50, 100, mPaint);
        canvas.drawBitmap(bitmap, 50, 100, mPaint);
    }
}
