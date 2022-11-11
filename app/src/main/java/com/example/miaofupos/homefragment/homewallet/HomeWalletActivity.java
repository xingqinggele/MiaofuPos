package com.example.miaofupos.homefragment.homewallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.databill.DataBillActivity;
import com.example.miaofupos.homefragment.homewallet.activity.WithdrawalActivity;
import com.example.miaofupos.mefragment.setup.MePayPassActivity;
import com.example.miaofupos.mefragment.setup.MeSigningActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyDialog1;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.miaofupos.utils.DESHelperUtil.decrypt;

/**
 * 作者: qgl
 * 创建日期：2020/12/16
 * 描述:我的钱包
 */
public class HomeWalletActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //账单明细按钮
    private TextView home_wallet_detail;
    //结算账户提现按钮
    private TextView wallet_settlement_bt;
    //奖励账户提现按钮
    private TextView wallet_reward_bt;
    //总余额
    private TextView data_price_tv1;
    //结算款账户余额
    private TextView data_price_tv_rewardAmount;
    //奖励账户余额
    private TextView data_price_tv_walletAmount;
    //是否设置支付密码标识
    private String patType = "0"; // 0 是未设置支付密码 ，1 已设置支付密码
    //0 未签约 1签约失败 2 审核中 3 签约成功
    private String Signing = "0";
    private String ret_msg = "";
    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.home_wallet_activity;
    }

    //控件初始化
    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        wallet_settlement_bt = findViewById(R.id.wallet_settlement_bt);
        home_wallet_detail = findViewById(R.id.home_wallet_detail);
        wallet_reward_bt = findViewById(R.id.wallet_reward_bt);
        data_price_tv1 = findViewById(R.id.data_price_tv1);
        data_price_tv_rewardAmount = findViewById(R.id.data_price_tv_rewardAmount);
        data_price_tv_walletAmount = findViewById(R.id.data_price_tv_walletAmount);
        posDate();
        posSigning();
    }

    //事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        wallet_settlement_bt.setOnClickListener(this);
        home_wallet_detail.setOnClickListener(this);
        wallet_reward_bt.setOnClickListener(this);
    }

    //数据处理
    @Override
    protected void initData() {

    }

    //点击事件触发
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, WithdrawalActivity.class);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.wallet_settlement_bt:
                if (patType.equals("0")) {
                    showDialog("您还未设置支付密码，是否前往设置支付密码？", 1);
                    return;
                }
                if (!Signing.equals("2")) {
                    showDialog("您是否前往签约？", 2);
                    return;
                }
                intent.putExtra("walletType", "1");  //结算提现
                startActivity(intent);
                break;
            case R.id.home_wallet_detail:
                startActivity(new Intent(this, DataBillActivity.class));
                break;
            case R.id.wallet_reward_bt:
                if (patType.equals("0")) {
                    showDialog("您还未设置支付密码，是否前往设置支付密码？", 1);
                    return;
                }
                if (!Signing.equals("2")) {
                    showDialog("您是否前往签约？", 2);
                    return;
                }
                intent.putExtra("walletType", "2");  //奖励提现
                startActivity(intent);
                break;
        }
    }

    //获取余额信息
    public void posDate() {
        RequestParams params = new RequestParams();
        HttpRequest.getBalanceOf(params, getToken(), new ResponseCallback() {
            //成功回调
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    //钱包总额
                    String walletAmount = result.getJSONObject("data").getString("walletAmount");
                    //结算账户总额
                    String profitAmount = result.getJSONObject("data").getString("profitAmount");
                    //奖励账户总额
                    String rewardAmount = result.getJSONObject("data").getString("rewardAmount");
                    //解密Key
                    String secretKey = result.getJSONObject("data").getString("secretKey");
                    //钱包总额
                    if (walletAmount.length() > 5) {
                        data_price_tv1.setText(decrypt(secretKey, walletAmount));
                    }
                    //结算账户总额
                    if (profitAmount.length() > 5) {
                        data_price_tv_rewardAmount.setText(decrypt(secretKey, profitAmount));
                        //调用判断方法、结算账户总额
                        TextBackColor(wallet_settlement_bt, Double.parseDouble(decrypt(secretKey, profitAmount)));
                    }
                    //奖励账户总额
                    if (rewardAmount.length() > 5) {
                        data_price_tv_walletAmount.setText(decrypt(secretKey, rewardAmount));
                        //调用判断方法、奖励账户总额
                        TextBackColor(wallet_reward_bt, Double.parseDouble(decrypt(secretKey, rewardAmount)));
                    }
                    //判断是否设置了支付密码
                    if (!result.getJSONObject("data").getString("payPassword").equals("") && !result.getJSONObject("data").getString("payPassword").equals("null")) {
                        patType = "1";
                    } else {
                        patType = "0";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //设置支付密码提示Dialog
    private void showDialog(String value, int index) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(value);
        Dialog dialog = new MyDialog1(HomeWalletActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (index == 1) {
                    startActivity(new Intent(HomeWalletActivity.this, MePayPassActivity.class));
                } else {
                    Intent intent = new Intent(HomeWalletActivity.this, MeSigningActivity.class);
                    intent.putExtra("Signing", Signing);
                    intent.putExtra("ret_msg", ret_msg);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    //返回到当前界面，调用此方法
    @Override
    protected void onRestart() {
        super.onRestart();
        //从新获取钱包数据
        posDate();
        posSigning();
    }

    /**
     * 判断金额大小显示不一样的效果
     *
     * @param textView
     * @param value    金额
     */
    private void TextBackColor(TextView textView, Double value) {
        if (value <= 0) {
            textView.setBackgroundResource(R.drawable.merchants_detail_failure_btn_bg1);
            textView.setClickable(false);
        } else {
            textView.setBackgroundResource(R.drawable.merchants_detail_failure_btn_bg);
            textView.setClickable(true);
        }
    }

    //获取签约状态
    private void posSigning() {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.getSigning(params, getToken(),new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject object = new JSONObject(responseObj.toString());
                    Signing = object.getString("data");
                    if (!Signing.equals("2")) {
                        ret_msg = object.getString("ret_msg");
                    }
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
