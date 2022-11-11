package com.example.miaofupos.newprojectview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.miaofupos.R;
import com.example.miaofupos.adapter.NewMerchantsGridViewAdapter;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.newprojectview.adapter.NewAreaAdapter;
import com.example.miaofupos.newprojectview.adapter.NewCityAdapter;
import com.example.miaofupos.newprojectview.adapter.NewProvinceAdapter;
import com.example.miaofupos.newprojectview.bean.NewCityBean;
import com.example.miaofupos.newprojectview.bean.NewDistrictBean;
import com.example.miaofupos.newprojectview.bean.NewProvinceBean;
import com.example.miaofupos.newprojectview.bean.NewRateBean;
import com.example.miaofupos.utils.Utils;
import com.example.miaofupos.views.MyDialog;
import com.example.miaofupos.views.MyDialog1;
import com.example.miaofupos.views.MyGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.miaofupos.utils.Utils.isLocServiceEnable;

/**
 * 作者: qgl
 * 创建日期：2021/8/17
 * 描述: POSP商户报件1
 */
public class AddMerchantsActivity1 extends BaseActivity implements View.OnClickListener {
    //选择商户注册省市区
    private RelativeLayout province_relative;
    //选择商户注册省市区、TextView
    private TextView province_tv;
    //联系人
    private EditText quote_contact_name;
    //商户简写
    private EditText quote_posCode;
    //详细地址
    private EditText quote_address;
    //申请人联系电话
    private EditText quote_phone;
    //提交按钮
    private Button submit_bt;
    /**********--选择地址，仿JD 开始--**************/
    private ListView mCityListView;
    private TextView mProTv;
    private TextView mCityTv;
    private TextView mAreaTv;
    private ImageView mCloseImg;
    private PopupWindow popWindow;
    private View mSelectedLine;
    private View popView;
    private NewProvinceAdapter mProvinceAdapter;
    private NewCityAdapter mCityAdapter;
    private NewAreaAdapter mAreaAdapter;
    private List<NewProvinceBean> provinceList = new ArrayList<>();
    private List<NewCityBean> cityList = null;
    private List<NewDistrictBean> areaList = null;
    private int tabIndex = 0;
    private Context context;
    private String colorSelected = "#ff181c20";
    private String colorAlert = "#ffff4444";
    private String province = ""; //省编码
    private String city = ""; //市编码
    private String area = ""; //区编码
    /**********--选择地址，仿JD 结束--**************/
    //返回键
    private LinearLayout iv_back;
    //需要关闭
    public static AddMerchantsActivity1 instance = null;
    private List<NewRateBean> rateBeans = new ArrayList<>();
    //类型适配器Adapter
    private NewMerchantsGridViewAdapter madapter;
    //费率ID
    private RelativeLayout feilv_relative;
    private TextView feilv_tv;
    private String rateId = "";

    /**************  地图定位  ****************/
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String Longitude = ""; //经
    private String Latitude = "";//纬度
    private String loctionEorr = "";
    /**************  地图定位  ****************/

