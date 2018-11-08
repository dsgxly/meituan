package com.itdr.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.itdr.common.ServerResponse;
import com.itdr.dao.ShoppingMapper;
import com.itdr.pojo.Shopping;
import com.itdr.service.IShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShoppingServiceImpl implements IShoppingService {

    @Autowired
    ShoppingMapper shoppingMapper;

    @Override
    public ServerResponse add(Integer userId, Shopping shopping) {

        //1.参数非空校验

        if(shopping==null){
            return ServerResponse.createByError("参数不能为空");
        }
        //2.添加收货信息
        shopping.setId(userId);
        int result = shoppingMapper.insert(shopping);
        if(result<=0){
            return ServerResponse.createByError("新建地址失败");
        }
        Map<String ,Integer> map = Maps.newHashMap();
        map.put("shippingId",shopping.getId());
        return  ServerResponse.createBySuccess("新建地址成功",map);


    }

    @Override
    public ServerResponse del(Integer userId, Integer shoppingId) {
        //1.参数非空校验
        if(shoppingId==null){
            return ServerResponse.createByError("参数不能为空");
        }
        //2.根据shoppingId和userId删除地址
        int result = shoppingMapper.deleteByuserIdAndShoppingId(userId,shoppingId);
        //3.返回结果
       if(result>0){
           return ServerResponse.createBySuccess("删除地址成功");
       }
       return ServerResponse.createByError("删除地址失败");
    }

    @Override
    public ServerResponse update(Shopping shopping) {

        //1.非空判断
        if(shopping==null){
            return ServerResponse.createByError("参数不能为空");
        }
        //2.更新
        int result = shoppingMapper.updateBySelectiveKey(shopping);
        //3.返回结果
        if(result>0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByError("更新地址失败");
    }

    @Override
    public ServerResponse select(Integer shoppingId) {

        //1.非空判断
        if(shoppingId==null){
            return ServerResponse.createByError("参数不能为空");
        }
        //2.查看
        Shopping shopping = shoppingMapper.selectByPrimaryKey(shoppingId);

        //3.返回结果

        return ServerResponse.createBySuccess(shopping);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Shopping> shoppingList = shoppingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(shoppingList);
        return  ServerResponse.createBySuccess(pageInfo);

    }
}
