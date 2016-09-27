package com.atsgg.customviewdemo11;

/**
 * Created by 那位程序猿Mr.W on 2016/9/25.
 * 微信:1024057635
 */
public class Person {
    private String name;
    private String pinyin;

    public Person(String name) {
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
