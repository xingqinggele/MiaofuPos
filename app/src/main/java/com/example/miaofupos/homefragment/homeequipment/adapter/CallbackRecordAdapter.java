package com.example.miaofupos.homefragment.homeequipment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homemessage.bean.BusinessMessageBean;
import com.example.miaofupos.utils.ScaleUtils;
import com.example.miaofupos.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/25
 * 描述:回调记录Adapter
 */
public class CallbackRecordAdapter extends RecyclerView.Adapter<CallbackRecordAdapter.ViewHolder> implements BaseQuickAdapter.RequestLoadMoreListener {
    private List<BusinessMessageBean> list = new ArrayList<>();
    private List<BusinessMessageBean> list1 = new ArrayList<>();
    private Context mContext;

    public CallbackRecordAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_callback_record, viewGroup, false);
        return new ViewHolder(view);
    }

    public void addAllData(List<BusinessMessageBean> dataList, List<BusinessMessageBean> dataList1) {
        this.list.addAll(dataList);
        this.list1.addAll(dataList1);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_callback_list_time.setText(list.get(i).getName());
        if (viewHolder.IAdapter == null) {
            viewHolder.IAdapter = new ItemCallbackRecordAdapter(R.layout.item_callback_record_item_list_view, list1);
            viewHolder.IAdapter.openLoadAnimation();
            viewHolder.IAdapter.setEnableLoadMore(false);
            viewHolder.IAdapter.setOnLoadMoreListener(this, viewHolder.item_callback_list_view);
            viewHolder.IAdapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.list_empty, null));
            viewHolder.item_callback_list_view.setLayoutManager(new LinearLayoutManager(mContext));
            viewHolder.item_callback_list_view.setAdapter(viewHolder.IAdapter);
            viewHolder.IAdapter.loadMoreEnd(true);
            //添加纵向分割线, 设置两边间距 ScaleUtils.dip2px dp转换成px
            viewHolder.item_callback_list_view.addItemDecoration(new DividerItemDecoration(LinearLayoutManager.VERTICAL, ContextCompat.getColor(mContext, R.color.item_line), 2, ScaleUtils.dip2px(mContext, 15)));
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
        TextView item_callback_list_time;
        ItemCallbackRecordAdapter IAdapter;
        RecyclerView item_callback_list_view;

        public ViewHolder(View view) {
            super(view);
            item_callback_list_time = view.findViewById(R.id.item_callback_list_time);
            item_callback_list_view = view.findViewById(R.id.item_callback_list_view);
        }
    }
}
