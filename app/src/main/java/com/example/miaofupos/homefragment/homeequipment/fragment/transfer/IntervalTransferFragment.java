package com.example.miaofupos.homefragment.homeequipment.fragment.transfer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.cap.android.CaptureActivity;
import com.example.miaofupos.cap.bean.ZxingConfig;
import com.example.miaofupos.cap.common.Constant;
import com.example.miaofupos.homefragment.homeequipment.activity.TerminalTransferActivity;
import com.example.miaofupos.homefragment.homeequipment.activity.TransferPersonActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.ChooserRecyclerAdapter;
import com.example.miaofupos.homefragment.homeequipment.adapter.RecyclerViewItemDecoration;
import com.example.miaofupos.homefragment.homeequipment.bean.TerminalEvenBusBean1;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * ??????: qgl
 * ???????????????2020/12/24
 * ??????:????????????
 */
public class IntervalTransferFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private TextView tv_number1;
    private EditText interval_ed_search;
    private EditText interval_ed_search1;
    private RecyclerView listview;
    private RecyclerViewItemDecoration recyclerViewItemDecoration;
    private ChooserRecyclerAdapter manyRecyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AppBarLayout appBarLayout;
    private TextView tv;
    private boolean isType = false;
    private TextView check_box_type;
    private CheckBox check_box;
    private Button bt_sub;
    private TextView interval_transfer_tv;
    //??????Bean
    private List<TerminalBean> beans = new ArrayList<>();
    private List<TerminalBean> beanList_size = new ArrayList<>();
    //??????????????????
    private ImageView scan_code_btn;
    //??????????????????
    private ImageView scan_code_end_btn;
    //???????????????????????????
    private final int REQUEST_CODE_START_SCAN = 1;
    //???????????????????????????
    private final int REQUEST_CODE_END_SCAN = 2;
    //????????????
    private Button serch_code_btn;

    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.interval_transfer_fragment;
    }

    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        tv_number1 = rootView.findViewById(R.id.tv_number1);
        interval_ed_search = rootView.findViewById(R.id.interval_ed_search);
        interval_ed_search1 = rootView.findViewById(R.id.interval_ed_search1);
        tv = rootView.findViewById(R.id.merchants_transfer_number);
        recyclerViewItemDecoration = new RecyclerViewItemDecoration(getActivity(), 1);
        appBarLayout = rootView.findViewById(R.id.appBarLayout);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_layout);
        //????????????
        mSwipeRefreshLayout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        listview = rootView.findViewById(R.id.listview);
        check_box_type = rootView.findViewById(R.id.check_box_type);
        check_box = rootView.findViewById(R.id.check_box);
        bt_sub = rootView.findViewById(R.id.bt_sub);
        scan_code_btn = rootView.findViewById(R.id.scan_code_btn);
        scan_code_end_btn = rootView.findViewById(R.id.scan_code_end_btn);
        interval_transfer_tv = rootView.findViewById(R.id.interval_transfer_tv);
        serch_code_btn = rootView.findViewById(R.id.serch_code_btn);
        check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!beans.isEmpty()) {
                        if (isType) {
                            manyRecyclerAdapter.setAllSelect();
                            isType = false;
                            tv.setText("??????:" + 0 + "???");
                        } else {
                            isType = true;
                            manyRecyclerAdapter.getAllSelect();
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
        ini();
        search();
    }



    @Override
    protected void initListener() {
        bt_sub.setOnClickListener(this);
        interval_transfer_tv.setOnClickListener(this);
        scan_code_end_btn.setOnClickListener(this);
        scan_code_btn.setOnClickListener(this);
        serch_code_btn.setOnClickListener(this);
    }

    public void ini() {
        //????????????
        mSwipeRefreshLayout.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        listview.addItemDecoration(recyclerViewItemDecoration);
        listview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onClick(View view) {
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
        switch (view.getId()) {
            case R.id.bt_sub:
                StringBuffer sb = new StringBuffer();
                beanList_size = new ArrayList<>();
                //?????????????????????
                for (int i = 0; i < beans.size(); i++) {
                    if (manyRecyclerAdapter.ischeck.get(i, false)) {
                        sb.append(beans.get(i).getPosId().toString());
                        beanList_size.add(beans.get(i));
                    }
                }

                if (sb.toString().equals("")) {
                    Toast.makeText(getActivity(), "???????????????????????????", Toast.LENGTH_LONG).show();
                } else {
                    getPosJudge(beanList_size.size());
                }
                break;
            case R.id.interval_transfer_tv:
                ((TerminalTransferActivity) getActivity()).setListSize(2);
                break;
            case R.id.scan_code_btn:
                startActivityForResult(intent, REQUEST_CODE_START_SCAN);
                break;
            case R.id.scan_code_end_btn:
                startActivityForResult(intent, REQUEST_CODE_END_SCAN);
                break;
            case R.id.serch_code_btn:
                if (TextUtils.isEmpty(interval_ed_search.getText().toString().trim())){
                    showToast("????????????????????????????????????");
                    return ;
                }
                if (TextUtils.isEmpty(interval_ed_search1.getText().toString().trim())){
                    showToast("????????????????????????????????????");
                    return ;
                }
                posData(interval_ed_search.getText().toString().trim(), interval_ed_search1.getText().toString().trim());
                break;
        }
    }

    //?????????
    private void search() {
        interval_ed_search1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // ????????????????????????????????????
                    Utils.hideKeyboard(interval_ed_search1);
                    if (TextUtils.isEmpty(interval_ed_search.getText().toString().trim())){
                        showToast("????????????????????????????????????");
                        return false;
                    }
                    if (TextUtils.isEmpty(v.getText().toString().trim())){
                        showToast("????????????????????????????????????");
                        return false;
                    }
                     posData(interval_ed_search.getText().toString().trim(), v.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
    }


    //??????????????????
    public void posData(String startCode, String endCode) {
        beans.clear();
        RequestParams params = new RequestParams();
        params.put("userId", SPUtils.get(getActivity(), "userId", "-1").toString());
        params.put("posActivateStatus", "0"); // ?????????????????????0-????????????1-?????????
        params.put("posCodeStart", startCode);
        params.put("posCodeEnd", endCode);
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
                    tv_number1.setText(memberList.size() + "");
                    manyRecyclerAdapter = new ChooserRecyclerAdapter(beans, getActivity());
                    listview.setAdapter(manyRecyclerAdapter);
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

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        tv.setText("??????:" + "0" + "???");
        check_box.setChecked(false);
        beans.clear();
        beanList_size.clear();
        posData(interval_ed_search.getText().toString().trim(), interval_ed_search1.getText().toString().trim());
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


    /*************Adapter????????????********************/
    ChooserRecyclerAdapter.OnAddClickListener onItemActionClick = new ChooserRecyclerAdapter.OnAddClickListener() {
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
                                if (manyRecyclerAdapter.ischeck.get(i, false)) {
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

    public void onEventMainThread(IntervalTransferFragment ev) {
        shouLog("?????????", "???????????????????????????");
        interval_ed_search.setText("");
        interval_ed_search1.setText("");
        onRefresh();
    }

    public void onEventMainThread(TerminalEvenBusBean1 busBean){
        shouLog("????????????????????????",busBean.getTerminalType());
        shouLog("????????????????????????",busBean.getMostType());
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
