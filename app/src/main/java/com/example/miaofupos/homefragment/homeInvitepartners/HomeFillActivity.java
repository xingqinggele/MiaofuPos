package com.example.miaofupos.homefragment.homeInvitepartners;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.databillbean.BillBean;
import com.example.miaofupos.homefragment.homequoteactivity.bean.MerchTypeBean1;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.SwitchButtonView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/8/27
 * 描述:填写生成二维码
 */
public class HomeFillActivity extends BaseActivity implements View.OnClickListener {
    //信用卡结算
    private RelativeLayout js_flt1_relative;
    private TextView js_flt1_tv;
    private String type1 = "";
    //扫码结算
    private RelativeLayout js_flt0_relative;
    private TextView js_flt0_tv;
    private String type2 = "";
    //提交按钮
    private TextView submit_btn;
    //套餐ID
    private String accountId = "";
    //选择商户类型弹出控件
    private OptionsPickerView reasonPicker1;
    private OptionsPickerView reasonPicker2;
    //界面状态、1 新增 2 修改
    private String Status = "1";
    //返回键
    private LinearLayout iv_back;

    private TextView serverThreeSixTv;
    private TextView serverFourEightTv;
    private TextView serverNineNineTv;
    private TextView serverTwoNineNineTv;
    private TextView flowTwoFourTv;

    private TextView flowThreeSixTv;
    private TextView flowNineNineTv;
    private TextView flowFourEightTv;

    private TextView serverThreeSixTv_mine;
    private TextView serverFourEightTv_mine;
    private TextView serverNineNineTv_mine;
    private TextView serverTwoNineNineTv_mine;
    private TextView flowTwoFourTv_mine;

    private TextView flowThreeSixTv_mine;
    private TextView flowNineNineTv_mine;
    private TextView flowFourEightTv_mine;


    private EditText serverThreeSixEdit;
    private EditText serverFourEightEdit;
    private EditText serverNineNineEdit;
    private EditText serverTwoNineNineEdit;
    private EditText flowTwoFourEdit;
    private EditText flowThreeSixEdit;
    private EditText flowNineNineEdit;
    private EditText flowFourEightEdit;
    private SwitchButtonView mBtnSwitch;

    private int num0 = 0;
    private int num1 = 0;
    private int num2 = 0;
    private int num3 = 0;
    private int num4 = 0;
    private int num5 = 0;
    private int num6 = 0;
    private int num7 = 0;

    private String serverSwitch = "0";   //0 关 1开
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homefill_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        js_flt1_relative = findViewById(R.id.js_flt1_relative);
        js_flt1_tv = findViewById(R.id.js_flt1_tv);
        js_flt0_relative = findViewById(R.id.js_flt0_relative);
        js_flt0_tv = findViewById(R.id.js_flt0_tv);
        submit_btn = findViewById(R.id.submit_btn);
        serverThreeSixTv = findViewById(R.id.serverThreeSixTv);
        serverFourEightTv = findViewById(R.id.serverFourEightTv);
        serverNineNineTv = findViewById(R.id.serverNineNineTv);
        serverTwoNineNineTv = findViewById(R.id.serverTwoNineNineTv);
        flowTwoFourTv = findViewById(R.id.flowTwoFourTv);
        flowThreeSixTv = findViewById(R.id.flowThreeSixTv);
        flowNineNineTv = findViewById(R.id.flowNineNineTv);
        flowFourEightTv = findViewById(R.id.flowFourEightTv);

        serverThreeSixTv_mine = findViewById(R.id.serverThreeSixTv_mine);
        serverFourEightTv_mine = findViewById(R.id.serverFourEightTv_mine);
        serverNineNineTv_mine = findViewById(R.id.serverNineNineTv_mine);
        serverTwoNineNineTv_mine = findViewById(R.id.serverTwoNineNineTv_mine);
        flowTwoFourTv_mine = findViewById(R.id.flowTwoFour_mine);
        flowThreeSixTv_mine = findViewById(R.id.flowThreeSixTv_mine);
        flowNineNineTv_mine = findViewById(R.id.flowNineNineTv_mine);
        flowFourEightTv_mine = findViewById(R.id.flowFourEightTv_mine);

