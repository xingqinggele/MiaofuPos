package com.example.miaofupos.mefragment.mebank;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.bean.BankCardInfo;
import com.example.miaofupos.cos.CosServiceFactory;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.adapter.CityAdapter;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.adapter.ProvinceAdapter;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.CityBean;
import com.example.miaofupos.homefragment.homemerchants.homenewmerchants.merchantstype.bean.ProvinceBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.CustomConfigUtil;
import com.example.miaofupos.utils.ImageConvertUtil;
import com.example.miaofupos.utils.TimeUtils;
import com.example.miaofupos.utils.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.ocr.sdk.common.ISDKKitResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.miaofupos.utils.Utility.checkBankCard;
import static com.example.miaofupos.utils.checkID.validateCard;

/**
 * ??????: qgl
 * ???????????????2020/12/22
 * ??????:???????????????
 */
public class MeBankModifyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private EditText bank_modify_idname;
    private EditText bank_modify_idnum;
    private EditText bank_modify_phone;
    private EditText me_bank_modify_banknumber;
    private TextView merchant_detail2_verification_code;
    private TextView me_bankcard_tv_bank_where;
    private TextView me_bankcard_tv_bank_city;
    private SimpleDraweeView me_bank_modify_bank_card;
    private Button me_bank_modify_sub;

    // ??????????????????JD
    private ListView mCityListView;
    private TextView mProTv;
    private TextView mCityTv;
    private TextView mAreaTv;
    private ImageView mCloseImg;
    private PopupWindow popwindow;
    private View mSelectedLine;
    private View popview;
    private ProvinceAdapter mProvinceAdapter;
    private CityAdapter mCityAdapter;
    private List<ProvinceBean> provinceList = new ArrayList<>();
    private List<CityBean> cityList = null;
    private int tabIndex = 0;
    private Context context;
    private String colorSelected = "#ff181c20";
    private String colorAlert = "#ffff4444";
    private String region = "ap-beijing"; // ???????????????
    private String folderName = "";
    private CosXmlService cosXmlService;
    private TransferManager transferManager;
    private COSXMLUploadTask cosxmlTask;
    private Bitmap retBitmap;
    private String Bank_Url = "none"; //?????????????????????
    private EditText me_bank_modify_mcode; //?????????
    private String banUrl_type = "1"; //  ?????????1??? ???????????????url,?????????2?????????????????????

    //?????????????????????
    private ImageView bank_btn;
    //?????????????????????
    private List<String>bankNameList;
    private OptionsPickerView reasonPicker;//???????????????????????????
    private String mctBankType = "";
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

            }
            updateTabsStyle(tabIndex);
            updateIndicator();
            return true;
        }
    });

    @Override
    protected int getLayoutId() {
        //?????????????????????
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.me_bank_modify_activity;
    }

    @Override
    protected void initView() {
        context = MeBankModifyActivity.this;
        // ????????????????????????
        cosXmlService = CosServiceFactory.getCosXmlService(this, region, getSecretId(), getSecretKey(), true);
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);

        iv_back = findViewById(R.id.iv_back);
        bank_modify_idname = findViewById(R.id.bank_modify_idname);
        bank_modify_idnum = findViewById(R.id.bank_modify_idnum);
        bank_modify_phone = findViewById(R.id.bank_modify_phone);
        merchant_detail2_verification_code = findViewById(R.id.merchant_detail2_verification_code);
        me_bank_modify_banknumber = findViewById(R.id.me_bank_modify_banknumber);
        me_bankcard_tv_bank_where = findViewById(R.id.me_bankcard_tv_bank_where);
        me_bankcard_tv_bank_city = findViewById(R.id.me_bankcard_tv_bank_city);
        me_bank_modify_bank_card = findViewById(R.id.me_bank_modify_bank_card);
        me_bank_modify_sub = findViewById(R.id.me_bank_modify_sub);
        me_bank_modify_mcode = findViewById(R.id.me_bank_modify_mcode);
        bank_btn = findViewById(R.id.bank_btn);
        initReason();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        merchant_detail2_verification_code.setOnClickListener(this);
        me_bankcard_tv_bank_city.setOnClickListener(this);
        me_bank_modify_bank_card.setOnClickListener(this);
        me_bank_modify_sub.setOnClickListener(this);
        bank_btn.setOnClickListener(this);
        me_bankcard_tv_bank_where.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bank_modify_idname.setText(getIntent().getStringExtra("idCardName"));
        bank_modify_idnum.setText(getIntent().getStringExtra("idCard"));
        bank_modify_phone.setText(getIntent().getStringExtra("bankReservedMobile").substring(0,3) + "****" + getIntent().getStringExtra("bankReservedMobile").substring(getIntent().getStringExtra("bankReservedMobile").length() - 4));
        me_bank_modify_banknumber.setText(getIntent().getStringExtra("bankCardNo"));
        me_bankcard_tv_bank_where.setText(getIntent().getStringExtra("bankName"));
        mctBankType = getIntent().getStringExtra("bankName");
        me_bankcard_tv_bank_city.setText(getIntent().getStringExtra("bankCity"));
        Uri imgurl=Uri.parse(getIntent().getStringExtra("bankCardImg"));
        // ??????Fresco???????????????????????????
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imgurl);
        imagePipeline.evictFromDiskCache(imgurl);
        // combines above two lines
        imagePipeline.evictFromCache(imgurl);
        me_bank_modify_bank_card.setImageURI(imgurl);
        Bank_Url = getIntent().getStringExtra("bankCardImg");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.merchant_detail2_verification_code:
                //if (isChinaPhoneLegal(getIntent().getStringExtra("bankReservedMobile"))) {
                    //????????????
                    getPhoneCode(getIntent().getStringExtra("bankReservedMobile"),merchant_detail2_verification_code);
              //  } else {
                  //  Toast.makeText(MeBankModifyActivity.this, "???????????????????????????", Toast.LENGTH_LONG).show();
                //}
                break;
            case R.id.me_bankcard_tv_bank_city:
                showCityPicker();
                break;
