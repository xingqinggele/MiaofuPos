package com.example.miaofupos.newprojectview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.newprojectview.bean.NewProvinceBean;

import java.util.List;


/**
 * 作者: qgl
 * 创建日期：2021/1/25
 * 描述:
 */
public class NewProvinceAdapter extends BaseAdapter {
    Context context;
    List<NewProvinceBean> mProList;
    private int provinceIndex = -1;

    public NewProvinceAdapter(Context context, List<NewProvinceBean> mProList) {
        this.context = context;
        this.mProList = mProList;
    }

    public void updateSelectedPosition(int index) {
        this.provinceIndex = index;
    }

    public int getSelectedPosition() {
        return this.provinceIndex;
    }

    public int getCount() {
        return this.mProList.size();
    }

    public NewProvinceBean getItem(int position) {
        return (NewProvinceBean)this.mProList.get(position);
    }

    public long getItemId(int position) {
        return Long.parseLong(((NewProvinceBean)this.mProList.get(position)).getAreaCode());
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
       Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_jdcitypicker_item, parent, false);
            holder = new Holder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView)convertView.findViewById(R.id.selectImg);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        NewProvinceBean item = this.getItem(position);
        holder.name.setText(item.getAreaName());
        boolean checked = this.provinceIndex != -1 && ((NewProvinceBean)this.mProList.get(this.provinceIndex)).getAreaName().equals(item.getAreaName());
        holder.name.setEnabled(!checked);
        holder.selectImg.setVisibility(checked ? 8 : 8);
        return convertView;
    }

    class Holder {
        TextView name;
        ImageView selectImg;

        Holder() {
        }
    }
}

