package com.example.miaofupos.homefragment.transaction.adpter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.transaction.bean.TransactionBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/10/29
 * 描述:交易记录Adapter
 */
public class TransactionAdapter extends BaseQuickAdapter<TransactionBean, BaseViewHolder> {

    public TransactionAdapter(int layoutResId, @Nullable List<TransactionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransactionBean item) {
        helper.setText(R.id.trans_name, item.getShmc());
        helper.setText(R.id.trans_price, "￥"+item.getJyje());
        helper.setText(R.id.trans_number, item.getShbh());
        helper.setText(R.id.trans_static, item.getJyzt());
        helper.setText(R.id.trans_type, item.getJymc());
        helper.setText(R.id.trans_card_type, item.getKlx());
        helper.setText(R.id.trans_rate, item.getFl());
        helper.setText(R.id.trans_bank_card, item.getYhk());
        helper.setText(R.id.trans_time, item.getJysj());
        helper.setText(R.id.trans_agent, item.getSs());
    }

}