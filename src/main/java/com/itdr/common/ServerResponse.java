package com.itdr.common;
/**
 * 服务端返回到前端的高复用的响应对象
 * */
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)  //值为null的时候不会放到序列化里面
public class ServerResponse<T> implements Serializable  {

    private int status;     //返回到前端的状态码
    private String msg;     //当status!=0时，封装了错误信息
    private T data;         //返回给前端的数据

    private ServerResponse(){

    }
    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse (int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,T data){
        this.status=status;
        this.data=data;
    }
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    //判断接口返回数据是否成功，，当status的值为0时成功
    @JsonIgnore     //序列化是会当成一个属性，而我们并不需要
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }

    /*
    *调用接口成功时的回调
    * */
    public static <T> ServerResponse createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createBySuccess(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    /*
     *调用接口失败时的回调
     * */
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }
    public static <T> ServerResponse<T> createByError(int status){
        return new ServerResponse<T>(status);
    }
    public static <T> ServerResponse<T> createByError(String msg){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),msg);
    }
    public static <T> ServerResponse<T> createByError(int status,String msg){
        return new ServerResponse<T>(status,msg);
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
/*
    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static void main(String[] args) {
      //ServerResponse serverResponse = new ServerResponse(0,new Object());
        ServerResponse serverResponse=ServerResponse.createBySuccess(null,"hello");
        System.out.println(serverResponse);

    }*/
}
