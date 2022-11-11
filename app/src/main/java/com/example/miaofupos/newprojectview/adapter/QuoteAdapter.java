package com.example.miaofupos.newprojectview.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.miaofupos.R;
import com.example.miaofupos.newprojectview.bean.QuoteBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2022/8/22
 * 描述:新报件Adapter
 */
public class QuoteAdapter extends BaseQuickAdapter<QuoteBean, BaseViewHolder> {
    //绑定回调方法
    private BindCallback callback;
    private EditCallback editCallback;
    public QuoteAdapter(int layoutResId, @Nullable List<QuoteBean> data, BindCallback callback, EditCallback editCallback) {
        super(layoutResId, data);
        this.callback = callback;
        this.editCallback = editCallback;
    }

    @SuppressLint("Range")
    @Override
    protected void convert(BaseViewHolder helper, QuoteBean item) {
        helper.setText(R.id.me_merchants_name, "商户姓名: " + item.getMerchantName());
        helper.setText(R.id.me_merchants_number, "商户编号: " + item.getMerchCode());
        helper.setText(R.id.me_merchants_price, new BigDecimal(item.getTransAmount()).toString());
        //修改
        boolean repair = true;
        //绑定
        boolean bind = true;
        //修改文字
        String edit_test = "";
        //绑定文字
        String bind_test = "";
        //状态颜色
        String color = "";
        //状态文字
        String title = "";

        boolean isFaill = false;

        if (item.getType().equals("1")){
            isFaill = false;
            bind = false;
            if (item.getIsAudit().equals("1")){
                title = "报件失败";
                color = "#DC143C";
                repair = true;
                edit_test = "重新报件";
            }else if (item.getIsAudit().equals("2")){
                title = "进行中";
                color = "#3CA0FF";
                repair = false;
            }else if (item.getIsAudit().equals("3")){
                title = "报件成功";
                edit_test = "修改";
                color = "#29D385";
                repair = true;
            }

        }else {
            if (item.getActivateStatus().equals("0")){
                bind_test = "未绑定";

            }else {
                bind_test = "已绑定";

            }
            if (item.getIsAudit().equals("1") ){
                title = "报件失败";
                color = "#DC143C";
                repair = true;
                bind = false;
                edit_test = "重新报件";
                isFaill = true;

            }else if (item.getIsAudit().equals("2") || item.getIsAudit().equals("0")){
                title = "进行中";
                color = "#3CA0FF";
                repair = false;
                bind = false;
                isFaill = false;
            }else if (item.getIsAudit().equals("3")){
                title = "报件成功";
                edit_test = "修改";
                color = "#29D385";
                repair = true;
                bind = true;
                isFaill = false;
            }
        }
        helper.setVisible(R.id.edit_merchants, repair);
        helper.setVisible(R.id.bind_merchants, bind);
        helper.setText(R.id.tv_status, title);
        helper.setTextColor(R.id.tv_status, Color.parseColor(color));
        helper.setText(R.id.edit_merchants, edit_test);
        helper.setText(R.id.bind_merchants, bind_test);

        helper.setVisible(R.id.failure_btn, isFaill);
        //绑定
        helper.setOnClickListener(R.id.bind_merchants, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback == null) return;
                if (!item.getActivateStatus().equals("0"))return;
                    callback.addBind(item.getMerchCode(),item.getId());
            }
        });

        //修改
        helper.setOnClickListener(R.id.edit_merchants, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editCallback == null) return;
                editCallback.edit(item.getMerchCode(),item.getIsAudit(),item.getType());
            }
        });
        helper.setOnClickListener(R.id.failure_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(helper.itemView.getContext(),item.getAuditMsg(),Toast.LENGTH_LONG).show();
            }
        });
    }

    //绑定接口
    public interface BindCallback {
        void addBind(String id, String a);
    }

    //修改接口
    public interface EditCallback{
        void edit(String id,String type,String isSta);
    }
}