package com.example.miaofupos.mefragment.meorder.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.cap.android.CaptureActivity;
import com.example.miaofupos.cap.bean.ZxingConfig;
import com.example.miaofupos.cap.common.Constant;
import com.example.miaofupos.homefragment.homeequipment.adapter.ChooserRecyclerAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.HomeIntergraChooserRecyclerAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.RecyclerViewItemDecoration;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.net.Utils;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2021/3/26
 * 描述:极具发放，区间划拨
 */
public class HomeIntegerIntervalFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //可划拨的极具数量
    private static int posNum = 5;
    //接受极具者商户ID
    private static String merchantId;
    //订单Id
    private static String orderID;
    //可划拨TV
    private TextView tv_number1;
    //请输入划拨终端起始序列号
    private EditText interval_ed_search;
    //请输入划拨终端终止序列号
    private EditText interval_ed_search1;
    //列表控件
    private RecyclerView listview;
    //分割线
    private RecyclerViewItemDecoration recyclerViewItemDecoration;
    //适配器Adapter
    private HomeIntergraChooserRecyclerAdapter manyRecyclerAdapter;
    //上拉刷新控件
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //头部布局可滑动，吸顶
    private AppBarLayout appBarLayout;
    //总计已选择的数量
    private TextView merchants_transfer_number;
    //全选状态
    private boolean isType = false;
    //全选状态TV
    private TextView check_box_type;
    //全选按钮CheckBox
    private CheckBox check_box;
    //提交按钮
    private Button bt_sub;
    //列表Bean
    private List<TerminalBean> beans = new ArrayList<>();
    private List<TerminalBean> beanList_size = new ArrayList<>();
    //起始扫码按钮
    private ImageView scan_code_btn;
    //结束扫码按钮
    private ImageView scan_code_end_btn;
    //起始扫码成功返回值
    private final int REQUEST_CODE_START_SCAN = 1;
    //结束扫码成功返回值
    private final int REQUEST_CODE_END_SCAN = 2;
    //搜索按钮
    private Button search_code_btn;
    //需要给后台返回pos机value值
    private int[] data;
    //pos机类型
    private static String posType = "";
    /**
     * 接受activity数据
     *
     * @param parentNum
     * @param merchId
     * @param orderId
     * @return
     */
    public static HomeIntegerIntervalFragment newInstance(String parentNum, String merchId, String orderId,String posTypeId) {
        HomeIntegerIntervalFragment fragment = new HomeIntegerIntervalFragment();
        posNum = Integer.parseInt(parentNum);
        merchantId = merchId;
        orderID = orderId;
        posType = posTypeId;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.homeinteger_interval_fragment;
    }

    @Override
    protected void initView(View rootView) {
        tv_number1 = rootView.findViewById(R.id.tv_number1);
        interval_ed_search = rootView.findViewById(R.id.interval_ed_search);
        interval_ed_search1 = rootView.findViewById(R.id.interval_ed_search1);
        merchants_transfer_number = rootView.findViewById(R.id.merchants_transfer_number);
        recyclerViewItemDecoration = new RecyclerViewItemDecoration(getActivity(), 1);
        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);
        listview = rootView.findViewById(R.id.listview);
        check_box_type = rootView.findViewById(R.id.check_box_type);
        check_box = rootView.findViewById(R.id.check_box);
        bt_sub = rootView.findViewById(R.id.bt_sub);
        scan_code_btn = rootView.findViewById(R.id.scan_code_btn);
        scan_code_end_btn = rootView.findViewById(R.id.scan_code_end_btn);
        search_code_btn = rootView.findViewById(R.id.search_code_btn);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });

        tv_number1.setText(posNum + "");
        ini();
        search();
    }


    public void ini() {
        //下拉样式
        mSwipeRefreshLayout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        listview.addItemDecoration(recyclerViewItemDecoration);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //搜索框
    private void search() {
        interval_ed_search1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    Utils.hideKeyboard(interval_ed_search1);
                    if (TextUtils.isEmpty(interval_ed_search.getText().toString().trim())) {
                        showToast("请输入划拨终端起始序列号");
                        return false;
                    }
                    if (TextUtils.isEmpty(v.getText().toString().trim())) {
                        showToast("请输入划拨终端结束序列号");
                        return false;
                    }
                    check_box.setChecked(false);
                    merchants_transfer_number.setText("总计:" + 0 + "台");
                    posData(interval_ed_search.getText().toString().trim(), v.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }

    //请求区间数据
    public void posData(String startCode, String endCode) {
        beans.clear();
        RequestParams params = new RequestParams();
        params.put("userId", SPUtils.get(getActivity(), "userId", "-1").toString());
        params.put("posActivateStatus", "0"); // 终端激活状态，0-未激活，1-已激活
        params.put("posCodeStart", startCode);
        params.put("posCodeEnd", endCode);
        params.put("posTypeId", posType); // pos类型
        HttpRequest.updPosintervalList(params, SPUtils.get(getActivity(), "Token", "-1").toString(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                mSwipeRefreshLayout.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<TerminalBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<TerminalBean>>() {
                            }.getType());
                    beans.addAll(memberList);
                    manyRecyclerAdapter = new HomeIntergraChooserRecyclerAdapter(beans, getActivity());
                    listview.setAdapter(manyRecyclerAdapter);
                    manyRecyclerAdapter.addNum(posNum);
                    manyRecyclerAdapter.setOnAddClickListener(onItemActionClick);
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

    /*************Adapter接口回调********************/
    HomeIntergraChooserRecyclerAdapter.OnAddClickListener onItemActionClick = new HomeIntergraChooserRecyclerAdapter.OnAddClickListener() {
        @Override
        public void onItemClick() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int len = 0;
                        int lenght = beans.size();
                        if (lenght >= 1) {
                            for (int i = 0; i < lenght; i++) {
                                if (manyRecyclerAdapter.ischeck.get(i, false)) {
                                    len = len + 1;
                                }
                            }
                            merchants_transfer_number.setText("总计:" + len + "台");
                            if (len == 0) {
                                isType = false;
                                check_box_type.setText("全选");
                                check_box.setChecked(false);

                            } else if (len > 0 & len < lenght) {
                                isType = false;
                                check_box_type.setText("全选");
                                check_box.setChecked(false);

                            } else if (len == lenght) {
                                isType = true;
                                check_box_type.setText("取消");
                                check_box.setChecked(true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void initListener() {
        check_box.setOnClickListener(this);
        scan_code_btn.setOnClickListener(this);
        scan_code_end_btn.setOnClickListener(this);
        search_code_btn.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */
        ZxingConfig config = new ZxingConfig();
        config.setShowbottomLayout(false);//底部布局（包括闪光灯和相册）
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setFullScreenScan(true);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        switch (view.getId()) {
            case R.id.scan_code_btn:
                startActivityForResult(intent, REQUEST_CODE_START_SCAN);
                break;
            case R.id.scan_code_end_btn:
                startActivityForResult(intent, REQUEST_CODE_END_SCAN);
                break;
            case R.id.search_code_btn:
                if (TextUtils.isEmpty(interval_ed_search.getText().toString().trim())) {
                    showToast("请输入划拨终端起始序列号");
                    return;
                }
                if (TextUtils.isEmpty(interval_ed_search1.getText().toString().trim())) {
                    showToast("请输入划拨终端结束序列号");
                    return;
                }
                check_box.setChecked(false);
                merchants_transfer_number.setText("总计:" + 0 + "台");
                posData(interval_ed_search.getText().toString().trim(), interval_ed_search1.getText().toString().trim());
                break;
            case R.id.check_box:
                try {
                    if (!beans.isEmpty()) {
                        if (beans.size() > posNum){
                            showToast("最多可划拨" + posNum + "台");
                            check_box.setChecked(false);
                            return;
                        }
                        if (isType) {
                            manyRecyclerAdapter.setAllSelect();
                            isType = false;
                        } else {
                            isType = true;
                            manyRecyclerAdapter.getAllSelect();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_sub:
                StringBuffer sb = new StringBuffer();
                beanList_size = new ArrayList<>();
                for (int i = 0; i < beans.size(); i++) {
                    if (manyRecyclerAdapter.ischeck.get(i, false)) {
                        sb.append(beans.get(i).getPosId().toString());
                        beanList_size.add(beans.get(i));
                    }
                }
                if (sb.toString().equals("") || beanList_size.size() < posNum) {
                    Toast.makeText(getActivity(), "请选择正确选择划拨的机器", Toast.LENGTH_LONG).show();
                } else {
                    //做划拨
                    showDialog();
                }
                break;

        }
    }

    @Override
    public void onRefresh() {
        check_box.setChecked(false);
        merchants_transfer_number.setText("总计:" + 0 + "台");
        posData(interval_ed_search.getText().toString().trim(), interval_ed_search1.getText().toString().trim());
    }


    // 可划拨弹框
    private void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_fragment, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("共" + beanList_size.size() + "台,可划拨" + beanList_size.size() + "台");
        Dialog dialog = new MyDialog1(getActivity(), true, true, (float) 0.7).setNewView(view);
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
                getPost(beanList_size);
            }
        });
    }

    public void getPost(List<TerminalBean> beanList_size) {
        loadDialog.show();
        data = new int[beanList_size.size()];
        for (int i = 0; i < beanList_size.size(); i++) {
            data[i] = Integer.valueOf(beanList_size.get(i).getPosId());
        }
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("merchId", merchantId);
        params.put("operType", "1");  //1，划拨，2 回调
        params.put("orderId", orderID);  //
        HttpRequest.updPosListFrom(params, SPUtils.get(getActivity(), "Token", "-1").toString(), data, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                Toast.makeText(getActivity(), "划拨成功", Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new MeExchangeFragment());
                EventBus.getDefault().post(new ApplyExchangeFragment());
                getActivity().finish();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_START_SCAN) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                interval_ed_search.setText(content);
            }
        }else if (requestCode == REQUEST_CODE_END_SCAN){
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                interval_ed_search1.setText(content);
            }
        }
    }
}
