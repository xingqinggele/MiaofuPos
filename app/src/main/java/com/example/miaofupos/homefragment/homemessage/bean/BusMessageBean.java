package com.example.miaofupos.homefragment.homemessage.bean;

/**
 * 作者: qgl
 * 创建日期：2021/2/23
 * 描述: 业务消息Bean
 */
public class BusMessageBean {
    private String msgId;// 消息ID
    private String msgType;// 消息类型
    private String msgContent;// 消息内容
    private String createTime;// 消息时间
    private String orderStatus; //积分兑换，状态 1完成 2，未完成
    private String orderId;  //积分兑换，订单id


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
