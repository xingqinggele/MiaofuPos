package com.example.miaofupos.mefragment.setup;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;

import static com.example.miaofupos.utils.Utility.isChinaPhoneLegal;

/**
 * 作者: qgl
 * 创建日期：2020/12/26
 * 描述:设置支付密码(1)
 */
public class MePayPassActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private Button pay_password_bt;
    private EditText me_pay_password_phone_ed;
    private EditText pay_password_code_ed;
//    private EditText pay_password_id_card_ed;
    private TextView pay_password_mCode_tv;
    private boolean phone = true;
    private boolean code = false;
//    private boolean idCard = false;

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.me_pay_pass_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        pay_password_bt = findViewById(R.id.pay_password_bt);
        me_pay_password_phone_ed = findViewById(R.id.me_pay_password_phone_ed);
        pay_password_code_ed = findViewById(R.id.pay_password_code_ed);
        pay_password_mCode_tv = findViewById(R.id.pay_password_mCode_tv);
//        pay_password_id_card_ed = findViewById(R.id.pay_password_id_card_ed);


    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        pay_password_bt.setEnabled(false);
        pay_password_bt.setOnClickListener(this);
        pay_password_mCode_tv.setOnClickListener(this);
        me_pay_password_phone_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                convenient(1, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pay_password_code_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                convenient(2, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        pay_password_id_card_ed.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                convenient(3, charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    @Override
    protected void initData() {
        me_pay_password_phone_ed.setText(getUserName().substring(0,3) + "****" + getUserName().substring(getUserName().length() - 4));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.pay_password_bt:
               // startActivity(new Intent(this,MePayPassTwoActivity.class));
                posData();
                break;
            case R.id.pay_password_mCode_tv:
//                if (isChinaPhoneLegal(getUserName())) {
                    //发送短信
                    getPhoneCode(getUserName(),pay_password_mCode_tv);
//                } else {
//                    Toast.makeText(MePayPassActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
//                }
                break;

        }
    }

    /**
     * 判断输入框是否都有值
     */
    public void convenient(int type, String value) {
        if (type == 1) {
            if (value.equals("")) {
                phone = false;
            } else {
                phone = true;
            }
        } else if (type == 2) {
            if (value.equals("")) {
                code = false;
            } else {
                code = true;
            }
        }
//        else {
//            if (value.equals("")) {
//                idCard = false;
//            } else {
//                idCard = true;
//            }
//        }
//        && idCard == true
        if (phone == true && code == true ) {
            pay_password_bt.setTextColor(Color.parseColor("#FFFFFF"));
            pay_password_bt.setBackground(getDrawable(R.drawable.login_btn_on_bg));
            pay_password_bt.setEnabled(true);
        } else {
            pay_password_bt.setTextColor(Color.parseColor("#B2B1B1"));
            pay_password_bt.setBackground(getDrawable(R.drawable.pay_password_btn_bg));
            pay_password_bt.setEnabled(false);
        }
    }

    //发送数据，，判断输入是否正确
    public void posData(){
        RequestParams params = new RequestParams();
        params.put("verifyCode",pay_password_code_ed.getText().toString().trim());
//        params.put("cardNo",pay_password_id_card_ed.getText().toString().trim());
        params.put("mobile",getUserName());
        HttpRequest.getPay_password1(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                startActivity(new Intent(MePayPassActivity.this,MePayPassTwoActivity.class));
                finish();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(),failuer.getEmsg());
            }
        });

    }
}
