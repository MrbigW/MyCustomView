package com.atsgg.youku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    // 三级菜单的显示与隐藏的标记
    private boolean level1IsShowing = true;
    private boolean level2IsShowing = true;
    private boolean level3IsShowing = true;

    private RelativeLayout level2;
    private RelativeLayout level1;
    private RelativeLayout level3;

    private ImageView icon_menu;
    private ImageView icon_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        level1 = (RelativeLayout)findViewById(R.id.level1);
        level2 = (RelativeLayout)findViewById(R.id.level2);
        level3 = (RelativeLayout)findViewById(R.id.level3);

        icon_menu = (ImageView)findViewById(R.id.icon_menu);
        icon_home = (ImageView)findViewById(R.id.icon_home);

        icon_home.setOnClickListener(new MyOnclickListener());
        icon_menu.setOnClickListener(new MyOnclickListener());

    }

    class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case  R.id.icon_home:
                    if(level2IsShowing) {
                        Tools.hideView(level2);
                        level2IsShowing = false;
                        if(level3IsShowing) {
                            Tools.hideView(level3,50);
                            level3IsShowing = false;
                        }
                    }else {
                        Tools.showView(level2);
                        level2IsShowing = true;
                    }
                    break;
                case  R.id.icon_menu:
                    if(level3IsShowing) {
                        Tools.hideView(level3);
                        level3IsShowing = false;
                    }else {
                        Tools.showView(level3);
                        level3IsShowing = true;
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME) {
            if(level1IsShowing) {
                level1IsShowing = false;
                Tools.hideView(level1);
                if(level2IsShowing) {
                    level2IsShowing = false;
                    Tools.hideView(level2,50);
                    if(level3IsShowing) {
                        level3IsShowing = false;
                        Tools.hideView(level3,100);
                    }
                }
            }else {
                level1IsShowing = true;
                Tools.showView(level1);

                level2IsShowing = true;
                Tools.showView(level2,50);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