    private String provinceName;  // 省名称
    private String cityName;  // 市名称
    private String areaName;  // 区名称
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.addmerchants_activity_01;
    }

    @Override
    protected void initView() {
        instance = this;
        context = this;
        if (isLocServiceEnable(this)){
            //初始化定位
            initLocation();
            //开启定位
            startLocation();
        }else {
            isDialog("请您设置位置权限在进行报件！");
        }
        iv_back = findViewById(R.id.iv_back);
        province_relative = findViewById(R.id.province_relative);
        province_tv = findViewById(R.id.province_tv);
        quote_phone = findViewById(R.id.quote_phone);
        submit_bt = findViewById(R.id.submit_bt);
        quote_contact_name = findViewById(R.id.quote_contact_name);
        quote_posCode = findViewById(R.id.quote_posCode);
        quote_address = findViewById(R.id.quote_address);
        feilv_relative = findViewById(R.id.feilv_relative);
        feilv_tv = findViewById(R.id.feilv_tv);
        posRate();
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        try {
            //等待框提示词
            loadDialog.setTitleStr("获取定位中...");
            //开启等待框
            loadDialog.show();
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 初始化定位
     */
    private void initLocation() {
        AMapLocationClient.updatePrivacyShow(this, true, true);
        AMapLocationClient.updatePrivacyAgree(this, true);
        //初始化client
        try {
            locationClient = new AMapLocationClient(this.getApplicationContext());
            locationOption = getDefaultOption();
            //设置定位参数
            locationClient.setLocationOption(locationOption);
            // 设置定位监听
            locationClient.setLocationListener(locationListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //关闭等待框
                loadDialog.dismiss();
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    Longitude = location.getLongitude() + "";
                    Latitude = location.getLatitude() + "";
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");
                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    shouLog("地址信息1：", sb.toString());
                    quote_address.setText(location.getAddress());
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    loctionEorr = sb.toString();
                    shouLog("---》",sb.toString());

                }
                //停止定位
                stopLocation();
            }else {
                shouLog("---》","失败了");
            }
        }
    };

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        try {
            // 停止定位
            locationClient.stopLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        province_relative.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        feilv_relative.setOnClickListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void initData() {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.province_relative:
                showCityPicker();
                break;
            case R.id.submit_bt:
                Intent intent = new Intent(AddMerchantsActivity1.this, AddMerchantsActivity2.class);
                if (TextUtils.isEmpty(quote_posCode.getText().toString().trim())) {
                    showToast(3, "请输入SN");
                    return;
                }
                if (TextUtils.isEmpty(rateId)) {
                    showToast(3, "请选择费率");
                    return;
                }
                if (TextUtils.isEmpty(quote_contact_name.getText().toString().trim())) {
                    showToast(3, "联系人");
                    return;
                }

                if (TextUtils.isEmpty(quote_phone.getText().toString().trim())) {
                    showToast(3, "联系电话");
                    return;
                }

                if (TextUtils.isEmpty(province.trim())) {
                    showToast(3, "商户省市区");
                    return;
                }
                if (TextUtils.isEmpty(quote_address.getText().toString().trim())) {
                    showToast(3, "详细地址");
                    return;
                }
                if (Longitude.equals("") && Longitude == ""){
                    showToast(3, loctionEorr);
                    return;
                }
                intent.putExtra("quote_contact_name", quote_contact_name.getText().toString().trim());
                intent.putExtra("quote_shop_jname", quote_contact_name.getText().toString().trim());
                intent.putExtra("PosCode", quote_posCode.getText().toString().trim());
                intent.putExtra("rateId", rateId);
                intent.putExtra("quote_phone", quote_phone.getText().toString().trim());
                intent.putExtra("quote_service_phone", quote_phone.getText().toString().trim());
                intent.putExtra("quote_address", quote_address.getText().toString().trim());
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("area", area);
                intent.putExtra("Longitude", Longitude);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("provinceName", provinceName);
                intent.putExtra("cityName", cityName);
                intent.putExtra("areaName", areaName);

                startActivity(intent);
                break;
            case R.id.feilv_relative:
                showDialog(rateBeans);
                break;
        }
    }

    //获取费率
    private void posRate() {
        RequestParams params = new RequestParams();
        HttpRequest.posEchoFeeId(params,getToken(),new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    rateBeans = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<NewRateBean>>() {
                            }.getType());
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

    /***
     * 选择类型
     */
    public void showDialog(List<NewRateBean> mList) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_quote_type_dialog, null);
        Button data_bill_dialog_btn = view.findViewById(R.id.data_bill_dialog_btn);
        MyGridView data_bill_dialog_grid = view.findViewById(R.id.data_bill_dialog_grid);
        madapter = new NewMerchantsGridViewAdapter(AddMerchantsActivity1.this, mList);
        data_bill_dialog_grid.setAdapter(madapter);
        data_bill_dialog_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //把点击的position传递到adapter里面去
                madapter.changeState(i);
                feilv_tv.setText(mList.get(i).getFeeValue());
                rateId = mList.get(i).getFeeId();
            }
        });
        Dialog dialog = new MyDialog(AddMerchantsActivity1.this, true, true, (float) 1).setNewView(view);
        dialog.show();
        data_bill_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 如果没有权限、弹框
     * @param title
     */
    private void isDialog(String title){
        View view = LayoutInflater.from(mContext).inflate(R.layout.is_dialog_layout, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText(title);
        Dialog dialog = new MyDialog1(AddMerchantsActivity1.this, true, true, (float) 0.7).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);;
        dialog.show();
        dialog_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                dialog.dismiss();
                finish();
            }
        });
    }
    /************************************** 获取省市区功能开始--（代码太繁琐后期要优化）--****************************************/
    private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                case 0:
                    provinceList = (List) msg.obj;
                    mProvinceAdapter.notifyDataSetChanged();
                    mCityListView.setAdapter(mProvinceAdapter);
                    break;
                case 1:
                    cityList = (List) msg.obj;
                    mCityAdapter.notifyDataSetChanged();
                    if (cityList != null && !cityList.isEmpty()) {
                        mCityListView.setAdapter(mCityAdapter);
                        tabIndex = 1;
                    }
                    break;
                case 2:
                    areaList = (List) msg.obj;
                    mAreaAdapter.notifyDataSetChanged();
                    if (areaList != null && !areaList.isEmpty()) {
                        mCityListView.setAdapter(mAreaAdapter);
                        tabIndex = 2;
                    }
            }
            updateTabsStyle(tabIndex);
            updateIndicator();
            return true;
        }
    });

    public void showCityPicker() {
        initJDCityPickerPop();
        if (!isShow()) {
            popWindow.showAtLocation(popView, 80, 0, 0);
        }

    }

    private void initJDCityPickerPop() {
        tabIndex = 0;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popView = layoutInflater.inflate(R.layout.pop_jdcitypicker, (ViewGroup) null);
        mCityListView = (ListView) popView.findViewById(R.id.city_listview);
        mProTv = (TextView) popView.findViewById(R.id.province_tv);
        mCityTv = (TextView) popView.findViewById(R.id.city_tv);
        mAreaTv = (TextView) popView.findViewById(R.id.area_tv);
        mCloseImg = (ImageView) popView.findViewById(R.id.close_img);
        mSelectedLine = popView.findViewById(R.id.selected_line);
        popWindow = new PopupWindow(popView, -1, -2);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mCloseImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hidePop();
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mProTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 0;
                if (mProvinceAdapter != null) {
                    mCityListView.setAdapter(mProvinceAdapter);
                    if (mProvinceAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mProvinceAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 1;
                if (mCityAdapter != null) {
                    mCityListView.setAdapter(mCityAdapter);
                    if (mCityAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mCityAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mAreaTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 2;
                if (mAreaAdapter != null) {
                    mCityListView.setAdapter(mAreaAdapter);
                    if (mAreaAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mAreaAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList(position);
            }
        });
        Utils.setBackgroundAlpha(context, 0.5F);
        updateIndicator();
        updateTabsStyle(-1);
        posCity();
    }

    @SuppressLint("WrongConstant")
    private void updateTabsStyle(int tabIndex) {
        switch (tabIndex) {
            case -1:
            case 0:
                mProTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(8);
                mAreaTv.setVisibility(8);
                break;
            case 1:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
                mAreaTv.setVisibility(8);
                break;
            case 2:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorSelected));
                mAreaTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
                mAreaTv.setVisibility(0);
        }

    }

    private void updateIndicator() {
        popView.post(new Runnable() {
            public void run() {
                switch (tabIndex) {
                    case 0:
                        tabSelectedIndicatorAnimation(mProTv).start();
                        break;
                    case 1:
                        tabSelectedIndicatorAnimation(mCityTv).start();
                        break;
                    case 2:
                        tabSelectedIndicatorAnimation(mAreaTv).start();
                }

            }
        });
    }

    private AnimatorSet tabSelectedIndicatorAnimation(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mSelectedLine, "X", new float[]{mSelectedLine.getX(), tab.getX()});
        final ViewGroup.LayoutParams params = mSelectedLine.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(new int[]{params.width, tab.getMeasuredWidth()});
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (Integer) animation.getAnimatedValue();
                mSelectedLine.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(new Animator[]{xAnimator, widthAnimator});
        return set;
    }

    private void hidePop() {
        if (isShow()) {
            popWindow.dismiss();
        }
    }

    private boolean isShow() {
        return popWindow.isShowing();
    }

    @SuppressLint("WrongConstant")
    private void updateTabVisible() {
        mProTv.setVisibility(provinceList != null && !provinceList.isEmpty() ? 0 : 8);
        mCityTv.setVisibility(cityList != null && !cityList.isEmpty() ? 0 : 8);
        mAreaTv.setVisibility(areaList != null && !areaList.isEmpty() ? 0 : 8);
    }

    private void selectedList(int position) {
        switch (tabIndex) {
            case 0:
                NewProvinceBean provinceBean = mProvinceAdapter.getItem(position);
                if (provinceBean != null) {
                    mProTv.setText("" + provinceBean.getAreaName());
                    mCityTv.setText("请选择");
                    mProvinceAdapter.updateSelectedPosition(position);
                    mProvinceAdapter.notifyDataSetChanged();
                    posCity1(mProvinceAdapter.getItem(position).getAreaCode());
                }
                break;
            case 1:
                NewCityBean cityBean = mCityAdapter.getItem(position);
                if (cityBean != null) {
                    mCityTv.setText("" + cityBean.getAreaName());
                    mAreaTv.setText("请选择");
                    mCityAdapter.updateSelectedPosition(position);
                    mCityAdapter.notifyDataSetChanged();
                    posCity2(mCityAdapter.getItem(position).getAreaCode());
                }
                break;
            case 2:
                NewDistrictBean districtBean = mAreaAdapter.getItem(position);
                if (districtBean != null) {
                    callback(districtBean);
                }
        }

    }

    private void callback(NewDistrictBean districtBean) {
        NewProvinceBean provinceBean = provinceList != null && !provinceList.isEmpty() && mProvinceAdapter != null && mProvinceAdapter.getSelectedPosition() != -1 ? (NewProvinceBean) provinceList.get(mProvinceAdapter.getSelectedPosition()) : null;
        NewCityBean cityBean = cityList != null && !cityList.isEmpty() && mCityAdapter != null && mCityAdapter.getSelectedPosition() != -1 ? (NewCityBean) cityList.get(mCityAdapter.getSelectedPosition()) : null;
        //返回的结果
        province = provinceBean.getAreaCode();
        city = cityBean.getAreaCode();
        area = districtBean.getAreaCode();

        provinceName = provinceBean.getAreaName();
        cityName = cityBean.getAreaName();
        areaName = districtBean.getAreaName();
        Log.e("地址", provinceBean.getAreaCode() + "" + cityBean.getAreaCode() + "" + districtBean.getAreaCode());
        province_tv.setText(provinceBean + "-" + cityBean + "-" + districtBean);
        hidePop();
    }

    // 获取地区编码省
    public void posCity() {
        RequestParams params = new RequestParams();
        params.put("areaLevel", "1");
        params.put("parentCode", "");
        HttpRequest.getArea(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                ceshi(responseObj);
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    private void ceshi(Object responseObj) {
        //需要转化为实体对象
        Gson gson = new GsonBuilder().serializeNulls().create();
        try {
            JSONObject result = new JSONObject(responseObj.toString());
            provinceList = gson.fromJson(result.getJSONArray("data").toString(),
                    new TypeToken<List<NewProvinceBean>>() {
                    }.getType());
            if (provinceList != null && !provinceList.isEmpty()) {
                mProvinceAdapter = new NewProvinceAdapter(context, provinceList);
                mCityListView.setAdapter(mProvinceAdapter);
            } else {
                Log.e("MainActivity.tshi", "解析本地城市数据失败！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 获取地区编码市
    public void posCity1(String code) {
        RequestParams params = new RequestParams();
        params.put("areaLevel", "2");
        params.put("parentCode", code);
        HttpRequest.getArea(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    cityList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<NewCityBean>>() {
                            }.getType());
                    mCityAdapter = new NewCityAdapter(context, cityList);
                    mHandler.sendMessage(Message.obtain(mHandler, 1, cityList));

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

    // 获取地区编区
    public void posCity2(String code) {
        RequestParams params = new RequestParams();
        params.put("areaLevel", "3");
        params.put("parentCode", code);
        HttpRequest.getArea(params,getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    areaList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<NewDistrictBean>>() {
                            }.getType());
                    mAreaAdapter = new NewAreaAdapter(context, areaList);
                    mHandler.sendMessage(Message.obtain(mHandler, 2, areaList));
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
    /***************************************** 获取省市区功能结束 --（代码太繁琐后期要优化）***************************************************************/
}
