package com.example.miaofupos.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.RealNameOnActivity;
import com.example.miaofupos.homefragment.homewallet.HomeWalletActivity;
import com.example.miaofupos.mefragment.meabout.MeAboutActivity;
import com.example.miaofupos.mefragment.meaddres.MeAddressActivity;
import com.example.miaofupos.mefragment.mebank.MeBankActivity;
import com.example.miaofupos.mefragment.mefeilv.MeFeilvActivity;
import com.example.miaofupos.mefragment.meinvitationpolite.MeInvitationPoliteActivity;
import com.example.miaofupos.mefragment.meorder.MeOrderActivity;
import com.example.miaofupos.mefragment.mepowerattorney.MePowerAttorneyActivity;
import com.example.miaofupos.mefragment.mereferees.MeRefereesActivity;
import com.example.miaofupos.mefragment.meupgrade.MeUpgradeActivity;
import com.example.miaofupos.mefragment.setup.PersonalActivity;
import com.example.miaofupos.mefragment.setup.SetUpActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.MyDialog1;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2020/12/10
 * 描述:我的，Fragment
 */
public class MeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout main_me_refresh; //下拉刷新
    private SimpleDraweeView me_logo; // 头像
    private ConstraintLayout main_me_or_code; // 二维码
    private TextView main_me_name; // 姓名
    private TextView main_me_cooperation_code; // 合作方编号
    private TextView main_me_order; // 订单
    private TextView main_me_machine; // 购机
    private TextView main_me_wallet; // 钱包
    private TextView main_me_bank_card; // 银行卡
    //邀请有礼
    private RelativeLayout me_r1;
    //我的推荐人
    private RelativeLayout me_r2;
    //授权书
    private RelativeLayout me_r3;
    //设置
    private RelativeLayout me_r4;
    //关于我们
    private RelativeLayout me_r5;
    private String nickName;  // 用户名
    private String merchCode;  // 商户号
    private String headUrl;  // 用户头像
    private String Token;  // 密钥
    private String userId; // 用户ID
    //入驻状态，1入驻2未入住
    private String Code = "1";
    private String servicePhone = ""; //客服电话
    //收货地址
    private RelativeLayout me_r33;
    //积分
    private String integral;
    //钱包
    private String walletAmount;
    private TextView intger_tv;
    private TextView wellat_tv;
    /**
     * 接受activity数据
     *
     * @param requestJson
     * @return
     */
    public static MeFragment newInstance(String requestJson) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 界面。xml
     *
     * @return
     */
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.mefragment_main;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        main_me_refresh = rootView.findViewById(R.id.main_me_refresh);
        me_logo = rootView.findViewById(R.id.me_logo);
        main_me_or_code = rootView.findViewById(R.id.main_me_or_code);
        main_me_name = rootView.findViewById(R.id.main_me_name);
        main_me_cooperation_code = rootView.findViewById(R.id.main_me_cooperation_code);
        main_me_order = rootView.findViewById(R.id.main_me_order);
        main_me_machine = rootView.findViewById(R.id.main_me_machine);
        main_me_wallet = rootView.findViewById(R.id.main_me_wallet);
        main_me_bank_card = rootView.findViewById(R.id.main_me_bank_card);
        me_r1 = rootView.findViewById(R.id.me_r1);
        me_r2 = rootView.findViewById(R.id.me_r2);
        me_r3 = rootView.findViewById(R.id.me_r3);
        me_r4 = rootView.findViewById(R.id.me_r4);
        me_r5 = rootView.findViewById(R.id.me_r5);
        me_r33 = rootView.findViewById(R.id.me_r33);
        intger_tv = rootView.findViewById(R.id.intger_tv);
        wellat_tv = rootView.findViewById(R.id.wellat_tv);

    }

    //触发事件初始化
    @Override
    protected void initListener() {
        main_me_or_code.setOnClickListener(this);
        main_me_order.setOnClickListener(this);
        main_me_machine.setOnClickListener(this);
        main_me_wallet.setOnClickListener(this);
        main_me_bank_card.setOnClickListener(this);
        me_r1.setOnClickListener(this);
        me_r2.setOnClickListener(this);
        me_r3.setOnClickListener(this);
        me_r4.setOnClickListener(this);
        me_r5.setOnClickListener(this);
        me_r33.setOnClickListener(this);
        SwipeData();
    }

    // 设置下拉样式
    public void SwipeData() {
        main_me_refresh.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        main_me_refresh.setOnRefreshListener(this);
    }

    //填充数据
    @Override
    protected void loadData() {
        Token = SPUtils.get(getActivity(), "Token", "").toString();
        userId = SPUtils.get(getActivity(), "userId", "").toString();
        //posData();
        MeData();
    }

    //触发事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_me_bank_card:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    //我的银行卡
                    startActivity(new Intent(getActivity(), MeBankActivity.class));
                }
                break;
            case R.id.main_me_order:
                // 订单
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeOrderActivity.class));
                }
                break;
            case R.id.main_me_machine:
                // 购机
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeUpgradeActivity.class));
                }
                break;
            case R.id.me_r1:
                //邀请有礼
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeFeilvActivity.class));
                }
                break;
            case R.id.me_r2:
                //我的推荐人
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MeRefereesActivity.class));
                }
                break;
            case R.id.me_r3:
                //授权书
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), MePowerAttorneyActivity.class));
                }
                break;
            case R.id.me_r4:
                //设置
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), SetUpActivity.class));
                }
                break;
            case R.id.me_r5:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    //关于我们
                    startActivity(new Intent(getActivity(), MeAboutActivity.class));
                }
                break;
            case R.id.main_me_wallet:
                //我的钱包
                if (Code.equals("2")) {
                    showDialog();
                } else {

                    //startActivity(new Intent(getActivity(), HomeWalletActivity.class));
                    startActivityForResult(new Intent(getActivity(), HomeWalletActivity.class),140);
                }
                break;
            case R.id.main_me_or_code:
                //个人信息
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), PersonalActivity.class));
                }
                break;
            case R.id.me_r33:
                //我的收货地址
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    Intent intent = new Intent(getActivity(), MeAddressActivity.class);
                    intent.putExtra("status","2");
                    startActivity(intent);
                }
                break;
        }
    }

    //刷新
    @Override
    public void onRefresh() {
        //进行刷新操作
        main_me_refresh.setRefreshing(true);
//        posData();
        MeData();
    }


    private void setText() {
        Uri imgurl=Uri.parse(headUrl);
        // 清除Fresco对这条验证码的缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);

        me_logo.setImageURI(imgurl);
        main_me_name.setText(nickName);
        main_me_cooperation_code.setText("合作方编号：" + merchCode);
        intger_tv.setText(integral + "");
        wellat_tv.setText(walletAmount);
    }

    //数据配置
    public void posData() {
        //进行网络请求数据
        RequestParams params = new RequestParams();
        params.put("userId", userId);
        HttpRequest.getHomeDate(params, Token, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                main_me_refresh.setRefreshing(false);
                try {
                    Code = "1";
                    JSONObject result = new JSONObject(responseObj.toString());
                    nickName = result.getJSONObject("data").getString("nickName");
                    merchCode = result.getJSONObject("data").getString("merchCode");
                    headUrl = result.getJSONObject("data").getString("portrait");
                    servicePhone = result.getJSONObject("data").getString("servicePhone");
                    //SPUtils.put(getActivity(), "servicePhone", servicePhone);

                    setText();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                main_me_refresh.setRefreshing(false);
                if (failuer.getEcode() == 500001) {
                    Code = "2";
                    showDialog();
                } else {
                    Failuer(failuer.getEcode(), failuer.getEmsg());
                }
            }
        });

    }

    public void MeData(){
        //进行网络请求数据
        RequestParams params = new RequestParams();
        params.put("userId",userId);
        HttpRequest.getMeData(params, Token, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                main_me_refresh.setRefreshing(false);
                try {
                    Code = "1";
                    JSONObject result = new JSONObject(responseObj.toString());
                    nickName = result.getJSONObject("data").getString("nickName");
                    merchCode = result.getJSONObject("data").getString("merchCode");
                    headUrl = result.getJSONObject("data").getString("portrait");
//                    servicePhone = result.getJSONObject("data").getString("servicePhone");
//                    SPUtils.put(getActivity(), "servicePhone", servicePhone);
                    integral = result.getJSONObject("data").getString("integral");
                    walletAmount = result.getJSONObject("data").getString("walletAmount");
                    setText();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                main_me_refresh.setRefreshing(false);
                if (failuer.getEcode() == 500001) {
                    Code = "2";
                    showDialog();
                } else {
                    Failuer(failuer.getEcode(), failuer.getEmsg());
                }
            }

        });
    }


    /**
     * 提示用户去完善信息，报件
     */
    public void showDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.perfect_dialog_layout, null);
        Button perfect_into = view.findViewById(R.id.perfect_into);
        ImageView perfect_out = view.findViewById(R.id.perfect_out);
        Dialog dialog = new MyDialog1(getActivity(), true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        perfect_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        perfect_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), RealNameOnActivity.class);
                intent.putExtra("infoCode", Code);
                startActivity(intent);
            }
        });
    }

    // 拨打电话dialog
    private void showDialog1(String value) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("您是否拨打  " + value);
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
                callPhone(value);
            }
        });
    }


    //更新一下界面
    public void onEventMainThread(MeFragment ev) {
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shouLog("------------------>","刷新了");
        onRefresh();
    }
}