//            case R.id.me_bank_modify_bank_card:
//                initSdk(getSecretId(), getSecretKey());
//                OcrSDKKit.getInstance().startProcessOcr(MeBankModifyActivity.this, OcrType.BankCardOCR,
//                        CustomConfigUtil.getInstance().getCustomConfigUi(), new ISDKKitResultListener() {
//                            @Override
//                            public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
//                                //?????????????????????
//                                getBank_information(response, srcBase64Image);
//                            }
//
//                            @Override
//                            public void onProcessFailed(String errorCode, String message, String requestId) {
//                                popTip(errorCode, message);
//                                Log.e("requestId", requestId);
//                            }
//                        });
//                break;
            case R.id.bank_btn:
                initSdk(getSecretId(), getSecretKey());
                OcrSDKKit.getInstance().startProcessOcr(MeBankModifyActivity.this, OcrType.BankCardOCR,
                        CustomConfigUtil.getInstance().getCustomConfigUi(), new ISDKKitResultListener() {
                            @Override
                            public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
                                //?????????????????????
                                getBank_information(response, srcBase64Image);
                            }

                            @Override
                            public void onProcessFailed(String errorCode, String message, String requestId) {
                                popTip(errorCode, message);
                                Log.e("requestId", requestId);
                            }
                        });
                break;
            case R.id.me_bankcard_tv_bank_where:
                reasonPicker.show();
                break;
            case R.id.me_bank_modify_sub:
                if (!validateCard(bank_modify_idnum.getText().toString().trim())) {
                    showToast(3, "?????????????????????????????????");
                    return;
                }

                if (!checkBankCard(me_bank_modify_banknumber.getText().toString().trim())){
                    showToast(3, "??????????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(mctBankType)) {
                    showToast(3, "??????????????????");
                    return;
                }
                if (TextUtils.isEmpty(me_bank_modify_mcode.getText().toString().trim())) {
                    showToast(3, "??????????????????");
                    return;
                }
                if (banUrl_type.equals("2")){
                    folderName = "authentication" + "/" + getIntent().getStringExtra("idCard") + "/" + TimeUtils.getNowTime("day");
                    upload(folderName);
                }else {
                    Bank_Url = "none";
                    getData();
                }
                break;
        }
    }

    /************************************* ??????????????? ?????? ***************************************/
    // ?????????????????????
    public void posCity() {
        RequestParams params = new RequestParams();
        params.put("dictType", "1");
        params.put("dictLevelCode", "");
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
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
                        Log.e("MainActivity.tshi", "?????????????????????????????????");
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

    // ?????????????????????
    public void posCity1(String code) {
        RequestParams params = new RequestParams();
        params.put("dictType", "");
        params.put("dictLevelCode", code);
        HttpRequest.getCity(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //???????????????????????????
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

    public void showCityPicker() {
        initJDCityPickerPop();
        if (!isShow()) {
            popwindow.showAtLocation(popview, 80, 0, 0);
        }

    }

    private void initJDCityPickerPop() {
        tabIndex = 0;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_jdcitypicker, (ViewGroup) null);
        mCityListView = (ListView) popview.findViewById(R.id.city_listview);
        mProTv = (TextView) popview.findViewById(R.id.province_tv);
        mCityTv = (TextView) popview.findViewById(R.id.city_tv);
        mAreaTv = (TextView) popview.findViewById(R.id.area_tv);
        mCloseImg = (ImageView) popview.findViewById(R.id.close_img);
        mSelectedLine = popview.findViewById(R.id.selected_line);
        popwindow = new PopupWindow(popview, -1, -2);
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setBackgroundDrawable(new ColorDrawable());
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);
        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
        popview.post(new Runnable() {
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
            popwindow.dismiss();
        }

    }

    private boolean isShow() {
        return popwindow.isShowing();
    }

    @SuppressLint("WrongConstant")
    private void updateTabVisible() {
        mProTv.setVisibility(provinceList != null && !provinceList.isEmpty() ? 0 : 8);
        mCityTv.setVisibility(cityList != null && !cityList.isEmpty() ? 0 : 8);
    }


    private void selectedList(int position) {
        switch (tabIndex) {
            case 0:
                ProvinceBean provinceBean = mProvinceAdapter.getItem(position);
                if (provinceBean != null) {
                    mProTv.setText("" + provinceBean.getCname());
                    mCityTv.setText("?????????");
                    mProvinceAdapter.updateSelectedPosition(position);
                    mProvinceAdapter.notifyDataSetChanged();
                    posCity1(mProvinceAdapter.getItem(position).getDictValue());
                }
                break;
            case 1:
                CityBean cityBean = mCityAdapter.getItem(position);
                if (cityBean != null) {
                    mCityTv.setText("" + cityBean.getCname());
                    mAreaTv.setText("?????????");
                    mCityAdapter.updateSelectedPosition(position);
                    callback();
                }
                break;

        }

    }

    private void callback() {
        ProvinceBean provinceBean = provinceList != null && !provinceList.isEmpty() && mProvinceAdapter != null && mProvinceAdapter.getSelectedPosition() != -1 ? (ProvinceBean) provinceList.get(mProvinceAdapter.getSelectedPosition()) : null;
        CityBean cityBean = cityList != null && !cityList.isEmpty() && mCityAdapter != null && mCityAdapter.getSelectedPosition() != -1 ? (CityBean) cityList.get(mCityAdapter.getSelectedPosition()) : null;
        //???????????????
        Log.e("??????", cityBean.getDictValue());
        me_bankcard_tv_bank_city.setText(provinceBean + "" + cityBean);
        hidePop();
    }

    /**************************************?????????????????????**************************************/
    /**
     * ??????????????????????????????
     */
    private void initSdk(String secretId, String secretKey) {
        // ??????????????????
        OcrType ocrType = OcrType.BankCardOCR; // ???????????????????????????????????????
        OcrSDKConfig configBuilder = OcrSDKConfig.newBuilder(secretId, secretKey, null)
                .OcrType(ocrType)
                .ModeType(OcrModeType.OCR_DETECT_MANUAL)
                .build();
        // ?????????SDK
        OcrSDKKit.getInstance().initWithConfig(this.getApplicationContext(), configBuilder);
    }

    /**
     * ?????????????????????
     *
     * @param response
     * @param srcBase64Image
     */
    public void getBank_information(String response, String srcBase64Image) {
        try {
            if (!srcBase64Image.isEmpty()) {
                retBitmap = ImageConvertUtil.base64ToBitmap(srcBase64Image);
            }
            if (retBitmap != null) {
                banUrl_type = "2";
                me_bank_modify_bank_card.setImageBitmap(retBitmap);
                Bank_Url = ImageConvertUtil.getFile(retBitmap).getCanonicalPath();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!response.isEmpty()) {
            final BankCardInfo bankCardInfo = new Gson().fromJson(response, BankCardInfo.class);
            Log.e("?????????", bankCardInfo.getCardNo() + "----" + bankCardInfo.getBankInfo());
            me_bank_modify_banknumber.setText(bankCardInfo.getCardNo());
            me_bankcard_tv_bank_where.setText(bankCardInfo.getBankInfo().substring(0, bankCardInfo.getBankInfo().indexOf("(")));
            mctBankType = bankCardInfo.getBankInfo().substring(0, bankCardInfo.getBankInfo().indexOf("("));
        } else {
            //Toast.makeText(this, "result is empty", Toast.LENGTH_SHORT).show();
        }
    }
    /*************************************??????????????????????????????**********************************/

    /**
     * ????????????,????????????
     */
    private void upload(String newfolderName) {
        if (TextUtils.isEmpty(Bank_Url)) {
            return;
        }

        if (cosxmlTask == null) {
            File file = new File(Bank_Url);
            String cosPath;
            if (TextUtils.isEmpty(newfolderName)) {
                cosPath = file.getName();
            } else {
                cosPath = newfolderName + File.separator + file.getName();
            }
            cosxmlTask = transferManager.upload(getBucketName(), cosPath, Bank_Url, null);
            Log.e("??????-------???", getBucketName() + "----" + cosPath + "---" + Bank_Url);
            cosxmlTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(final TransferState state) {
                    // refreshUploadState(state);
                }
            });
            cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(final long complete, final long target) {
                    // refreshUploadProgress(complete, target);
                }
            });
            cosxmlTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                    cosxmlTask = null;
                    Log.e("1111", "??????");
                    Bank_Url = cOSXMLUploadTaskResult.accessUrl;
                    getData();
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;
                        Log.e("1111", "????????????");

                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                }
            });

        }
    }


    /************************************??????????????????*************************************/
    // ????????????
    public void getData() {
        RequestParams params = new RequestParams();
        params.put("bankCardNo", me_bank_modify_banknumber.getText().toString().trim());
        params.put("bankCity", me_bankcard_tv_bank_city.getText().toString().trim());
        params.put("bankName", mctBankType);
        params.put("bankReservedMobile", getIntent().getStringExtra("bankReservedMobile"));
        params.put("idCard", bank_modify_idnum.getText().toString().trim());
        params.put("idCardName", bank_modify_idname.getText().toString().trim());
        params.put("userId", getUserId());
        params.put("bankCardImg", Bank_Url);
        params.put("verifyCode", me_bank_modify_mcode.getText().toString().trim());
        HttpRequest.getBankChange(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    showToast(3, result.getString("msg"));
                    finish();
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
    protected void onDestroy() {
        super.onDestroy();
        OcrSDKKit.getInstance().release();
    }



    //??????????????????
    //????????????????????????
    private void initReason() {
        bankNameList = new ArrayList<>();
        bankNameList.add("??????????????????");
        bankNameList.add("??????????????????");
        bankNameList.add("????????????");
        bankNameList.add("??????????????????");
        bankNameList.add("????????????????????????");
        bankNameList.add("????????????");
        bankNameList.add("????????????");
        bankNameList.add("????????????");
        bankNameList.add("????????????");
        bankNameList.add("????????????");
        bankNameList.add("????????????");
        bankNameList.add("????????????");
        bankNameList.add("????????????");
        reasonPicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //??????????????????
                me_bankcard_tv_bank_where.setText(bankNameList.get(options1));
                mctBankType = bankNameList.get(options1);
            }
        }).setTitleText("??????????????????").setContentTextSize(17).setTitleSize(17).setSubCalSize(16).build();
        reasonPicker.setPicker(bankNameList);
    }

}
