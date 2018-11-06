package com.itdr.service.impl;

import com.google.common.collect.Lists;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.dao.CartMapper;
import com.itdr.dao.ProductMapper;
import com.itdr.pojo.Cart;
import com.itdr.pojo.Product;
import com.itdr.service.ICartService;
import com.itdr.utils.BigDecimalUtils;
import com.itdr.vo.CartProductVO;
import com.itdr.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {

        //1.参数非空校验
        if (productId == null || count == null) {
            return ServerResponse.createByError("参数不能为空");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByError("添加的商品不存在");
        }
        //2.根据productId和userId查询购物信息

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            //添加
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(ResponseCode.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        } else {
            //更新
            Cart cart1 = new Cart();
            cart1.setId(cart.getId());
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }
        CartVO cartVO = getCartVOLimit(userId);

        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {

        //1.参数的非空判断

        if (productId == null || count == null) {
            return ServerResponse.createByError("参数不能为空");
        }
        //2.根据userId和productId查询商品

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart != null) {
            //3.更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        //4.返回cartvo
        return ServerResponse.createBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {

        //1.参数的非空校验
        if (productIds == null || productIds.equals("")) {
            return ServerResponse.createByError("参数不能为空");
        }
        List<Integer> productIdList = Lists.newArrayList();
        //2.productIds  --->List<Integer>
        String[] productIdsStr = productIds.split(",");
        if (productIdsStr != null && productIdsStr.length > 0) {
            for (String productIdstr : productIdsStr) {
                Integer productId = Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        //3.调用Dao
        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);
        //4.返回结果

        return ServerResponse.createBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse select(Integer userId, Integer productId, Integer check) {

        //1.非空校验
/*
        if(productId==null){
            return ServerResponse.createByError("参数不能为空");
        }*/
        //2.Dao接口

        cartMapper.selectOrUnselectProduct(userId, productId, check);
        //3.返回结果

        return ServerResponse.createBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int quantity = cartMapper.get_cart_product_count(userId);
        return ServerResponse.createBySuccess(quantity);
    }

    /* private CartVO getCartVOLimit(Integer userId){
         CartVO cartVO = new CartVO();
         //1.根据userId查询购物信息 --> List<Cart>
         List<Cart> cartList = cartMapper.selectCartByUserId(userId);

         //2.List<Cart> -->List<CartProductVO>
         List<CartProductVO> cartProductVOList = Lists.newArrayList();

         //购物车总价格
         BigDecimal cartTotalPrice = new BigDecimal("0");
         if(cartList!=null && cartList.size()>0){
             for (Cart cart:cartList
                  ) {
                 CartProductVO cartProductVO = new CartProductVO();
                 cartProductVO.setId(cart.getId());
                 cartProductVO.setUserId(userId);
                 cartProductVO.setQuantity(cart.getQuantity());
                 cartProductVO.setProductChecked(cart.getChecked());

                 //查询商品
                 Product product = productMapper.selectByPrimaryKey(cart.getId());
                 if(product!=null){
                     cartProductVO.setId(cart.getId());
                     cartProductVO.setProductMainImage(product.getMainImage());
                     cartProductVO.setProductName(product.getName());
                     cartProductVO.setProductPrice(product.getPrice());
                     cartProductVO.setProductStatus(product.getStatus());
                     cartProductVO.setProductStock(product.getStock());
                     int stock = product.getStock();
                     int limitProductCount=0;
                     if(stock>cart.getQuantity()){
                         limitProductCount=cart.getQuantity();
                         cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                     }else {  //商品库存不足
                         limitProductCount=stock;
                         //更新购物车中商品的数量
                         Cart cart1 = new Cart();
                         cart1.setQuantity(stock);
                         cart1.setProductId(cart.getProductId());
                         cart1.setChecked(cart.getChecked());
                         cart1.setUserId(userId);
                         cart1.setId(cart.getId());
                         cartMapper.updateByPrimaryKey(cart1);
                         cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                     }
                     cartProductVO.setProductSubtitle(product.getSubtitle());
                     cartProductVO.setQuantity(limitProductCount);
                     cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));
                 }
                 //计算选中商品的总价
                 //if(cartProductVO.getProductChecked()==ResponseCode.PRODUCT_CHECKED.getCode()) {
                     cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                 //}
                 cartProductVOList.add(cartProductVO);
             }

         }
         //3.计算购物车总价格

         cartVO.setCartProductVOList(cartProductVOList);
         cartVO.setCartTotalPrice(cartTotalPrice);

         //4.判断购物车是否全选
         int count = cartMapper.isCheckedAll(userId);
         if(count>0){
             cartVO.setAllChecked(false);
         }else {
             cartVO.setAllChecked(true);
         }
         //5.返回结果

         return cartVO;
     }
 }*/
    private CartVO getCartVOLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        //step1:根据userId查询购物信息--》LIST<Cart>

        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        //step2:List<Cart>-->List<CartProductVO>
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        //购物车总价格
        BigDecimal carttotalprice = new BigDecimal("0");

        if (cartList != null && cartList.size() > 0) {
            for (Cart cart : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductChecked(cart.getChecked());
                //查询商品
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null) {
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());

                    int stock = product.getStock();
                    int limitProductCount = 0;
                    if (stock > cart.getQuantity()) {
                        limitProductCount = cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");

                    } else {
                        //商品库存不足
                        limitProductCount = stock;
                        //更新购物车中商品数量
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setChecked(stock);
                        cart1.setProductId(cart.getProductId());
                        cart1.setChecked(cart.getChecked());
                        cart1.setUserId(cart.getUserId());
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FATL");

                    }
                    cartProductVO.setQuantity(limitProductCount);
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),
                            Double.valueOf(cartProductVO.getQuantity())));

                }
                if (cartProductVO.getProductChecked() == ResponseCode.PRODUCT_CHECKED.getCode()) {//被选中计算总价
                    carttotalprice = BigDecimalUtils.add(carttotalprice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);
        //step3:计算总价格
        cartVO.setCartTotalPrice(carttotalprice);
        //cartVO.setCarttotalprice(carttotalprice);


        //step4:判断购物车是否全选
        int count = cartMapper.isCheckedAll(userId);
        if (count > 0) {
            cartVO.setAllChecked(false);
        } else {
            cartVO.setAllChecked(true);
        }
        //step5:返回结果
        return cartVO;
    }
}
