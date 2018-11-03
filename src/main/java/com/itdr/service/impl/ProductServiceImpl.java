package com.itdr.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.dao.CategoryMapper;
import com.itdr.dao.ProductMapper;
import com.itdr.pojo.Category;
import com.itdr.pojo.Product;
import com.itdr.service.ICategoruService;
import com.itdr.service.IProductService;
import com.itdr.utils.DateUtils;
import com.itdr.utils.PropertiesUtils;
import com.itdr.vo.ProductDetailVO;
import com.itdr.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ICategoruService categoruService;

    @Override
    public ServerResponse saveOrUpdate(Product product) {

        //1.参数的非空校验
        if(product==null){
            return ServerResponse.createByError("参数为空");
        }

        //2.设置商品的主图  sub_image --> 1. .jpg  2. .png  3. .jpg 主图，我只需要取第一张就可以
        String subImage = product.getSubImages();
        if(subImage!=null && !subImage.equals("")){
            String[] subImageArr = subImage.split(",");
            if(subImageArr.length>0){
                //设置商品主图
                product.setSubImages(subImageArr[0]);
            }
        }
        //3.判断商品是要添加还是更新根据id，并且返回结果
        if(product.getId()==null){
            //添加
            int result = productMapper.insert(product);
            if(result>0){
                return ServerResponse.createBySuccess("添加成功");
            }else{
                return ServerResponse.createByError("添加失败");
            }
        }else {
            //更新
            int result = productMapper.updateByPrimaryKey(product);
            if(result>0){
                return ServerResponse.createBySuccess("更新成功");
            }else{
                return ServerResponse.createByError("更新失败");
            }
        }

    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {

        //1.参数的非空校验
        if (productId==null){
            return ServerResponse.createByError("商品id不能为空");
        }
        if(status==null){
            return  ServerResponse.createByError("商品的状态参数不能为空");
        }

        //2.更新商品的状态
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateProductKeyBySelective(product);

        //3.返回结果

        if(result>0){
            return ServerResponse.createBySuccess("更新成功");
        }else{
            return ServerResponse.createByError("更新失败");
        }

    }

    @Override
    public ServerResponse detail(Integer productId) {

        //1.参数的非空校验
        if (productId==null){
            return ServerResponse.createByError("商品id不能为空");
        }

        //2.根据商品的id查询商品product
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createByError("商品不存在");
        }
        //3.product  -->  productDetailVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);

        //4.返回结果

        return ServerResponse.createBySuccess(productDetailVO);
    }

    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        //从数据库中的时间转化成String类型的，此时需要一个工具类DateUtils
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        //设置域名，最好放在配置文件里，方便修改,这是需要封装一个读取配置文件的工具类PropertiesUtils
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImage(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));

        //设置它的父类id
        Category category = categoryMapper.selectByPrimaryKey(product.getId());
        if(category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else {
            productDetailVO.setParentCategoryId(0);
        }

        return productDetailVO;
    }
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        //1.参数非空校验不用了，因为参数不是必要的

        //AOP select * from product limit (pageNum-1)*pageSize
        PageHelper.startPage(pageNum,pageSize);     //一定要在2之前执行

        //2.直接进行查询商品数据
        List<Product> productList = productMapper.selectAll();
        List<ProductListVO> productListVOS = new ArrayList<ProductListVO>();
        if(productList!=null && productList.size()>0){
            for(Product product :productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOS.add(productListVO);
            }
        }
        //把结果以对象的形式返回前端
        PageInfo pageInfo = new PageInfo(productListVOS);


        return ServerResponse.createBySuccess(pageInfo);
    }


    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {

        //都不是必须的参数，可以不进行参数校验

        //select * from product where productId=? and productName like %name%;
        PageHelper.startPage(pageNum,pageSize);

        if(productName!=null  && !productName.equals("")){
            productName = "%"+productName+"%";
        }
        List<Product> productList = productMapper.findProductByPnameOrPid(productId,productName);

        List<ProductListVO> productListVOList = Lists.newArrayList();
        if(productList!=null && productList.size()>0){
            for(Product product :productList){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {

        //1.非空判断
        if(file==null){
            return ServerResponse.createByError();
        }

        //2.获取图片名称
        String orignalFileName = file.getOriginalFilename();
        //不能改变图片格式，故需要得到他的后缀即扩展名
        String exName = orignalFileName.substring(orignalFileName.lastIndexOf("."));
        //将图片的名字设成唯一的
        String newFileName = UUID.randomUUID().toString()+exName;

        File pathFile = new File(path);
        if(!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }

        File file1 = new File(path,newFileName);
        try {
            file.transferTo(file1);
            //将图片上传到图片服务器。。。。

            Map<String,String> map = Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            return ServerResponse.createBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 前台接口-商品详情
     * */
    @Override
    public ServerResponse detail_protal(Integer productId) {

        //1.参数的非空校验
        if (productId==null){
            return ServerResponse.createByError("商品id不能为空");
        }

        //2.根据商品的id查询商品product
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createByError("商品不存在");
        }

        //3.校验商品的状态
        if(product.getStatus()!=ResponseCode.PRODUCT_ONLINE.getCode()){
            return ServerResponse.createByError("商品已下架或删除");
        }

        //4.获取ProductDetailVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);

        //5.返回结果
        return ServerResponse.createBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse list_protal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderby) {

        //1.参数都不是必须的，但要求不能同时为空
        if(categoryId==null&&(keyword==null||keyword.equals(""))){
            return ServerResponse.createByError("参数错误");
        }

        //2.categoryId进行查询
        Set<Integer> integerSet = Sets.newHashSet();
        if(categoryId!=null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&(keyword==null||keyword.equals(""))){
                //说明没有商品数据,但也要按照分页格式返回
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            //根据类别id递归查询其子节点，得到商品Id的Set集合
            ServerResponse serverResponse = categoruService.get_deep_category(categoryId);
            if(serverResponse.isSuccess()){
                integerSet = (Set<Integer>) serverResponse.getData();
            }
        }
        //3.keyword进行查询
        if(keyword!=null&& !keyword.equals("")){
            keyword = "%"+keyword+"%";
        }

        //判断传过来的orderby
        if(orderby.equals("")){
            //不需要自定义排序
            PageHelper.startPage(pageNum,pageSize);
        }else {
            String[] orderByArr = orderby.split("_");
            if(orderByArr.length>1){
                PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }else {
                PageHelper.startPage(pageNum,pageSize);
            }
        }

        List<Product> productList = productMapper.searchProduct(integerSet,keyword);

        //4.List<Product> --->  List<ProductListVO>
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if(productList!=null && productList.size()>0){
            for (Product product:
                    productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }

        //5.分页
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(productListVOList);

        //6.返回结果

        return ServerResponse.createBySuccess(pageInfo);
    }
}
