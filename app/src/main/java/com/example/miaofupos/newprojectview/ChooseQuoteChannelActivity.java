package com.example.miaofupos.newprojectview;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homequoteactivity.HomeQuoteActivity1;

/**
 * 作者: qgl
 * 创建日期：2022/8/30
 * 描述:选择报件渠道
 */
public class ChooseQuoteChannelActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private ConstraintLayout old_quote_constrain;
    private ConstraintLayout new_quote_constrain;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.choose_quote_channel_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        old_quote_constrain = findViewById(R.id.old_quote_constrain);
        new_quote_constrain = findViewById(R.id.new_quote_constrain);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        old_quote_constrain.setOnClickListener(this);
        new_quote_constrain.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_quote_constrain:
                startActivity(new Intent(this, AddMerchantsActivity1.class));
                break;
            case R.id.old_quote_constrain:
                Intent intent = new Intent(this, HomeQuoteActivity1.class);
                intent.putExtra("type","1");
                intent.putExtra("bj_type","no");
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}