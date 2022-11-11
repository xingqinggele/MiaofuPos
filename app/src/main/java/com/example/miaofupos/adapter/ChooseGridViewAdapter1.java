package com.example.miaofupos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalActivityBean;

import java.util.List;

public class ChooseGridViewAdapter1 extends BaseAdapter {
    LayoutInflater layoutInflater;
    private TextView bt_sel_peo;
    private int selectorPosition;
    private List<TerminalActivityBean> list1;

    public ChooseGridViewAdapter1(Context context, List<TerminalActivityBean> list) {
        this.list1 = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list1.size();//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.item_choose_grid, null);
        bt_sel_peo = (TextView) convertView.findViewById(R.id.tv);
        bt_sel_peo.setText(list1.get(position).getTypeName());

        if (selectorPosition == position) {
            bt_sel_peo.setBackgroundResource(R.drawable.item_choose_tv_false);
            bt_sel_peo.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            bt_sel_peo.setBackgroundResource(R.drawable.item_choose_tv_true);
            bt_sel_peo.setTextColor(Color.parseColor("#AAAAAA"));
        }
        bt_sel_peo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState(position);
                onItemAddClick.onItemClick(list1.get(position).getId());
            }
        });
        return convertView;
    }

    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();
    }


    public void newAdd(){
        selectorPosition = 0;
        notifyDataSetChanged();
    }


    public static interface OnAddClickListener {
        // true add; false cancel
        public void onItemClick(String position); //传递boolean类型数据给activity
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }

}
