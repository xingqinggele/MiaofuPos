package com.example.miaofupos.homefragment.homeintegral.bean;

/**
 * 作者: qgl
 * 创建日期：2021/4/8
 * 描述:积分兑换选择兑换对象
 */
public class OrderPersonBean {
    //商户名称
    private String merchName;
    //商户ID
    private String merchId;
    //商户手机号
    private String mobile;
    //商户头像
    private String portrait;

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
