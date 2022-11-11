package com.example.miaofupos.datafragment.databill.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.miaofupos.R;
import com.example.miaofupos.datafragment.databillbean.BillTypeBean;

import java.util.List;

public class DataBillDialogGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<BillTypeBean> list;
    LayoutInflater layoutInflater;
    private TextView bt_sel_peo;
    private int selectorPosition;
    public DataBillDialogGridViewAdapter(Context context, List<BillTypeBean> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.item_data_bill_dialog, null);
        bt_sel_peo = (TextView) convertView.findViewById(R.id.tv);
        bt_sel_peo.setText(list.get(position).getDictLabel());

        if (selectorPosition == position) {
            bt_sel_peo.setBackgroundResource(R.drawable.item_data_bill_dialog_tv_false);
            bt_sel_peo.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            bt_sel_peo.setBackgroundResource(R.drawable.item_data_bill_dialog_tv_true);
            bt_sel_peo.setTextColor(Color.parseColor("#AAAAAA"));
        }
        return convertView;
    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }

}
