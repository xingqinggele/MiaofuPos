package com.example.miaofupos.homefragment.homeequipment.bean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/16
 * 描述: 筛选条件Bean
 */
public class ScreeningBean {
    private List<DictData>dictData;
    private DictType dictType;

    public List<DictData> getDictData() {
        return dictData;
    }

    public void setDictData(List<DictData> dictData) {
        this.dictData = dictData;
    }

    public DictType getDictType() {
        return dictType;
    }

    public void setDictType(DictType dictType) {
        this.dictType = dictType;
    }

    public static class DictData{
        private String searchValue;
        private String createBy;
        private String updateBy;
        private String updateTime;
        private String remark;
        private int dictCode;
        private int dictSort;
        private String dictLabel;
        private String dictValue;
        private String dictType;
        private String cssClass;
        private String listClass;
        private String isDefault;
        private String status;


        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }

        public String getSearchValue() {
            return searchValue;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }


        public void setDictCode(int dictCode) {
            this.dictCode = dictCode;
        }

        public int getDictCode() {
            return dictCode;
        }

        public void setDictSort(int dictSort) {
            this.dictSort = dictSort;
        }

        public int getDictSort() {
            return dictSort;
        }

        public void setDictLabel(String dictLabel) {
            this.dictLabel = dictLabel;
        }

        public String getDictLabel() {
            return dictLabel;
        }

        public void setDictValue(String dictValue) {
            this.dictValue = dictValue;
        }

        public String getDictValue() {
            return dictValue;
        }

        public void setDictType(String dictType) {
            this.dictType = dictType;
        }

        public String getDictType() {
            return dictType;
        }

        public void setCssClass(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }

        public void setListClass(String listClass) {
            this.listClass = listClass;
        }

        public String getListClass() {
            return listClass;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

    }

    public static class DictType{
        private String searchValue;
        private String createBy;
        private String updateBy;
        private String updateTime;
        private String remark;
        private int dictId;
        private String dictName;
        private String dictType;
        private String status;
        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }
        public String getSearchValue() {
            return searchValue;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }
        public String getCreateBy() {
            return createBy;
        }


        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }
        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
        public String getUpdateTime() {
            return updateTime;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
        public String getRemark() {
            return remark;
        }


        public void setDictId(int dictId) {
            this.dictId = dictId;
        }
        public int getDictId() {
            return dictId;
        }

        public void setDictName(String dictName) {
            this.dictName = dictName;
        }
        public String getDictName() {
            return dictName;
        }

        public void setDictType(String dictType) {
            this.dictType = dictType;
        }
        public String getDictType() {
            return dictType;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }
    }

}

