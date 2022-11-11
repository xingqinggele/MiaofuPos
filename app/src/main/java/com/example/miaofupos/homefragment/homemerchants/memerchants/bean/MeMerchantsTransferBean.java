package com.example.miaofupos.homefragment.homemerchants.memerchants.bean;

/**
 * 作者: qgl
 * 创建日期：2021/3/27
 * 描述:商户转移Bean
 */
public class MeMerchantsTransferBean{
    private String portrait; //头像
    private String nickName; //商户名称
    private String merchId; //商户ID
    private String phonenumber; //商户手机号

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
