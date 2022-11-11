package com.example.miaofupos.mefragment.setup;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 作者: qgl
 * 创建日期：2020/12/31
 * 描述: 支付密码设置选项页
 */
public class MePayPassOptionsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private RelativeLayout me_pay_pass_options_set_up_relative;
    private RelativeLayout me_pay_pass_options_modify_relative;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.me_pay_pass_options_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        me_pay_pass_options_set_up_relative = findViewById(R.id.me_pay_pass_options_set_up_relative);
        me_pay_pass_options_modify_relative = findViewById(R.id.me_pay_pass_options_modify_relative);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        me_pay_pass_options_set_up_relative.setOnClickListener(this);
        me_pay_pass_options_modify_relative.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_pay_pass_options_set_up_relative:
                //设置支付密码
                startActivity(new Intent(MePayPassOptionsActivity.this, MePayPassActivity.class));
                break;
            case R.id.me_pay_pass_options_modify_relative:
                // 修改支付密码
                startActivity(new Intent(MePayPassOptionsActivity.this, MeModifyPayPassActivity.class));
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
