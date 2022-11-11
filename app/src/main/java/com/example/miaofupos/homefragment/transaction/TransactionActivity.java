package com.example.miaofupos.homefragment.transaction;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allenliu.versionchecklib.core.http.HttpParams;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.CallbackRecordAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.RecordListAdapter;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.MeMerchantsBean;
import com.example.miaofupos.homefragment.transaction.adpter.TransactionAdapter;
import com.example.miaofupos.homefragment.transaction.bean.TransactionBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.miaofupos.utils.TimeUtils.getNowTime;

/**
 * 作者: qgl
 * 创建日期：2021/10/27
 * 描述:交易记录
 */
public class TransactionActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private LinearLayout iv_back;
    private TextView transaction_start_time_tv;
    private TextView transaction_end_time_tv;
    private TextView transaction_item_num_tv;
    private SwipeRefreshLayout transaction_swipe_refresh;
    private RecyclerView transaction_list_view;
    private TransactionAdapter adapter;
    List<TransactionBean> list = new ArrayList<>();
    private String startTime = "";
    private String endTime = "";
    //页码
    private int mCount = 1;
    //请求数据数量
    private int pageSize = 20;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.transaction_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        transaction_start_time_tv = findViewById(R.id.transaction_start_time_tv);
        transaction_end_time_tv = findViewById(R.id.transaction_end_time_tv);
        transaction_item_num_tv = findViewById(R.id.transaction_item_num_tv);
        transaction_swipe_refresh = findViewById(R.id.transaction_swipe_refresh);
        transaction_list_view = findViewById(R.id.transaction_list_view);
        //下拉样式
        transaction_swipe_refresh.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        transaction_swipe_refresh.setOnRefreshListener(this);
        adapter = new TransactionAdapter(R.layout.item_transaction_view, list);
        adapter.openLoadAnimation();
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, transaction_list_view);
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        transaction_list_view.setLayoutManager(new LinearLayoutManager(this));
        transaction_list_view.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        transaction_start_time_tv.setOnClickListener(this);
        transaction_end_time_tv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        posData(true);
    }

    private void posData(boolean isRefresh) {
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("pageNum", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        HttpRequest.putRecordList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载条
                transaction_swipe_refresh.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    String total = result.getString("total");
                    List<TransactionBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<TransactionBean>>() {
                            }.getType());
                    //判断刷新还是加载
                    if (isRefresh) {
                        //判断数组是否为空、为空不需要清空，不为空才需要清空
                        if (list != null) {
                            list.clear();
                        }
                    }
                    list.addAll(memberList);
                    transaction_item_num_tv.setText("共" +total+ "条");
                    if (memberList.size() < pageSize) {
                        adapter.loadMoreEnd();
                    } else {
                        adapter.loadMoreComplete();
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                //关闭加载条
                transaction_swipe_refresh.setRefreshing(false);
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.transaction_start_time_tv:
                selectTime(transaction_start_time_tv, 1);
                break;
            case R.id.transaction_end_time_tv:
                selectTime(transaction_end_time_tv, 2);
                break;
        }
    }

    @Override
    public void onRefresh() {
        //开启刷新
        transaction_swipe_refresh.setRefreshing(true);
        //调用刷新逻辑
        setRefresh();
        //请求接口、填充数据
        posData(true);
    }

    @Override
    public void onLoadMoreRequested() {
        //页码 n + 1
        mCount = mCount + 1;
        //请求接口、填充数据
        posData(false);
    }

    //处理刷新逻辑
    private void setRefresh() {
        //页码设置为1
        mCount = 1;
    }

    /**
     * 选择时间
     */
    private void selectTime(TextView textView, int type) {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //开始时间
                if (type == 1) {
                    startTime = TimeUtils.getTimes(date);
                }
                //结束时间
                else {
                    endTime = TimeUtils.getTimes(date);
                }
                if (startTime != "" && endTime != "") {
                    boolean compareDate = TimeUtils.compareNewDate(startTime, endTime);
                    shouLog("--------", compareDate + "");
                    if (!compareDate) {
                        showToast(2, "请选择正确的时间");
                        return;
                    }
                    //请求接口
                    onRefresh();
                }

                textView.setText(TimeUtils.getTimes(date));


            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
        pvTime.show();
    }


}