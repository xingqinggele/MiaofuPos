package com.example.miaofupos.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.datafragment.databenefit.DataBenefitActivity;
import com.example.miaofupos.datafragment.databill.DataBillActivity;
import com.example.miaofupos.datafragment.datapersonalresults.DataPartnerResultsActivity;
import com.example.miaofupos.datafragment.datapersonalresults.DataPersonalResultsActivity;
import com.example.miaofupos.datafragment.datapersonalresults.DataTotalResultsActivity;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.RealNameOnActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyDialog1;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2020/12/12
 * 描述: 数据界面
 */
public class DataFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //我的收益
    private LinearLayout data_earnings_tv;
    //我的账单
    private LinearLayout data_bill_tv;
    //个人业绩
    private ConstraintLayout data_personal_results_relative;
    //伙伴业绩
    private ConstraintLayout data_partner_results_relative;
    //本月直营商户交易额
    private TextView data_price_tv1;
    //本月直营商户交易额
    private TextView data_price_tv2;
    //个人 累计伙伴
    private TextView data_new_merchants_tv;
    //个人 累计商户
    private TextView data_total_merchants_tv;
    //累计伙伴
    private TextView data_cumulative_partner_tv;
    //累计商户
    private TextView data_cumulative_merchants_tv;
    //刷新控件
    private SwipeRefreshLayout data_fragment_swipe;
    //入驻状态，1入驻2未入住
    private String Code = "1";
    //个人业绩
    private LinearLayout data_personal_tv;
    //伙伴业绩
    private LinearLayout data_partner_tv;
    //总业绩 本月直营商户交易额
    private TextView data_total_price_tv;
    //总业绩累计伙伴
    private TextView data_total_new_merchants_tv;
    //总业绩累计商户
    private TextView data_total_total_merchants_tv;
    private LinearLayout data_total_tv;

    /**
     * 接受activity数据
     *
     * @param requestJson
     * @return
     */
    public static DataFragment newInstance(String requestJson) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 界面。xml
     *
     * @return
     */
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.datafragment_main;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        data_earnings_tv = rootView.findViewById(R.id.data_earnings_tv);
        data_bill_tv = rootView.findViewById(R.id.data_bill_tv);
        data_personal_results_relative = rootView.findViewById(R.id.data_personal_results_relative);
        data_partner_results_relative = rootView.findViewById(R.id.data_partner_results_relative);
        data_price_tv1 = rootView.findViewById(R.id.data_price_tv1);
        data_price_tv2 = rootView.findViewById(R.id.data_price_tv2);
        data_new_merchants_tv = rootView.findViewById(R.id.data_new_merchants_tv);
        data_total_merchants_tv = rootView.findViewById(R.id.data_total_merchants_tv);
        data_cumulative_partner_tv = rootView.findViewById(R.id.data_cumulative_partner_tv);
        data_cumulative_merchants_tv = rootView.findViewById(R.id.data_cumulative_merchants_tv);
        data_fragment_swipe = rootView.findViewById(R.id.data_fragment_swipe);
        data_personal_tv = rootView.findViewById(R.id.data_personal_tv);
        data_partner_tv = rootView.findViewById(R.id.data_partner_tv);
        data_total_price_tv = rootView.findViewById(R.id.data_total_price_tv);
        data_total_new_merchants_tv = rootView.findViewById(R.id.data_total_new_merchants_tv);
        data_total_total_merchants_tv = rootView.findViewById(R.id.data_total_total_merchants_tv);
        data_total_tv = rootView.findViewById(R.id.data_total_tv);
    }

    //触发事件
    @Override
    protected void initListener() {
        data_earnings_tv.setOnClickListener(this);
        data_bill_tv.setOnClickListener(this);
        data_personal_tv.setOnClickListener(this);
        data_partner_tv.setOnClickListener(this);
        data_personal_results_relative.setOnClickListener(this);
        data_partner_results_relative.setOnClickListener(this);
        data_total_tv.setOnClickListener(this);
        SwipeData();
        //获取数据界面接口
        posData();
    }

    //设置下拉样式
    public void SwipeData() {
        data_fragment_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        data_fragment_swipe.setOnRefreshListener(this);
    }

    //填充数据
    @Override
    protected void loadData() {

    }

    //刷新
    @Override
    public void onRefresh() {
        //进行刷新操作
        data_fragment_swipe.setRefreshing(true);
        posData();
    }

    //数据配置
    public void posData() {
        //进行网络请求数据
        RequestParams params = new RequestParams();
        HttpRequest.getTransAmount(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    data_fragment_swipe.setRefreshing(false);
                    JSONObject result = new JSONObject(responseObj.toString());
                    data_total_price_tv.setText(new BigDecimal(result.getJSONObject("data").getString("monthlyTransAmount")).toString());
                    data_total_new_merchants_tv.setText(result.getJSONObject("data").getString("allParentsCounts"));
                    data_total_total_merchants_tv.setText(result.getJSONObject("data").getString("allMerchantCounts"));
                    data_price_tv1.setText(new BigDecimal(result.getJSONObject("data").getString("merchantTransAmount")).toString());
                    data_new_merchants_tv.setText(result.getJSONObject("data").getString("ownTotalParentCounts"));
                    data_total_merchants_tv.setText(result.getJSONObject("data").getString("ownTotalMerchantCounts"));
                    data_price_tv2.setText(new BigDecimal(result.getJSONObject("data").getString("partnerTransAmount")).toString());
                    data_cumulative_partner_tv.setText(result.getJSONObject("data").getString("parentTotalParentCounts"));
                    data_cumulative_merchants_tv.setText(result.getJSONObject("data").getString("parentTotalMerchantCounts"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                data_fragment_swipe.setRefreshing(false);
                if (failuer.getEcode() == 500001) {
                    showDialog();
                } else {
                    Failuer(failuer.getEcode(), failuer.getEmsg());
                }
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_bill_tv:
                //我的账单
                startActivity(new Intent(getActivity(), DataBillActivity.class));
                break;
            case R.id.data_personal_results_relative:
            case R.id.data_personal_tv:
                //个人业绩
                startActivity(new Intent(getActivity(), DataPersonalResultsActivity.class));
                break;
            case R.id.data_earnings_tv:
                //我的受益
                startActivity(new Intent(getActivity(), DataBenefitActivity.class));
                break;
            case R.id.data_partner_results_relative:
            case R.id.data_partner_tv:
                //伙伴业绩
                startActivity(new Intent(getActivity(), DataPartnerResultsActivity.class));
                break;
            case R.id.data_total_tv:
                //总业绩
                startActivity(new Intent(getActivity(), DataTotalResultsActivity.class));
                break;
        }
    }


    /**
     * 提示用户去完善信息，报件
     */
    public void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.perfect_dialog_layout, null);
        Button perfect_into = view.findViewById(R.id.perfect_into);
        ImageView perfect_out = view.findViewById(R.id.perfect_out);
        Dialog dialog = new MyDialog1(getActivity(), true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        perfect_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        perfect_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), RealNameOnActivity.class);
                intent.putExtra("infoCode", Code);
                startActivity(intent);
            }
        });
    }

    public void onEventMainThread(DataFragment ev) {
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
