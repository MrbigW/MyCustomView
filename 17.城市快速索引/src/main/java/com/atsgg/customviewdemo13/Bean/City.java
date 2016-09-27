package com.atsgg.customviewdemo13.Bean;

import com.atsgg.customviewdemo13.Utils.PinYinUtils;

/**
 * Created by 那位程序猿Mr.W on 2016/9/25.
 * 微信:1024057635
 */
public class City {


    private String name;
    private String pinyin;


    public City(String name) {
        this.name = name;
        this.pinyin = PinYinUtils.getPinYin(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPinyin() {
        return pinyin;
    }

}
