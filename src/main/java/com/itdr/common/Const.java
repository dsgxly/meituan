package com.itdr.common;

public class Const {
    public static final String CURRENTUSER="currentuser";
    public static final String TRADE_SUCCESS="TRADE_SUCCESS";
    public enum PaymentPlatformEnum{
        ALIPAY(1,"支付宝")
        ;
        private int code;
        private String desc;

        PaymentPlatformEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
