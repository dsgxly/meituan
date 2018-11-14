package com.itdr.controller.common.interceptor;

import com.google.gson.Gson;
import com.itdr.common.Const;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class AuthorityInterceptor implements HandlerInterceptor {
    @Autowired
    IUserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("---------preHandler---------");
/*        //拦截具体的方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String className = handlerMethod.getBean().getClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        if (className.equals("ProductManageController") && methodName.equals("set_sale_status")) {
            return false;
        }*/
        HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);

        if(userInfo==null){     //用户未登录,可以从cookie中获取token信息
            Cookie[] cookies = request.getCookies();
            if(cookies!=null&&cookies.length>0){
                for (Cookie cookie:cookies){
                    String cookieName = cookie.getName();
                    if(cookieName.equals(Const.AUTOLOGINTOKEN)){
                        String autoLoginToken = cookie.getValue();
                        //根据token查询用户信息
                        userInfo = userService.findUserInfoByToken(autoLoginToken);
                        if(userInfo!=null) {
                            session.setAttribute(Const.CURRENTUSER, userInfo);
                        }
                        break;
                    }
                }
            }
        }

        if (userInfo == null || userInfo.getRole() != ResponseCode.ROLE_ADMIN.getCode()) {
            //重构HTTPServerResponse
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            if (userInfo == null) {
                //未登录
                ServerResponse serverResponse = ServerResponse.createByError("用户未登录");
                Gson gson = new Gson();
                String json = gson.toJson(serverResponse);
                printWriter.write(json);
            } else {
                ServerResponse serverResponse = ServerResponse.createByError("无权限操作");
                Gson gson = new Gson();
                String json = gson.toJson(serverResponse);
                printWriter.write(json);
            }
            printWriter.flush();
            printWriter.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("---------postHandler---------");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("---------afterCompletion---------");
    }
}
