package com.itdr.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * 购物车实体类
 * */
public class CartVO implements Serializable {
    //购物车信息
    private List<CartProductVO> cartProductVOList;
    //是否全选
    private boolean allChecked;
    //总价格
    private BigDecimal cartTotalPrice;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
