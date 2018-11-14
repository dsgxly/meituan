package com.itdr.controller.backend;

import com.itdr.common.Const;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IUserService;
import com.itdr.utils.IpUtils;
import com.itdr.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 后台用户控制器
 * */

@RestController
@RequestMapping(value ="/manage/user")
public class UserManagerController {

    @Autowired
    IUserService userService;

    @RequestMapping(value ="/login.do")
    public ServerResponse login(HttpSession session, String username, String password, HttpServletResponse response, HttpServletRequest request){

        ServerResponse serverResponse = userService.login(username,password);
        if(serverResponse.isSuccess()){     //登录成功
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            if(userInfo.getRole()==ResponseCode.ROLE_CUSTOMER.getCode()){
                return ServerResponse.createByError("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
            //生成AutoLoginToken
            String ip = IpUtils.getRemoteAddress(request);
            try {
                String mac =  IpUtils.getMACAddress(ip);
                String token = MD5Utils.getMD5Code(mac);
                userService.updateTokenByUserId(userInfo.getId(),token);
                Cookie autoLoginCookie = new Cookie(Const.AUTOLOGINTOKEN,token);
                autoLoginCookie.setMaxAge(7*60*60*24);
                autoLoginCookie.setPath("/");
                response.addCookie(autoLoginCookie);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return serverResponse;
    }

}
