package com.example.miaofupos.mefragment.meorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.mefragment.meorder.bean.MeExhangeItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/7/23
 * 描述: 我的订单 二级列表 adapter
 */
public class HomeintegerselectAdapter extends BaseAdapter {
    private Context mContext;
    private List<MeExhangeItemBean> mDatas = new ArrayList();

    public HomeintegerselectAdapter(Context context) {
        mContext = context;
    }

    public void setDates(List<MeExhangeItemBean> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.homeintegerselectitem, null);
            mViewHolder.homeintegerselectitem_name = convertView.findViewById(R.id.homeintegerselectitem_name);
            mViewHolder.homeintegerselectitem_num = convertView.findViewById(R.id.homeintegerselectitem_num);
            mViewHolder.homeintegerselectitem_seletc = convertView.findViewById(R.id.homeintegerselectitem_seletc);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.homeintegerselectitem_name.setText(mDatas.get(position).getTypeName());
        mViewHolder.homeintegerselectitem_num.setText("/" + mDatas.get(position).getPosNum() + ")");
        mViewHolder.homeintegerselectitem_seletc.setText("("+mDatas.get(position).getSelectNum());
        return convertView;
    }

    class ViewHolder {
        TextView homeintegerselectitem_name;
        TextView homeintegerselectitem_seletc;
        TextView homeintegerselectitem_num;
    }
}
