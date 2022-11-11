package com.example.miaofupos.homefragment.homemerchants.memerchants.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.homefragment.homeintegral.adpter.ShoppingCartAdapter;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.MeMerchantsBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/3/9
 * 描述:我的商户Adapter
 */
public class MeMerchantsAdapter extends BaseQuickAdapter<MeMerchantsBean, BaseViewHolder> {
    //回调方法
    private FoodActionCallback callback;

    public MeMerchantsAdapter(int layoutResId, @Nullable List<MeMerchantsBean> data, FoodActionCallback callback) {
        super(layoutResId, data);
        this.callback = callback;
    }

    @SuppressLint("Range")
    @Override
    protected void convert(BaseViewHolder helper, MeMerchantsBean item) {
        helper.setText(R.id.me_merchants_name, "商户姓名: " + item.getName());
        helper.setText(R.id.me_merchants_number, "商户编号: " + item.getMerchCode());
        helper.setText(R.id.me_merchants_price, new BigDecimal(item.getMonthTurnover()).toString());
        String title = "";
        String color = "";
        String edit_test = "";
        boolean edit = true;
        if (item.getIsAudit().equals("-2")) {
            title = "商户不存在";
            color = "#DC143C";
            edit = false;
        } else if (item.getIsAudit().equals("-1")) {
            title = "待完善资料";
            color = "#DC143C";
            edit = false;
        } else if (item.getIsAudit().equals("0")) {
            title = "待审核";
            color = "#3CA0FF";
            edit = false;
        } else if (item.getIsAudit().equals("1")) {
            title = "审核失败";
            color = "#DC143C";
            edit = true;
            edit_test = "重新报件";
        } else if (item.getIsAudit().equals("2")) {
            title = "人工审核";
            color = "#3CA0FF";
            edit = false;
        } else if (item.getIsAudit().equals("3")) {
            title = "审核成功";
            color = "#29D385";
            edit = true;
            edit_test = "修改";
        } else if (item.getIsAudit().equals("11")) {
            title = "注销申请";
            color = "#E73118";
            edit = false;
        } else if (item.getIsAudit().equals("12")) {
            title = "注销成功";
            color = "#E73118";
            edit = false;
        }
        helper.setVisible(R.id.edit_merchants, edit);
        helper.setText(R.id.status, title);
        helper.setText(R.id.edit_merchants, edit_test);
        helper.setTextColor(R.id.status, Color.parseColor(color));
        helper.setOnClickListener(R.id.edit_merchants, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback == null) return;
                callback.addAction(item.getMerchCode(), item.getIsAudit());
            }
        });

    }

    public interface FoodActionCallback {
        void addAction(String id, String a);
    }
}
