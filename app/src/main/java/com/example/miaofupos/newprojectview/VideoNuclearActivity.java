package com.example.miaofupos.newprojectview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.PathUtils;
import com.example.miaofupos.BuildConfig;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.BitMapUtil;
import com.example.miaofupos.utils.VideoUtil;
import com.nanchen.screenrecordhelper.ScreenRecordHelper;
import com.tencent.cloud.huiyansdkface.facelight.api.WbCloudFaceContant;
import com.tencent.cloud.huiyansdkface.facelight.api.WbCloudFaceVerifySdk;
import com.tencent.cloud.huiyansdkface.facelight.api.listeners.WbCloudFaceVerifyLoginListener;
import com.tencent.cloud.huiyansdkface.facelight.api.listeners.WbCloudFaceVerifyResultListener;
import com.tencent.cloud.huiyansdkface.facelight.api.result.WbFaceError;
import com.tencent.cloud.huiyansdkface.facelight.api.result.WbFaceVerifyResult;
import com.tencent.cloud.huiyansdkface.facelight.process.FaceVerifyStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


/**
 * 作者: qgl
 * 创建日期：2022/7/11
 * 描述:测试人脸核身功能
 */
public class VideoNuclearActivity extends BaseActivity implements View.OnClickListener {
    private String merchantNo = "";
    private String snCode = "";
    private static final int SETTING_ACTIVITY = 2;
    private final String TAG = "DemoActivity";
    private boolean isShowSuccess;
    private boolean isFaceVerifyInService;
    //此处为demo使用体验，实际生产请使用控制台给您分配的appid
    private String appId = "";
    //此处为demo模拟，请输入标识唯一用户的userId
    private String userId = "";
    //此处为demo模拟，请输入32位随机数
    private String nonce = "";
    //此处为demo模拟，请输入可以唯一标识此次识别的订单号
    private String orderNo;
    //此处为demo模拟
    private String faceId;
    private String sign;
    private String version;
    //此处为demo使用，由合作方提供包名申请，统一下发
    private String keyLicence;
    private ScreenRecordHelper screenRecordHelper;
    private String filePath = PathUtils.getExternalStoragePath() + "/face";
    private String fileName = "android_" + System.currentTimeMillis();
    //视频base64
    private String base = "";
    private boolean recording = false;
    private LinearLayout iv_back;

    @Override
    protected int getLayoutId() {
        return R.layout.demo;
    }

