package com.atsgg.customviewdemo12;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private int imagIds[] = new int[]{R.drawable.a1, R.drawable.b2,
            R.drawable.c3, R.drawable.d4, R.drawable.e5,
            R.drawable.f6, R.drawable.j7};

    private ListView listview_01;
    private ListView listview_02;
    private ListView listview_03;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview_01 = (ListView) findViewById(R.id.listview_01);
        listview_02 = (ListView) findViewById(R.id.listview_02);
        listview_03 = (ListView) findViewById(R.id.listview_03);

        listview_01.setAdapter(new MyImageAdapter());
        listview_02.setAdapter(new MyImageAdapter());
        listview_03.setAdapter(new MyImageAdapter());

    }

    class MyImageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 2000;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image = (ImageView) View.inflate(getApplicationContext(),R.layout.item_ls_layout,null);
            int resId = (int) (Math.random()*7);
            image.setImageResource(imagIds[resId]);
            return image;
        }
    }
}
