package com.itdr.pojo;

import java.util.Date;

public class PayInfo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.id
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.user_id
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.order_no
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private Long orderNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.pay_platform
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private Integer payPlatform;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.platform_number
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private String platformNumber;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.platform_status
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private String platformStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.create_time
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column neuedu_payinfo.update_time
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.id
     *
     * @return the value of neuedu_payinfo.id
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.id
     *
     * @param id the value for neuedu_payinfo.id
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.user_id
     *
     * @return the value of neuedu_payinfo.user_id
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.user_id
     *
     * @param userId the value for neuedu_payinfo.user_id
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.order_no
     *
     * @return the value of neuedu_payinfo.order_no
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public Long getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.order_no
     *
     * @param orderNo the value for neuedu_payinfo.order_no
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.pay_platform
     *
     * @return the value of neuedu_payinfo.pay_platform
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public Integer getPayPlatform() {
        return payPlatform;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.pay_platform
     *
     * @param payPlatform the value for neuedu_payinfo.pay_platform
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setPayPlatform(Integer payPlatform) {
        this.payPlatform = payPlatform;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.platform_number
     *
     * @return the value of neuedu_payinfo.platform_number
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public String getPlatformNumber() {
        return platformNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.platform_number
     *
     * @param platformNumber the value for neuedu_payinfo.platform_number
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber == null ? null : platformNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.platform_status
     *
     * @return the value of neuedu_payinfo.platform_status
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public String getPlatformStatus() {
        return platformStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.platform_status
     *
     * @param platformStatus the value for neuedu_payinfo.platform_status
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus == null ? null : platformStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.create_time
     *
     * @return the value of neuedu_payinfo.create_time
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.create_time
     *
     * @param createTime the value for neuedu_payinfo.create_time
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column neuedu_payinfo.update_time
     *
     * @return the value of neuedu_payinfo.update_time
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column neuedu_payinfo.update_time
     *
     * @param updateTime the value for neuedu_payinfo.update_time
     *
     * @mbg.generated Mon Oct 29 15:48:44 CST 2018
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}