package com.itdr.controller.protal;

import com.itdr.common.Const;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    ICartService cartService;

    /**
     * 购物车中添加商品
     * */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session,Integer productId,Integer count){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return cartService.add(userInfo.getId(),productId,count);
    }

    /**
     * 购物车列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.list(userInfo.getId()) ;
    }

    /**
     * 更新购物车中某个产品数量
     * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session,Integer productId,Integer count){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.update(userInfo.getId(),productId,count) ;
    }

    /**
     * 移除购物车中某个产品
     * */
    @RequestMapping(value = "/delete_product.do")
    public ServerResponse delete_product(HttpSession session,String productIds){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.delete_product(userInfo.getId(),productIds) ;
    }

    /**
     * 购物车中选中某个商品
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session,Integer productId){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.select(userInfo.getId(),productId,ResponseCode.PRODUCT_CHECKED.getCode()) ;
    }

    /**
     * 购物车中取消选中某个商品
     * */
    @RequestMapping(value = "/un_select.do")
    public ServerResponse un_select(HttpSession session,Integer productId){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.select(userInfo.getId(),productId,ResponseCode.PRODUCT_UMCHECKED.getCode()) ;
    }

    /**
     * 购物车中全选
     * */
    @RequestMapping(value = "/select_all.do")
    public ServerResponse select_all(HttpSession session){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.select(userInfo.getId(),null,ResponseCode.PRODUCT_CHECKED.getCode()) ;
    }
    /**
     * 购物车中取消全选
     * */
    @RequestMapping(value = "/un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.select(userInfo.getId(),null,ResponseCode.PRODUCT_UMCHECKED.getCode()) ;
    }

    /**
     * 查询购物车产品数量
     * */
    @RequestMapping(value = "/get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        return cartService.get_cart_product_count(userInfo.getId()) ;
    }
}
