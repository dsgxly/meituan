package com.itdr.dao;

import com.itdr.pojo.Product;
import java.util.List;

public interface ProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    int insert(Product record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    Product selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    List<Product> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    int updateByPrimaryKey(Product record);
}