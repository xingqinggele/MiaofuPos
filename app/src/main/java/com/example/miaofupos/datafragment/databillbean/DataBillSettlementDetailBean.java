package com.example.miaofupos.datafragment.databillbean;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：qgl 时间： 2020/5/7 14:53
 * Describe: 报销统计bean
 */
public class DataBillSettlementDetailBean {

    @SerializedName("typeName")
    private String state; // 类型名称

    @SerializedName("sum")
    private String num; // 金额


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

}
