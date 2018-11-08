package com.itdr.service.impl;

import com.google.common.collect.Sets;
import com.itdr.common.ServerResponse;
import com.itdr.dao.CategoryMapper;
import com.itdr.pojo.Category;
import com.itdr.service.ICategoruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoruService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse get_category(Integer categoryId) {

        //1.非空校验
        if(categoryId==null){
            return ServerResponse.createByError("参数不能为空");
        }
        //2.根据categoryId 查询类别
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category==null){
            return ServerResponse.createByError("查询的类别不存在");
        }
        //3.查询子类别
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        //4.返回结果


        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {

        //1.参数非空校验
        if (parentId == null) {
            return ServerResponse.createByError("类别id不能为空");
        }
        if(categoryName==null||categoryName.equals("")){
            return ServerResponse.createByError("类别名称不能为空");
        }

        //2.判断是否已存在该节点
        int checkNodeName = categoryMapper.slectSameChildNode(categoryName);
        if(checkNodeName > 0){
            return ServerResponse.createByError("已存在该节点");
        }

        //3.增加节点
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        int reslut = categoryMapper.insert(category);

        //4.返回结果
        if(reslut>0){
            //添加成功
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError("添加失败");
    }

    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {

        //1.参数的非空校验
        if(categoryId==null||categoryId.equals("")){
            return ServerResponse.createByError("类别id不能为空");
        }
        if(categoryName==null||categoryName.equals("")){
            return ServerResponse.createByError("类别名称不能为空");
        }
        //2.根据categoryId查询
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category==null){
            return ServerResponse.createByError("要修改的为别不存在");
        }
        //3.修改
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKey(category);
        //4.返回结果
        if(result>0){
            //添加成功
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError("修改失败");
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {

        //1.参数非空校验
        if(categoryId==null){
            return ServerResponse.createByError("类别id不能为空");
        }
        //2.查询
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = findAllChildCategory(categorySet,categoryId);
        Set<Integer> integerSet = Sets.newHashSet();

        Iterator<Category> categoryIterator = categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category = categoryIterator.next();
            integerSet.add(category.getId());
        }

        return ServerResponse.createBySuccess(integerSet);
    }

    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){

        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){       //查找本节点
            categorySet.add(category);
        }
        //查找category下的子节点（平级）
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        if(categoryList!=null&&categoryList.size()>0){
            for (Category category1:categoryList
                    ) {
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}