package com.atsgg.popupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView down_arrow;
    private EditText et_input;

    private PopupWindow mWindow;

    private ListView mListView;

    //  数据集合
    private ArrayList<String> mStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        down_arrow = (ImageView) findViewById(R.id.down_arrow);
        et_input = (EditText) findViewById(R.id.et_input);

        down_arrow.setOnClickListener(new MyOnClickListener());

        mListView = new ListView(this);
        mListView.setBackgroundResource(R.drawable.listview_background);

        mStrings = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            mStrings.add(i + "adjajdahdajsdh" + i);
        }

        mListView.setAdapter(new MyAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = mStrings.get(position);
                et_input.setText(msg);
                if (mWindow != null && mWindow.isShowing()) {
                    mWindow.dismiss();
                    mWindow = null;
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mStrings.size();
        }

        @Override
        public Object getItem(int position) {
            return mStrings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_lv_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                viewHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String msg = mStrings.get(position);
            viewHolder.tv_msg.setText(msg);

            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStrings.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_msg;
        ImageView iv_delete;
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.down_arrow:
                    if (mWindow == null) {
                        mWindow = new PopupWindow(MainActivity.this);
                        mWindow.setWidth(et_input.getWidth());
                        mWindow.setHeight(DensityUtil.dip2px(MainActivity.this, 200));
                        mWindow.setContentView(mListView);
                        //  设置PopupWindow焦点
                        mWindow.setFocusable(true);
                    }
                    mWindow.showAsDropDown(et_input, 0, 0);
                    break;
            }
        }
    }

}
