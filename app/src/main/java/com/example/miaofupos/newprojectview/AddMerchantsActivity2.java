package com.example.miaofupos.newprojectview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.bean.IdCardInfo;
import com.example.miaofupos.utils.ImageConvertUtil;
import com.example.miaofupos.utils.TimeUtils;
import com.example.miaofupos.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.tencent.ocr.sdk.common.ISDKKitResultListener;
import com.tencent.ocr.sdk.common.OcrModeType;
import com.tencent.ocr.sdk.common.OcrSDKConfig;
import com.tencent.ocr.sdk.common.OcrSDKKit;
import com.tencent.ocr.sdk.common.OcrType;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;


/**
 * 作者: qgl
 * 创建日期：2021/8/17
 * 描述: POSP商户报件2
 */
public class AddMerchantsActivity2 extends BaseActivity implements View.OnClickListener {
    /************第一页传递的参数************/
    private String quote_contact_name = ""; //联系人
    private String quote_shop_jname = ""; //商户简写
    private String quote_phone = ""; //联系电话
    private String rateId = ""; //费率ID
    private String PosCode = ""; //SN
    private String quote_service_phone = ""; //客服电话
    private String quote_address = ""; //详细地址
    private String province = ""; //商户注册省份
    private String city = ""; //商户注册城市
    private String area = ""; //商户注册区县
    /************第一页传递的参数************/
    //返回键
    private LinearLayout iv_back;
    // 身份证正面
    private SimpleDraweeView id_card_is;
    // 身份证反面
    private SimpleDraweeView id_card_the;
    //手持身份证照片
    private SimpleDraweeView id_card_pay;
    //身份证正面照片地址
    private String IdCard1_Url = "";
    //身份证反面照片地址
    private String IdCard2_Url = "";
    //手持身份证照片地址
    private String Handheld_url = "";
    // 身份证名字
    private String IdName;
    // 身份证号码
    private String IdNumber;
    // 身份证有效期
    private String IdValidDate;
    //有效期开始时间
    private String st1 = "";
    private String s = "";
    //有效期结束时间
    private String st2 = "";
    private String t = "";
    // 姓名
    private EditText name_ed;
    // 身份证号码
    private EditText card_number_ed;
    //开始时间
    private TextView home_quote_start_time;
    //结束时间
    private TextView home_quote_un_time;
    //下一步按钮
    private Button submit_bt;

    // 需要关闭
    public static AddMerchantsActivity2 instance = null;

    final private int IdCardIn = 1004;
    final private int IdCardOn = 1005;
    private PopupWindow popWindow;
    private View popView;


