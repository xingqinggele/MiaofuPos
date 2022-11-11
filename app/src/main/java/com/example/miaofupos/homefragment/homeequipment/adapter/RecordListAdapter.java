package com.example.miaofupos.homefragment.homeequipment.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.bean.MerMachineBean;
import com.example.miaofupos.homefragment.homeequipment.bean.CallbackRecordBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/14
 * 描述: 调拨记录Adapter
 */
public class RecordListAdapter extends BaseQuickAdapter<CallbackRecordBean, BaseViewHolder> {
    private boolean value = true;
    public RecordListAdapter(int layoutResId, @Nullable List<CallbackRecordBean> data,boolean value) {
        super(layoutResId,data);
        this.value = value;
    }

    @Override
    protected void convert(BaseViewHolder helper, CallbackRecordBean item) {
        if (value){
            helper.setText(R.id.item_callback_record_item_title,"划拨成功");
        }else {
            helper.setText(R.id.item_callback_record_item_title,"回调成功");
        }
        helper.setText(R.id.item_callback_record_item_num,item.getPosCounts());
        helper.setText(R.id.item_callback_record_item_issued_person,item.getAdjustName());
        helper.setText(R.id.item_callback_record_item_accept_person,item.getAllocName());
        helper.setText(R.id.item_callback_record_item_time,item.getOperateTime());
    }
}
