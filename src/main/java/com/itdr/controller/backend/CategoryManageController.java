package com.itdr.controller.backend;

import com.itdr.common.Const;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.UserInfo;
import com.itdr.service.ICategoruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {
    @Autowired
    ICategoruService categoruService;
    /**
     * 获取品类子节点（平级）
     * */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(Integer categoryId){
        return categoruService.get_category(categoryId);
    }
    @RequestMapping(value = "/get_category/{categoryId}")
    public ServerResponse get_category_restful(@PathVariable("categoryId") Integer categoryId){
        return categoruService.get_category(categoryId);
    }

    /**
     * 增加节点
     * */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(@RequestParam(required = false,defaultValue = "0") Integer parentId,
                                       String categoryName){

        return categoruService.add_category(parentId,categoryName);
    }

    /**
     * 修改节点
     * */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse set_category_name(Integer categoryId,
                                            String categoryName){
        return categoruService.set_category_name(categoryId,categoryName);
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(Integer categoryId){
        return categoruService.get_deep_category(categoryId);
    }
}