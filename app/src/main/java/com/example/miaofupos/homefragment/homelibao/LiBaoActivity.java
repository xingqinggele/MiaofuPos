package com.example.miaofupos.homefragment.homelibao;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 作者: qgl
 * 创建日期：2021/9/3
 * 描述:
 */
public class LiBaoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private TextView title;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.libao_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        title = findViewById(R.id.title);

    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        title.setText(getIntent().getStringExtra("title"));
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
