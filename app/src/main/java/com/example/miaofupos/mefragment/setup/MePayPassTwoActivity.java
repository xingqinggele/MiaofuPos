package com.example.miaofupos.mefragment.setup;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;

import static com.example.miaofupos.utils.Utility.equalStr;
import static com.example.miaofupos.utils.Utility.isContinuousNum;

/**
 * 作者: qgl
 * 创建日期：2020/12/26
 * 描述:设置支付密码(2)
 */
public class MePayPassTwoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private EditText me_pay_pass_two_word_ed;
    private EditText me_pay_pass_two_word_ed1;
    private Button me_pay_pass_two_bt;

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.me_pay_pass_two_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        me_pay_pass_two_bt = findViewById(R.id.me_pay_pass_two_bt);
        me_pay_pass_two_word_ed = findViewById(R.id.me_pay_pass_two_word_ed);
        me_pay_pass_two_word_ed1 = findViewById(R.id.me_pay_pass_two_word_ed1);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        me_pay_pass_two_bt.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.me_pay_pass_two_bt:
                if (me_pay_pass_two_word_ed.getText().toString().trim().length()<6){
                    showToast(3,"密码长度必须6位");
                    return;
                }
                String str = me_pay_pass_two_word_ed.getText().toString().trim();
                if (equalStr(str)){
                    showToast(3,"密码不能重复数字");
                    return;
                }
                if (isContinuousNum(str)){
                    showToast(3,"密码不能连续数字");
                    return;
                }
                if (!TextUtils.equals(me_pay_pass_two_word_ed.getText().toString().trim(),me_pay_pass_two_word_ed1.getText().toString().trim())) {
                    showToast(3,"两次输入密码不一致，请详细检查");
                    return;
                }
                //提交修改支付密码
                posData();
                break;
        }
    }


    // 提交支付密码
    public void posData(){
        RequestParams params = new RequestParams();
        params.put("password",me_pay_pass_two_word_ed.getText().toString().trim());
        HttpRequest.getPay_password2(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                showToast(3,"支付密码设置成功");
                finish();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }




}
