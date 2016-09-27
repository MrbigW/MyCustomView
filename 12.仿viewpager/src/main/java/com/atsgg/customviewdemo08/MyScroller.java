package com.atsgg.customviewdemo08;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Mr.W on 2016/9/23.
 */

public class MyScroller {

    private float startX;
    private int distanceX;

    private long startTime;// 记录开始的时间

    /**
     * 标记是否移动完成
     * true：移动结束
     * false：还在移动
     */
    private boolean isFinish = false;

    private long totalTime = 300;// 总时间
    private float currX; // 移动那一小段后对应的坐标

    /**
     * 得到移动那一小段后对应的坐标
     *
     * @return
     */
    public float getCurrX() {
        return currX;
    }


    /**
     * 开始计时
     *
     * @param startX
     * @param startY
     * @param distanceX 在x轴要移动的距离
     * @param distanceY 在y轴要移动的距离
     */
    public void startScroll(float startX, float startY, int distanceX, int distanceY) {
        this.startX = startX;
        this.distanceX = distanceX;
        startTime = SystemClock.uptimeMillis();
        this.isFinish = false;
    }

    /**
     * 是否移动
     * true:在移动
     * false:停止移动
     *
     * @return
     */
    public boolean computeScrollOffset() {
        if (isFinish) {
            return false;
        }

        long endTime = SystemClock.uptimeMillis();// 结束时间
        long passTime = endTime - startTime;// 移动时间
        Log.e("TAG", passTime + "");
        if (passTime < totalTime) {
            float distanceSmallX = passTime * distanceX / totalTime;
            currX = startX + distanceSmallX;
        } else {
            currX = startX + distanceX;
            //  移动结束
            isFinish = true;
        }

        return true;
    }
}
