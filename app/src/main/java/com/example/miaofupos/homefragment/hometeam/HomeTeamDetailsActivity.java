package com.example.miaofupos.homefragment.hometeam;

import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamChartBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.Utility;
import com.example.miaofupos.views.MPLineChartManager;
import com.example.miaofupos.views.MyDialog1;
import com.example.miaofupos.views.MyMarkerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
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
 * 创建日期：2020/12/19
 * 描述:我的伙伴详情
 */
public class HomeTeamDetailsActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    //返回键
    private LinearLayout iv_back;
    //伙伴ID
    private String ParnterId = "";
    //姓名
    private TextView home_team_name;
    //电话
    private TextView home_team_phone;
    //注册时间
    private TextView home_team_time;
    //团队交易额 (本月)
    private TextView home_team_this_month_money;
    //团队交易额 (上月)
    private TextView home_team_last_month_money;
    //未绑定标签机具
    private TextView home_team_this_month_most;
    //已绑定未激活机具
    private TextView home_team_last_month_most;
    //今日新增伙伴
    private TextView home_team_new_person;
    //累计新增伙伴
    private TextView home_team_cumulative_person;
    //头像
    private SimpleDraweeView home_team_logo;
    //拨打电话按钮
    private ImageView phone_iv;
    //交易额单选按钮框
    private RadioGroup radio_group;
    //交易额查询状态 1 7天 2 半年
    private String TradingStatus = "1";
    //设备激活单选按钮框
    private RadioGroup radio_group_activation;
    //设备激活查询状态 0 7天 1 半年
    private String EquipmentStatus = "1";
    //----------------------折线图新的-----------------
    private MyMarkerView mv;
    private MPLineChartManager mpLineChartManager;
    //交易额折线图
    private LineChart chart_v_LineChart;
    //设备激活折线图
    private LineChart chart_v_LineChart2;
    private ArrayList<Entry> entryList = new ArrayList<>();
    private ArrayList<String> xValue = new ArrayList<>();
    private ArrayList<Entry> entryList1 = new ArrayList<>();
    private ArrayList<String> xValue1= new ArrayList<>();

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.home_team_deteils_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        home_team_logo = findViewById(R.id.home_team_logo);
        home_team_name = findViewById(R.id.home_team_name);
        home_team_phone = findViewById(R.id.home_team_phone);
        home_team_time = findViewById(R.id.home_team_time);
        home_team_this_month_money = findViewById(R.id.home_team_this_month_money);
        home_team_last_month_money = findViewById(R.id.home_team_last_month_money);
        home_team_this_month_most = findViewById(R.id.home_team_this_month_most);
        home_team_last_month_most = findViewById(R.id.home_team_last_month_most);
        home_team_new_person = findViewById(R.id.home_team_new_person);
        home_team_cumulative_person = findViewById(R.id.home_team_cumulative_person);
        phone_iv = findViewById(R.id.phone_iv);
        radio_group = findViewById(R.id.radio_group);
        radio_group_activation = findViewById(R.id.radio_group_activation);
        chart_v_LineChart = (LineChart) findViewById(R.id.chart_v_LineChart);
        chart_v_LineChart2 = (LineChart) findViewById(R.id.chart_v_LineChart2);

    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        phone_iv.setOnClickListener(this);
        radio_group.setOnCheckedChangeListener(this);
        radio_group_activation.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        ParnterId = getIntent().getStringExtra("id");
        posData();
        //请求交易额数据
        getTradingData(TradingStatus);
        //请求设备数据
        getEquipmentData(EquipmentStatus);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.phone_iv:
                showDialog(home_team_phone.getText().toString().trim());
                break;
        }
    }

    //获取伙伴详情
    public void posData() {
        RequestParams params = new RequestParams();
        params.put("userId", ParnterId);
        HttpRequest.updMypartnerDetail(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");
                    Uri imgurl = Uri.parse(data.getString("portrait"));
                    // 清除Fresco对这条验证码的缓存
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.evictFromMemoryCache(imgurl);
                    imagePipeline.evictFromDiskCache(imgurl);
                    // combines above two lines
                    imagePipeline.evictFromCache(imgurl);
                    home_team_logo.setImageURI(imgurl);

                    home_team_name.setText(data.getString("partnerName"));
                    home_team_phone.setText(data.getString("partnerMobile").substring(0, 3) + "****" + data.getString("partnerMobile").substring(data.getString("partnerMobile").length() - 4));
                    home_team_time.setText("注册时间：" + data.getString("registerTime"));
                    home_team_this_month_money.setText(new BigDecimal(data.getString("todayTeamsTransAmount")).toString());
                    home_team_last_month_money.setText(new BigDecimal(data.getString("teamTransAmount")).toString());
                    home_team_this_month_most.setText(data.getString("todayteamActiveMacines"));
                    home_team_last_month_most.setText(data.getString("teamActiveMachines"));
                    home_team_new_person.setText(data.getString("todayPartnerCounts"));
                    home_team_cumulative_person.setText(data.getString("partnerCounts"));

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


    // 拨打电话dialog
    private void showDialog(String value) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("您是否拨打  " + value);
        Dialog dialog = new MyDialog1(HomeTeamDetailsActivity.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                dialog.dismiss();
                Utility.callPhone(HomeTeamDetailsActivity.this, value);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            //交易额 7天
            case R.id.radio_day_btn:
                TradingStatus = "1";
                getTradingData(TradingStatus);
                break;
            //交易额 半年
            case R.id.radio_month_btn:
                TradingStatus = "2";
                getTradingData(TradingStatus);
                break;
            //设备激活 7天
            case R.id.radio_day_activation_btn:
                EquipmentStatus = "1";
                getEquipmentData(EquipmentStatus);
                break;
            //设备激活 半年
            case R.id.radio_month_activation_btn:
                EquipmentStatus = "2";
                getEquipmentData(EquipmentStatus);
                break;
        }
    }

    /**
     * 请求设备激活折线图
     *
     * @param equipmentStatus 类型
     */
    private void getEquipmentData(String equipmentStatus) {
        xValue1.clear();
        entryList1.clear();
        RequestParams params = new RequestParams();
        params.put("userId", ParnterId);
        params.put("signActiveMachines", equipmentStatus);
        HttpRequest.getEquipmentDataList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {

                    JSONObject result = new JSONObject(responseObj.toString());
                    List<HomeTeamChartBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<HomeTeamChartBean>>() {
                            }.getType());
                    for (int i = 0; i < memberList.size(); i++) {
                        xValue1.add(memberList.get(i).getTime());
                        entryList1.add(new Entry(i, memberList.get(i).getNum()));
                    }
                    //填充数据到线形图
                    clickLineChart(chart_v_LineChart2, entryList1, xValue1, memberList.size(),2);

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

    /**
     * 请求交易额折线图数据
     *
     * @param tradingStatus 类型
     */
    private void getTradingData(String tradingStatus) {
        xValue.clear();
        entryList.clear();
        RequestParams params = new RequestParams();
        params.put("userId", ParnterId);
        params.put("signAmount", tradingStatus);
        HttpRequest.getTradingDataList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<HomeTeamChartBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<HomeTeamChartBean>>() {
                            }.getType());
                    for (int i = 0; i < memberList.size(); i++) {
                        xValue.add(memberList.get(i).getTime());
                        float kk = memberList.get(i).getMoney().floatValue();
                        entryList.add(new Entry(i, kk));
                    }
                    //填充数据到线形图
                    clickLineChart(chart_v_LineChart, entryList, xValue, memberList.size(),1);
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

    /*************************************************** 新的折线图 start *****************************************/

    private void clickLineChart(LineChart lineChart, ArrayList<Entry> values1, ArrayList<String> xvalue, int xCount,int i) {
        //关闭描述
        lineChart.getDescription().setEnabled(false);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        mv = new MyMarkerView(this, R.layout.marker_view, lineChart, xvalue,i);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        //引入方法
        mpLineChartManager = new MPLineChartManager(lineChart);
        //X轴数据
        mpLineChartManager.setXAxis(xvalue, xCount, true, false, 10f, getResources().getColor(R.color.textcolor), XAxis.XAxisPosition.BOTTOM, 0);
        // 添加限制线值
        List<LimitLine> lineList = new ArrayList<>();
        //Y轴数据
        mpLineChartManager.setYLeftAndLimit(lineList, 6f, getResources().getColor(R.color.textcolor), getResources().getColor(R.color.linec));
        //Y轴动画 动画速度
        mpLineChartManager.animationY(1000); // 图4
        //添加线
        mpLineChartManager.addData(values1, null, 0f, getResources().getColor(R.color.linecolor), 8f, true, 5f, 0, true, getResources().getColor(R.color.bomcolor));
        //切换立方
        mpLineChartManager.changeMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //是否显示顶点值
        mpLineChartManager.changeTheVerValue(false);
        mpLineChartManager.setData();
        //设置与图表交互
        mpLineChartManager.setInteraction(true, false, false, false, false, false, false, false);
        //设置图例
        mpLineChartManager.setLegend(Legend.LegendPosition.RIGHT_OF_CHART, 10f, Color.BLACK, Legend.LegendForm.NONE);
    }

    /*************************************************** 新的折线图 end *****************************************/

}
