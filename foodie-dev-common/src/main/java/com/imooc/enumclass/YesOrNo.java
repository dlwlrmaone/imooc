package com.imooc.enumclass;

/**
 * 是否枚举
 */
public enum YesOrNo {

    YES(0,"是"),
    NO(1,"否");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
