package com.example.miaofupos.datafragment.datapersonalresults.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeequipment.adapter.ItemCallbackRecordAdapter;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean;


import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/25
 * 描述:旧的业绩Adapter
 */
public class DataDayResultAdapter extends RecyclerView.Adapter<DataDayResultAdapter.ViewHolder> implements BaseQuickAdapter.RequestLoadMoreListener {
    private List<BusinessMessageBean> list = new ArrayList<>();
    private List<BusinessMessageBean> list1 = new ArrayList<>();
    private Context mContext;

    public DataDayResultAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_data_day_results, viewGroup, false);
        return new ViewHolder(view);
    }

    public void addAllData(List<BusinessMessageBean> dataList, List<BusinessMessageBean> dataList1) {
        this.list.addAll(dataList);
        this.list1.addAll(dataList1);
        notifyDataSetChanged();
    }
    //更新adapter 清除List
    public void clearData() {
        this.list.clear();
        this.list1.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_data_day_results_list_time.setText(list.get(i).getName());
        if (viewHolder.IAdapter == null) {
            viewHolder.IAdapter = new ItemCallbackRecordAdapter(R.layout.item_data_day_results_item, list1);
            viewHolder.IAdapter.openLoadAnimation();
            viewHolder.IAdapter.setEnableLoadMore(false);
            viewHolder.IAdapter.setOnLoadMoreListener(this, viewHolder.item_data_day_results_item_recycler);
            viewHolder.IAdapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.list_empty, null));
            viewHolder.item_data_day_results_item_recycler.setLayoutManager(new LinearLayoutManager(mContext));
            viewHolder.item_data_day_results_item_recycler.setAdapter(viewHolder.IAdapter);
            viewHolder.IAdapter.loadMoreEnd(true);
        } else {
            viewHolder.IAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onLoadMoreRequested() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_data_day_results_list_time;
        ItemCallbackRecordAdapter IAdapter;
        RecyclerView item_data_day_results_item_recycler;

        public ViewHolder(View view) {
            super(view);
            item_data_day_results_list_time = view.findViewById(R.id.item_data_day_results_list_time);
            item_data_day_results_item_recycler = view.findViewById(R.id.item_data_day_results_item_recycler);
        }
    }
}
