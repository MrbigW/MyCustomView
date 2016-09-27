package com.atsgg.customviewdemo10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Mr.W on 2016/9/23.
 */

public class PolylineView extends View {
    private static final float LEFT = 1 / 16F, TOP = 1 / 16F,
            RIGHT = 15 / 16F, BOTTOM = 7 / 8F;// 网格区域相对位置
    private static final float TIME_X = 3 / 32F, TIME_Y = 1 / 16F,
            MONEY_X = 31 / 32F, MONEY_Y = 15 / 16F;// 文字坐标相对位置
    private static final float TEXT_SIGN = 1 / 32F;// 文字相对大小
    private static final float THICK_LINE_WIDTH = 1 / 128F,
            THIN_LINE_WIDTH = 1 / 512F;// 粗线和细线相对大小

    private TextPaint mTextPaint;// 文字画笔
    private Paint linePaint, pointPainr;// 线条与点画笔
    private Path mPath;// 路径对象
    private Bitmap mBitmap;// 绘制曲线的Bitmap对象
    private Canvas mCanvas;// 装在mBitmap的Canvas对象

    private List<PointF> mPointFs;// 数据列表
    private float[] rulerX, rulerY;// x，y轴向刻度

    private String signX, signY;// 设置x和y坐标分别表示什么的文字
    private float textY_X, textY_Y, textX_X, textX_Y;// 文字坐标
    private float textSignSize;// xy坐标标示文本字体大小
    private float thickLineWidth, thinLineWidth;// 粗线和细线宽度
    private float left, top, right, bottom;// 网格区域左上右下两点坐标
    private int viewSize;// 控件尺寸
    private float maxX, maxY;// 横纵轴向最大刻度
    private float spaceX, spaceY;// 刻度间隔

    public PolylineView(Context context) {
        this(context, null);
    }

    public PolylineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolylineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setColor(Color.WHITE);
    }
}
