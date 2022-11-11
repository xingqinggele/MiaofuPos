package com.example.miaofupos.homefragment.homeequipment.fragment.callback;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.bean.MerMachineBean;
import com.example.miaofupos.cap.android.CaptureActivity;
import com.example.miaofupos.cap.bean.ZxingConfig;
import com.example.miaofupos.cap.common.Constant;
import com.example.miaofupos.homefragment.homeequipment.activity.TransferCallbackActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.CeshiAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.ChooserRecyclerAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.RecyclerViewItemDecoration;
import com.example.miaofupos.homefragment.homeequipment.bean.CallbackEvenBusBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean;
import com.example.miaofupos.homefragment.hometeam.bean.HomeTeamBean;
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
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2020/12/23
 * 描述:选择回调
 */
public class CallSelectFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, PullLoadMoreRecyclerView.PullLoadMoreListener {
    //需要查询的伙伴商户ID
    private static String UserId = "";
    //扫描一维码按钮
    private ImageView scan_code_btn;
    //扫码成功返回值
    private final int REQUEST_CODE_SCAN = 10;
    //搜索按钮
    private Button serch_code_btn;
    //页码
    private int mCount = 1;
    //请求数据数量
    private int pageSize = 20;
    //机器数量
    private TextView callback_num_tv;
    //搜索输入框
    private EditText callback_query_ed_search;
    //机器列表控件
    PullLoadMoreRecyclerView callback_listview;
    private RecyclerView mRecyclerView;
    //下拉刷新
    private SwipeRefreshLayout swipe_layout;
    //滑动需要隐藏的部分 控件
    private AppBarLayout appBarLayout;
    //分割线
    private RecyclerViewItemDecoration recyclerViewItemDecoration;
    //列表适配器
    private CeshiAdapter ceshiAdapter;
    //搜索内容
    private String sertch_value = "";
    //列表Bean
    private List<TerminalBean> beans = new ArrayList<>();
    private List<TerminalBean> beanList_size = new ArrayList<>();
    //暂无数据界面
    private RelativeLayout emptyBg;
    //选择的总数
    private TextView callback_transfer_number;
    //全选状态
    private boolean isType = false;
    //全选字
    private TextView check_box_type;
    //全选按钮
    private CheckBox check_box;
    //回调提交按钮
    private Button bt_sub;
    //需要给后台返回pos机value值
    private int[] data;
    /**
     * 接受activity数据
     *
     * @param requestJson
     * @return
     */
    public static CallSelectFragment newInstance(String requestJson) {
        CallSelectFragment fragment = new CallSelectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        UserId = requestJson;
        return fragment;
    }

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.select_fragment;
    }

    @Override
    protected void initView(View rootView) {
        mContext = getActivity();
        //EventBus.getDefault().register(this);
        scan_code_btn = rootView.findViewById(R.id.scan_code_btn);
        serch_code_btn = rootView.findViewById(R.id.serch_code_btn);
        callback_num_tv = rootView.findViewById(R.id.callback_num_tv);
        callback_query_ed_search = rootView.findViewById(R.id.callback_query_ed_search);
        callback_listview = rootView.findViewById(R.id.callback_listview);
        swipe_layout = rootView.findViewById(R.id.swipe_layout);
        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        emptyBg = rootView.findViewById(R.id.emptyBg);
        callback_transfer_number = rootView.findViewById(R.id.callback_transfer_number);
        check_box_type = rootView.findViewById(R.id.check_box_type);
        check_box = rootView.findViewById(R.id.check_box);
        bt_sub = rootView.findViewById(R.id.bt_sub);
        initList();
    }

    @Override
    protected void initListener() {
        //滑动监听，显示、隐藏
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    swipe_layout.setEnabled(true);
                } else {
                    swipe_layout.setEnabled(false);
                }
            }
        });
        scan_code_btn.setOnClickListener(this);
        serch_code_btn.setOnClickListener(this);
        check_box.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
    }

    private void initList() {
        recyclerViewItemDecoration = new RecyclerViewItemDecoration(getActivity(), 1);
        //下拉样式
        swipe_layout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        swipe_layout.setOnRefreshListener(this);
        //获取mRecyclerView对象
        mRecyclerView = callback_listview.getRecyclerView();
        //代码设置scrollbar无效？未解决！
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //关闭下拉刷新
        callback_listview.setPullRefreshEnable(false);
        mRecyclerView.addItemDecoration(recyclerViewItemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置上拉刷新文字
        callback_listview.setFooterViewText("loading");
        callback_listview.setLinearLayout();
        callback_listview.setOnPullLoadMoreListener(this);
        ceshiAdapter = new CeshiAdapter(getActivity());
        callback_listview.setAdapter(ceshiAdapter);
        postData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EventBus.getDefault().unregister(this);

    }

    public void postData() {
        RequestParams params = new RequestParams();
        params.put("merchId", UserId);
        params.put("posActivateStatus", "0");
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("posCode", sertch_value);
        params.put("operType", "2"); // 1. 划拨 2.回调
        HttpRequest.getPosList(params, SPUtils.get(getActivity(), "Token", "-1").toString(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                swipe_layout.setRefreshing(false);
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<TerminalBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<TerminalBean>>() {
                            }.getType());
                    beans.addAll(memberList);
                    callback_num_tv.setText(result.getString("totalNum") + "");
                    ceshiAdapter.addAllData(memberList);
                    if (mCount == 1 && memberList.size() == 0) {
                        emptyBg.setVisibility(View.VISIBLE);
                    } else {
                        emptyBg.setVisibility(View.GONE);
                    }
                    callback_listview.setPullLoadMoreCompleted();
                    ceshiAdapter.setOnAddClickListener(onItemActionClick);
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
    CeshiAdapter.OnAddClickListener onItemActionClick = new CeshiAdapter.OnAddClickListener() {
        @Override
        public void onItemClick() {
            Log.e("啊", "走了");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int len = 0;
                        int lenght = beans.size();
                        if (lenght >= 1) {
                            for (int i = 0; i < lenght; i++) {
                                if (ceshiAdapter.ischeck.get(i, false)) {
                                    len = len + 1;
                                }
                            }
                            callback_transfer_number.setText("总计:" + len + "台");
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
    public void onRefresh() {
        ceshiAdapter.setAllSelect();
        ceshiAdapter.clearData();
        swipe_layout.setRefreshing(true);
        callback_transfer_number.setText("总计:" + "0" + "台");
        check_box.setChecked(false);
        beans.clear();
        beanList_size.clear();
        sertch_value = "";
        mCount = 1;
        postData();
    }

    @Override
    public void onLoadMore() {
        Log.e("操作","用户上拉了");
        mCount = mCount + 1;
        postData();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //扫描条形码按钮
            case R.id.scan_code_btn:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                 * 也可以不传这个参数
                 * 不传的话  默认都为默认不震动  其他都为true
                 * */
                ZxingConfig config = new ZxingConfig();
                config.setShowbottomLayout(false);//底部布局（包括闪光灯和相册）
                config.setDecodeBarCode(true);//是否扫描条形码 默认为true
                config.setFullScreenScan(true);
                //config.setPlayBeep(true);//是否播放提示音
                //config.setShake(true);//是否震动
                //config.setShowAlbum(true);//是否显示相册
                //config.setShowFlashLight(true);//是否显示闪光灯
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            //搜索按钮
            case R.id.serch_code_btn:
                sertch_value = callback_query_ed_search.getText().toString();
                ceshiAdapter.setAllSelect();
                ceshiAdapter.clearData();
                swipe_layout.setRefreshing(true);
                callback_transfer_number.setText("总计:" + "0" + "台");
                check_box.setChecked(false);
                beans.clear();
                beanList_size.clear();
                mCount = 1;
                postData();
                break;
            case R.id.check_box:
                try {
                    if (!beans.isEmpty()) {
                        if (isType) {
                            ceshiAdapter.setAllSelect();
                            isType = false;
                            callback_transfer_number.setText("总计:" + 0 + "台");
                        } else {
                            isType = true;
                            ceshiAdapter.getAllSelect();
                            callback_transfer_number.setText("总计:" + beans.size() + "台");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_sub:
                StringBuffer sb = new StringBuffer();
                beanList_size = new ArrayList<>();
                //获取选中的数据
                for (int i = 0; i < beans.size(); i++) {
                    if (ceshiAdapter.ischeck.get(i, false)) {
                        sb.append(beans.get(i).getPosId().toString());
                        beanList_size.add(beans.get(i));
                    }
                }
                if (sb.toString().equals("")) {
                    Toast.makeText(getActivity(), "请选择要回调的机器", Toast.LENGTH_LONG).show();
                } else {
                    showDialog();
                }
                break;
        }
    }

    //回调弹框
    private void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_fragment, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("共" + beanList_size.size() + "台,可回拨" + beanList_size.size() + "台");
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
                //跳转到人员选择界面
                data = new int[beanList_size.size()];
                for (int i = 0; i < beanList_size.size(); i++) {
                    data[i] = Integer.valueOf(beanList_size.get(i).getPosId());
                }
                SubMit();
            }
        });
    }

    //提交回调数据
    public void SubMit() {
        // 开启加载框
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("userId", getUserId());
        params.put("merchId", UserId);
        params.put("operType", "2");  //1，划拨，2 回调
        HttpRequest.updPosListFrom(params, SPUtils.get(getActivity(), "Token", "-1").toString(), data, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                // 关闭加载框
                loadDialog.dismiss();
                showToast( "回调成功");
                //回调成功，刷新接口
                onRefresh();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                // 关闭加载框
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                callback_query_ed_search.setText(content);
            }
        }
    }
}