        serverThreeSixEdit = findViewById(R.id.serverThreeSixEdit);
        serverFourEightEdit = findViewById(R.id.serverFourEightEdit);
        serverNineNineEdit = findViewById(R.id.serverNineNineEdit);
        serverTwoNineNineEdit = findViewById(R.id.serverTwoNineNineEdit);
        flowTwoFourEdit = findViewById(R.id.flowTwoFourEdit);
        flowThreeSixEdit = findViewById(R.id.flowThreeSixEdit);
        flowNineNineEdit = findViewById(R.id.flowNineNineEdit);
        flowFourEightEdit = findViewById(R.id.flowFourEightEdit);
        mBtnSwitch = findViewById(R.id.swith_btn);


        posData();
        newPosData();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        js_flt1_relative.setOnClickListener(this);
        js_flt0_relative.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        mBtnSwitch.setmOnCheckedChangeListener(new SwitchButtonView.OnCheckedChangeListener() {
            @Override
            public void OnCheckedChanged(boolean isChecked) {
                if (isChecked){
                    serverSwitch = "1";
                }else {
                    serverSwitch = "0";
                }
            }
        });

    }

    @Override
    protected void initData() {
        Status = getIntent().getStringExtra("type");
        shouLog("Status",Status);
        if (Status.equals("2")) {
            accountId = getIntent().getStringExtra("accoundId");
            posDept();
        }
    }

    //获取后台类别数据
    private void posData() {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.getBizTerminalRateList(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = new JSONObject(result.getJSONObject("data").toString());
                    List<FillBean> rateT1list = gson.fromJson(data.getJSONArray("rateT0list").toString(),
                            new TypeToken<List<FillBean>>() {
                            }.getType());
                    List<FillBean> rateT0list = gson.fromJson(data.getJSONArray("settlementQrT0list").toString(),
                            new TypeToken<List<FillBean>>() {
                            }.getType());
                    //结算费率T1
                    initReason1(rateT1list, js_flt1_tv);
                    //结算费率T0
                    initReason2(rateT0list, js_flt0_tv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //获取类目
    private void newPosData(){
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        HttpRequest.getEchoServer(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<HomeNewFilBean> homeNewFilBeans = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<HomeNewFilBean>>() {
                            }.getType());
                    serverThreeSixTv.setText(homeNewFilBeans.get(0).getServerName());
                    serverThreeSixTv_mine.setText("(0~"+homeNewFilBeans.get(0).getServerMoney()+")");
                    num0 = Integer.parseInt(homeNewFilBeans.get(0).getServerMoney());

                    serverFourEightTv.setText(homeNewFilBeans.get(1).getServerName());
                    serverFourEightTv_mine.setText("(0~"+homeNewFilBeans.get(1).getServerMoney()+")");
                    num1 = Integer.parseInt(homeNewFilBeans.get(1).getServerMoney());

                    num2 = Integer.parseInt(homeNewFilBeans.get(2).getServerMoney());
                    serverNineNineTv.setText(homeNewFilBeans.get(2).getServerName());
                    serverNineNineTv_mine.setText("(0~"+homeNewFilBeans.get(2).getServerMoney()+")");


                    serverTwoNineNineTv.setText(homeNewFilBeans.get(3).getServerName());
                    serverTwoNineNineTv_mine.setText("(0~"+homeNewFilBeans.get(3).getServerMoney()+")");
                    num3 = Integer.parseInt(homeNewFilBeans.get(3).getServerMoney());



                    flowTwoFourTv.setText(homeNewFilBeans.get(4).getServerName());
                    flowTwoFourTv_mine.setText("(0~"+homeNewFilBeans.get(4).getServerMoney()+")");
                    num4 = Integer.parseInt(homeNewFilBeans.get(4).getServerMoney());


                     flowThreeSixTv.setText(homeNewFilBeans.get(5).getServerName());
                    flowThreeSixTv_mine.setText("(0~"+homeNewFilBeans.get(5).getServerMoney()+")");
                     num5 = Integer.parseInt(homeNewFilBeans.get(5).getServerMoney());



                    flowFourEightTv.setText(homeNewFilBeans.get(6).getServerName());
                    flowFourEightTv_mine.setText("(0~"+homeNewFilBeans.get(6).getServerMoney()+")");
                    num6 = Integer.parseInt(homeNewFilBeans.get(6).getServerMoney());

                    flowNineNineTv.setText(homeNewFilBeans.get(7).getServerName());
                    flowNineNineTv_mine.setText("(0~"+homeNewFilBeans.get(7).getServerMoney()+")");
                    num7 = Integer.parseInt(homeNewFilBeans.get(7).getServerMoney());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    //获取已填写的类别数据
    private void posDept() {
        RequestParams params = new RequestParams();
        params.put("accountId", accountId);
        HttpRequest.getBizTerminalRateLists(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = new JSONObject(result.getJSONObject("data").toString());
                    js_flt1_tv.setText(data.getJSONObject("rateT0").getString("name"));
                    type1 = data.getJSONObject("rateT0").getString("id");
                    js_flt0_tv.setText(data.getJSONObject("qrsettleRate").getString("name"));
                    type2 = data.getJSONObject("qrsettleRate").getString("id");
                    serverThreeSixEdit.setText(data.getString("serverThirtySix"));
                    serverFourEightEdit.setText(data.getString("serverFortyEight"));
                    serverNineNineEdit.setText(data.getString("serverNinetyNine"));
                    serverTwoNineNineEdit.setText(data.getString("serverTwoNinetyNine"));
                    flowTwoFourEdit.setText(data.getString("flowTwoFour"));
                    flowThreeSixEdit.setText(data.getString("flowThirtySix"));
                    flowFourEightEdit.setText(data.getString("flowFortyEight"));
                    flowNineNineEdit.setText(data.getString("flowNinetyNine"));
                    serverSwitch = data.getString("serverSwitch");
                    if(data.getString("serverSwitch").equals("0")) {
                        mBtnSwitch.setChecked(false);

                    }else {
                        mBtnSwitch.setChecked(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    //提交数据
    private void subMit() {
        RequestParams params = new RequestParams();
        params.put("rateT0", type1);
        params.put("qrsettleRate", type2);
        params.put("serverThirtySix", serverThreeSixEdit.getText().toString().trim());
        params.put("serverFortyEight", serverFourEightEdit.getText().toString().trim());
        params.put("serverNinetyNine", serverNineNineEdit.getText().toString().trim());
        params.put("serverTwoNinetyNine", serverTwoNineNineEdit.getText().toString().trim());
        params.put("flowTwoFour", flowTwoFourEdit.getText().toString().trim());
        params.put("flowThirtySix", flowThreeSixEdit.getText().toString().trim());
        params.put("flowFortyEight", flowFourEightEdit.getText().toString().trim());
        params.put("flowNinetyNine", flowNineNineEdit.getText().toString().trim());
        params.put("serverSwitch", serverSwitch);
        HttpRequest.postOpenAccount(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("code").equals("200")) {
                        showToast(3, "添加成功");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id", result.getString("data"));
                        setResult(5, resultIntent);
                        finish();
                    } else {
                        showToast(3, "添加失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //修改数据
    private void EditData() {
        RequestParams params = new RequestParams();
        params.put("accountId", accountId);
        params.put("rateT0", type1);
        params.put("qrsettleRate", type2);
        params.put("serverThirtySix", serverThreeSixEdit.getText().toString().trim());
        params.put("serverFortyEight", serverFourEightEdit.getText().toString().trim());
        params.put("serverNinetyNine", serverNineNineEdit.getText().toString().trim());
        params.put("serverTwoNinetyNine", serverTwoNineNineEdit.getText().toString().trim());
        params.put("flowTwoFour", flowTwoFourEdit.getText().toString().trim());
        params.put("flowThirtySix", flowThreeSixEdit.getText().toString().trim());
        params.put("flowFortyEight", flowFourEightEdit.getText().toString().trim());
        params.put("flowNinetyNine", flowNineNineEdit.getText().toString().trim());
        params.put("serverSwitch", serverSwitch);
        HttpRequest.putOpenAccount(params, "", new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("code").equals("200")) {
                        showToast(3, "修改成功");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id", result.getString("accountId"));
                        setResult(5, resultIntent);
                        finish();
                    } else {
                        showToast(3, "修改失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }


    //结算费率T1
    private void initReason1(List<FillBean> mList, TextView tv) {
        reasonPicker1 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //兑换对象赋值
                tv.setText(mList.get(options1).getName());
                type1 = mList.get(options1).getId();
            }
        }).setTitleText("请选择商户类型").setContentTextSize(17).setTitleSize(17).setSubCalSize(16).build();
        reasonPicker1.setPicker(mList);
    }

    //结算费率T0
    private void initReason2(List<FillBean> mList, TextView tv) {
        reasonPicker2 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //兑换对象赋值
                tv.setText(mList.get(options1).getName());
                type2 = mList.get(options1).getId();
            }
        }).setTitleText("请选择商户类型").setContentTextSize(17).setTitleSize(17).setSubCalSize(16).build();
        reasonPicker2.setPicker(mList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.js_flt1_relative:
                reasonPicker1.show();
                break;
            case R.id.js_flt0_relative:
                reasonPicker2.show();
                break;
            case R.id.submit_btn:
                if (TextUtils.isEmpty(type1)) {
                    showToast(3, "选择信用卡结算");
                    return;
                }
                if (TextUtils.isEmpty(type2)) {
                    showToast(3, "选择扫码结算");
                    return;
                }
                if (num0 < Integer.parseInt(serverThreeSixEdit.getText().toString().trim())){
                    shouLog("111","----------------");
                    serverThreeSixEdit.setError("不能大于基本值");
                    return;
                }
                if (num1 < Integer.parseInt(serverFourEightEdit.getText().toString().trim())){
                    shouLog("222","----------------");
                    serverFourEightEdit.setError("不能大于基本值");
                    return;
                }
                if (num2 < Integer.parseInt(serverNineNineEdit.getText().toString().trim())){
                    shouLog("333","----------------");
                    serverNineNineEdit.setError("不能大于基本值");

                    return;
                }
                if (num3 < Integer.parseInt(serverTwoNineNineEdit.getText().toString().trim())){
                    shouLog("444","----------------");
                    serverTwoNineNineEdit.setError("不能大于基本值");
                    return;
                }

                if (num4 < Integer.parseInt(flowTwoFourEdit.getText().toString().trim())){
                    shouLog("555","----------------");
                    flowTwoFourEdit.setError("不能大于基本值");
                    return;
                }


                if (num5 < Integer.parseInt(flowThreeSixEdit.getText().toString().trim())){
                    shouLog("666","----------------");
                    flowThreeSixEdit.setError("不能大于基本值");
                    return;

                }
                if (num6 < Integer.parseInt(flowFourEightEdit.getText().toString().trim())){
                    shouLog("777","----------------");
                    flowFourEightEdit.setError("不能大于基本值");
                    return;
                }
                if (num7 < Integer.parseInt(flowNineNineEdit.getText().toString().trim())){
                    shouLog("888","----------------");
                    flowNineNineEdit.setError("不能大于基本值");
                    return;
                }
                if (Status.equals("1")) {
                    subMit();
                } else {
                    EditData();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


}
