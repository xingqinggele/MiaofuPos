package com.example.miaofupos.mefragment.meabout;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.useractivity.LoginActivity;
import com.example.miaofupos.useractivity.PrivacyActivity;
import com.example.miaofupos.utils.VersionUtils;

/**
 * 作者: qgl
 * 创建日期：2020/12/22
 * 描述:关于我们
 */
public class MeAboutActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private TextView about_version;
    private TextView privacy_btn_tv;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.me_about_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        about_version = findViewById(R.id.about_version);
        privacy_btn_tv = findViewById(R.id.privacy_btn_tv);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        privacy_btn_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        about_version.setText("版本号：" + VersionUtils.getVersionName(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.privacy_btn_tv:
                startActivity(new Intent(MeAboutActivity.this, PrivacyActivity.class));
                break;
        }
    }
}
