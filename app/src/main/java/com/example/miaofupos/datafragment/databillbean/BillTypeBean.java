package com.example.miaofupos.datafragment.databillbean;

/**
 * 作者: qgl
 * 创建日期：2021/2/7
 * 描述: 账单类型
 */
public class BillTypeBean {
    private String dictLabel;// 类型名称
    private String dictValue;// 类型值

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }
}