    private String Longitude = "";
    private String Latitude = "";
    private String provinceName;  // 省名称
    private String cityName;  // 市名称
    private String areaName;  // 区名称
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.addmerchants_activity_02;
    }

    @Override
    protected void initView() {
        instance = this;
        iv_back = findViewById(R.id.iv_back);
        id_card_is = findViewById(R.id.id_card_is);
        name_ed = findViewById(R.id.name_ed);
        card_number_ed = findViewById(R.id.card_number_ed);
        id_card_the = findViewById(R.id.id_card_the);
        id_card_pay = findViewById(R.id.id_card_pay);
        home_quote_start_time = findViewById(R.id.home_quote_start_time);
        home_quote_un_time = findViewById(R.id.home_quote_un_time);
        submit_bt = findViewById(R.id.submit_bt);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        id_card_is.setOnClickListener(this);
        id_card_the.setOnClickListener(this);
        id_card_pay.setOnClickListener(this);
        submit_bt.setOnClickListener(this);
        home_quote_start_time.setOnClickListener(this);
        home_quote_un_time.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        quote_contact_name = getIntent().getStringExtra("quote_contact_name");
        quote_shop_jname = getIntent().getStringExtra("quote_shop_jname");
        quote_phone = getIntent().getStringExtra("quote_phone");
        rateId = getIntent().getStringExtra("rateId");
        PosCode = getIntent().getStringExtra("PosCode");
        quote_service_phone = getIntent().getStringExtra("quote_service_phone");
        quote_address = getIntent().getStringExtra("quote_address");
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        area = getIntent().getStringExtra("area");
        Longitude = getIntent().getStringExtra("Longitude");
        Latitude = getIntent().getStringExtra("Latitude");
        provinceName = getIntent().getStringExtra("provinceName");
        cityName = getIntent().getStringExtra("cityName");
        areaName = getIntent().getStringExtra("areaName");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.home_quote_start_time:
                selectTime(home_quote_start_time, 1);
                break;
            case R.id.home_quote_un_time:
                selectTime(home_quote_un_time, 2);
                break;
            // 获取身份证正面
            case R.id.id_card_is:
                showEditPhotoWindow(IdCardIn);
                break;
            // 获取身份证反面
            case R.id.id_card_the:
                showEditPhotoWindow(IdCardOn);
                break;
            // 获取手持身份证照片
            case R.id.id_card_pay:
                PictureSelector
                        .create(this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(false);
                break;
            case R.id.submit_bt:
                Intent intent = new Intent(this, AddMerchantsActivity3.class);
                if (TextUtils.isEmpty(IdCard1_Url)) {
                    showToast(3, "选择身份证正面照");
                    return;
                }
                if (TextUtils.isEmpty(IdCard2_Url)) {
                    showToast(3, "选择身份证背面照");
                    return;
                }
                if (TextUtils.isEmpty(Handheld_url)) {
                    showToast(3, "选择手持身份证照");
                    return;
                }
                if (TextUtils.isEmpty(name_ed.getText().toString().trim())) {
                    showToast(3, "法人姓名");
                    return;
                }
                if (TextUtils.isEmpty(card_number_ed.getText().toString().trim())) {
                    showToast(3, "法人身份证号码");
                    return;
                }
                if (TextUtils.isEmpty(s)) {
                    showToast(3, "有效期开始时间");
                    return;
                }
                if (TextUtils.isEmpty(t)) {
                    showToast(3, "有效期结束时间");
                    return;
                }
                /*****第一页数据******************/
                intent.putExtra("quote_contact_name", quote_contact_name);
                intent.putExtra("quote_shop_jname", quote_shop_jname);
                intent.putExtra("quote_service_phone", quote_service_phone);
                intent.putExtra("rateId", rateId);
                intent.putExtra("PosCode", PosCode);
                intent.putExtra("quote_address", quote_address);
                intent.putExtra("quote_phone", quote_phone);
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("area", area);
                intent.putExtra("Longitude", Longitude);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("provinceName", provinceName);
                intent.putExtra("cityName", cityName);
                intent.putExtra("areaName", areaName);
                /*****第一页数据******************/
                intent.putExtra("IdUrl1", IdCard1_Url);
                intent.putExtra("IdUrl2", IdCard2_Url);
                intent.putExtra("IdUrl3", Handheld_url);
                intent.putExtra("fName", name_ed.getText().toString().trim());
                intent.putExtra("fNumber", card_number_ed.getText().toString().trim());
                intent.putExtra("startTime", s);
                intent.putExtra("endTime", t);
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
    private void setResultListData() {
        if (IdName != null && !IdName.isEmpty()) {
            name_ed.setText(IdName);
            card_number_ed.setText(IdNumber);
        }
        if (IdValidDate != null && !IdValidDate.isEmpty()) {
            st1 = IdValidDate.substring(0, IdValidDate.indexOf("-"));
            st2 = IdValidDate.substring(st1.length() + 1);
            s = st1.replace(".", "");
            t = st2.replace(".", "");
            home_quote_start_time.setText(s);
            home_quote_un_time.setText(t);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OcrSDKKit.getInstance().release();
    }


    /**
     * 弹出框
     *
     * @param value
     */
    private void showEditPhotoWindow(int value) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        popView = layoutInflater.inflate(R.layout.popwindow_main, (ViewGroup) null);
        popWindow = new PopupWindow(popView, -1, -2);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(popView, 80, 0, 0);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);
        TextView tv_select_pic = popView.findViewById(R.id.tv_photo);
        TextView tv_pai_pic = popView.findViewById(R.id.tv_photograph);
        TextView tv_cancl = popView.findViewById(R.id.tv_cancle);
        LinearLayout layout = popView.findViewById(R.id.dialog_ll);
        tv_select_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                if (value == IdCardIn) {

                    getIDCardIn();
                } else {
                    getIDCardOn();
                }
            }
        });
        tv_pai_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                PictureSelector
                        .create(AddMerchantsActivity2.this, value)
                        .selectPicture(false);
            }
        });
        tv_cancl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });

    }

    /************************************** 选取照片开始 ***********************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                Luban.with(this)
                        .load(pictureBean.getPath())
                        .ignoreBy(100)
                        .setTargetDir(Utility.getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                Handheld_url = file.getPath();
                                id_card_pay.setImageBitmap(BitmapFactory.decodeFile(Handheld_url));
                                shouLog("图片--->",Handheld_url);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        String result = Calendar.getInstance().getTimeInMillis() + ".jpg";
                        return result;
                    }
                })

                        .launch();
            }
        } else if (requestCode == IdCardIn) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                Luban.with(this)
                        .load(pictureBean.getPath())
                        .ignoreBy(100)
                        .setTargetDir(Utility.getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                IdCard1_Url = file.getPath();
                                id_card_is.setImageBitmap(BitmapFactory.decodeFile(IdCard1_Url));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        } else if (requestCode == IdCardOn) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                Luban.with(this)
                        .load(pictureBean.getPath())
                        .ignoreBy(100)
                        .setTargetDir(Utility.getPath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(File file) {
                                IdCard2_Url = file.getPath();
                                id_card_the.setImageBitmap(BitmapFactory.decodeFile(IdCard2_Url));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
        }

    }

    /************************************** 选取照片结束 ***********************************************************************/

    //身份证正面识别
    private void getIDCardIn() {
        initSdk(getSecretId(), getSecretKey());
        //弹出界面
        OcrSDKKit.getInstance().startProcessOcr(this, OcrType.IDCardOCR_FRONT, null,
                new ISDKKitResultListener() {
                    @Override
                    public void onProcessSucceed(String response, String srcBase64Image, String requestId) {
                        IdCardInfo tempIdCardInfo = new Gson().fromJson(response, IdCardInfo.class);
                        Log.e("response", tempIdCardInfo.toString());
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
                        setResultListData();
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, String requestId) {
                        popTip(errorCode, message);
                        Log.e("requestId", requestId);
                        IdName = "";
                        IdNumber = "";
                    }
                });
    }

    //身份证反面
    private void getIDCardOn() {
        initSdk(getSecretId(), getSecretKey());
        //身份证反面
        OcrSDKKit.getInstance().startProcessOcr(this, OcrType.IDCardOCR_BACK, null,
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
                        setResultListData();
                    }

                    @Override
                    public void onProcessFailed(String errorCode, String message, String requestId) {
                        popTip(errorCode, message);
                        Log.e("11111requestId", requestId);
                        IdValidDate = "";
                    }
                });
    }

    /**
     * 选择时间
     */
    private void selectTime(TextView textView, int type) {
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //开始时间
                if (type == 1) {
                    s = TimeUtils.getNewTimes(date);
                }
                //结束时间
                else {
                    t = TimeUtils.getNewTimes(date);
                }
                textView.setText(TimeUtils.getNewTimes(date));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .build();
        pvTime.show();
    }
}
