package com.itdr.controller.protal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    IOrderService orderService;
    /**
     * 创建订单
     * */
    @RequestMapping(value = "/create.do")
    public ServerResponse createOrder(HttpSession session,Integer shoppingId){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return orderService.create(userInfo.getId(),shoppingId);
    }

    /**
     * 取消订单
     * */
    @RequestMapping(value = "/cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return orderService.cancel(userInfo.getId(),orderNo);
    }

    /**
     * 获取购物车中的商品信息
     * */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return orderService.get_order_cart_product(userInfo.getId());
    }

    /**
     * 获取订单列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "5") Integer pageSize){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return orderService.list(userInfo.getId(),pageNum,pageSize);
    }
    /**
     * 获取订单详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return orderService.detail(userInfo.getId(),orderNo);
    }

    /**
     * 支付接口
     * */
    @RequestMapping(value = "/pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return orderService.pay(userInfo.getId(),orderNo);
    }

    /**
     * 支付宝服务器回调应用服务器
     * */
    @RequestMapping(value = "/alipay_callback.do")
    public ServerResponse callback(HttpServletRequest request){

        System.out.println("支付宝服务器回调应用服务器接口");

        Map<String,String[]> params = request.getParameterMap();
        Map<String ,String > requestparam = Maps.newHashMap();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            String[] strArr = params.get(key);
            String value="";
            for (int i = 0;i<strArr.length;i++){
                value = (i==strArr.length-1)?value+strArr[i]:value+strArr[i]+",";
            }
            requestparam.put(key,value);
        }
        Iterator<String> it1 = requestparam.keySet().iterator();
        while(it.hasNext()){
            String key = it1.next();
            String value = requestparam.get(key);
            System.out.println(key+":"+value);
        }
        System.out.println("************************");
        //step1:支付宝验签
        try {
            requestparam.remove("sign_type");

            boolean result = AlipaySignature.rsaCheckV2(requestparam, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if (!result){
                return ServerResponse.createByError("非法请求，验证不通过");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //step2:处理业务逻辑
        return orderService.alipay_callback(requestparam);
    }

    /**
     * 查询订单支付状态
     * */
    @RequestMapping(value = "/query_order_pay_status.do")
    public ServerResponse query_order_pay_status(HttpSession session, Long orderNo){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo == null){
            return ServerResponse.createBySuccess("需要登录");
        }

        return orderService.query_order_pay_status(orderNo);
    }
}
