package com.example.miaofupos.homefragment.homeintegral.bean;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2021/7/22
 * 描述:购物车Bean
 */
public class ShoppingCartBean implements Serializable {
    private String commId;
    private String posTypeId;
    private String posName;
    private String img;
    private int posNum;
    private String returnMoney;

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public String getPosTypeId() {
        return posTypeId;
    }

    public void setPosTypeId(String posTypeId) {
        this.posTypeId = posTypeId;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPosNum() {
        return posNum;
    }

    public void setPosNum(int posNum) {
        this.posNum = posNum;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

}
