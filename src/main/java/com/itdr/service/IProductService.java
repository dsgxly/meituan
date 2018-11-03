package com.itdr.service;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    /**
     * 新增或者更新产品
     * */
    ServerResponse saveOrUpdate(Product product);

    /**
     * 产品上下架
     * productId  商品Id
     * status      商品的状态
     * */
    ServerResponse set_sale_status(Integer productId, Integer status);

    /**
     * 查看后台商品详情
     * */
    ServerResponse detail(Integer productId);

    /**
     * 查看商品列表
     * */
    ServerResponse list(Integer pageNum, Integer pageSize);

    /**
     * 产品搜索
     * */
    ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize);

    /**
     * 图片上传
     * */
    ServerResponse upload(MultipartFile file, String path);

    /**
     * 查看前台商品详情
     * */
    ServerResponse detail_protal(Integer productId);

    /**
     * 前台商品搜索
     * */
    ServerResponse list_protal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderby);
}
