package com.example.miaofupos.useractivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 创建：  qgl
 * 时间：
 * 描述：轮播图详情页
 */
public class HomeAdvPictureActivity extends BaseActivity implements View.OnClickListener {
    private ImageView simple;
    private TextView title_tv;
    private LinearLayout iv_back;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.home_advpicture_activity;
    }

    @Override
    protected void initView() {
        simple = findViewById(R.id.simple);
        title_tv = findViewById(R.id.title_tv);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
            String title = getIntent().getStringExtra("title");
            if (!title.equals("信用卡申请")){
                simple.setScaleType(ImageView.ScaleType.CENTER);
            }
            String iv = getIntent().getStringExtra("iv");
            title_tv.setText(title);
            Glide.with(HomeAdvPictureActivity.this).load(iv).into(simple);
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
