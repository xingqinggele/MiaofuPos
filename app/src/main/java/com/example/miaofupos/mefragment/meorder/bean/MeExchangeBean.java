package com.example.miaofupos.mefragment.meorder.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/24
 * 描述:业务消息Bean
 */
public class MeExchangeBean implements Serializable {
    @SerializedName("orderId")  // 根据接口自定义
    private String id; //ID
    @SerializedName("createTime")  // 根据接口自定义
    private String time; //时间
    @SerializedName("status")  // 根据接口自定义 0，待发货 1，已完成
    private String state; //状态 已完成、代发货
    @SerializedName("num")  // 根据接口自定义
    private String number; //POS数量
    @SerializedName("money")  // 根据接口自定义
    private String totalamount; //pos机总金额
    @SerializedName("parentName")  // 根据接口自定义
    private String personstate; //上级角色
    @SerializedName("orderNo")  // 根据接口自定义
    private String orderNo; //订单号
    private String imgPath; //pos图片
    private String returnIntegral; //pos积分
    private String typeName; //pos名称
    //新增的List
    private List<list>list;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getPersonstate() {
        return personstate;
    }

    public void setPersonstate(String personstate) {
        this.personstate = personstate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getReturnIntegral() {
        return returnIntegral;
    }

    public void setReturnIntegral(String returnIntegral) {
        this.returnIntegral = returnIntegral;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<MeExchangeBean.list> getList() {
        return list;
    }

    public void setList(List<MeExchangeBean.list> list) {
        this.list = list;
    }

    public class list{
        private String typeName;
        private String returnIntegral;
        private String imgPath;
        private String posNum;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getReturnIntegral() {
            return returnIntegral;
        }

        public void setReturnIntegral(String returnIntegral) {
            this.returnIntegral = returnIntegral;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getPosNum() {
            return posNum;
        }

        public void setPosNum(String posNum) {
            this.posNum = posNum;
        }
    }
}
