package com.example.miaofupos.datafragment;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.databillbean.DataBillSettlementDetailBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.PieChartUtil;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/3/24
 * 描述:结算详情
 */
public class DataBillSettlementDetailActivity extends BaseActivity implements View.OnClickListener {
    //饼状图
    private PieChart mPieChart;
    //颜色
    int color[];
    //保存
    private List<DataBillSettlementDetailBean> mData_b = new ArrayList<>();
    //返回键
    private LinearLayout iv_back;
    //自身交易笔数
    private TextView bill_detail_tv1;
    //自身交易金额
    private TextView bill_detail_tv2;
    //自身交易刷卡
    private TextView bill_detail_tv3;
    //自身交易闪付
    private TextView bill_detail_tv4;
    //自身交易扫码
    private TextView bill_detail_tv5;
    //下级整体交易笔数
    private TextView bill_detail_tv6;
    //下级整体交易量
    private TextView bill_detail_tv7;
    //下级整体交易量刷卡
    private TextView bill_detail_tv8;
    //下级整体交易量闪付
    private TextView bill_detail_tv9;
    //下级整体交易量扫码
    private TextView bill_detail_tv10;
    //交易分润比例
    private TextView bill_detail_tv11;
    //交易分润
    private TextView bill_detail_tv12;
    //管理津贴
    private TextView bill_detail_tv13;
    //皇冠比例
    private TextView bill_detail_tv14;
    //皇冠津贴
    private TextView bill_detail_tv15;
    //钻石奖金
    private TextView bill_detail_tv16;
    //分润补贴
    private TextView bill_detail_tv17;
    //五项总计
    private TextView bill_detail_tv18;
    //五项税后
    private TextView bill_detail_tv19;
    //账单ID
    private String billId;
    //账单类型
    private String billTypeValue = "2";
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.data_bill_settlement_detail_activity;
    }

    @Override
    protected void initView() {
        mPieChart = findViewById(R.id.pie_chart);
        iv_back = findViewById(R.id.iv_back);
        bill_detail_tv1 = findViewById(R.id.bill_detail_tv1);
        bill_detail_tv2 = findViewById(R.id.bill_detail_tv2);
        bill_detail_tv3 = findViewById(R.id.bill_detail_tv3);
        bill_detail_tv4 = findViewById(R.id.bill_detail_tv4);
        bill_detail_tv5 = findViewById(R.id.bill_detail_tv5);
        bill_detail_tv6 = findViewById(R.id.bill_detail_tv6);
        bill_detail_tv7 = findViewById(R.id.bill_detail_tv7);
        bill_detail_tv8 = findViewById(R.id.bill_detail_tv8);
        bill_detail_tv9 = findViewById(R.id.bill_detail_tv9);
        bill_detail_tv10 = findViewById(R.id.bill_detail_tv10);
        bill_detail_tv11 = findViewById(R.id.bill_detail_tv11);
        bill_detail_tv12 = findViewById(R.id.bill_detail_tv12);
        bill_detail_tv13 = findViewById(R.id.bill_detail_tv13);
        bill_detail_tv14 = findViewById(R.id.bill_detail_tv14);
        bill_detail_tv15 = findViewById(R.id.bill_detail_tv15);
        bill_detail_tv16 = findViewById(R.id.bill_detail_tv16);
        bill_detail_tv17 = findViewById(R.id.bill_detail_tv17);
        bill_detail_tv18 = findViewById(R.id.bill_detail_tv18);
        bill_detail_tv19 = findViewById(R.id.bill_detail_tv19);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        billId = getIntent().getStringExtra("billId");
        billTypeValue = getIntent().getStringExtra("billType");
        posData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
    //获取
    public void posData(){
        //进行网络请求数据
        RequestParams params = new RequestParams();
        params.put("billId",billId);
        params.put("billType",billTypeValue);
        HttpRequest.getBillDetails(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    /**************************饼状图数据配置**************************/
                    color = new int[]{
                            Color.rgb(252, 137, 51),
                            Color.rgb(0, 199, 225),
                            Color.rgb(172, 213, 152),
                            Color.rgb(255, 218, 68),
                            Color.rgb(241, 158, 194),
                    };
                    DataBillSettlementDetailBean expenseStaBean1 = new DataBillSettlementDetailBean();
                    expenseStaBean1.setNum(result.getJSONObject("data").getString("dealShare"));
                    expenseStaBean1.setState("交易分润");
                    mData_b.add(expenseStaBean1);
                    DataBillSettlementDetailBean expenseStaBean2 = new DataBillSettlementDetailBean();
                    expenseStaBean2.setNum(result.getJSONObject("data").getString("manaAllow"));
                    expenseStaBean2.setState("管理津贴");
                    mData_b.add(expenseStaBean2);
                    DataBillSettlementDetailBean expenseStaBean3 = new DataBillSettlementDetailBean();
                    expenseStaBean3.setNum(result.getJSONObject("data").getString("diamondBonus"));
                    expenseStaBean3.setState("钻石奖金");
                    mData_b.add(expenseStaBean3);
                    DataBillSettlementDetailBean expenseStaBean4 = new DataBillSettlementDetailBean();
                    expenseStaBean4.setNum(result.getJSONObject("data").getString("crownAward"));
                    expenseStaBean4.setState("皇冠奖金");
                    mData_b.add(expenseStaBean4);
                    DataBillSettlementDetailBean expenseStaBean5 = new DataBillSettlementDetailBean();
                    expenseStaBean5.setNum(result.getJSONObject("data").getString("shareSubsidy"));
                    expenseStaBean5.setState("分润补贴");
                    mData_b.add(expenseStaBean5);
                    PieChartUtil.getPitChart().setPieChart(mPieChart, mData_b, "五项统计", result.getJSONObject("data").getString("fourAfterTax"), false, color);
                    /**************************数据配置**************************/
                    //自身交易笔数
                    bill_detail_tv1.setText(result.getJSONObject("data").getString("ownDealNum") + "笔");
                    //自身交易金额
                    String ownDealSum = new BigDecimal(result.getJSONObject("data").getString("ownDealSum")).toString();
                    bill_detail_tv2.setText( "￥" + ownDealSum);
                    //自身交易刷卡
                    String ownSlotCard = new BigDecimal(result.getJSONObject("data").getString("ownSlotCard")).toString();
                    bill_detail_tv3.setText( "￥" + ownSlotCard);
                    //自身交易闪付
                    String ownQuickPass = new BigDecimal(result.getJSONObject("data").getString("ownQuickPass")).toString();
                    bill_detail_tv4.setText( "￥" + ownQuickPass);
                    //自身交易扫码
                    String ownScanPay = new BigDecimal(result.getJSONObject("data").getString("ownScanPay")).toString();
                    bill_detail_tv5.setText( "￥" + ownScanPay);
                    //下级整体交易笔数
                    bill_detail_tv6.setText(result.getJSONObject("data").getString("downAllDealNum") + "笔");
                    //下级整体交易量
                    String downAllDealSum = new BigDecimal(result.getJSONObject("data").getString("downAllDealSum")).toString();
                    bill_detail_tv7.setText("￥" + downAllDealSum);
                    //下级整体交易量刷卡
                    String downAllSlotCard = new BigDecimal(result.getJSONObject("data").getString("downAllSlotCard")).toString();
                    bill_detail_tv8.setText("￥" + downAllSlotCard);
                    //下级整体交易量闪付
                    String downAllQuickPass = new BigDecimal(result.getJSONObject("data").getString("downAllQuickPass")).toString();
                    bill_detail_tv9.setText("￥" + downAllQuickPass);
                    //下级整体交易量扫码
                    String downAllScanPay = new BigDecimal(result.getJSONObject("data").getString("downAllScanPay")).toString();
                    bill_detail_tv10.setText("￥" + downAllScanPay);
                    //交易分润比例
                    String dealShareRatio = new BigDecimal(result.getJSONObject("data").getString("dealShareRatio")).toString();
                    bill_detail_tv11.setText(dealShareRatio + "%");
                    //交易分润
                    bill_detail_tv12.setText("￥" + result.getJSONObject("data").getString("dealShare"));
                    //管理津贴
                    bill_detail_tv13.setText("￥" + result.getJSONObject("data").getString("manaAllow"));
                    //皇冠比例
                    String crownRatio = new BigDecimal(result.getJSONObject("data").getString("crownRatio")).toString();
                    bill_detail_tv14.setText(crownRatio + "%");
                    //皇冠奖金
                    bill_detail_tv15.setText("￥" + result.getJSONObject("data").getString("crownAward"));
                    //钻石奖金
                    bill_detail_tv16.setText("￥" + result.getJSONObject("data").getString("diamondBonus"));
                    //分润补贴
                    bill_detail_tv17.setText("￥" + result.getJSONObject("data").getString("shareSubsidy"));
                    //五项总计
                    bill_detail_tv18.setText("￥" + result.getJSONObject("data").getString("fourAggregate"));
                    //五项税后
                    bill_detail_tv19.setText("￥" + result.getJSONObject("data").getString("fourAfterTax"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                    Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }
}
