package com.example.miaofupos.bean;

/**
 * 作者: qgl
 * 创建日期：2021/7/25
 * 描述: 积分兑话内设备时候的Bean
 */
public class OrderBean {

    private String posTypeId; //POS机类型
    private String orderPosNum; //POS机数量

    public String getPosTypeId() {
        return posTypeId;
    }

    public void setPosTypeId(String posTypeId) {
        this.posTypeId = posTypeId;
    }

    public String getOrderPosNum() {
        return orderPosNum;
    }

    public void setOrderPosNum(String orderPosNum) {
        this.orderPosNum = orderPosNum;
    }
}
