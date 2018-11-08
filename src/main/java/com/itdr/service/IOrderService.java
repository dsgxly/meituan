package com.itdr.service;

import com.itdr.common.ServerResponse;

public interface IOrderService {
    /**
     * 创建订单
     * */
    ServerResponse create(Integer userId,Integer shoppingId);
}
