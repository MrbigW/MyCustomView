package com.atsgg.customviewdemo14;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv_main_content;
    private List<DataBean> mBeanList;
    private MyDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();


        mAdapter = new MyDataAdapter();
        lv_main_content.setAdapter(mAdapter);

    }

    private void initView() {
        lv_main_content = (ListView) findViewById(R.id.lv_main_content);
        mBeanList = new ArrayList<>();
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mBeanList.add(new DataBean("Content" + i));
        }
    }

    // 记录位置

    class MyDataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder mHolder;

            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_lv_main, null);
                mHolder.item_lv_content = (TextView) convertView.findViewById(R.id.item_lv_content);
                mHolder.item_lv_delete = (TextView) convertView.findViewById(R.id.item_lv_delete);
                mHolder.item_lv_top = (TextView) convertView.findViewById(R.id.item_lv_top);

                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            final DataBean bean = mBeanList.get(position);
            mHolder.item_lv_content.setText(bean.getName());

            /**
             * 实现条目内容点击事件
             */
            mHolder.item_lv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "postion = " + bean.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            /**
             * 实现条目删除功能
             */
            mHolder.item_lv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MySlideLayout slideLayout = (MySlideLayout) v.getParent();
                    slideLayout.closeMenu();
                    mBeanList.remove(position);
                    notifyDataSetChanged();
                }
            });


            /**
             * 实现置顶功能
             */
            mHolder.item_lv_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MySlideLayout slideLayout = (MySlideLayout) v.getParent();
                    slideLayout.closeMenu();
                    // 获取当前位置databean
                    final DataBean bean = mBeanList.get(position);
                    // 移除当前位置
                    mBeanList.remove(position);
                    // 获得headView
                    final View head = View.inflate(MainActivity.this, R.layout.item_lv_main, null);
                    head.setMinimumHeight(DensityUtil.dip2px(MainActivity.this, 60));
                    head.setBackgroundColor(0xffeeeeee);
                    head.setTag(position);
                    // 更改Content内容与top的内容
                    TextView headContent = (TextView) head.findViewById(R.id.item_lv_content);
                    TextView headTop = (TextView) head.findViewById(R.id.item_lv_top);
                    headTop.setText("取消置顶");
                    headTop.setTextSize(DensityUtil.dip2px(MainActivity.this, 5));
                    headContent.setText("这是被置顶的" + bean.getName());

                    /**
                     * 实现取消置顶的功能
                     */
                    headTop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (int) head.getTag();
                            mBeanList.add(position, bean);
                            lv_main_content.removeHeaderView(head);
                        }
                    });
                    TextView headDelete = (TextView) head.findViewById(R.id.item_lv_delete);

                    /**
                     * 实现置顶条目删除功能
                     */
                    headDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lv_main_content.removeHeaderView(head);
                        }
                    });

                    lv_main_content.addHeaderView(head);

                    notifyDataSetChanged();
                }
            });

            // 对该item设置监听
            MySlideLayout mSlide = (MySlideLayout) convertView;
            mSlide.setOnStateChangeListener(new MyOnStateChangeListener());

            return convertView;
        }
    }

    static class ViewHolder {
        TextView item_lv_content, item_lv_delete, item_lv_top;
    }

    private MySlideLayout openedLayout;

    class MyOnStateChangeListener implements MySlideLayout.onStateChangeListener {
        @Override
        public void onDrown(MySlideLayout layout) {
            if (openedLayout != null && openedLayout != layout) {
                openedLayout.closeMenu();
            }
        }

        @Override
        public void onOpen(MySlideLayout layout) {
            openedLayout = layout; // 记录，变化在下个item 被Down的时候关闭打开的
        }

        @Override
        public void onClose(MySlideLayout layout) {
            if (openedLayout == layout) {
                openedLayout = null; // 释放
            }
        }
    }

}
