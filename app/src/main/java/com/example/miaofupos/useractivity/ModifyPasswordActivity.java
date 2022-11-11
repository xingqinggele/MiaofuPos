package com.example.miaofupos.useractivity;

import android.text.TextUtils;
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
import com.example.miaofupos.utils.SPUtils;

import static com.example.miaofupos.utils.Utility.isChinaPhoneLegal;

/**
 * 作者: qgl
 * 创建日期：2020/12/24
 * 描述:修改密码
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private EditText retrieve_pass_et_userName;
    private EditText retrieve_pass_et_code;
    private TextView retrieve_pass_tv_mCode;
    private EditText retrieve_pass_et_password;
    private EditText retrieve_pass_et_password1;
    private Button retrieve_pass_btn;

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.modify_password_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        retrieve_pass_et_userName = findViewById(R.id.retrieve_pass_et_userName);
        retrieve_pass_et_code = findViewById(R.id.retrieve_pass_et_code);
        retrieve_pass_et_password = findViewById(R.id.retrieve_pass_et_password);
        retrieve_pass_et_password1 = findViewById(R.id.retrieve_pass_et_password1);
        retrieve_pass_btn = findViewById(R.id.retrieve_pass_btn);
        retrieve_pass_tv_mCode = findViewById(R.id.retrieve_pass_tv_mCode);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        retrieve_pass_btn.setOnClickListener(this);
        retrieve_pass_tv_mCode.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        retrieve_pass_et_userName.setText(getUserName().substring(0,3) + "****" + getUserName().substring(getUserName().length() - 4));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.retrieve_pass_btn:
                if (!isChinaPhoneLegal(getUserName())) {
                    showToast(3, "请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(retrieve_pass_et_code.getText().toString().trim())) {
                    showToast(3, "请输入验证码");
                    return;
                }
//                if (TextUtils.isEmpty(retrieve_pass_et_password.getText().toString().trim())) {
//                    showToast(3, "请输入新密码");
//                    return;
//                }
                if (retrieve_pass_et_password.getText().toString().trim().length() < 6) {
                    showToast(3, "密码长度不可小于6位");
                    return;
                }

                if (TextUtils.isEmpty(retrieve_pass_et_password1.getText().toString().trim())) {
                    showToast(3, "请再次输入密码");
                    return;
                }
                if (!retrieve_pass_et_password.getText().toString().trim().equals(retrieve_pass_et_password1.getText().toString().trim())) {
                    showToast(3, "两次输入密码不一致");
                    return;
                }
                getRetrieve(retrieve_pass_et_code.getText().toString().trim(), getUserName(), retrieve_pass_et_password.getText().toString().trim());
                break;
            case R.id.retrieve_pass_tv_mCode:
                if (isChinaPhoneLegal(getUserName())) {
                    //发送短信
                    getPhoneCode(getUserName(),retrieve_pass_tv_mCode);
                } else {
                    Toast.makeText(ModifyPasswordActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    public void getRetrieve(String code, String userName, String passWord) {
        RequestParams params = new RequestParams();
        params.put("mobile", userName);
        params.put("password", passWord);
        params.put("verifyCode", code);
        HttpRequest.getPass(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Toast.makeText(ModifyPasswordActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
                // 退出登录,清除本地数据
                SPUtils.clear(ModifyPasswordActivity.this);
                exitApp(ModifyPasswordActivity.this);
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(),failuer.getEmsg());

            }
        });

    }
}
