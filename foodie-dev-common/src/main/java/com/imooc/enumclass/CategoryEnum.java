package com.imooc.enumclass;

/**
 * 分类枚举
 */
public enum CategoryEnum {

    RootCategory(1,"一级分类"),
    TwoCategory(2,"二级分类"),
    ThreeCategory(3,"三级分类");

    public final Integer type;
    public final String value;

    CategoryEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
