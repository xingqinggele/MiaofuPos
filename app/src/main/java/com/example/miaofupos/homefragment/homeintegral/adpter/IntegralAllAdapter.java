package com.example.miaofupos.homefragment.homeintegral.adpter;

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
import com.example.miaofupos.datafragment.datapersonalresults.adapter.DataDayResultAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.ItemCallbackRecordAdapter;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralAllBean;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/2/23
 * 描述:全部积分列表adapter
 */
public class IntegralAllAdapter extends RecyclerView.Adapter<IntegralAllAdapter.ViewHolder> implements BaseQuickAdapter.RequestLoadMoreListener {
    private List<IntegralAllBean> list = new ArrayList<>();
    private Context mContext;

    public IntegralAllAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_integarall, viewGroup, false);
        return new ViewHolder(view);
    }

    public void addAllData(List<IntegralAllBean> dataList) {
        this.list.addAll(dataList);
        notifyDataSetChanged();
    }

    //更新adapter 清除List
    public void clearData() {
        this.list.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_data_day_results_list_time.setText(list.get(i).getMonthly());
        if (viewHolder.IAdapter == null) {
            viewHolder.IAdapter = new ItemIntegralAllAdapter(R.layout.item_integarall_item, list.get(i).getDetail());
            viewHolder.IAdapter.openLoadAnimation();
            viewHolder.IAdapter.setEnableLoadMore(false);
            viewHolder.IAdapter.setOnLoadMoreListener(this, viewHolder.item_data_day_results_item_recycler);
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
        ItemIntegralAllAdapter IAdapter;
        RecyclerView item_data_day_results_item_recycler;
        public ViewHolder(View view) {
            super(view);
            item_data_day_results_list_time = view.findViewById(R.id.item_data_day_results_list_time);
            item_data_day_results_item_recycler = view.findViewById(R.id.item_data_day_results_item_recycler);
        }
    }
}
