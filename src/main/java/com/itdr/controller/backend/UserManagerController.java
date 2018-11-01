package com.itdr.controller.backend;

import com.itdr.common.Const;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台用户控制器
 * */

@RestController
@RequestMapping(value ="/mana/user")
public class UserManagerController {

    @Autowired
    IUserService userService;

    @RequestMapping(value ="/login.do")
    public ServerResponse login(HttpSession session,String username,String password){

        ServerResponse serverResponse = userService.login(username,password);
        if(serverResponse.isSuccess()){     //登录成功
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            if(userInfo.getRole()==ResponseCode.ROLE_CUSTOMER.getCode()){
                return ServerResponse.createByError("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }

}
