package com.example.miaofupos.homefragment.homeintegral.bean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/2/23
 * 描述:
 */
public class IntegralAllBean {
    private String monthly; //日期
    private List<Detail>detail;

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }


    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }


    public static class Detail{
        private int id;
        private int merchId;
        private String posCode;
        private String integralType;
        private int value;
        private String createTime;
        private String transType;
        private String orderId;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setMerchId(int merchId) {
            this.merchId = merchId;
        }
        public int getMerchId() {
            return merchId;
        }

        public void setPosCode(String posCode) {
            this.posCode = posCode;
        }
        public String getPosCode() {
            return posCode;
        }

        public void setIntegralType(String integralType) {
            this.integralType = integralType;
        }
        public String getIntegralType() {
            return integralType;
        }

        public void setValue(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
        public String getCreateTime() {
            return createTime;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }
        public String getTransType() {
            return transType;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public String getOrderId() {
            return orderId;
        }
    }

}
