package com.itdr.controller.backend;

import com.itdr.common.Const;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/order")
public class OrderManageController {
    @Autowired
    IOrderService orderService;
    /**
     * 订单列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize ){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=ResponseCode.ROLE_ADMIN.getCode()){
            return ServerResponse.createByError(ResponseCode.NO_PRIVILEGE.getCode(),ResponseCode.NO_PRIVILEGE.getDesc());
        }
        return orderService.list(userInfo.getId(),pageNum,pageSize);

    }

    /**
     * 订单发货
     * */
    @RequestMapping(value = "/send_goods.do")
    public ServerResponse send_goods(HttpSession session,Long orderNo){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if(userInfo.getRole()!=ResponseCode.ROLE_ADMIN.getCode()){
            return ServerResponse.createByError(ResponseCode.NO_PRIVILEGE.getCode(),ResponseCode.NO_PRIVILEGE.getDesc());
        }
        return orderService.send_goods(orderNo);

    }
}
