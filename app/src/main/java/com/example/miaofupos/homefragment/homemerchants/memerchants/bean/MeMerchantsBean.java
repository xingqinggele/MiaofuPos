package com.example.miaofupos.homefragment.homemerchants.memerchants.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2021/3/9
 * 描述:我的商户Bean
 */
public class MeMerchantsBean implements Serializable {
    @SerializedName("id")  // 根据接口自定义
    private String id;  //商户ID
    @SerializedName("merchantName")  // 根据接口自定义
    private String name;  //商户姓名
    @SerializedName("transAmount")  // 根据接口自定义
    private String monthTurnover;  //本月交易额
    private String terminalNo;
    private String merchCode;

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getAuditMsg() {
        return auditMsg;
    }

    public void setAuditMsg(String auditMsg) {
        this.auditMsg = auditMsg;
    }

    private String auditMsg;  //

    private String isAudit;  //0（审核中）1（审核成功）2（审核失败）


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMonthTurnover() {
        return monthTurnover;
    }

    public void setMonthTurnover(String monthTurnover) {
        this.monthTurnover = monthTurnover;
    }

    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public String getMerchCode() {
        return merchCode;
    }

    public void setMerchCode(String merchCode) {
        this.merchCode = merchCode;
    }

    @Override
    public String toString() {
        return "MeMerchantsBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", monthTurnover='" + monthTurnover + '\'' +
                '}';
    }
}
