package com.example.miaofupos.datafragment.datapersonalresults.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.example.miaofupos.R;
import com.example.miaofupos.datafragment.datapersonalresults.bean.DataPersonBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/3/15
 * 描述:新的交易量Adapter
 */
public class DataTradingVolumeAdapter extends BaseQuickAdapter<DataPersonBean, BaseViewHolder> {
    public DataTradingVolumeAdapter(int layoutResId, @Nullable List<DataPersonBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataPersonBean item) {
        helper.setText(R.id.item_data_day_results_list_time,item.getDay());
        helper.setText(R.id.item_data_day_results_item_partner_num,item.getTotalPartnerNum() + "人");
        helper.setText(R.id.item_data_day_results_item_merchants_num,item.getTatalActionNum() + "户");
        helper.setText(R.id.item_data_day_results_item_total_volume_num,new BigDecimal(item.getTotalTransAmount()).toString() + "元");
        helper.setText(R.id.item_data_day_results_item_tone_total,new BigDecimal(item.getTotalShuka()).toString() + "元");
        helper.setText(R.id.item_data_day_results_item_flash_total,new BigDecimal(item.getTotalShaoma()).toString() + "元");
//        helper.setText(R.id.item_data_day_results_item_scan_total,new BigDecimal(item.getTotalShanfu()).toString() + "元");
//        helper.setText(R.id.item_data_day_results_item_ss_tone_total,new BigDecimal(item.getSftTotalShuka()).toString() + "元");
//        helper.setText(R.id.item_data_day_results_item_ss_flash_total,new BigDecimal(item.getSftTotalShaoma()).toString() + "元");
    }
}
