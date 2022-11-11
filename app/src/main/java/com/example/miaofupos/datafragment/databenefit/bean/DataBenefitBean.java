package com.example.miaofupos.datafragment.databenefit.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2021/3/15
 * 描述:我的收益Bean
 */
public class DataBenefitBean implements Serializable {
    @SerializedName("merchId")  // 根据接口自定义
    private String merchId ;  //商户ID
    @SerializedName("activateMoney")  // 根据接口自定义
    private String activateMoney;  //激活奖励金额
    @SerializedName("closeMoney")  // 根据接口自定义
    private String closeMoney;  // 结算金额
    @SerializedName("createTime")  // 根据接口自定义
    private String createTime;  // 创建时间
    @SerializedName("aggregateAmount")  // 根据接口自定义
    private String aggregateAmount;  //总金额
    @SerializedName("orderTime")  // 根据接口自定义
    private String orderTime ;  //结算时间


    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getActivateMoney() {
        return activateMoney;
    }

    public void setActivateMoney(String activateMoney) {
        this.activateMoney = activateMoney;
    }

    public String getCloseMoney() {
        return closeMoney;
    }

    public void setCloseMoney(String closeMoney) {
        this.closeMoney = closeMoney;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAggregateAmount() {
        return aggregateAmount;
    }

    public void setAggregateAmount(String aggregateAmount) {
        this.aggregateAmount = aggregateAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
