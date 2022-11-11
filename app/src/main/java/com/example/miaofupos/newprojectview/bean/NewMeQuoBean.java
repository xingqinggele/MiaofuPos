package com.example.miaofupos.newprojectview.bean;

/**
 * 作者: qgl
 * 创建日期：2022/8/25
 * 描述:新商户筛选类型
 */
public class NewMeQuoBean {
    private String id;
    private String name;

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

    public NewMeQuoBean(String id, String name) {
        this.id = id;
        this.name = name;
    }
}