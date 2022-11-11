package com.example.miaofupos.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.MainActivity;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.fragment.bean.HomeBean;
import com.example.miaofupos.homefragment.homeInvitepartners.HomeInvitePartnersActivity;
import com.example.miaofupos.homefragment.homebagactivity.HomeBagActivity;
import com.example.miaofupos.homefragment.homeequipment.HomeEquipmentActivity;
import com.example.miaofupos.homefragment.homeintegral.HomeIntegralActivity;
import com.example.miaofupos.homefragment.homelibao.LiBaoActivity;
import com.example.miaofupos.homefragment.homelist.HomeListActivity;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.RealNameOnActivity;
import com.example.miaofupos.homefragment.homemerchants.memerchants.activity.MeMerchantsActivity;
import com.example.miaofupos.homefragment.homemessage.HomeMessageActivity;
import com.example.miaofupos.homefragment.homequoteactivity.HomeQuoteActivity1;
import com.example.miaofupos.homefragment.hometeam.HomeTeamActivity;
import com.example.miaofupos.homefragment.transaction.TransactionActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.newprojectview.ChooseQuoteChannelActivity;
import com.example.miaofupos.newprojectview.NewMeQuoteActivity;
import com.example.miaofupos.useractivity.HomeAdvPictureActivity;
import com.example.miaofupos.utils.GlideImageLoader;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.MyDialog1;
import com.example.miaofupos.views.VpSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 作者: qgl
 * 创建日期：2020/12/10
 * 描述:首页，Fragment
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //下拉刷新
    private VpSwipeRefreshLayout ri_home_refresh;
    //用户名
    private String nickName;
    //商户号
    private String merchCode;
    //密钥
    private String Token;
    //用户ID
    private String userId;
    //月交易总额
    private String monthlyTransAmount;
    //月新增伙伴
    private String monthlyNewPartnerCounts;
    //月新增商户数
    private String monthlyNewMerchantCounts;
    //标题姓名
    private TextView ri_home_name_tv;
    //消息按钮
    private ImageView ri_home_message_iv;
    //交易额
    private TextView ri_home_turnover_tv;
    //新增伙伴
    private TextView ri_home_new_partner;
    //新增商户
    private TextView ri_home_new_merchants;
    //轮播图
    private Banner ri_banner;
    //我的商户
    private LinearLayout main_home_me_merchants;
    //邀请伙伴
    private LinearLayout main_home_invite_partners;
    //我的伙伴
    private LinearLayout main_home_my_partner;
    //终端管理
    private LinearLayout main_home_terminal_management;
    //排行榜
    private LinearLayout main_home_list;
    //实名认证
    private LinearLayout main_home_real_name;
    //礼包活动
    private LinearLayout main_home_gift_bag;
    //积分兑换
    private LinearLayout main_home_integral;
    //入驻状态，1入驻2未入住
    private String Code = "1";
    //底部轮播图片容器
    private List<HomeBean> list3 = new ArrayList<>();

    /**
     * 接受activity数据
     *
     * @param requestJson
     * @return
     */
    public static HomeFragment newInstance(String requestJson) {
        HomeFragment fragment = new HomeFragment();
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
        return R.layout.home_fragment_main;
    }

    /**
     * 初始化控件
     *
     * @param rootView
     */
    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        ri_home_refresh = rootView.findViewById(R.id.ri_home_refresh);
        ri_banner = rootView.findViewById(R.id.ri_banner);
        ri_home_name_tv = rootView.findViewById(R.id.ri_home_name_tv);
        ri_home_turnover_tv = rootView.findViewById(R.id.ri_home_turnover_tv);
        ri_home_new_partner = rootView.findViewById(R.id.ri_home_new_partner);
        ri_home_new_merchants = rootView.findViewById(R.id.ri_home_new_merchants);
        main_home_me_merchants = rootView.findViewById(R.id.main_home_me_merchants);
        main_home_invite_partners = rootView.findViewById(R.id.main_home_invite_partners);
        main_home_my_partner = rootView.findViewById(R.id.main_home_my_partner);
        main_home_terminal_management = rootView.findViewById(R.id.main_home_terminal_management);
        main_home_list = rootView.findViewById(R.id.main_home_list);
        main_home_real_name = rootView.findViewById(R.id.main_home_real_name);
        main_home_gift_bag = rootView.findViewById(R.id.main_home_gift_bag);
        main_home_integral = rootView.findViewById(R.id.main_home_integral);
        ri_home_message_iv = rootView.findViewById(R.id.ri_home_message_iv);
    }

    //填充数据
    @Override
    protected void loadData() {
        //获取本地存储的密钥
        Token = SPUtils.get(getActivity(), "Token", "").toString();
        //获取本地用户ID
        userId = SPUtils.get(getActivity(), "userId", "").toString();
        //请求首页数据
        posData();
        //请求广告图
        GetAdvertising();
    }

    //触发事件初始化
    @Override
    protected void initListener() {
        main_home_me_merchants.setOnClickListener(this);
        main_home_invite_partners.setOnClickListener(this);
        main_home_my_partner.setOnClickListener(this);
        main_home_terminal_management.setOnClickListener(this);
        main_home_list.setOnClickListener(this);
        main_home_real_name.setOnClickListener(this);
        main_home_gift_bag.setOnClickListener(this);
        main_home_integral.setOnClickListener(this);
        ri_home_message_iv.setOnClickListener(this);
        ri_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (!list3.get(position).getNewsUrl().equals("")) {
                    Intent intent = new Intent(getActivity(), HomeAdvPictureActivity.class);
                    intent.putExtra("title", list3.get(position).getNewsTitle());
                    intent.putExtra("iv", list3.get(position).getNewsUrl());
                    startActivity(intent);
                }
            }
        });
        //设置下拉框的样式
        SwipeData();
    }

    //设置下拉样式
    public void SwipeData() {
        //设置配色
        ri_home_refresh.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        //下拉监听
        ri_home_refresh.setOnRefreshListener(this);
    }

    // 点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //我的商户
            case R.id.main_home_me_merchants:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), NewMeQuoteActivity.class));
                }
                break;
            //商户报件
            case R.id.main_home_invite_partners:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    Intent intent = new Intent(getActivity(), ChooseQuoteChannelActivity.class);
                    startActivity(intent);
                }
                break;
            //我的伙伴
            case R.id.main_home_my_partner:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), HomeTeamActivity.class));
                }
                break;
            //终端管理
            case R.id.main_home_terminal_management:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), HomeEquipmentActivity.class));
                }
                break;
            //商学院
            case R.id.main_home_list:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    Intent intent = new Intent(getActivity(),LiBaoActivity.class);
                    intent.putExtra("title","商学院");
                    startActivity(intent);
                }
                break;
            //实名认证
            case R.id.main_home_real_name:
                if (Code.equals("2")) {
                    //直接跳转实名认证界面
                    Intent intent = new Intent(getActivity(), RealNameOnActivity.class);
                    intent.putExtra("infoCode", Code);
                    startActivity(intent);
                } else {
                    showToast("您已实名认证");
                }
                break;
            //邀请伙伴
            case R.id.main_home_gift_bag:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), HomeInvitePartnersActivity.class));
                }
                break;
            //礼包活动
            case R.id.main_home_integral:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    Intent intent = new Intent(getActivity(), TransactionActivity.class);
                    startActivity(intent);
                }
                break;
            //消息
            case R.id.ri_home_message_iv:
                if (Code.equals("2")) {
                    showDialog();
                } else {
                    startActivity(new Intent(getActivity(), HomeMessageActivity.class));
                }
                break;
        }
    }

    //刷新
    @Override
    public void onRefresh() {
        //进行刷新操作
        ri_home_refresh.setRefreshing(true);
        //获取首页数据
        posData();
        //请求广告图
        GetAdvertising();
    }

    //进行网络请求数据
    public void posData() {
        RequestParams params = new RequestParams();
        //用户ID
        params.put("userId", userId);
        //发起 params、token 、回调
        HttpRequest.getHomeDate(params, Token, new ResponseCallback() {
            //返回成功
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载框
                ri_home_refresh.setRefreshing(false);
                try {
                    //实名状态赋值
                    Code = "1";
                    //String 转 JSONObject
                    JSONObject result = new JSONObject(responseObj.toString());
                    //月交易总额
                    monthlyTransAmount = new BigDecimal(result.getJSONObject("data").getString("monthlyTransAmount")).toString();
//                    monthlyTransAmount = new BigDecimal("170548832.01").toString();
                    //月新增伙伴
                    monthlyNewPartnerCounts = result.getJSONObject("data").getString("monthlyNewPartnerCounts");
                    //月新增商户数
                    monthlyNewMerchantCounts = result.getJSONObject("data").getString("monthlyNewMerchantCounts");
                    //昵称
                    nickName = result.getJSONObject("data").getString("nickName");
                    //邀请码
                    merchCode = result.getJSONObject("data").getString("merchCode");
                    //本地存储昵称
                    SPUtils.put(getActivity(), "nickName", nickName);
                    //本地存储邀请码
                    SPUtils.put(getActivity(), "merchCode", merchCode);
                    //调用显示方法
                    setText();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            //返回失败
            @Override
            public void onFailure(OkHttpException failuer) {
                //关闭加载框
                ri_home_refresh.setRefreshing(false);
                //返回值500001 证明用户还未实名认证
                if (failuer.getEcode() == 500001) {
                    //实名状态赋值
                    Code = "2";
                    //弹出实名认证dialog
                    showDialog();
                } else {
                    //调用失败的返回方法
                    Failuer(failuer.getEcode(), failuer.getEmsg());
                }
            }
        });

    }

    //显示用户信息
    private void setText() {
        ri_home_name_tv.setText("Hi，" + nickName);
        ri_home_turnover_tv.setText(monthlyTransAmount);
        ri_home_new_partner.setText(monthlyNewPartnerCounts);
        ri_home_new_merchants.setText(monthlyNewMerchantCounts);
    }

    /**
     * 提示用户去完善信息，实名认证
     */
    public void showDialog() {
        //dialog xml
        View view = LayoutInflater.from(mContext).inflate(R.layout.perfect_dialog_layout, null);
        //初始化控件
        Button perfect_into = view.findViewById(R.id.perfect_into);
        ImageView perfect_out = view.findViewById(R.id.perfect_out);
        //设置dialog界面宽度
        Dialog dialog = new MyDialog1(getActivity(), true, true, (float) 0.7).setNewView(view);
        //设置点击dialog外围是否消失
        dialog.setCanceledOnTouchOutside(false);
        //显示
        dialog.show();
        //点击取消
        perfect_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭dialog
                dialog.dismiss();
            }
        });
        //点击确认
        perfect_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭dialog
                dialog.dismiss();
                //跳转到实名认证界面
                Intent intent = new Intent(getActivity(), RealNameOnActivity.class);
                startActivity(intent);
            }
        });
    }

    //根据实名认证完返回主页，刷新界面
    public void onEventMainThread(HomeFragment ev) {
        //刷新方法
        onRefresh();
    }

    //请求广告位
    private void GetAdvertising() {
        RequestParams params = new RequestParams();
        HttpRequest.getAdvertising(params, Token, new ResponseCallback() {
            //返回成功
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    List<HomeBean> memberList = gson.fromJson(result.getJSONArray("data0").toString(),
                            new TypeToken<List<HomeBean>>() {
                            }.getType());
                    list3.clear();
                    List<HomeBean> memberList3 = gson.fromJson(result.getJSONArray("data2").toString(),
                            new TypeToken<List<HomeBean>>() {
                            }.getType());
                    list3.addAll(memberList3);
                    List<String> list_path3 = new ArrayList<>();
                    for (int i = 0; i < memberList3.size(); i++) {
                        list_path3.add(memberList3.get(i).getAdvPicture());
                    }
                    ri_banner.setImages(list_path3)
                            .setImageLoader(new GlideImageLoader())
                            .setBannerAnimation(Transformer.Default)
                            //设置轮播间隔时间
                            .setDelayTime(5000)
                            //设置是否为自动轮播，默认是“是”。
                            .isAutoPlay(true)
                            .start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //返回失败
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });


    }


    /**
     * 界面销毁
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除EventBus绑定
        EventBus.getDefault().unregister(this);
    }

}
