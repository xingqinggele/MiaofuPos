package com.example.miaofupos.homefragment.homebagactivity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.useractivity.HomeAdvPictureActivity;
import com.example.miaofupos.useractivity.HomeAdvPictureActivity1;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

/**
 * 作者: qgl
 * 创建日期：2020/12/16
 * 描述: 礼包活动
 */
public class HomeBagActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private ImageView simple;
    private Button btn;
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.home_bag_activity;
    }

    @Override
    protected void initView() {
        simple = findViewById(R.id.simple);
        btn = findViewById(R.id.btn);
        iv_back = findViewById(R.id.iv_back);
        Uri imgurl=Uri.parse("https://cykj-1303987307.cos.ap-beijing.myqcloud.com/advertising/HomeAdvP.png");
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        simple.setImageURI(imgurl);
    }
    //点击事件
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
                startActivity(new Intent(this, HomeAdvPictureActivity1.class));
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.simple:
                Intent intent = new Intent(this, HomeAdvPictureActivity.class);
                intent.putExtra("title", "信用卡申请");
                intent.putExtra("iv", "https://cykj-1303987307.cos.ap-beijing.myqcloud.com/advertising/bankadetail2.png");
                startActivity(intent);
                break;
        }
    }
}
