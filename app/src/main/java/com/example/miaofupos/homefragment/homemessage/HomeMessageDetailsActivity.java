package com.example.miaofupos.homefragment.homemessage;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/29
 * 描述:消息详情
 */
public class HomeMessageDetailsActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //成功提示语
    private TextView home_message_details_tv1;
    //极具序列号
    private TextView home_message_details_pos_number;
    //划拨台数
    private TextView home_message_details_pos_num;
    //下发者
    private TextView home_message_details_superior_person;
    //创建时间
    private TextView home_message_details_time;
    //界面提示img
    private ImageView home_message_details_iv;
    //当前的界面标识, 入库、极具激活、提现成功、提现失败，返积分
    private String Status = "1";
    //界面提示img
    private int src;
    //标题
    private String title = "";
    //消息列表ID
    private String message_id = "";
    //机器编号弹框控件
    private OptionsPickerView reasonPicker;
    //极具编号容器
    private List<String> select = new ArrayList<>();
    //入库布局
    private ConstraintLayout constrain_storage;
    //提现成功布局
    private ConstraintLayout constrain_success;
    //极具激活布局
    private ConstraintLayout constrain_activation;
    //提现失败原因
    private TextView home_message_details_failure_tv;
    //机具激活返现
    private TextView home_message_details_activation_price;
    //极具激活编号
    private TextView home_message_details_activation_num;
    //极具激活持有人
    private TextView home_message_details_activation_person;
    //机具激活区分title
    private TextView home_message_details_tv91;
    //极具激活持时间
    private TextView home_message_details_activation_time;
    //提现收款账户
    private TextView home_message_details_success_bank_name;
    //提现金额
    private TextView home_message_details_success_price;
    //提现时间
    private TextView home_message_details_success_time;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.home_message_details_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        home_message_details_iv = findViewById(R.id.home_message_details_iv);
        home_message_details_tv1 = findViewById(R.id.home_message_details_tv1);
        constrain_storage = findViewById(R.id.constrain_storage);
        constrain_success = findViewById(R.id.constrain_success);
        constrain_activation = findViewById(R.id.constrain_activation);
        /****************************入库***************************/
        home_message_details_pos_number = findViewById(R.id.home_message_details_pos_number);
        home_message_details_pos_num = findViewById(R.id.home_message_details_pos_num);
        home_message_details_superior_person = findViewById(R.id.home_message_details_superior_person);
        home_message_details_time = findViewById(R.id.home_message_details_time);
        /****************************极具激活***************************/
        home_message_details_activation_num = findViewById(R.id.home_message_details_activation_num);
        home_message_details_activation_person = findViewById(R.id.home_message_details_activation_person);
        home_message_details_activation_time = findViewById(R.id.home_message_details_activation_time);
        home_message_details_activation_price = findViewById(R.id.home_message_details_activation_price);
        home_message_details_tv91 = findViewById(R.id.home_message_details_tv91);
        /****************************提现成功***************************/
        home_message_details_success_bank_name = findViewById(R.id.home_message_details_success_bank_name);
        home_message_details_success_price = findViewById(R.id.home_message_details_success_price);
        home_message_details_success_time = findViewById(R.id.home_message_details_success_time);
        /****************************提现失败***************************/
        home_message_details_failure_tv = findViewById(R.id.home_message_details_failure_tv);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        home_message_details_pos_number.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Status = getIntent().getStringExtra("msgType");
        message_id = getIntent().getStringExtra("message_id");
        switch (Status) {
            case "1":
                src = R.mipmap.me_orderdetail_complete_iv;
                title = "入库成功";
                home_message_details_tv1.setTextColor(Color.parseColor("#ff9eda6d"));
                //显示入库布局
                constrain_storage.setVisibility(View.VISIBLE);
                //隐藏极具激活布局
                constrain_activation.setVisibility(View.GONE);
                //隐藏提现成功布局
                constrain_success.setVisibility(View.GONE);
                //隐藏提现失败原因
                home_message_details_failure_tv.setVisibility(View.GONE);
                break;
            case "2":
                src = R.mipmap.me_orderdetail_complete_iv;
                title = "激活返现成功";
                home_message_details_tv1.setTextColor(Color.parseColor("#ff9eda6d"));
                home_message_details_tv91.setText("激活返现金额");
                //隐藏入库布局
                constrain_storage.setVisibility(View.GONE);
                //隐藏提现成功布局
                constrain_success.setVisibility(View.GONE);
                //显示极具激活布局
                constrain_activation.setVisibility(View.VISIBLE);
                //隐藏提现失败原因
                home_message_details_failure_tv.setVisibility(View.GONE);
                break;
            case "4":
                src = R.mipmap.me_orderdetail_complete_iv;
                title = "提现成功";
                home_message_details_tv1.setTextColor(Color.parseColor("#ff9eda6d"));
                //隐藏入库布局
                constrain_storage.setVisibility(View.GONE);
                //隐藏极具激活布局
                constrain_activation.setVisibility(View.GONE);
                //显示提现成功布局
                constrain_success.setVisibility(View.VISIBLE);
                //隐藏提现失败原因
                home_message_details_failure_tv.setVisibility(View.GONE);
                break;
            case "5":
                src = R.mipmap.clonse;
                title = "提现失败";
                home_message_details_tv1.setTextColor(Color.parseColor("#FF5745"));
                //隐藏入库布局
                constrain_storage.setVisibility(View.GONE);
                //隐藏极具激活布局
                constrain_activation.setVisibility(View.GONE);
                //显示提现成功布局
                constrain_success.setVisibility(View.VISIBLE);
                //显示提现失败原因
                home_message_details_failure_tv.setVisibility(View.VISIBLE);
                break;
            case "8":
                src = R.mipmap.me_orderdetail_complete_iv;
                title = "激活返积分成功";
                home_message_details_tv1.setTextColor(Color.parseColor("#ff9eda6d"));
                home_message_details_tv91.setText("激活积分奖励");
                //隐藏入库布局
                constrain_storage.setVisibility(View.GONE);
                //隐藏提现成功布局
                constrain_success.setVisibility(View.GONE);
                //显示极具激活布局
                constrain_activation.setVisibility(View.VISIBLE);
                //隐藏提现失败原因
                home_message_details_failure_tv.setVisibility(View.GONE);
                break;
        }
        home_message_details_iv.setImageResource(src);
        home_message_details_tv1.setText(title);
        posData(Status, message_id);
    }

    //请求消息详情接口
    private void posData(String type, String id) {
        RequestParams params = new RequestParams();
        params.put("msgType", type);
        params.put("msgId", id);
        HttpRequest.getMessageDetail(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    switch (type) {
                        case "1":
                            //入库台数
                            home_message_details_pos_num.setText(result.getJSONObject("data").getString("num") + "台");
                            //发货人
                            home_message_details_superior_person.setText(result.getJSONObject("data").getString("oldUserName"));
                            home_message_details_time.setText(result.getJSONObject("data").getString("createTime"));
                            //接口新机编号赋值到编号容器中 select
                            select = gson.fromJson(result.getJSONObject("data").getJSONArray("posCodeList").toString(),
                                    new TypeToken<List<String>>() {
                                    }.getType());
                            //调用配置弹出控件数据方法
                            initReason();
                            //默认显示第一位
                            home_message_details_pos_number.setText(select.get(0));
                            break;
                        case "2":
                            //机具激活返现，类型转换
                            home_message_details_activation_price.setText(new BigDecimal(result.getJSONObject("data").getString("amount")).toString() + "元");
                            //机具激活编号
                            home_message_details_activation_num.setText(result.getJSONObject("data").getString("posCode"));
                            //机具持有人
                            home_message_details_activation_person.setText(result.getJSONObject("data").getString("merchName"));
                            //机具激活时间
                            home_message_details_activation_time.setText(result.getJSONObject("data").getString("billDate"));
                            break;
                        case "5":
                            //提现失败原因
                            home_message_details_failure_tv.setText(result.getJSONObject("data").getString("errorMsg"));
                        case "4":
                            //提现开户行、银行卡尾号
                            home_message_details_success_bank_name.setText(result.getJSONObject("data").getString("receiptAccount"));
                            //提现金额
                            home_message_details_success_price.setText(result.getJSONObject("data").getString("intoAmount") + "元");
                            //提现金额
                            home_message_details_success_time.setText(result.getJSONObject("data").getString("createTime"));
                            break;
                        case "8":
                            //机具激活返现，类型转换
                            home_message_details_activation_price.setText(new BigDecimal(result.getJSONObject("data").getString("value")).toString() + "积分");
                            //机具激活编号
                            home_message_details_activation_num.setText(result.getJSONObject("data").getString("posCode"));
                            //机具持有人
                            home_message_details_activation_person.setText(result.getJSONObject("data").getString("merchName"));
                            //机具激活时间
                            home_message_details_activation_time.setText(result.getJSONObject("data").getString("createTime"));
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {

            }
        });
    }


    //配置弹出控件数据
    private void initReason() {
        reasonPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //兑换对象赋值
                home_message_details_pos_number.setText(select.get(options1));
            }
        }).setTitleText("极具列表").setContentTextSize(17).setTitleSize(17).setSubCalSize(16).build();
        //机器list 赋值
        reasonPicker.setPicker(select);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //点击极具编号
            case R.id.home_message_details_pos_number:
                //显示弹出框
                reasonPicker.show();
                break;
        }
    }


}
