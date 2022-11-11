package com.example.miaofupos.homefragment.homewallet.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 作者: qgl
 * 创建日期：2020/12/23
 * 描述:提现成功
 */
public class WithdrawalSuccessActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //完成按钮
    private Button withdrawal_success_btn;
    //开户行
    private TextView success_bank_name;
    //银行卡号
    private TextView success_bank_num;
    //提现的金额
    private TextView success_bank_amount;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.withdrawal_success_activity;
    }

    //控件初始化
    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        withdrawal_success_btn = findViewById(R.id.withdrawal_success_btn);
        success_bank_name = findViewById(R.id.success_bank_name);
        success_bank_num = findViewById(R.id.success_bank_num);
        success_bank_amount = findViewById(R.id.success_bank_amount);
    }

    //事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        withdrawal_success_btn.setOnClickListener(this);
    }

    //数据处理
    @Override
    protected void initData() {
        success_bank_name.setText(getIntent().getStringExtra("bank_name"));
        success_bank_num.setText("尾号" + getIntent().getStringExtra("bank_num"));
        success_bank_amount.setText(getIntent().getStringExtra("amount") + "元");
    }

    //点击事件触发
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.withdrawal_success_btn:
            case R.id.iv_back:
                WithdrawalActivity.instance.finish();//调用
                finish();
                break;
        }
    }
}
