package com.itdr.dao;

import com.itdr.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    int insert(OrderItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    OrderItem selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    List<OrderItem> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    int updateByPrimaryKey(OrderItem record);

    //订单明细批量插入
    int insertBatch(List<OrderItem> orderItemList);

    List<OrderItem> findOrderItemByOrderNo(Long orderNo);

    OrderItem findOrderByUserIdAndOrderNo(@Param("userId") Integer userId,
                                          @Param("orderNo") Long orderNo);

}