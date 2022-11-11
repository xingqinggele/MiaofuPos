package com.example.miaofupos.homefragment.homeintegral.bean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/5/18
 * 描述:积分设备列表Bean
 */
public class IntegralMostBean {
    private String brandName;
    private String brandDesc;
    private String createTime;
    private List<PosTypeList> posTypeList;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandDesc() {
        return brandDesc;
    }

    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<PosTypeList> getPosTypeList() {
        return posTypeList;
    }

    public void setPosTypeList(List<PosTypeList> posTypeList) {
        this.posTypeList = posTypeList;
    }

    public static class PosTypeList{
        private String id;
        private String typeName;
        private String returnMoney;
        private String returnIntegral;
        private String imgPath;
        private String detailImg;
        private String brandId;
        private int num; //商品数量

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getReturnMoney() {
            return returnMoney;
        }

        public void setReturnMoney(String returnMoney) {
            this.returnMoney = returnMoney;
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

        public String getDetailImg() {
            return detailImg;
        }

        public void setDetailImg(String detailImg) {
            this.detailImg = detailImg;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
