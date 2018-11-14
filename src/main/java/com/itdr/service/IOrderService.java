package com.itdr.service;

import com.itdr.common.ServerResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface IOrderService {
    /**
     * 创建订单
     * */
    ServerResponse create(Integer userId,Integer shoppingId);

    /**
     * 取消订单
     * */
    ServerResponse cancel(Integer userId, Long orderNo);

    /**
     * 获取购物车中的商品信息
     * */
    ServerResponse get_order_cart_product(Integer id);



    /**
     * 获取订单列表
     * */
    ServerResponse list(Integer id, Integer pageNum, Integer pageSize);

    /**
     * 获取订单详情
     * */
    ServerResponse detail(Integer id, Long orderNo);

    /**
     * 后台-订单发货
     * */
    ServerResponse send_goods(Long orderNo);


    /**
     * 支付接口
     * */
    ServerResponse pay(Integer id, Long orderNo);




    /*    *//**
     * 后台-订单列表
     * *//*
    ServerResponse list_backend(Integer pageNum, Integer pageSize);*/


    /**
     *支付宝回调
     **/
    ServerResponse alipay_callback(Map<String,String> requestparam);

    //查询订单支付状态
    ServerResponse query_order_pay_status(Long orderNo);

    void closeOrder(String date);
}
