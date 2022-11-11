package com.example.miaofupos.mefragment.meorder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralAllBean;
import com.example.miaofupos.mefragment.meorder.adapter.MeExchangeItemAdapter;
import com.example.miaofupos.mefragment.meorder.bean.MeExchangeBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyListView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/2/20
 * 描述:我的订单详情
 */
public class MeOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //图片 待发货、已完成
    private ImageView me_order_detail_complete_logo;
    //文字 待发货、已完成
    private TextView me_order_detail_complete_tv;
    //发放极具按钮
    private Button issue_most;
    //是否显示订单划拨按钮
    private String pageCode = "";
    //订单ID
    private String message_id = "";
    //支付积分
    private TextView total_integral_tv;
    //订单编号
    private TextView order_num;
    //申请人
    private TextView order_person;
    //申请人前缀tv
    private TextView order_person_tv1;
    //订单创建时间
    private TextView order_time;
    //新机编号
    private TextView pos_number;
    //下划线
    private View order_view;
    //新机编号模块框
    private RelativeLayout order_relative;
    //被划拨者商户ID
    private String merchId = "";
    //划拨数量
    private String parentNum = "";
    //订单ID
    private String orderId = "";
    //对象选择容器
    private List<String> select = new ArrayList<>();
    //选择兑换对象弹出控件
    private OptionsPickerView reasonPicker;
    //配送方式
    private TextView distribution_type;
    //地址
    private TextView address_tv;
    //复制按钮
    private TextView tv_my1;
    //快递方式 显示布局
    private LinearLayout add_liner;
    //收货人姓名
    private TextView address_name_tv;
    //收货人电话
    private TextView address_phone_tv;
    //pos机类型
    private String posId = "";

    //实体类
    private List<MeExchangeBean.list> mData = new ArrayList<>();
    //商品Adapter
    private MeExchangeItemAdapter adapter;
    MyListView my_listView;
    //xml 界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.meorderdetail_activity;
    }

    //初始化控件
    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        me_order_detail_complete_logo = findViewById(R.id.me_order_detail_complete_logo);
        me_order_detail_complete_tv = findViewById(R.id.me_order_detail_complete_tv);
        issue_most = findViewById(R.id.issue_most);
        total_integral_tv = findViewById(R.id.total_integral_tv);
        order_num = findViewById(R.id.order_num);
        order_person = findViewById(R.id.order_person);
        order_person_tv1 = findViewById(R.id.order_person_tv1);
        order_time = findViewById(R.id.order_time);
        order_view = findViewById(R.id.order_view);
        order_relative = findViewById(R.id.order_relative);
        pos_number = findViewById(R.id.pos_number);
        distribution_type = findViewById(R.id.distribution_type);
        address_tv = findViewById(R.id.address_tv);
        tv_my1 = findViewById(R.id.tv_my1);
        add_liner = findViewById(R.id.add_liner);
        address_name_tv = findViewById(R.id.address_name_tv);
        address_phone_tv = findViewById(R.id.address_phone_tv);
        my_listView = findViewById(R.id.my_listView);
        //获取列表页返回的状态
        pageCode = getIntent().getStringExtra("pageCode");
        //获取列表页返回的订单ID
        message_id = getIntent().getStringExtra("message_id");
        //获取订单详情数据
        getData();
    }

    //点击事件绑定
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        issue_most.setOnClickListener(this);
        order_relative.setOnClickListener(this);
        tv_my1.setOnClickListener(this);
    }

    //数据填充
    @Override
    protected void initData() {

    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回键
            case R.id.iv_back:
                finish();
                break;
            //发放极具
            case R.id.issue_most:
                Intent intent = new Intent(MeOrderDetailActivity.this, HomeIntegraTransferActivity.class);
                //订单ID
                intent.putExtra("orderId", orderId);
                //划拨极具数、pos机数量
                intent.putExtra("parentNum", parentNum);
                //被划拨者商户ID
                intent.putExtra("merchId", merchId);
                //pos机类型
                intent.putExtra("posId", posId);
                //订单号
                intent.putExtra("orderNo", order_num.getText().toString());
                //执行
                startActivity(intent);
                finish();
                break;
            //划拨的机器数量，弹出框
            case R.id.order_relative:
                //显示弹出框
                reasonPicker.show();
                break;
            case R.id.tv_my1:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", address_tv.getText().toString().trim());
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                showToast(2,"复制成功");
                break;
        }
    }

    //获取订单详情
    public void getData() {
        RequestParams params = new RequestParams();
        //订单ID
        params.put("orderId", message_id);
        //订单类型 1.我申请的 2.别人申请的
        params.put("orderType", pageCode);
        HttpRequest.getOrderList_detail(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    //String 转 JSONObject
                    JSONObject result = new JSONObject(responseObj.toString());
                    //取出data.toString 再次转 JSONObject
                    JSONObject data = new JSONObject(result.getJSONObject("data").toString());
                    mData = gson.fromJson(data.getJSONArray("list").toString(),
                            new TypeToken<List<MeExchangeBean.list>>() {
                            }.getType());
                    adapter = new MeExchangeItemAdapter(MeOrderDetailActivity.this);
                    adapter.setDates(mData);
                    my_listView.setAdapter(adapter);
                    posId = data.getString("posId");
                    //判断返回的订单状态 0.未完成 else 已完成
                    if (data.getString("status").equals("0")) {
                        //修改显示图标
                        me_order_detail_complete_logo.setImageResource(R.mipmap.me_order_detail_waiting_iv);
                        //修改字体颜色
                        me_order_detail_complete_tv.setTextColor(Color.parseColor("#FFB156"));
                        //显示待发货字
                        me_order_detail_complete_tv.setText("待发货");
                        //隐藏下划线
                        order_view.setVisibility(View.GONE);
                        //隐藏新机编号框
                        order_relative.setVisibility(View.GONE);
                        //判断订单类型
                        if (pageCode.equals("1")) {
                            //根据接口返回来判断 发放极具按钮是否显示， 只有向我申请的待发货才显示按钮
                            issue_most.setVisibility(View.GONE);
                        } else {
                            //根据接口返回来判断 发放极具按钮是否显示， 只有向我申请的待发货才显示按钮
                            issue_most.setVisibility(View.VISIBLE);
                        }
                    } else if (data.getString("status").equals("1")){
                        //修改显示图标
                        me_order_detail_complete_logo.setImageResource(R.mipmap.me_orderdetail_complete_iv);
                        //修改字体颜色
                        me_order_detail_complete_tv.setTextColor(Color.parseColor("#9EDA6D"));
                        //显示已完成字
                        me_order_detail_complete_tv.setText("已完成");
                        //显示下划线
                        order_view.setVisibility(View.VISIBLE);
                        //显示新机编号框
                        order_relative.setVisibility(View.VISIBLE);
                        //接口新机编号赋值到编号容器中 select
                        select = gson.fromJson(result.getJSONArray("posCodeList").toString(),
                                new TypeToken<List<String>>() {
                                }.getType());
                        //调用配置弹出控件数据方法
                        initReason();
                        //默认显示第一位
                        pos_number.setText(select.get(0));
                    }else {
                        //修改显示图标
                        me_order_detail_complete_logo.setImageResource(R.mipmap.me_order_detail_waiting_iv);
                        //修改字体颜色
                        me_order_detail_complete_tv.setTextColor(Color.parseColor("#FFB156"));
                        //显示待发货字
                        me_order_detail_complete_tv.setText("已超时");
                        //隐藏下划线
                        order_view.setVisibility(View.GONE);
                        //隐藏新机编号框
                        order_relative.setVisibility(View.GONE);
                        //根据接口返回来判断 发放极具按钮是否显示， 只有向我申请的待发货才显示按钮
                        issue_most.setVisibility(View.GONE);
                    }
                    //显示积分金额
                    total_integral_tv.setText("￥" + data.getString("money"));
                    //订单编号
                    order_num.setText(data.getString("orderNo"));
                    //申请人姓名
                    order_person.setText(data.getString("parentName"));
                    //判断接口返回的申请人姓名显示不一样的前缀
                    if (data.getString("parentName").equals("服务中心")) {
                        order_person_tv1.setText("服务商");
                    } else {
                        order_person_tv1.setText("申请人");
                    }
                    //订单创建事件
                    order_time.setText(data.getString("createTime"));
                    //划拨数量
                    parentNum = data.getString("num");
                    //被划拨者商户ID
                    merchId = data.getString("merchId");
                    //订单ID
                    orderId = data.getString("orderId");
                    //配送方式
                    distribution_type.setText(data.getString("dispatchType"));
                    //判断是否是快递运送，快递运送显示收货人信息   ---------> 后期需要修改 不能拿汉字判断
                    if (data.getString("dispatchType").equals("快递运送")){
                        JSONObject dataAddress = new JSONObject(result.getJSONObject("dataAddress").toString());
                        add_liner.setVisibility(View.VISIBLE);
                        address_tv.setText(dataAddress.getString("address"));
                        address_name_tv.setText(dataAddress.getString("name"));
                        address_phone_tv.setText(dataAddress.getString("phone"));
                    }else {
                        add_liner.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    //配置弹出控件数据
    private void initReason() {
        reasonPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //兑换对象赋值
                pos_number.setText(select.get(options1));
            }
        }).setTitleText("极具列表").setContentTextSize(17).setTitleSize(17).setSubCalSize(16).build();
        //机器list 赋值
        reasonPicker.setPicker(select);
    }
}
