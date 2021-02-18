package com.imooc.enumclass;

/**
 * 订单支付方式枚举
 */
public enum PayMethodEnum {

    WECHAT(1,"微信"),
    ALIPAY(2,"支付宝");

    public final Integer type;
    public final String value;

    PayMethodEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public static Boolean CodeBool(Integer type) {
        for (PayMethodEnum docType : PayMethodEnum.values()) {
            if (docType.type.equals(type)) {
                return true;
            }else {
                continue;
            }
        }
        return false;
    }
}
