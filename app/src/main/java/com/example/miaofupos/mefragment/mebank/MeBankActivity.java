package com.example.miaofupos.mefragment.mebank;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.useractivity.LoginActivity;
import com.example.miaofupos.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2020/12/16
 * 描述: 我的银行卡
 */
public class MeBankActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private TextView me_bank_modify_tv;
    private TextView me_bank_add_tv; // 添加银行卡按钮
    private TextView me_bank_name; // 银行卡名称
    private TextView my_bank_number; // 银行卡号
    private String bankName = "";
    private String bankCardNo = "";
    private String bankCity = "";
    private String bankReservedMobile = "";
    private String idCardName = "";
    private String idCard = "";
    private String bankCardImg = ""; //url
    private TextView me_bank_tv;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.me_bank_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        me_bank_modify_tv = findViewById(R.id.me_bank_modify_tv);
        me_bank_add_tv = findViewById(R.id.me_bank_add_tv);
        me_bank_name = findViewById(R.id.me_bank_name);
        my_bank_number = findViewById(R.id.my_bank_number);
        me_bank_tv = findViewById(R.id.me_bank_tv);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        me_bank_modify_tv.setOnClickListener(this);
        me_bank_add_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        me_bank_tv.setText("为了保障您的资金账户安全，请务必添加您本人持有的银行卡，如有疑问，请拨打客服热线：" + SPUtils.get(MeBankActivity.this, "servicePhone", "-1").toString());
        posData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.me_bank_modify_tv:
                Intent intent = new Intent(MeBankActivity.this, MeBankModifyActivity.class);
                intent.putExtra("bankName", bankName);
                intent.putExtra("bankCardNo", bankCardNo);
                intent.putExtra("bankCity", bankCity);
                intent.putExtra("bankReservedMobile", bankReservedMobile);
                intent.putExtra("idCardName", idCardName);
                intent.putExtra("idCard", idCard);
                intent.putExtra("bankCardImg", bankCardImg);
                startActivity(intent);
                break;
            case R.id.me_bank_add_tv:
                startActivity(new Intent(MeBankActivity.this, MeBankAddActivity.class));
                break;
        }
    }

    public void posData() {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.getBankInfo(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    bankName = result.getJSONObject("data").getString("bankName");
                    bankCardNo = result.getJSONObject("data").getString("bankCardNo");
                    bankCity = result.getJSONObject("data").getString("bankCity");
                    bankReservedMobile = result.getJSONObject("data").getString("bankReservedMobile");
                    idCardName = result.getJSONObject("data").getString("idCardName");
                    idCard = result.getJSONObject("data").getString("idCard");
                    bankCardImg = result.getJSONObject("data").getString("bankCardImg");
                    me_bank_name.setText(bankName + "");
                    my_bank_number.setText(bankCardNo.substring(bankCardNo.length() - 4));

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

    @Override
    protected void onRestart() {
        super.onRestart();
        posData();
    }
}
