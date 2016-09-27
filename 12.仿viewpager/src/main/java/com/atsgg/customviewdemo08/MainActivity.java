package com.atsgg.customviewdemo08;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    int[] imageIds = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};

    private MyCustomViewPager myViewPager;

    private RadioGroup rg_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        myViewPager = (MyCustomViewPager) findViewById(R.id.myViewPager);


        for (int i = 0; i < imageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(imageIds[i]);
            myViewPager.addView(image);
        }

        View textView = View.inflate(this,R.layout.test,null);
        myViewPager.addView(textView,3);

        for (int i = 0; i < myViewPager.getChildCount(); i++) {
            RadioButton mRaBtn = new RadioButton(this);
            mRaBtn.setId(i);
            if (i != 0) {
                mRaBtn.setChecked(false);
            } else {
                mRaBtn.setChecked(true);
            }
            rg_main.addView(mRaBtn);
        }

        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myViewPager.moveToPager(checkedId);
            }
        });

        myViewPager.setOnPagerChangeListener(new MyCustomViewPager.onPagerChangeListener() {
            @Override
            public void onChangePager(int index) {
                rg_main.check(index);
            }
        });



    }
}
