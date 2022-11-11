package com.example.miaofupos.homefragment.homemessage.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homemessage.bean.BusMessageBean;

import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/11/13
 * 描述:业务消息adapter
 */
public class BusinessNewsAdapter extends BaseQuickAdapter<BusMessageBean, BaseViewHolder> {
    /**
     * 重载
     *
     * @param layoutResId xml界面
     * @param data        数据
     */
    public BusinessNewsAdapter(int layoutResId, @Nullable List<BusMessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BusMessageBean item) {
        //标题
        String title = "";
        //图标
        int src;
        //判断返回类型 1.入库 2.极具激活 3.预约提现成功 4.提现成功 6.兑换申请 7.兑换 8.返积分
        if (item.getMsgType().equals("1")) {
            title = "入库";
            src = R.mipmap.business_message_iv4;
            helper.setVisible(R.id.my_tv,true);
        } else if (item.getMsgType().equals("2")) {
            title = "机具激活";
            src = R.mipmap.business_message_iv5;
            helper.setVisible(R.id.my_tv,true);

        } else if (item.getMsgType().equals("3")) {
            title = "预约提现成功";
            src = R.mipmap.business_message_iv2;
            helper.setVisible(R.id.my_tv,true);

        } else if (item.getMsgType().equals("4")) {
            title = "提现成功";
            src = R.mipmap.business_message_iv1;
            helper.setVisible(R.id.my_tv,true);

        } else if (item.getMsgType().equals("5")) {
            title = "提现失败";
            src = R.mipmap.business_message_iv1;
            helper.setVisible(R.id.my_tv,true);

        } else if (item.getMsgType().equals("6")) {
            title = "兑换申请";
            src = R.mipmap.business_message_iv8;
            helper.setVisible(R.id.my_tv,true);

        } else if (item.getMsgType().equals("7")) {
            title = "兑换";
            src = R.mipmap.business_message_iv7;
            helper.setVisible(R.id.my_tv,true);

        } else if (item.getMsgType().equals("8")) {
            title = "返积分";
            src = R.mipmap.business_message_iv6;
            helper.setVisible(R.id.my_tv,true);

        } else if (item.getMsgType().equals("9")){
            title = "报件通知";
            src = R.mipmap.business_message_iv1;
            helper.setVisible(R.id.my_tv,false);
        }else {
            title = "未知";
            src = R.mipmap.business_message_iv1;
            helper.setVisible(R.id.my_tv,true);
        }
        //图标显示
        helper.setImageResource(R.id.item_business_message_iv, src);
        //标题显示
        helper.setText(R.id.item_business_message_tv_title, title);
        //内容显示
        helper.setText(R.id.item_business_message_tv_content, item.getMsgContent());
        //时间显示
        helper.setText(R.id.item_business_message_time, item.getCreateTime());
    }
}
