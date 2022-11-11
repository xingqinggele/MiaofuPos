package com.example.miaofupos.newprojectview.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述: 区
 */
public class NewDistrictBean implements Parcelable {
    private String areaCode;
    private String areaName;
    private String parentCode;
    private String areaLevel;
    public static final Creator<NewDistrictBean> CREATOR = new Creator<NewDistrictBean>() {
        public NewDistrictBean createFromParcel(Parcel source) {
            return new NewDistrictBean(source);
        }

        public NewDistrictBean[] newArray(int size) {
            return new NewDistrictBean[size];
        }
    };

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.areaCode);
        dest.writeString(this.areaName);
    }

    @Override
    public String toString() {
        return areaName;
    }

    public NewDistrictBean() {
    }

    protected NewDistrictBean(Parcel in) {
        this.areaCode = in.readString();
        this.areaName = in.readString();
    }
}

