package com.example.miaofupos.datafragment.databenefit.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.datafragment.databenefit.bean.DataBenefitBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/3/15
 * 描述:我的收益Adapter
 */
public class DataBenefitAdapter extends BaseQuickAdapter<DataBenefitBean,BaseViewHolder> {

    public DataBenefitAdapter(int layoutResId, @Nullable List<DataBenefitBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBenefitBean item) {
        helper.setText(R.id.item_data_benefit_time_tv,item.getOrderTime());
        helper.setText(R.id.item_data_benefit_total_amount_tv,item.getAggregateAmount());
        helper.setText(R.id.item_data_benefit_reward_tv,item.getActivateMoney());
        helper.setText(R.id.item_data_benefit_settlement_tv,item.getCloseMoney());
    }
}
