package com.itdr.controller.protal;

import com.itdr.common.ServerResponse;
import com.itdr.service.IProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    IProductService productService;

    /**
     * 前台查看商品详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail_protal(Integer productId){
        return productService.detail_protal(productId);
    }

    /**
     * 前台-搜索商品并排序
     **/
    @RequestMapping(value = "/list.do")
    public ServerResponse list_protal(@RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "5") Integer pageSize,
                               @RequestParam(required = false,defaultValue = "") String orderby ){


        return productService.list_protal(categoryId,keyword,pageNum,pageSize,orderby);
    }
}
