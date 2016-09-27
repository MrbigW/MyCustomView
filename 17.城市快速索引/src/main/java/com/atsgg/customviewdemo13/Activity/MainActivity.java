package com.atsgg.customviewdemo13.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atsgg.customviewdemo13.Bean.City;
import com.atsgg.customviewdemo13.R;
import com.atsgg.customviewdemo13.View.IndexView;
import com.atsgg.customviewdemo13.View.MySearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity implements MySearchView.SearchViewListener {

    // 89765faf5bd6b40292d70c26d0e4b53c
    private static final String URLPATH = "http://web.juhe.cn:8080/environment/air/airCities?key=3edb65839dfd99d303f5a08492e345f5";


    private ListView ls_main_name;
    private TextView tv_main_word;
    private IndexView iv_main_words;

    private Handler mHandler;
    private ArrayList<City> mCities;

    private MySearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    /**
     * 对word进行更新
     *
     * @param word
     */
    private void onUpdateWord(String word) {
        for (int i = 0; i < mCities.size(); i++) {
            String listWord = mCities.get(i).getPinyin().substring(0, 1);
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

//    private String cityName = "";

    /**
     * 当搜索框 文本改变时 触发的回调
     *
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        Log.e("222", text);
        String newText = text.toUpperCase();
        for (int i = 0; i < mCities.size(); i++) {
            if (newText.equals(mCities.get(i).getPinyin().substring(0, 1))) {
                ls_main_name.setSelection(i);
                break;
            }
        }
        tv_main_word.setVisibility(View.VISIBLE);
        tv_main_word.setText(newText);
        mHandler.removeCallbacksAndMessages(null);// 移除所有消息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_main_word.setVisibility(View.GONE);
            }
        }, 1000);

        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {

    }


    class MyNameAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCities.size();
        }

        @Override
        public Object getItem(int position) {
            return mCities.get(position);
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
            String word = mCities.get(position).getPinyin().substring(0, 1);
            String name = mCities.get(position).getName();
            mHolder.tv_lv_name.setText(name);
            mHolder.tv_lv_word.setText(word);

            if (position == 0) {
                mHolder.tv_lv_word.setVisibility(View.VISIBLE);
            } else {
                // 得到上一条的首字母
                String preWord = mCities.get(position - 1).getPinyin().substring(0, 1);
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
        mSearchView = (MySearchView) findViewById(R.id.main_search_layout);
        ls_main_name = (ListView) findViewById(R.id.ls_main_name);
        tv_main_word = (TextView) findViewById(R.id.tv_main_word);
        iv_main_words = (IndexView) findViewById(R.id.iv_main_words);
        mHandler = new Handler();
        mCities = new ArrayList<>();
    }

    /**
     * 通过Url将返回的数据解析成json格式数据，并转换为我们所封装的NewsBean
     *
     * @param Url
     * @return
     */
    private ArrayList<City> getJsonData(String Url) {
        ArrayList<City> newsList = new ArrayList<>();
        try {

            String jsonString = readStream(new URL(Url).openStream());


            //  Gson截下Json,需要变量名一一对应
//            Gson gson = new Gson();
//            gson.fromJson(jsonString,new TypeToken<NewsBean>(){}.getType());

//              原生API解析Json
            //  得到返回的JsonObj;
            JSONObject jsonObject = new JSONObject(jsonString);

            //  得到JsonObj对象中data的JsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            //  得到JsonArray中的数据并将其赋给NewsBean对象
            City citybean;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                citybean = new City(jsonObject.getString("name"));
                newsList.add(citybean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsList;
    }

    /**
     * 将从服务器得到的输入流传进来读取
     *
     * @param is
     * @return
     */
    private String readStream(InputStream is) {
        InputStreamReader isr = null;
        StringBuilder sb = new StringBuilder("");
        BufferedReader br = null;
        String line = "";
        try {
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        new AsyncTask<String, Void, ArrayList<City>>() {


            @Override
            protected ArrayList<City> doInBackground(String... params) {
                return getJsonData(params[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<City> cities) {
                super.onPostExecute(cities);
                mCities = cities;

                /**
                 * 排序
                 */
                Collections.sort(mCities, new Comparator<City>() {
                    @Override
                    public int compare(City lhs, City rhs) {
                        return lhs.getPinyin().compareTo(rhs.getPinyin());
                    }
                });
                iv_main_words.setOnWordChangeListener(new IndexView.onWordChangeListener() {
                    @Override
                    public void onWordChange(String word) {

                        onUpdateNotice(word);

                        onUpdateWord(word);
                    }
                });

                ls_main_name.setAdapter(new MyNameAdapter());
                mSearchView.setSearchViewListener(MainActivity.this);


            }
        }.execute(URLPATH);

    }
}
