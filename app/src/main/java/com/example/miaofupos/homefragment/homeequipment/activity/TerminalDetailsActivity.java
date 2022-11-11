package com.example.miaofupos.homefragment.homeequipment.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 作者: qgl
 * 创建日期：2020/12/29
 * 描述:设备详情
 */
public class TerminalDetailsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private TextView terminal_details_num_tv; //商户号
    private TextView terminal_details_sequence_num_tv; //序列号
    private TextView terminal_details_type_tv; //终端类型
    private TextView terminal_details_state_tv; //终端状态
    private TextView terminal_details_time_tv; //绑定时间
    private TextView terminal_details_activity_tv; //终端活动
    private TextView terminal_details_price_tv; //押金
    private TextView terminal_details_back_price_tv; //返现
    private TextView terminal_details_model_tv; //刷够返模式

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.terminal_details_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        terminal_details_num_tv = findViewById(R.id.terminal_details_num_tv);
        terminal_details_sequence_num_tv = findViewById(R.id.terminal_details_sequence_num_tv);
        terminal_details_type_tv = findViewById(R.id.terminal_details_type_tv);
        terminal_details_state_tv = findViewById(R.id.terminal_details_state_tv);
        terminal_details_time_tv = findViewById(R.id.terminal_details_time_tv);
        terminal_details_activity_tv = findViewById(R.id.terminal_details_activity_tv);
        terminal_details_price_tv = findViewById(R.id.terminal_details_price_tv);
        terminal_details_back_price_tv = findViewById(R.id.terminal_details_back_price_tv);
        terminal_details_model_tv = findViewById(R.id.terminal_details_model_tv);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        terminal_details_num_tv.setText(getIntent().getStringExtra("var2"));
        terminal_details_sequence_num_tv.setText(getIntent().getStringExtra("posCode"));
        terminal_details_activity_tv.setText(getIntent().getStringExtra("var1").equals("1") ? "传统POS机" : "电签POS机");
        if (getIntent().getStringExtra("getPosActivateStatus").equals("null")){
            terminal_details_state_tv.setText("未激活");
        }else {
            terminal_details_state_tv.setText(getIntent().getStringExtra("getPosActivateStatus").equals("0") ? "未激活" : "已激活");
        }
        terminal_details_time_tv.setText(getIntent().getStringExtra("posBindTime"));
        terminal_details_activity_tv.setText(getIntent().getStringExtra("posActivity").equals("1") ? "传统POS刷够返回活动" : "");
        terminal_details_price_tv.setText(getIntent().getStringExtra("posDeposit") + "元");
        terminal_details_back_price_tv.setText(getIntent().getStringExtra("posCashback")+ "元");
        terminal_details_model_tv.setText(getIntent().getStringExtra("posModel").equals("1") ? "返合作方" : "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
