package com.itdr.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CartOrderItemVO implements Serializable {
    private List<OrderItemVO> orderItemVOList;
    private String imageHost;
    private BigDecimal productTotalPrice;

    public List<OrderItemVO> getOrderItemVOList() {
        return orderItemVOList;
    }

    public void setOrderItemVOList(List<OrderItemVO> orderItemVOList) {
        this.orderItemVOList = orderItemVOList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
