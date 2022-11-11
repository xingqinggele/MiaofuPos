package com.example.miaofupos.datafragment.databill;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.DataBillSettlementDetailActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2021/3/24
 * 描述: 账单详情
 */
public class DataBillDetailActivity extends BaseActivity implements View.OnClickListener {
    //列表ID
    private String billId = "";
    //列表类型value
    private String billTypeValue = "";
    //当前界面xml
    private int layout;
    //返回键
    private LinearLayout iv_back;
    /*************返现**************/
    //返现金额
    private TextView detail_activity_price;
    //返现极具编号
    private TextView detail_activity_pos_number;
    //返现创建时间
    private TextView detail_activity_time;
    /*************结算**************/
    //结算金额
    private TextView detail_settlement_price;
    //结算查看分润明细
    private TextView detail_settlement_detail;
    //结算创建时间
    private TextView detail_settlement_time;
    /*************提现**************/
    //提现金额1
    private TextView detail_withdrawal_price;
    //提现 ，开户银行、尾号
    private TextView detail_withdrawal_bank_num;
    //提现金额2
    private TextView detail_withdrawal_amount;
    //提现订单号
    private TextView detail_withdrawal_order_num;
    //提现创建时间
    private TextView detail_withdrawal_time;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        //根据账单列表返回的类型，判断显示不一样的详情页
        billTypeValue = getIntent().getStringExtra("billTypeValue");
        switch (billTypeValue){
            //1.返现
            case "1":
                layout = R.layout.data_bill_detail_activation_activity;
                break;
                //2.结算
            case "2":
                layout = R.layout.data_bill_detail_settlement_activity;
                break;
                //4.提现
            case "4":
                layout = R.layout.data_bill_detail_withdrawal_activity;
                break;
            default:
                break;
        }
        return layout;
    }


    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        //提现
        detail_activity_price = findViewById(R.id.detail_activity_price);
        detail_activity_pos_number = findViewById(R.id.detail_activity_pos_number);
        detail_activity_time = findViewById(R.id.detail_activity_time);
        //结算
        detail_settlement_price = findViewById(R.id.detail_settlement_price);
        detail_settlement_detail = findViewById(R.id.detail_settlement_detail);
        detail_settlement_time = findViewById(R.id.detail_settlement_time);
        //提现
        detail_withdrawal_price = findViewById(R.id.detail_withdrawal_price);
        detail_withdrawal_bank_num = findViewById(R.id.detail_withdrawal_bank_num);
        detail_withdrawal_amount = findViewById(R.id.detail_withdrawal_amount);
        detail_withdrawal_order_num = findViewById(R.id.detail_withdrawal_order_num);
        detail_withdrawal_time = findViewById(R.id.detail_withdrawal_time);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        if (billTypeValue.equals("2")){
            detail_settlement_detail.setOnClickListener(this);
        }
    }

    @Override
    protected void initData() {
        //获取列表ID
        billId = getIntent().getStringExtra("billId");
        //请求接口，获取数据
        posData(billId,billTypeValue);
    }

    /**
     * 请求订单详情接口，获取数据
     *
     * @param billId 列表ID
     */
    private void posData(String billId,String billTypeValue) {
        RequestParams params = new RequestParams();
        //账单ID
        params.put("billId", billId);
        //账单类型
        params.put("billType",billTypeValue);
        //发起请求
        HttpRequest.getBillDetails(params, getToken(), new ResponseCallback() {
            //返回成功
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    switch (billTypeValue){
                        case "1":
                            detail_activity_price.setText(result.getJSONObject("data").getString("amount"));
                            detail_activity_pos_number.setText(result.getJSONObject("data").getString("posCode"));
                            detail_activity_time.setText(result.getJSONObject("data").getString("billDate"));
                            break;
                        case "2":
                            detail_settlement_price.setText(result.getJSONObject("data").getString("fourAfterTax"));
                            detail_settlement_time.setText(result.getJSONObject("data").getString("createTime"));
                            break;
                        case "4":
                            detail_withdrawal_bank_num.setText(result.getJSONObject("data").getString("dealBank"));
                            detail_withdrawal_price.setText(result.getJSONObject("data").getString("cashoutAmount"));
                            detail_withdrawal_amount.setText(result.getJSONObject("data").getString("cashoutAmount") + "元");
                            detail_withdrawal_order_num.setText(result.getJSONObject("data").getString("var2"));
                            detail_withdrawal_time.setText(result.getJSONObject("data").getString("depositTime"));
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //关闭加载框
            @Override
            public void onFailure(OkHttpException failuer) {
                //调用失败的返回方法
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回键
            case R.id.iv_back:
                finish();
                break;
                //查看结算明细
            case R.id.detail_settlement_detail:
                Intent intent = new Intent(this, DataBillSettlementDetailActivity.class);
                intent.putExtra("billId",billId);
                intent.putExtra("billType",billTypeValue);
                startActivity(intent);
                break;
        }
    }

}
