package com.example.miaofupos.datafragment.databillbean;

/**
 * 作者: qgl
 * 创建日期：2021/2/7
 * 描述: 账单列表Bean
 */
public class BillBean {

    private String  billId; //交易账单主键
    private String billTypeLabel; //账单交易类型名称
    private String amount;//交易金额
    private String billDate;// 账单日期
    private String billTypeValue;// 账单交易类型值

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillTypeLabel() {
        return billTypeLabel;
    }

    public void setBillTypeLabel(String billTypeLabel) {
        this.billTypeLabel = billTypeLabel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillTypeValue() {
        return billTypeValue;
    }

    public void setBillTypeValue(String billTypeValue) {
        this.billTypeValue = billTypeValue;
    }
}