    @Override
    protected void initView() {
        isShowSuccess = true;
        merchantNo = getIntent().getStringExtra("mcId");
        snCode = getIntent().getStringExtra("snCode");
        iv_back = findViewById(R.id.iv_back);
        getDataFaceId();
    }


    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    /**
     * 获取调用人脸核身的参数
     */
    private void getDataFaceId() {
        RequestParams params = new RequestParams();
        params.put("sn", snCode);
        params.put("merchantNo", merchantNo);
        HttpRequest.postFaceID(params, getToken(),new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    faceId = result.getString("faceId");
                    sign = result.getString("sign");
                    appId = result.getString("appId");
                    orderNo = result.getString("orderNo");
                    version = result.getString("version");
                    nonce = result.getString("nonce");
                    userId = result.getString("userId");
                    keyLicence = result.getString("keyLicence");
                    startRecord();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }

            }
        });

    }

    //拉起刷脸sdk
    public void openCloudFaceService(String sign) {
        Log.d(TAG, "openCloudFaceService");
        Bundle data = new Bundle();
        WbCloudFaceVerifySdk.InputData inputData = new WbCloudFaceVerifySdk.InputData(
                faceId,
                orderNo,
                appId,
                version,
                nonce,
                userId,
                sign,
                FaceVerifyStatus.Mode.GRADE,
                keyLicence);
        data.putSerializable(WbCloudFaceContant.INPUT_DATA, inputData);
        //是否展示刷脸成功页面，默认不展示
        data.putBoolean(WbCloudFaceContant.SHOW_SUCCESS_PAGE, false);
        //是否展示刷脸失败页面，默认不展示
        data.putBoolean(WbCloudFaceContant.SHOW_FAIL_PAGE, false);
        //是否需要录制上传视频 默认不需要
        data.putBoolean(WbCloudFaceContant.VIDEO_UPLOAD, false);
        //是否播放提示音，默认不播放
        data.putBoolean(WbCloudFaceContant.PLAY_VOICE, true);
        // 如果设置了录制，视频地址将在刷脸结果中返回
        //【特别注意】拿到录制的视频地址后请及时清理删除
        data.putBoolean(WbCloudFaceContant.RECORD_WILL_VIDEO, false);
        //检查录制的意愿性视频大小,默认不检查
        //如果设置了不录制视频，这个设置则默认无效
        data.putBoolean(WbCloudFaceContant.CHECK_WILL_VIDEO, false);
        //sdk log开关，默认关闭，debug调试sdk问题的时候可以打开
        //【特别注意】上线前请务必关闭sdk log开关！！！
        data.putBoolean(WbCloudFaceContant.IS_ENABLE_LOG, BuildConfig.DEBUG);
        Log.d(TAG, "WbCloudFaceVerifySdk initWillSdk");
        isFaceVerifyInService = true;
        //【特别注意】请使用activity context拉起sdk
        //【特别注意】请在主线程拉起sdk！
        WbCloudFaceVerifySdk.getInstance().initWillSdk(VideoNuclearActivity.this, data, new WbCloudFaceVerifyLoginListener() {
            @Override
            public void onLoginSuccess() {
                //登录sdk成功
                Log.i(TAG, "onLoginSuccess");
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }

                //拉起刷脸页面
                //【特别注意】请使用activity context拉起sdk
                //【特别注意】请在主线程拉起sdk！
                WbCloudFaceVerifySdk.getInstance().startWbFaceVerifySdk(VideoNuclearActivity.this,
                        new WbCloudFaceVerifyResultListener() {
                            @Override
                            public void onFinish(WbFaceVerifyResult result) {
                                isFaceVerifyInService = false;
                                screenRecordHelper.stopRecord(0, 0, null);
                                String log = "default";
                                //得到刷脸结果
                                if (result != null) {
                                    if (result.isSuccess()) {
                                        Log.d(TAG, "意愿性刷脸成功!" + result.toString());
                                        if (!isShowSuccess) {
                                            Toast.makeText(VideoNuclearActivity.this, "刷脸成功", Toast.LENGTH_SHORT).show();

                                        }
                                        recording = true;
                                        log = "意愿性成功 :" + result.toString();

                                        // todo  刷脸成功可以去后台查询刷脸结果
                                    } else {

                                        Log.d(TAG, "意愿性刷脸失败!" + result.toString());
                                        WbFaceError error = result.getError();
                                        log = "意愿性失败:" + result.toString();
                                        recording = false;
                                        if (error != null) {
                                            Log.d(TAG, "失败详情：domain=" + error.getDomain() + " ;code= " + error.getCode() + " ;desc=" + error.getDesc() + ";reason=" + error.getReason());
                                            if (!isShowSuccess) {
                                                Toast.makeText(VideoNuclearActivity.this, "刷脸失败!" + error.getDesc(), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Log.e(TAG, "sdk返回error为空！");
                                        }


                                    }
                                } else {
                                    Log.e(TAG, "sdk返回结果为空！");
                                    recording = false;
                                }

                                final String finalLog = log;
                                shouLog("---->",finalLog);
                                stopRecord();
                                //刷脸结束后，释放资源
                                WbCloudFaceVerifySdk.getInstance().release();
                            }
                        });
            }

            @Override
            public void onLoginFailed(WbFaceError error) {
                //登录失败
                Log.i(TAG, "onLoginFailed!");
                isFaceVerifyInService = false;
                recording = false;
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                if (error != null) {
                    Log.d(TAG, "登录失败！domain=" + error.getDomain() + " ;code= " + error.getCode() + " ;desc=" + error.getDesc() + ";reason=" + error.getReason());
                    if (error.getDomain().equals(WbFaceError.WBFaceErrorDomainParams)) {
                        Toast.makeText(VideoNuclearActivity.this, "传入参数有误！" + error.getDesc(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VideoNuclearActivity.this, "登录刷脸sdk失败！" + error.getDesc(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "sdk返回error为空！");
                }
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTING_ACTIVITY) {
            isShowSuccess = data.getBooleanExtra(WbCloudFaceContant.SHOW_SUCCESS_PAGE, false);
        } else {
            if (resultCode == 0) {
                //用户没有同意录制视屏
                Toast.makeText(this, "您为授权录制、无法进行人脸核身！", Toast.LENGTH_LONG).show();
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
            } else {
                if (loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                //用户同意录制视频
                Log.d(TAG, "onActivityResult: 在录制....");
                if (isFaceVerifyInService) {
                    Log.w(TAG, "already in Service!No more clicks!");
                    return;
                }
                openCloudFaceService(sign);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && data != null) {
                    screenRecordHelper.onActivityResult(requestCode, resultCode, data);
                }
            }

        }
    }

    //开启录制
    private void startRecord() {
        screenRecordHelper = null;
        screenRecordHelper = new ScreenRecordHelper(this, new ScreenRecordHelper.OnVideoRecordListener() {
            @Override
            public void onBeforeRecord() {
                Log.e("onBeforeRecord-->", "");

            }

            @Override
            public void onStartRecord() {
                Log.e("onStartRecord--->", "");

            }

            @Override
            public void onCancelRecord() {
                Log.e("onCancelRecord------>", "");

            }

            @Override
            public void onEndRecord() {

            }
        }, filePath, fileName);
        screenRecordHelper.setRecordAudio(true);
        screenRecordHelper.startRecord();
    }

    //暂停录制
    private void stopRecord() {
        screenRecordHelper.stopRecord(0, 0, null);
        Log.e("文件地址---------->", filePath);
        Log.e("文件名称---------->", fileName);
        String files = filePath + "/" + fileName + ".mp4";
        shouLog("视频大小------->", BitMapUtil.getFileSize(files));
        if (recording){
            loadDialog.show();
            VideoUtil.compressVideo2(this,files, msg -> {

                String path= (String) msg.obj;
                File compressFile = new File(path);
                Log.e("aaa", "视频1" + compressFile.length());
                Log.e("aaa", "视频2" + compressFile.getPath());
                Log.e("aaa", "视频3" + compressFile.getName());
                String s = BitMapUtil.fileBase64String(compressFile.getPath());
                Log.e("Base64--", s);
                base = s;

                posVideo(base);
                return false;
            });
        }else {

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }




    /**
     * 上传视频
     * @param str
     */
    private void posVideo(String str){
        RequestParams params = new RequestParams();
        params.put("merchantNo",merchantNo);
        params.put("video",str);
        HttpRequest.posVideoUpload(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                finish();
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

}