package com.example.miaofupos.homefragment.homemessage.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 作者: qgl
 * 创建日期：2020/12/31
 * 描述:消息中心，业务消息bean
 */
public class BusinessMessageBean1 implements Serializable {
    @SerializedName("id")  // 根据接口自定义
    private String id;
    @SerializedName("title")  // 根据接口自定义
    private String title; // 标题
    @SerializedName("context")  // 根据接口自定义
    private String context; // 内容
    @SerializedName("date")  // 根据接口自定义
    private String date; // 时间
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
