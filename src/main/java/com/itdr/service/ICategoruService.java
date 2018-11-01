package com.itdr.service;

import com.itdr.common.ServerResponse;

public interface ICategoruService {
    /**
     * 获取品类的子节点
     * */
    ServerResponse get_category(Integer categoryId);

    /**
     * 增加节点
     * */
    ServerResponse add_category(Integer parentId, String categoryName);

    /**
     * 修改节点
     * */
    ServerResponse set_category_name(Integer categoryId, String categoryName);

    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    ServerResponse get_deep_category(Integer categoryId);
}
