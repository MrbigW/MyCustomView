package com.atsgg.mycustomview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/** 根据页面改变设置文本  在onPageSelected()中
 *  添加指示点  selector、代码添加view
 *  支持左右无限滑动   设置count数目为最大数值
 *  自动滑动页面  handler消息机制
 *  当手滑动或者按下的时候停止滑动  onPageScrollStateChanged()与onTouch()
 *  添加点击事件 return false;
 */

public class MainActivity extends AppCompatActivity {

    public static final int MAX = Integer.MAX_VALUE;

    // 图片资源ID
    private final int[] imageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e};


    // 图片标题集合
    private final String[] imageDescriptions = {
            "尚硅谷拔河争霸赛！",
            "凝聚你我，放飞梦想！",
            "抱歉没座位了！",
            "7月就业名单全部曝光！",
            "平均起薪11345元"
    };

    private ViewPager vp_main_image;
    private TextView tv_main_title;
    private LinearLayout ll_main_point;
    private ArrayList<ImageView> mImageViews;

    //  上一次高亮显示的位置
    private int lastPosition;

    //  判断当前Activity是否销毁
    private boolean isActivityDestroy = false;

    //  判断是否为拖拽状态
    private boolean isDragging = false;

    //  使用Handler机制完成自动滑动
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int item = vp_main_image.getCurrentItem() + 1;
            vp_main_image.setCurrentItem(item);

            //利用自己给自己传消息来完成viewpager的循环滚动
            if (!isActivityDestroy) {
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        vp_main_image = (ViewPager) findViewById(R.id.vp_main_image);
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        ll_main_point = (LinearLayout) findViewById(R.id.ll_main_point);

        mImageViews = new ArrayList<>();

        for (int i = 0; i < imageIds.length; i++) {
            ImageView imag = new ImageView(this);
            imag.setBackgroundResource(imageIds[i]);
            mImageViews.add(imag);


            //  添加指示点，有多少个页面就添加多少个
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(this, 8), DensityUtil.dip2px(this, 8));

            point.setBackgroundResource(R.drawable.point_selector);

            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(this, 10);
                point.setEnabled(false);
            }

            point.setLayoutParams(params);
            ll_main_point.addView(point);
        }

        tv_main_title.setText(imageDescriptions[0]);
        vp_main_image.addOnPageChangeListener(new MyonPageChangeListener());
        ll_main_point.getChildAt(0).setEnabled(true);
        //  设置一开始就可以向左滑动
        int item = MAX / 2 - (MAX / 2) % mImageViews.size();
        vp_main_image.setCurrentItem(item); // 该方法是设置viewpager显示的位置

        vp_main_image.setAdapter(new MyPagerAdapter());

        //  利用handler开始循环滑动
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    class MyonPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动了的时候回调这个方法(必须掌握)
         *
         * @param position             滚动页面的位置
         * @param positionOffset       当前滑动页面的百分比，例如滑动一半是0.5
         * @param positionOffsetPixels 当前页面滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当页面改变了的时候回调这个方法
         *
         * @param position 当前被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int nowPosition = position % mImageViews.size();
            String title = imageDescriptions[nowPosition];
            tv_main_title.setText(title);
            ll_main_point.getChildAt(nowPosition).setEnabled(true);
            ll_main_point.getChildAt(lastPosition).setEnabled(false);
            lastPosition = nowPosition;
        }

        /**
         * 当页面状态发送变化的时候回调这个方法
         * 静止到滑动
         * 滑动到静止
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            //  如果当前是拖拽状态
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isDragging = true;
                handler.removeMessages(0);
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                //  如果当前是空闲状态
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
                //  如果当前是滑动状态
                isDragging = false;
                handler.removeMessages(0);
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        /**
         * 销毁
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);  该语句必须删除
            container.removeView((View) object);
        }

        /**
         * 作用类似于getView();
         * 把页面添加到ViewPager中
         * 并且返回当前页面的相关的特性
         * container:容器就是ViewPager自身
         * position：实例化当前页面的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView image = mImageViews.get(position % mImageViews.size());
            container.addView(image);

            //  对imageview进行触摸和点击监听
            image.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeMessages(0);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.sendEmptyMessageDelayed(0, 3000);
                            break;
                    }
                    return false;
                }
            });

            image.setTag(position);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, imageDescriptions[((int) image.getTag()) % mImageViews.size()], Toast.LENGTH_SHORT).show();
                }
            });
            return image;
        }

        /**
         * 返回总条数
         *
         * @return
         */
        @Override
        public int getCount() {
//            return mImageViews.size();
            return MAX;
        }

        /**
         * view和object比较是否是同一个View
         * 如果相同返回true
         * 不相同返回false
         * object:其实就是instantiateItem()方法返回的值
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    protected void onDestroy() {
        isActivityDestroy = true;
        super.onDestroy();
    }
}
