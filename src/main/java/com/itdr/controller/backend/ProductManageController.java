package com.itdr.controller.backend;

import com.itdr.common.Const;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Product;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @Autowired
    IProductService productService;

    /**
     * 新增或者更新产品
     * */
    @RequestMapping(value = "/save.do")
    public ServerResponse saveOrUpdate( Product product){
        ServerResponse serverResponse = productService.saveOrUpdate(product);
        return serverResponse;
    }

    /**
     * 产品上下架
     * */
    @RequestMapping(value = "/set_sale_status.do")
    public ServerResponse set_sale_status(Integer productId,Integer status){
        ServerResponse serverResponse = productService.set_sale_status(productId,status);
        return serverResponse;
    }

    /**
     * 查看商品详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(Integer productId){
        ServerResponse serverResponse = productService.detail(productId);
        return serverResponse;
    }

    /**
     * 查看商品列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(value = "pageNum" ,required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize" ,required = false,defaultValue = "5") Integer pageSize){


        ServerResponse serverResponse = productService.list(pageNum,pageSize);
        return serverResponse;
    }

    /**
     * 产品搜索
     * */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(@RequestParam(value = "productId" ,required = false) Integer productId,
                                 @RequestParam(value = "productName" ,required = false) String productName,
                                 @RequestParam(value = "pageNum" ,required = false,defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize" ,required = false,defaultValue = "5") Integer pageSize){

        ServerResponse serverResponse = productService.search(productId,productName,pageNum,pageSize);
        return serverResponse;
    }
}
