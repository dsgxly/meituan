package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Shopping;

public interface IShoppingService {

    /**
     * 添加收获地址
     * */

    ServerResponse add(Integer userId, Shopping shopping);

    /**
     * 删除收获地址
     * */
    ServerResponse del(Integer UserId, Integer shoppingId);

    /**
     * 登录状态更新地址
     * */
    ServerResponse update(Shopping shopping);

    /**
     * 选中查看具体地址
     * */
    ServerResponse select(Integer shoppingId);

    /**
     * 分页查询地址列表
     * */
    ServerResponse list(Integer pageNum, Integer pageSize);
}
