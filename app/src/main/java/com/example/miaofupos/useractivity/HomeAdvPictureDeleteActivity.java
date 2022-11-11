package com.example.miaofupos.useractivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 作者: qgl
 * 创建日期：2021/7/26
 * 描述: 办卡详情
 */
public class HomeAdvPictureDeleteActivity extends BaseActivity implements View.OnClickListener {
    private ImageView simple;
    private Button btn;
    private LinearLayout iv_back;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homeadvpicturedelete_activity;
    }

    @Override
    protected void initView() {
        simple = findViewById(R.id.simple);
        btn = findViewById(R.id.btn);
        iv_back = findViewById(R.id.iv_back);
        Glide.with(HomeAdvPictureDeleteActivity.this).load("https://cykj-1303987307.cos.ap-beijing.myqcloud.com/advertising/HomeAdvP.png").into(simple);
    }

    @Override
    protected void initListener() {
        btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        simple.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn:
                startActivity(new Intent(this,HomeAdvPictureActivity1.class));
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.simple:
                Intent intent = new Intent(this, HomeAdvPictureActivity.class);
                intent.putExtra("title", "政策");
                intent.putExtra("iv", "https://cykj-1303987307.cos.ap-beijing.myqcloud.com/advertising/bankadetail.png");
                startActivity(intent);
                break;
        }
    }
}
