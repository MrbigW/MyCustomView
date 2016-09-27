package com.atsgg.customviewdemo11;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity {

    private ListView ls_main_name;
    private TextView tv_main_word;
    private IndexView iv_main_words;

    private Handler mHandler;
    private ArrayList<Person> mPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

        iv_main_words.setOnWordChangeListener(new IndexView.onWordChangeListener() {
            @Override
            public void onWordChange(String word) {

                onUpdateNotice(word);

                onUpdateWord(word);
            }
        });

        ls_main_name.setAdapter(new MyNameAdapter());

    }

    /**
     * 对word进行更新
     * @param word
     */
    private void onUpdateWord(String word) {
        for (int i = 0; i < mPersons.size(); i++) {
            String listWord = mPersons.get(i).getPinyin().substring(0, 1);
            if (word.equals(listWord)) {
                ls_main_name.setSelection(i);
                break;
            }
        }
    }

    /**
     * 对提示进行显示与消失操作
     */
    private void onUpdateNotice(String word) {
        tv_main_word.setVisibility(View.VISIBLE);
        tv_main_word.setText(word);
        mHandler.removeCallbacksAndMessages(null);// 移除所有消息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_main_word.setVisibility(View.GONE);
            }
        }, 1000);
    }

    class MyNameAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPersons.size();
        }

        @Override
        public Object getItem(int position) {
            return mPersons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_lv_layout, null);
                mHolder = new ViewHolder();
                mHolder.tv_lv_name = (TextView) convertView.findViewById(R.id.tv_lv_name);
                mHolder.tv_lv_word = (TextView) convertView.findViewById(R.id.tv_lv_word);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            //填充数据
            String word = mPersons.get(position).getPinyin().substring(0, 1);
            String name = mPersons.get(position).getName();
            mHolder.tv_lv_name.setText(name);
            mHolder.tv_lv_word.setText(word);

            if (position == 0) {
                mHolder.tv_lv_word.setVisibility(View.VISIBLE);
            } else {
                // 得到上一条的首字母
                String preWord = mPersons.get(position - 1).getPinyin().substring(0, 1);
                if (preWord.equals(word)) {
                    mHolder.tv_lv_word.setVisibility(View.GONE);
                } else {
                    mHolder.tv_lv_word.setVisibility(View.VISIBLE);
                }
            }


            return convertView;
        }
    }

    static class ViewHolder {
        private TextView tv_lv_word, tv_lv_name;
    }

    private void initView() {
        ls_main_name = (ListView) findViewById(R.id.ls_main_name);
        tv_main_word = (TextView) findViewById(R.id.tv_main_word);
        iv_main_words = (IndexView) findViewById(R.id.iv_main_words);
        mHandler = new Handler();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        mPersons = new ArrayList<>();
        mPersons.add(new Person("张晓飞"));
        mPersons.add(new Person("杨光福"));
        mPersons.add(new Person("胡继群"));
        mPersons.add(new Person("刘畅"));

        mPersons.add(new Person("钟泽兴"));
        mPersons.add(new Person("尹革新"));
        mPersons.add(new Person("安传鑫"));
        mPersons.add(new Person("张骞壬"));

        mPersons.add(new Person("温松"));
        mPersons.add(new Person("李凤秋"));
        mPersons.add(new Person("刘甫"));
        mPersons.add(new Person("娄全超"));
        mPersons.add(new Person("张猛"));

        mPersons.add(new Person("王英杰"));
        mPersons.add(new Person("李振南"));
        mPersons.add(new Person("孙仁政"));
        mPersons.add(new Person("唐春雷"));
        mPersons.add(new Person("牛鹏伟"));
        mPersons.add(new Person("姜宇航"));
        mPersons.add(new Person("胡凡"));
        mPersons.add(new Person("姚凯丽"));
        mPersons.add(new Person("李强"));
        mPersons.add(new Person("张海峰"));
        mPersons.add(new Person("孙含笑"));


        mPersons.add(new Person("刘挺"));
        mPersons.add(new Person("张洪瑞"));
        mPersons.add(new Person("张建忠"));
        mPersons.add(new Person("侯亚帅"));
        mPersons.add(new Person("刘帅"));

        mPersons.add(new Person("乔竞飞"));
        mPersons.add(new Person("徐雨健"));
        mPersons.add(new Person("吴亮"));
        mPersons.add(new Person("王兆霖"));

        mPersons.add(new Person("阿三"));


        /**
         * 排序
         */
        Collections.sort(mPersons, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });

    }
}
