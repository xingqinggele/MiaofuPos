package com.example.miaofupos.homefragment.homeintegral;

import android.content.Intent;
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

import static com.example.miaofupos.utils.DESHelperUtil.decrypt;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 作者: qgl
 * 创建日期：2021/2/19
 * 描述:我的积分
 */
public class HomeIntegralActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //积分明细按钮
    private TextView home_integral_tv_detail;
    //兑换积分按钮
    private Button home_integral_btn_exchange;
    //总可用积分
    private TextView home_integral_tv_num;
    //通用积分
    private TextView general_integral_tv;
    //活动积分
    private TextView activity_integral_tv;

    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homeintegral_activity;
    }

    //初始化控件
    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        home_integral_tv_detail = findViewById(R.id.home_integral_tv_detail);
        home_integral_btn_exchange = findViewById(R.id.home_integral_btn_exchange);
        home_integral_tv_num = findViewById(R.id.home_integral_tv_num);
        general_integral_tv = findViewById(R.id.general_integral_tv);
        activity_integral_tv = findViewById(R.id.activity_integral_tv);
        getData();
    }

    //控件点击事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        home_integral_tv_detail.setOnClickListener(this);
        home_integral_btn_exchange.setOnClickListener(this);
    }

    //数据适配
    @Override
    protected void initData() {
        if (getUserId().equals("2")) {
            home_integral_btn_exchange.setEnabled(false);
            home_integral_btn_exchange.setBackgroundResource(R.drawable.login_btn_un_bg);
        }else {
            home_integral_btn_exchange.setEnabled(true);
            home_integral_btn_exchange.setBackgroundResource(R.drawable.login_btn_on_bg);
        }

    }

    /**
     * 控件点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键
            case R.id.iv_back:
                finish();
                break;
            //积分明细
            case R.id.home_integral_tv_detail:
                startActivity(new Intent(HomeIntegralActivity.this, HomeIntegralDetailActivity.class));
                break;
            //积分兑换
            case R.id.home_integral_btn_exchange:
//                Intent intent = new Intent(HomeIntegralActivity.this, HomeIntegraExchangeActivity.class);
                Intent intent = new Intent(HomeIntegralActivity.this, IntegralMostActivity.class);
                // 通用积分
                intent.putExtra("integral", general_integral_tv.getText().toString().trim());
                //活动积分
                intent.putExtra("activityIntegral", activity_integral_tv.getText().toString().trim());
                //总积分
                intent.putExtra("totalintegral", home_integral_tv_num.getText().toString().trim());
                startActivity(intent);
                break;
        }
    }

    //获取积分明细数据
    public void getData() {
        RequestParams params = new RequestParams();
        //用户ID
        params.put("userId", getUserId());
        HttpRequest.getTotal_score(params, getToken(), new ResponseCallback() {
            //请求成功
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    //String 转 JSONObject
                    JSONObject result = new JSONObject(responseObj.toString());
                    //判断服务器返回值是否为空
                    if (TextUtils.isEmpty(result.getJSONObject("data").getString("integral")) && TextUtils.isEmpty(result.getJSONObject("data").getString("activityIntegral"))) {
                        //为空的时候、设置为0
                        general_integral_tv.setText("0");
                        activity_integral_tv.setText("0");
                        home_integral_tv_num.setText("0");
                    } else {
                        /** 加密后的通用积分 */
                        String integral = result.getJSONObject("data").getString("integral");
                        /** 加密后的活动积分 */
                        String activityIntegral = result.getJSONObject("data").getString("activityIntegral");
                        /** 秘钥 */
                        String secretKey = result.getJSONObject("data").getString("secretKey");
                        //解密的通用积分显示在tv
                        general_integral_tv.setText(decrypt(secretKey, integral));
                        //解密的活动积分显示在tv
                        activity_integral_tv.setText(decrypt(secretKey, activityIntegral));
                        // 通用积分 + 活动积分 = 总积分 显示在tv
                        home_integral_tv_num.setText(Integer.parseInt(decrypt(secretKey, integral)) + Integer.parseInt(decrypt(secretKey, activityIntegral)) + "");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //请求失败
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }
}
