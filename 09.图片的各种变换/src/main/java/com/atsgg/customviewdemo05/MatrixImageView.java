package com.atsgg.customviewdemo05;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * canvas也有setMatrix(matrix)方法来设置矩阵变换，
 * 更常见的是在ImageView中对ImageView进行变换，当
 * 我们手指在屏幕上划过一定的距离后根据这段距离来平移我们的控件，
 * 根据两根手指之间拉伸的距离和相对于上一次旋转的角度来缩放旋转我们的图片
 * <p>
 * Created by Mr.W on 2016/9/22.
 */

public class MatrixImageView extends ImageView {

    private static final int MODE_NONE = 0x00123;// 默认的触摸模式
    private static final int MODE_DRAG = 0x00321;// 拖拽模式
    private static final int MODE_ZOOM = 0x00132;// 缩放or旋转模式

    private int width;
    private int height;

    private int mode;// 当前的触摸模式

    private float preMove = 1F;// 上一次手指移动的距离
    private float savaRotate = 0F;// 保存角度
    private float rotate = 0F;// 旋转的角度

    private float[] preEventCoor;// 上一次个触摸点的坐标集合

    private PointF start, mid;// 起点、中点对象
    private Matrix currentMatrix, savaedMatrix;// 当前和保存的Matrix对象
    private Context mContext;


    public MatrixImageView(Context context) {
        this(context, null);
    }

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        // 初始化
        init();

    }

    private void init() {
        // 初始化
        currentMatrix = new Matrix();
        savaedMatrix = new Matrix();
        start = new PointF();
        mid = new PointF();

        // 模式初始化
        mode = MODE_NONE;

        // 资源初始化
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.genji);
//       mBitmap = Bitmap.createScaledBitmap(mBitmap, width/2, height/2, true);
        setImageBitmap(mBitmap);
    }

    /**
     * mode;// 当前的触摸模式
     * preMove = 1F;// 上一次手指移动的距离
     * savaRotate = 0F;// 保存角度
     * rotate = 0F;// 旋转的角度
     * preEventCoor;// 上一次触摸点的坐标集合
     * start, mid;// 起点、中点对象
     * currentMatrix, savaedMatrix;// 当前和保存的Matrix对象
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:// 单点接触屏幕时
                savaedMatrix.set(currentMatrix);
                start.set(event.getX(), event.getY());
                mode = MODE_DRAG;
                preEventCoor = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:// 第二个点接触屏幕时
                preMove = calSpacing(event);
                if (preMove > 10F) {
                    savaedMatrix.set(currentMatrix);
                    calMidPoint(mid, event);
                    mode = MODE_ZOOM;
                }
                preEventCoor = new float[4];
                preEventCoor[0] = event.getX(0);
                preEventCoor[1] = event.getX(1);
                preEventCoor[2] = event.getY(0);
                preEventCoor[3] = event.getY(1);
                savaRotate = calRotation(event);
                break;
            case MotionEvent.ACTION_UP:// 第一个单点离开屏幕时
                mode = MODE_NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:// 第二个点离开屏幕时
                mode = MODE_NONE;
                break;
            case MotionEvent.ACTION_MOVE:// 触摸点移动时
                /**
                 * 单点触控拖拽平移
                 */
                if (mode == MODE_DRAG) {
                    currentMatrix.set(savaedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    currentMatrix.postTranslate(dx, dy);
                } /**
                 * 两点触控缩放旋转
                 */
                else if (mode == MODE_ZOOM && event.getPointerCount() == 2) {
                    preEventCoor = new float[4];
                    preEventCoor[0] = event.getX(0);
                    preEventCoor[1] = event.getX(1);
                    preEventCoor[2] = event.getY(0);
                    preEventCoor[3] = event.getY(1);
                    float currentMove = calSpacing(event);
                    currentMatrix.set(savaedMatrix);
                    /**
                     * 指尖移动距离大于10F缩放
                     */
                    if (currentMove > 10F) {

                        float scale = currentMove / preMove;
                        currentMatrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    /**
                     * 保持两点时旋转
                     */
                    if (preEventCoor != null) {
                        rotate = calRotation(event);
                        float r = rotate - savaRotate;
                        currentMatrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                    }
                }
                break;
        }
        setImageMatrix(currentMatrix);
        return true;
    }

    /**
     * 计算两个触摸点的中点坐标
     *
     * @param mid
     * @param event
     */
    private void calMidPoint(PointF mid, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        mid.set(x / 2, y / 2);
    }

    /**
     * 计算两个触摸点之间距离
     *
     * @param event
     * @return
     */
    private float calSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算旋转的角度
     *
     * @param event
     * @return
     */
    private float calRotation(MotionEvent event) {
        double deltaX = (event.getX(0) - event.getX(1));
        double deltaY = (event.getY(0) - event.getY(1));
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = oldw;
        height = oldh;
        super.onSizeChanged(w, h, oldw, oldh);
    }
}



































