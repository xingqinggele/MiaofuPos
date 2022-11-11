package com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.bean.IdCardInfo;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.adapter.AreaAdapter;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.adapter.CityAdapter;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.adapter.ProvinceAdapter;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.CityBean;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.DistrictBean;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.ProvinceBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.ImageConvertUtil;
import com.example.miaofupos.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.ocr.sdk.common.ISDKKitResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.miaofupos.utils.checkID.validateCard;

/**
 * 作者: qgl
 * 创建日期：2020/12/16
 * 描述:实名认证（上）
 */
public class RealNameOnActivity extends BaseActivity implements View.OnClickListener {
    /**********--选择地址，仿JD 开始--**************/
    private ListView mCityListView;
    private TextView mProTv;
    private TextView mCityTv;
    private TextView mAreaTv;
    private ImageView mCloseImg;
    private PopupWindow popWindow;
    private View mSelectedLine;
    private View popView;
    private ProvinceAdapter mProvinceAdapter;
    private CityAdapter mCityAdapter;
    private AreaAdapter mAreaAdapter;
    private List<ProvinceBean> provinceList = new ArrayList<>();
    private List<CityBean> cityList = null;
    private List<DistrictBean> areaList = null;
    private int tabIndex = 0;
    private Context context;
    private String colorSelected = "#ff181c20";
    private String colorAlert = "#ffff4444";
    private String province = ""; //省编码
    private String city = ""; //市编码
    private String area = ""; //区编码
    /**********--选择地址，仿JD 结束--**************/
    //重新命名
    private String IdCard1_Url = ""; //身份证正面照片地址
    private String IdCard2_Url = ""; //身份证反面照片地址
    // 返回键
    private LinearLayout iv_back;
    // 身份证正面
    private SimpleDraweeView id_card_is;
    // 身份证反面
    private SimpleDraweeView id_card_the;
    // 姓名
    private EditText name_ed;
    // 身份证号码
    private EditText card_number_ed;
    // 身份证有效期
    private TextView card_year_tv;
    // 商铺地址
    private TextView address_tv;
    // 商户详细地址
    private EditText address_detail_ed;
    // 提交，保存，跳转
    private Button submit_bt;
    // 身份证名字
    private String IdName;
    // 身份证号码
    private String IdNumber;
    // 身份证有效期
    private String IdValidDate;
    // 需要关闭
    public static RealNameOnActivity instance = null;
    // 身份证是否被占用 true 为占用 false 未注册
    private boolean IdNumbers = false;


    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.real_name_on_activity;
    }

    @Override
    protected void initView() {
        instance = this;
        context = this;
        iv_back = findViewById(R.id.iv_back);
        submit_bt = findViewById(R.id.submit_bt);
        address_tv = findViewById(R.id.address_tv);
        id_card_is = findViewById(R.id.id_card_is);
        id_card_the = findViewById(R.id.id_card_the);
        name_ed = findViewById(R.id.name_ed);
        card_number_ed = findViewById(R.id.card_number_ed);
        card_year_tv = findViewById(R.id.card_year_tv);
        address_detail_ed = findViewById(R.id.address_detail_ed);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        address_tv.setOnClickListener(this);
        id_card_is.setOnClickListener(this);
        id_card_the.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //获取地址
            case R.id.address_tv:
                showCityPicker();
                break;
            // 获取身份证正面
            case R.id.id_card_is:
                initSdk(getSecretId(), getSecretKey());
                //弹出界面
                OcrSDKKit.getInstance().startProcessOcr(RealNameOnActivity.this, OcrType.IDCardOCR_FRONT, null,
                        new ISDKKitResultListener() {
                            @Override
                            public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
                                IdCardInfo tempIdCardInfo = new Gson().fromJson(response, IdCardInfo.class);
                                Log.e("response", tempIdCardInfo.getRequestId());
                                Bitmap bitmap = ImageConvertUtil.base64ToBitmap(srcBase64Image);
                                try {
                                    if (bitmap != null)
                                        id_card_is.setImageBitmap(bitmap);
                                    IdCard1_Url = ImageConvertUtil.getFile(bitmap).getCanonicalPath();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                IdName = tempIdCardInfo.getName();
                                IdNumber = tempIdCardInfo.getIdNum();
                                setResultListData(1);
                            }

                            @Override
                            public void onProcessFailed(String errorCode, String message, String requestId) {
                                popTip(errorCode, message);
                                Log.e("requestId", requestId);
                                IdName = "";
                                IdNumber = "";
                            }
                        });

                break;
            // 获取身份证反面
            case R.id.id_card_the:
                initSdk(getSecretId(), getSecretKey());
                //身份证反面
                OcrSDKKit.getInstance().startProcessOcr(RealNameOnActivity.this, OcrType.IDCardOCR_BACK, null,
                        new ISDKKitResultListener() {
                            @Override
                            public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
                                IdCardInfo tempIdCardInfo = new Gson().fromJson(response, IdCardInfo.class);
                                Log.e("response", tempIdCardInfo.getRequestId());
                                Bitmap bitmap = ImageConvertUtil.base64ToBitmap(srcBase64Image);
                                try {
                                    if (bitmap != null)
                                        id_card_the.setImageBitmap(bitmap);
                                    IdCard2_Url = ImageConvertUtil.getFile(bitmap).getCanonicalPath();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                IdValidDate = tempIdCardInfo.getValidDate();
                                setResultListData(2);
                            }

                            @Override
                            public void onProcessFailed(String errorCode, String message, String requestId) {
                                popTip(errorCode, message);
                                Log.e("11111requestId", requestId);
                                IdValidDate = "";
                            }
                        });

                break;
            case R.id.submit_bt:
                //新增小微1页面数据
                Intent intent = new Intent(RealNameOnActivity.this, RealNameUnActivity.class);
                if (TextUtils.isEmpty(IdCard1_Url)) {
                    showToast(3, "请上传身份证正面照片");
                    return;
                }
                if (TextUtils.isEmpty(IdCard2_Url)) {
                    showToast(3, "请上传身份证反面照片");
                    return;
                }
                if (TextUtils.isEmpty(name_ed.getText().toString().trim())) {
                    showToast(3, "请输入姓名");
                    return;
                }
                if (!validateCard(card_number_ed.getText().toString().trim())) {
                    showToast(3, "请选输入正确的身份证号");
                    return;
                }
                if (TextUtils.isEmpty(card_year_tv.getText().toString().trim())) {
                    showToast(3, "请选择身份证有效期");
                    return;
                }
                if (TextUtils.isEmpty(address_tv.getText().toString().trim())) {
                    showToast(3, "请选择地区");
                    return;
                }
                if (TextUtils.isEmpty(address_detail_ed.getText().toString().trim())) {
                    showToast(3, "请输入地址");
                    return;
                }
                if (IdNumbers) {
                    showToast(2, "您身份证已经存在，请更换身份证在提交");
                    return;
                }
                intent.putExtra("userName", name_ed.getText().toString().trim());
                intent.putExtra("idCard_number", card_number_ed.getText().toString().trim());
                intent.putExtra("idCard_year", card_year_tv.getText().toString().trim());
                intent.putExtra("address_province", province);
                intent.putExtra("address_city", city);
                intent.putExtra("address_area", area);
                intent.putExtra("address_detail", address_detail_ed.getText().toString().trim());
                intent.putExtra("IdCard1_Url", IdCard1_Url);
                intent.putExtra("IdCard2_Url", IdCard2_Url);
                startActivity(intent);
                break;
        }
    }

    /**
     * 腾讯卡片识别初始化
     */
    private void initSdk(String secretId, String secretKey) {
        // 启动参数配置
        OcrType ocrType = OcrType.BankCardOCR; // 设置默认的业务识别，银行卡
        OcrSDKConfig configBuilder = OcrSDKConfig.newBuilder(secretId, secretKey, null)
                .OcrType(ocrType)
                .ModeType(OcrModeType.OCR_DETECT_MANUAL)
                .build();
        // 初始化SDK
        OcrSDKKit.getInstance().initWithConfig(this.getApplicationContext(), configBuilder);
    }

    // 配置识别出来的数据
    private void setResultListData(int value) {
        if (IdName != null && !IdName.isEmpty()) {
            name_ed.setText(IdName);
            card_number_ed.setText(IdNumber);
            //调用接口，请求是否身份证号唯一
            if (value == 1)
                isIdNumber(IdNumber);
        }
        if (IdValidDate != null && !IdValidDate.isEmpty()) {
            card_year_tv.setText(IdValidDate);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OcrSDKKit.getInstance().release();
    }

    //请求接口，查询身份证号是否已注册
    public void isIdNumber(String value) {
        RequestParams params = new RequestParams();
        params.put("merchIdcard", value);
        HttpRequest.getIsIdNumber(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (result.getString("data").equals("true")) {
                        IdNumbers = true;
                        showToast(2, "您身份证已经存在，请更换身份证在提交");
                    } else {
                        IdNumbers = false;
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
                ProvinceBean provinceBean = mProvinceAdapter.getItem(position);
                if (provinceBean != null) {
                    mProTv.setText("" + provinceBean.getCname());
                    mCityTv.setText("请选择");
                    mProvinceAdapter.updateSelectedPosition(position);
                    mProvinceAdapter.notifyDataSetChanged();
                    posCity1(mProvinceAdapter.getItem(position).getDictValue());
                }
                break;
            case 1:
                CityBean cityBean = mCityAdapter.getItem(position);
                if (cityBean != null) {
                    mCityTv.setText("" + cityBean.getCname());
                    mAreaTv.setText("请选择");
                    mCityAdapter.updateSelectedPosition(position);
                    mCityAdapter.notifyDataSetChanged();
                    posCity2(mCityAdapter.getItem(position).getDictValue());
                }
                break;
            case 2:
                DistrictBean districtBean = mAreaAdapter.getItem(position);
                if (districtBean != null) {
                    callback(districtBean);
                }
        }

    }

    private void callback(DistrictBean districtBean) {
        ProvinceBean provinceBean = provinceList != null && !provinceList.isEmpty() && mProvinceAdapter != null && mProvinceAdapter.getSelectedPosition() != -1 ? (ProvinceBean) provinceList.get(mProvinceAdapter.getSelectedPosition()) : null;
        CityBean cityBean = cityList != null && !cityList.isEmpty() && mCityAdapter != null && mCityAdapter.getSelectedPosition() != -1 ? (CityBean) cityList.get(mCityAdapter.getSelectedPosition()) : null;
        //返回的结果
        province = provinceBean.getDictValue();
        city = cityBean.getDictValue();
        area = districtBean.getDictValue();
        Log.e("地址", provinceBean.getDictValue() + "" + cityBean.getDictValue() + "" + districtBean.getDictValue());
        address_tv.setText(provinceBean + "" + cityBean + "" + districtBean);
        hidePop();
    }

    // 获取地区编码省
    public void posCity() {
        RequestParams params = new RequestParams();
        params.put("dictType", "1");
        params.put("dictLevelCode", "");
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    provinceList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<ProvinceBean>>() {
                            }.getType());
                    if (provinceList != null && !provinceList.isEmpty()) {
                        mProvinceAdapter = new ProvinceAdapter(context, provinceList);
                        mCityListView.setAdapter(mProvinceAdapter);
                    } else {
                        Log.e("MainActivity.tshi", "解析本地城市数据失败！");
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

    // 获取地区编码市
    public void posCity1(String code) {
        RequestParams params = new RequestParams();
        params.put("dictType", "");
        params.put("dictLevelCode", code);
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    cityList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<CityBean>>() {
                            }.getType());
                    mCityAdapter = new CityAdapter(context, cityList);
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
        params.put("dictType", "");
        params.put("dictLevelCode", code);
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    areaList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<DistrictBean>>() {
                            }.getType());
                    mAreaAdapter = new AreaAdapter(context, areaList);
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
