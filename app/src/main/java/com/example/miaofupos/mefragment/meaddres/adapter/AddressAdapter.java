package com.example.miaofupos.mefragment.meaddres.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.miaofupos.mefragment.meaddres.MeAddressActivity;
import com.example.miaofupos.mefragment.meaddres.bean.AddressBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者: qgl
 * 创建日期：2021/5/10
 * 描述:地址列表适配器
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ItemViewHolder> {
    private List<AddressBean> list = new ArrayList<>();
    private int selectedPosition = -1;
    private MeAddressActivity mContext;

    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;

    public AddressAdapter(MeAddressActivity mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_address_list, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder myHolder, @SuppressLint("RecyclerView") final int position) {
        myHolder.name_phone_tv.setText(list.get(position).getName() + "\t\t" + list.get(position).getPhone());
        myHolder.address_tv.setText(list.get(position).getAddr() + list.get(position).getAddrInfo());
        if (("1").equals(list.get(position).getType())) {
            list.get(position).setSelected(true);
        } else {
            list.get(position).setSelected(false);
        }
        myHolder.cbChecked.setChecked(list.get(position).isSelected());
        myHolder.cbChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.AddressType(list.get(position).getId());
            }
        });
        //删除item
        myHolder.delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeItem(position);
            }
        });
        //编辑item
        myHolder.editor_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.AddressEditor(list.get(position).getId(), list.get(position).getName(), list.get(position).getPhone(), list.get(position).getAddr(), list.get(position).getAddrInfo());
            }
        });
        myHolder.item_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("-------->","点击");
                mContext.onClickItem(list.get(position).getId(),list.get(position).getAddr() + list.get(position).getAddrInfo());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public List<AddressBean> getList() {
        return list;
    }

    /**
     * 设置要显示的数据
     *
     * @param list
     */
    public void setList(List<AddressBean> list) {
        this.list = list;
    }

    /**
     * 返回选中的Item index
     *
     * @return
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    //初始化
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        //姓名电话
        TextView name_phone_tv;
        //地址
        TextView address_tv;
        //编辑
        TextView editor_tv;
        //删除
        TextView delete_tv;
        //单选
        CheckBox cbChecked;
        //
        ConstraintLayout item_address_layout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name_phone_tv = itemView.findViewById(R.id.name_phone_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
            editor_tv = itemView.findViewById(R.id.editor_tv);
            cbChecked = itemView.findViewById(R.id.checkBox_item);
            delete_tv = itemView.findViewById(R.id.delete_tv);
            item_address_layout = itemView.findViewById(R.id.item_address_layout);
        }
    }

    //删除操作
    public void removeItem(int position) {
        mContext.AddressDelete(list.get(position).getId());
        list.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
    }

}
