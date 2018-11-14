package com.itdr.controller.protal;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Shopping;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/shopping")
public class ShoppingController {

    @Autowired
    IShoppingService shoppingService;

    /**
     * 添加收获地址
     * */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Shopping shopping){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        shopping.setUserId(userInfo.getId());
        return shoppingService.add(userInfo.getId(),shopping);
    }
    /**
     * 删除收获地址
     * */
    @RequestMapping(value = "/del.do")
    public ServerResponse del(HttpSession session, Integer shoppingId){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return shoppingService.del(userInfo.getId(),shoppingId);
    }
    @RequestMapping(value = "/del/shoppingId/{shoppingId}")
    public ServerResponse del_restful(HttpSession session, @PathVariable("shoppingId") Integer shoppingId){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return shoppingService.del(userInfo.getId(),shoppingId);
    }

    /**
     * 登录状态更新地址
     * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session, Shopping shopping){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }
        shopping.setUserId(userInfo.getId());
        return shoppingService.update(shopping);
    }

    /**
     * 选中查看具体地址
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session, Integer shoppingId){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return shoppingService.select(shoppingId);
    }
    @RequestMapping(value = "/select/shoppingId/{shoppingId}")
    public ServerResponse select_restful(HttpSession session, @PathVariable("shoppingId") Integer shoppingId){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return shoppingService.select(shoppingId);
    }


    /**
     * 分页查询地址列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "5") Integer pageSize){

        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.createByError("请登录");
        }

        return shoppingService.list(pageNum,pageSize);
    }
}
