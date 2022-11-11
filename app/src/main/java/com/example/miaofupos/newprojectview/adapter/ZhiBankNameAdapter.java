package com.example.miaofupos.newprojectview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.newprojectview.bean.BankNameBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2022/8/15
 * 描述:银行编码Adapter
 */
public class ZhiBankNameAdapter extends BaseAdapter {
    Context context;
    List<BankNameBean> mDistrictList;
    private int districtIndex = -1;

    public ZhiBankNameAdapter(Context context, List<BankNameBean> mDistrictList) {
        this.context = context;
        this.mDistrictList = mDistrictList;
    }


    public int getSelectedPosition() {
        return this.districtIndex;
    }

    public void updateSelectedPosition(int index) {
        this.districtIndex = index;
    }

    public int getCount() {
        return this.mDistrictList.size();
    }

    public BankNameBean getItem(int position) {
        return (BankNameBean)this.mDistrictList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        mDistrictList.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("WrongConstant")
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_name_picker_item, parent, false);
            holder = new Holder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        BankNameBean item = this.getItem(position);
        holder.name.setText(item.getName());
        boolean checked = this.districtIndex != -1 && ((BankNameBean)this.mDistrictList.get(this.districtIndex)).getName().equals(item.getName());
        holder.name.setEnabled(!checked);
        return convertView;
    }

    class Holder {
        TextView name;
        Holder() {
        }
    }
} 