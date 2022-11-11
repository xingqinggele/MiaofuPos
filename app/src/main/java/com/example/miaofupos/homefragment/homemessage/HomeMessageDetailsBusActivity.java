package com.example.miaofupos.homefragment.homemessage;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;

/**
 * 作者: qgl
 * 创建日期：2021/3/20
 * 描述: 金钱积分业务类的消息详情 ,返现、返积分，预约提现
 */
public class HomeMessageDetailsBusActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //副标题img
    private ImageView home_message_details_bus_content_iv;
    //副标题
    private TextView home_message_details_title;
    //内容
    private TextView home_message_details_content;
    //时间
    private TextView home_message_details_time;
    //当前界面标识 3，预约提现成功 8 返积分
    private String status = "3";
    //消息类型img
    private int src;
    //消息类型title
    private String title = "";
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.home_message_details_bus_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        home_message_details_bus_content_iv = findViewById(R.id.home_message_details_bus_content_iv);
        home_message_details_title = findViewById(R.id.home_message_details_title);
        home_message_details_content = findViewById(R.id.home_message_details_content);
        home_message_details_time = findViewById(R.id.home_message_details_time);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        status = getIntent().getStringExtra("msgType");
        switch (status){
            case "3":
                title = "预约提现成功";
                src = R.mipmap.business_message_iv2;
                break;
        }
        home_message_details_bus_content_iv.setImageResource(src);
        home_message_details_title.setText(title);
        home_message_details_content.setText(getIntent().getStringExtra("msg_content"));
        home_message_details_time.setText(getIntent().getStringExtra("msg_time"));
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
