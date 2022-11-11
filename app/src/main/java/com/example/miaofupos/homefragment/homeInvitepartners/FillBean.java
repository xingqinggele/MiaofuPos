package com.example.miaofupos.homefragment.homeInvitepartners;

/**
 * 作者: qgl
 * 创建日期：2021/8/27
 * 描述:生成二维码Bean
 */
public class FillBean {
    private String name;
    private String nickName;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
