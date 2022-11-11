package com.example.miaofupos.homefragment.homemerchants.memerchants.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.fragment.HomeFragment;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.EquipmentEvnBusBean;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.TradingEvnBusBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2021/3/10
 * 描述:交易Fragment
 */
public class TradingFragment extends BaseFragment {
    //本月交易额
    private TextView monthAmount;
    //总交易额
    private TextView allAmount;
    //To本月交易额
    private TextView tmonthAmount;
    //刷卡交易笔数
    private TextView tnum;
    //列表ID
    private static String snCode = "";
    //闪付交易
//    private TextView flash_pay;
    //闪付交易笔数
//    private TextView fb_num;
    //扫码交易
    private TextView scan_the_code;
    //扫码交易笔数
    private TextView stc_num;

    /**
     * 接受activity数据
     *
     * @param requestJson
     * @return
     */
    public static TradingFragment newInstance(String requestJson) {
        TradingFragment fragment = new TradingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        snCode = requestJson;
        return fragment;
    }
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.trading_fragment;
    }

    @Override
    protected void initView(View rootView) {
        monthAmount = rootView.findViewById(R.id.monthAmount);
        allAmount = rootView.findViewById(R.id.allAmount);
        tmonthAmount = rootView.findViewById(R.id.tmonthAmount);
        tnum = rootView.findViewById(R.id.tnum);
//        flash_pay = rootView.findViewById(R.id.flash_pay);
//        fb_num = rootView.findViewById(R.id.fb_num);
        scan_the_code = rootView.findViewById(R.id.scan_the_code);
        stc_num = rootView.findViewById(R.id.stc_num);
        posData();
    }

    //请求接口 -->交易信息
    public void posData() {
        RequestParams params = new RequestParams();
        params.put("id", snCode);
        HttpRequest.getMeMerchants_detailTrading(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    monthAmount.setText("" + new BigDecimal(result.getJSONObject("data").getString("thisMonthAmount")).toString());
                    allAmount.setText("" + new BigDecimal(result.getJSONObject("data").getString("addAmount")).toString());

                    tmonthAmount.setText("" + new BigDecimal(result.getJSONObject("data").getString("thisMonthAmountSlotCard")).toString() + "元");
                    tnum.setText("" + result.getJSONObject("data").getString("monthSlotCardNum") + "笔");

//                    flash_pay.setText("" + new BigDecimal(result.getJSONObject("data").getString("thisMonthAmountQuickPass")).toString() + "元");
//                    fb_num.setText("" + result.getJSONObject("data").getString("monthQuickPassNum") + "笔");

                    scan_the_code.setText("" + new BigDecimal(result.getJSONObject("data").getString("thisMonthAmountScanPay")).toString() + "元");
                    stc_num.setText("" + "" + result.getJSONObject("data").getString("monthScanPayNum") + "笔");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(),failuer.getEmsg());
            }
        });
    }

}
