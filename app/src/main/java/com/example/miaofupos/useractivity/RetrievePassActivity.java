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
import com.example.miaofupos.utils.CountDownTimerUtils;
import com.example.miaofupos.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.miaofupos.utils.Utility.isChinaPhoneLegal;

/**
 * 作者: qgl
 * 创建日期：2020/12/23
 * 描述:忘记密码
 */
public class RetrievePassActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private EditText retrieve_pass_et_userName; // 手机号
    private EditText retrieve_pass_et_code; // 验证码
    private EditText retrieve_pass_et_password; // 新密码
    private EditText retrieve_pass_et_password1; // 再次输入密码
    private Button retrieve_pass_btn; // 提交修改
    private TextView retrieve_pass_tv_mCode; // 倒计时控件
    private TextView titele_tv;

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.retrievepassactivity;
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
        titele_tv = findViewById(R.id.titele_tv);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        retrieve_pass_btn.setOnClickListener(this);
        retrieve_pass_tv_mCode.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        titele_tv.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.retrieve_pass_tv_mCode:
                if (isChinaPhoneLegal(retrieve_pass_et_userName.getText().toString().trim())) {
                    //发送短信
                    getPhoneCode1(retrieve_pass_et_userName.getText().toString().trim());
                } else {
                    Toast.makeText(RetrievePassActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.retrieve_pass_btn:
                if (!isChinaPhoneLegal(retrieve_pass_et_userName.getText().toString().trim())) {
                    Toast.makeText(RetrievePassActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(retrieve_pass_et_code.getText().toString().trim())) {
                    Toast.makeText(RetrievePassActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    return;
                }
                if (retrieve_pass_et_password.getText().toString().trim().length() < 6) {
                    Toast.makeText(RetrievePassActivity.this, "密码长度不可小于6位", Toast.LENGTH_LONG).show();
                    return;
                }
//                if (TextUtils.isEmpty(retrieve_pass_et_password.getText().toString().trim())) {
//                    Toast.makeText(RetrievePassActivity.this, "请输入新密码", Toast.LENGTH_LONG).show();
//                    return;
//                }
                if (TextUtils.isEmpty(retrieve_pass_et_password1.getText().toString().trim())) {
                    Toast.makeText(RetrievePassActivity.this, "请再次输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!retrieve_pass_et_password.getText().toString().trim().equals(retrieve_pass_et_password1.getText().toString().trim())) {
                    Toast.makeText(RetrievePassActivity.this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                getRetrieve(retrieve_pass_et_code.getText().toString().trim(), retrieve_pass_et_userName.getText().toString().trim(), retrieve_pass_et_password.getText().toString().trim());
                break;
        }
    }


    public void getRetrieve(String code, String userName, String passWord) {
        RequestParams params = new RequestParams();
        params.put("mobile", userName);
        params.put("password", passWord);
        params.put("verifyCode", code);
        HttpRequest.getPass1(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Toast.makeText(RetrievePassActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
                // 退出登录,清除本地数据
                SPUtils.clear(RetrievePassActivity.this);
                exitApp(RetrievePassActivity.this);
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }


    /**
     * 发送手机验证码
     */
    public void getPhoneCode1(String Phone) {
        RequestParams params = new RequestParams();
        params.put("mobile", Phone);
        HttpRequest.getRegister_Code1(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    showToast(3, "短信验证码发送成功");
                    // 开始倒计时 60秒，间隔1秒
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(retrieve_pass_tv_mCode, 60000, 1000);
                    mCountDownTimerUtils.start();
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
