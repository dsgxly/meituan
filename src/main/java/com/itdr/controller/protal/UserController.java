package com.itdr.controller.protal;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    IUserService userService;

    /**
     * 登录
     */

    /*
     *@RequestParam(value = "username",required = false, defaultValue = "zhangsan"
     * 调用接口中的方法时传的参数必须用@RequestParam，除非@RequestParam(value = "username") String username,相同时写简写
     * required = false,  可设置参数可有可无，默认为true，必须有参数
     * defaultValue = "zhangsan"  设置参数的默认值
     * */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, @RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password){
        ServerResponse serverResponse = userService.login(username,password);
        if(serverResponse.isSuccess()){     //登录成功
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }

        return serverResponse;
    }
    /**
     * 注册
     */
    @RequestMapping(value = "/register.do")
    public ServerResponse register(UserInfo userInfo){
        ServerResponse serverResponse = userService.register(userInfo);
        return  serverResponse;
    }

    /**
     * 根据用户名查询密保问题
     */
    @RequestMapping(value = "/forget_get_question.do")
    public ServerResponse forget_get_question(String username){
        ServerResponse serverResponse = userService.forget_get_question(username);
        return serverResponse;
    }

    /**
     * 提交问题答案，并生成token给客户端，防止横向越权
     */
    @RequestMapping(value = "/forget_check_answer.do")
    public ServerResponse forget_check_answer(String username,String question,String answer){
        ServerResponse serverResponse = userService.forget_check_answer(username,question,answer);
        return serverResponse;
    }
    /**
        * 忘记密码重置密码
     */
    @RequestMapping(value = "/forget_reset_password.do")
    public ServerResponse forget_reset_password(String username,String newpassword,String forgetToken){
        ServerResponse serverResponse = userService.forget_reset_password(username,newpassword,forgetToken);

        return serverResponse;
    }
    /**
     * 检查用户名或者邮箱是否有效
     */
    @RequestMapping(value = "check_valid.do")
    public ServerResponse check_valid(String str,String type){
        ServerResponse serverResponse = userService.check_valid(str,type);

        return serverResponse;
    }

    /**
     * 获取用户登录信息
     */
    @RequestMapping(value = "get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("用户未登录");
        }
        userInfo.setPassword("");
        UserInfo newuserinfo = new UserInfo();
        newuserinfo.setId(userInfo.getId());
        newuserinfo.setUsername(userInfo.getUsername());
        newuserinfo.setPhone(userInfo.getPhone());
        newuserinfo.setEmail(userInfo.getEmail());
        newuserinfo.setQuestion(userInfo.getQuestion());
        newuserinfo.setAnswer(userInfo.getAnswer());
        newuserinfo.setCreateTime(userInfo.getCreateTime());
        newuserinfo.setUpdateTime(userInfo.getUpdateTime());
        return ServerResponse.createBySuccess(newuserinfo);
    }


    /**
     * 登录状态下重置密码
     */
    @RequestMapping(value = "reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordOld, String passwordNew){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("用户未登录");
        }
        return userService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
    }

    /**
     * 登录状态下更新用户信息
     */
    @RequestMapping(value = "update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("用户未登录");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse =  userService.update_information(user);
        if(serverResponse.isSuccess()){
            //更新session中的用户信息
            UserInfo userInfo1 = userService.findUserInfoByUserid(userInfo.getId());
            session.setAttribute(Const.CURRENTUSER,userInfo1);
        }
        return serverResponse;
    }


    /**
     * 获取登录用户的详细信息
     */
    @RequestMapping(value = "get_information.do")
    public ServerResponse get_information(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("用户未登录");
        }
        userInfo.setPassword("");
        return ServerResponse.createBySuccess(userInfo);
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.createBySuccess();
    }
}
