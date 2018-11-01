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
    NO_PRIVILEGE(3,"无权限操作")
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
}
