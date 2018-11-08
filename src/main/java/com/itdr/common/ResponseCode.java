package com.itdr.common;
/*
* 维护状态码的
* */
public enum ResponseCode {
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ROLE_ADMIN(0,"管理员"),
    ROLE_CUSTOMER(1,"普通用户"),
    NO_PRIVILEGE(3,"无权限操作"),
    PRODUCT_ONLINE(1,"在售"),
    PRODUCT_OFFLINE(2,"下架"),
    PRODUCT_DELETE(3,"删除"),
    PRODUCT_CHECKED(1,"已勾选"),
    PRODUCT_UMCHECKED(0,"未勾选"),
    ;
    private final int code;
    private final String desc;

    ResponseCode(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public enum Order{
        ORDER_CANCELED(0,"已取消"),
        ORDER_UN_PAY(10,"未付款"),
        ORDER_PAYED(20,"已付款"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_FAIL(60,"交易失败"),
        ONLINE(1,"线上支付")
        ;
        private final int code;
        private final String desc;

        Order(int code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static Order codeOf(Integer code){
            for(Order order
                    :values()){
                if(code == order.getCode()){
                    return order;
                }
            }
            return null;
        }
    }
}
