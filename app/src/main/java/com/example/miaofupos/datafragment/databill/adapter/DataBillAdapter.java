package com.example.miaofupos.datafragment.databill.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.datafragment.databillbean.BillBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/14
 * 描述:
 */
public class DataBillAdapter extends BaseQuickAdapter<BillBean, BaseViewHolder> {
    public DataBillAdapter(int layoutResId, @Nullable List<BillBean> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillBean item) {
        if (Float.valueOf(item.getAmount()) > 0){
            helper.setText(R.id.item_data_bill_type_tv,item.getBillTypeLabel() + " (收入)");
            helper.setText(R.id.item_data_bill_price_tv,"+ " + item.getAmount());
        }else if (Float.valueOf(item.getAmount()) == 0){
            //待研究
            helper.setText(R.id.item_data_bill_type_tv,item.getBillTypeLabel() + " (支出)");
            helper.setText(R.id.item_data_bill_price_tv,item.getAmount());
        }else {
            helper.setText(R.id.item_data_bill_type_tv,item.getBillTypeLabel() + " (支出)");
            String amount = item.getAmount().substring(1);
            helper.setText(R.id.item_data_bill_price_tv,"- " + amount);
        }
        helper.setText(R.id.item_data_bill_time_tv,item.getBillDate());

    }
}
