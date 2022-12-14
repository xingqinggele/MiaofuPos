package com.example.miaofupos.homefragment.homeequipment.fragment.transfer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.cap.android.CaptureActivity;
import com.example.miaofupos.cap.bean.ZxingConfig;
import com.example.miaofupos.cap.common.Constant;
import com.example.miaofupos.fragment.HomeFragment;
import com.example.miaofupos.homefragment.homeequipment.activity.TerminalTransferActivity;
import com.example.miaofupos.homefragment.homeequipment.activity.TransferPersonActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.CeshiAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.ChooserRecyclerAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.RecyclerViewItemDecoration;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalBean;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * ??????: qgl
 * ???????????????2020/12/24
 * ??????:????????????
 */
public class SelectTransferFragment1 extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, PullLoadMoreRecyclerView.PullLoadMoreListener {
    //?????????
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView mRecyclerView;
    private CeshiAdapter ceshiAdapter;
    private int mCount = 1; //??????
    private int pageSize = 20;  // ??????????????????
    /*********************/
    private TextView tv1;
    private RecyclerViewItemDecoration recyclerViewItemDecoration;
    private CheckBox check_box;
    private boolean isType = false;
    private TextView tv;
    private TextView check_box_type;
    private TextView tv_number1;
    private Button bt_sub;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AppBarLayout appBarLayout;
    //??????Bean
    private List<TerminalBean> beans = new ArrayList<>();
    private List<TerminalBean> beanList_size = new ArrayList<>();
    protected Context mContext;
    //?????????
    private EditText merchants_query_ed_search;
    private String sertch_value = "";
    //?????????????????????
    private ImageView scan_code_btn;
    //?????????????????????
    private final int REQUEST_CODE_SCAN = 1;
    //????????????
    private Button serch_code_btn;
    //??????????????????
    private RelativeLayout emptyBg;
    //pos??????
    private String posType = "";

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.select_transfer_fragment1;
    }

    @Override
    protected void initView(View rootView) {
        mContext = getActivity();
        EventBus.getDefault().register(this);
        recyclerViewItemDecoration = new RecyclerViewItemDecoration(getActivity(), 1);
        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView)rootView.findViewById(R.id.listviewa);
        tv = rootView.findViewById(R.id.merchants_transfer_number);
        check_box = rootView.findViewById(R.id.check_box);
        tv1 = rootView.findViewById(R.id.tv1);
        tv_number1 = rootView.findViewById(R.id.tv_number1);
        bt_sub = rootView.findViewById(R.id.bt_sub);
        check_box_type = rootView.findViewById(R.id.check_box_type);
        merchants_query_ed_search = rootView.findViewById(R.id.merchants_query_ed_search);
        scan_code_btn = rootView.findViewById(R.id.scan_code_btn);
        serch_code_btn = rootView.findViewById(R.id.serch_code_btn);
        emptyBg = rootView.findViewById(R.id.emptyBg);
        serch_code_btn.setOnClickListener(this);
        tv1.setOnClickListener(this);
        scan_code_btn.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
        check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!beans.isEmpty()) {
                        if (isType) {
                            ceshiAdapter.setAllSelect();
                            isType = false;
                            tv.setText("??????:" + 0 + "???");
                        } else {
                            isType = true;
                            ceshiAdapter.getAllSelect();
                            tv.setText("??????:" + beans.size() + "???");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        initList();
        search();
    }


    private void initList() {
        //????????????
        mSwipeRefreshLayout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //??????mRecyclerView??????
        mRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        //????????????scrollbar?????????????????????
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //??????????????????
        mPullLoadMoreRecyclerView.setPullRefreshEnable(false);
        mRecyclerView.addItemDecoration(recyclerViewItemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //????????????????????????
        mPullLoadMoreRecyclerView.setFooterViewText("loading");
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        ceshiAdapter = new CeshiAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(ceshiAdapter);
        postData();
    }

    /*************Adapter????????????********************/
    CeshiAdapter.OnAddClickListener onItemActionClick = new CeshiAdapter.OnAddClickListener() {
        @Override
        public void onItemClick() {
            Log.e("???", "??????");
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
                            tv.setText("??????:" + len + "???");
                            if (len == 0) {
                                isType = false;
                                check_box_type.setText("??????");
                                check_box.setChecked(false);

                            } else if (len > 0 & len < lenght) {
                                isType = false;
                                check_box_type.setText("??????");
                                check_box.setChecked(false);

                            } else if (len == lenght) {
                                isType = true;
                                check_box_type.setText("??????");
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
        mCount = 1;
        ceshiAdapter.setAllSelect();
        ceshiAdapter.clearData();
        mSwipeRefreshLayout.setRefreshing(true);
        tv.setText("??????:" + "0" + "???");
        check_box.setChecked(false);
        beans.clear();
        beanList_size.clear();
        sertch_value = "";
        postData();
    }

    @Override
    public void onLoadMore() {
        Log.e("??????","???????????????");
        mCount = mCount + 1;
        postData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                ((TerminalTransferActivity) getActivity()).setListSize(1);
                break;
            case R.id.bt_sub:
                StringBuffer sb = new StringBuffer();
                beanList_size = new ArrayList<>();
                //?????????????????????
                for (int i = 0; i < beans.size(); i++) {
                    if (ceshiAdapter.ischeck.get(i, false)) {
                        sb.append(beans.get(i).getPosId().toString());
                        beanList_size.add(beans.get(i));
                    }
                }
                if (sb.toString().equals("")) {
                    Toast.makeText(getActivity(), "???????????????????????????", Toast.LENGTH_LONG).show();
                }
                else {
                    showDialog();
                    //????????????
                    //getPosJudge(beanList_size.size());
                }
                break;
            case R.id.scan_code_btn:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                /*ZxingConfig????????????  ?????????????????????????????????????????????????????????????????????????????????  ???????????????
                 * ???????????????????????????
                 * ????????????  ???????????????????????????  ????????????true
                 * */
                ZxingConfig config = new ZxingConfig();
                config.setShowbottomLayout(false);//??????????????????????????????????????????
                config.setDecodeBarCode(true);//????????????????????? ?????????true
                config.setFullScreenScan(true);
                //config.setPlayBeep(true);//?????????????????????
                //config.setShake(true);//????????????
                //config.setShowAlbum(true);//??????????????????
                //config.setShowFlashLight(true);//?????????????????????
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.serch_code_btn:
                sertch_value = merchants_query_ed_search.getText().toString();
                tv.setText("??????:" + "0" + "???");
                mCount = 1;
                ceshiAdapter.clearData();
                beans.clear();
                beanList_size.clear();
                postData();
                break;
        }
    }

    /**
     * ??????????????????????????????????????????
     * @param size ???????????????
     */
    private void getPosJudge(int size) {
        RequestParams params = new RequestParams();
        params.put("posId", size +"");
        HttpRequest.getTransfer(params, SPUtils.get(getActivity(), "Token", "-1").toString(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    // 0 ???????????? 1 ????????????
                    String code = result.getJSONObject("data").getString("code");
                    // ????????????
                    String msg = result.getJSONObject("data").getString("msg");
                    if ("0".equals(code)){
                        Toast toast = Toast.makeText(getActivity(), msg + "", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        showDialog();
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


    public void postData() {
        RequestParams params = new RequestParams();
        params.put("userId", SPUtils.get(getActivity(), "userId", "-1").toString());
        params.put("posActivateStatus", "0");
        params.put("pageNo", mCount + "");
        params.put("pageSize", pageSize + "");
        params.put("posCode", sertch_value);
        params.put("operType", "1"); // 1. ?????? 2.??????
        params.put("posType", posType);
        HttpRequest.getEquipmentList(params, SPUtils.get(getActivity(), "Token", "-1").toString(), new ResponseCallback() {
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
                    tv_number1.setText(result.getString("totalNum") + "");
                    ceshiAdapter.addAllData(memberList);
                    if (mCount == 1 && memberList.size() == 0) {
                        emptyBg.setVisibility(View.VISIBLE);
                        //mPullLoadMoreRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyBg.setVisibility(View.GONE);
                        //mPullLoadMoreRecyclerView.setVisibility(View.VISIBLE);
                    }
                    mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    ceshiAdapter.setOnAddClickListener(onItemActionClick);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                mSwipeRefreshLayout.setRefreshing(false);
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }

    // ???????????????
    private void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_fragment, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("???" + beanList_size.size() + "???,?????????" + beanList_size.size() + "???");
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
                //??????
                dialog.dismiss();
                //???????????????????????????
                Intent intent = new Intent(getActivity(), TransferPersonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("beanList_size", (Serializable) beanList_size);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    //?????????
    private void search() {
        merchants_query_ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // ????????????????????????????????????
                    Utils.hideKeyboard(merchants_query_ed_search);
                    sertch_value = v.getText().toString();
                    tv.setText("??????:" + "0" + "???");
                    mCount = 1;
                    ceshiAdapter.clearData();
                    beans.clear();
                    beanList_size.clear();
                    postData();
                    return true;
                }
                return false;
            }
        });
    }

    public void onEventMainThread(SelectTransferFragment ev) {
        shouLog("?????????", "?????? ");
        onRefresh();
    }

    public void onEventMainThread(TerminalEvenBusBean busBean){
        shouLog("????????????????????????",busBean.getTerminalType());
        posType = busBean.getTerminalType();
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ???????????????/????????????
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                merchants_query_ed_search.setText(content);
            }
        }
    }


}
