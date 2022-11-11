package com.example.miaofupos.homefragment.homemessage.bean;

/**
 * 作者: qgl
 * 创建日期：2020/12/24
 * 描述:业务消息Bean
 */
public class BusinessMessageBean {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    private String name;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int price;
    private String pass;


}
