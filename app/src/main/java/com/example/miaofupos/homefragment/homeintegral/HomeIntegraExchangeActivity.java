package com.example.miaofupos.homefragment.homeintegral;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.miaofupos.utils.DESHelperUtil.decrypt;

/**
 * 作者: qgl
 * 创建日期：2021/2/19
 * 描述: 积分兑换设备
 */
public class HomeIntegraExchangeActivity extends BaseActivity implements View.OnClickListener {
    //兑换按钮
    //private Button submit_btn;
    //返回键
    private LinearLayout iv_back;

    //通用积分
    private String integral = "";
    //活动积分
    private String activityIntegral = "";
    //总积分
    private String totalintegral = "";
    //总积分tv
    private TextView total_integral;
    private TextView tv_integral_all;
    //pos名称
    private TextView tv_type;
    //pos价格
    private TextView tv_price;
    //pos类型ID
    private String TypeId = "";
    //详情图
    private SimpleDraweeView detail_img;
    //价格
    private String price = "";
    //名称
    private String name = "";
    //小图地址
    private String smailUrl = "";
    //购物车按钮
    private Button car_btn;



    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homeintegraexchange_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
//        submit_btn = findViewById(R.id.submit_btn);
        total_integral = findViewById(R.id.total_integral);
        tv_integral_all = findViewById(R.id.tv_integral_all);
        tv_type = findViewById(R.id.tv_type);
        tv_price = findViewById(R.id.tv_price);
        detail_img = findViewById(R.id.detail_img);
        car_btn = findViewById(R.id.car_btn);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
//        submit_btn.setOnClickListener(this);
        car_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Uri imgurl = Uri.parse(getIntent().getStringExtra("detailImg"));
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        detail_img.setImageURI(imgurl);
        integral = getIntent().getStringExtra("integral");
        TypeId = getIntent().getStringExtra("id");
        activityIntegral = getIntent().getStringExtra("activityIntegral");
        totalintegral = getIntent().getStringExtra("totalintegral");
        name = getIntent().getStringExtra("typeName");
        tv_type.setText(name);
        price = getIntent().getStringExtra("returnIntegral");
        tv_price.setText("￥" + price);
        total_integral.setText("积分：" + totalintegral);
        tv_integral_all.setText("（通用积分" + integral + ",活动积分" + activityIntegral + "）");
        smailUrl = getIntent().getStringExtra("smail");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.submit_btn:
//                Intent intent = new Intent(HomeIntegraExchangeActivity.this,HomeIntegralOrderActivity.class);
//                intent.putExtra("integral",integral);
//                intent.putExtra("id",TypeId);
//                intent.putExtra("price",price);
//                intent.putExtra("name",name);
//                intent.putExtra("smail",smailUrl);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.car_btn:
                posData();

                break;
        }
    }


    //添加购物
    private void posData() {
        RequestParams params = new RequestParams();
        //pos机类型ID
        params.put("commType", TypeId);
        HttpRequest.getTrolley(params, getToken(), new ResponseCallback() {
            //请求成功
            @Override
            public void onSuccess(Object responseObj) {

                //发送添加购物车的商品信息
                showToast(2, "添加购物成功");
            }

            //请求失败
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }


        });
    }
}
