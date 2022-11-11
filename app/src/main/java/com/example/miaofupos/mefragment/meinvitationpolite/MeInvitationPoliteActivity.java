package com.example.miaofupos.mefragment.meinvitationpolite;

import android.view.View;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 作者: qgl
 * 创建日期：2021/3/5
 * 描述:邀请有礼
 */
public class MeInvitationPoliteActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.meinvitationpolite_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
