package com.itdr.service;

import com.itdr.common.ServerResponse;

public interface ICartService {

    /**
     * 购物车中添加商品
     * */
    ServerResponse add(Integer userId,Integer productId,Integer count);

    /**
     * 购物车列表
     * */
    ServerResponse list(Integer userId);

    /**
     * 更新购物车中某个产品数量
     * */
    ServerResponse update(Integer UserId, Integer productId, Integer count);

    /**
     * 移除购物车中某个产品
     * */
    ServerResponse delete_product(Integer userId, String productIds);

    /**
     * 购物车中选中或取消选中某个商品
     * */
    ServerResponse select(Integer id, Integer productId,Integer check);

    /**
     * 查询购物车产品数量
     * */
    ServerResponse get_cart_product_count(Integer userId);
}
