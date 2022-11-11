package com.example.miaofupos.homefragment.homequoteactivity.bean;

/**
 * 作者: qgl
 * 创建日期：2021/8/25
 * 描述: 终端类型
 */
public class MerchTypeBean3 {
    private String feeChlId;
    private String feeChlName;

    public String getFeeChlId() {
        return feeChlId;
    }

    public void setFeeChlId(String feeChlId) {
        this.feeChlId = feeChlId;
    }

    public String getFeeChlName() {
        return feeChlName;
    }

    public void setFeeChlName(String feeChlName) {
        this.feeChlName = feeChlName;
    }

    @Override
    public String toString() {
        return feeChlName;
    }
}
