package com.example.miaofupos.mefragment.setup;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.cos.CosServiceFactory;
import com.example.miaofupos.fragment.MeFragment;
import com.example.miaofupos.homefragment.homeInvitepartners.HomeInvitePartnersActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

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
import com.wildma.pictureselector.FileUtils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2020/12/23
 * 描述:个人信息
 */
public class PersonalActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private RelativeLayout personal_mcode_line;  // 推荐界面
    private TextView user_name; // 用户姓名
    private TextView user_phone; // 用户手机号
    private TextView user_code; //  推荐号码
    private SimpleDraweeView me_head; //  用户头像
    private COSXMLUploadTask cosxmlTask;
    private String folderName = "portrait";
    private CosXmlService cosXmlService;
    private TransferManager transferManager;

    private Context context;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.personal_activity;
    }

    @Override
    protected void initView() {
        context = PersonalActivity.this;
        //初始化存储桶控件
        cosXmlService = CosServiceFactory.getCosXmlService(this, "ap-beijing", getSecretId(), getSecretKey(), true);
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);
        iv_back = findViewById(R.id.iv_back);
        personal_mcode_line = findViewById(R.id.personal_mcode_line);
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        user_code = findViewById(R.id.user_code);
        me_head = findViewById(R.id.me_head);

    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        personal_mcode_line.setOnClickListener(this);
        me_head.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        posDate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.personal_mcode_line:
                startActivity(new Intent(PersonalActivity.this, HomeInvitePartnersActivity.class));
                break;
            case R.id.me_head:
                PictureSelector
                        .create(PersonalActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(true);
                break;
        }
    }

    // 获取用户个人信息
    public void posDate() {
        RequestParams params = new RequestParams();
        HttpRequest.getCurrent(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");
                    JSONObject merchantDetail = data.getJSONObject("merchantDetail");
                    JSONObject merchantBrief = data.getJSONObject("merchantBrief");
                    user_name.setText(merchantDetail.getString("merchIdcardName").substring(0, 1) + "**");
                    user_phone.setText(merchantDetail.getString("merchBankMobile").substring(0, 3) + "****" + merchantDetail.getString("merchBankMobile").substring(merchantDetail.getString("merchBankMobile").length() - 4));
                    user_code.setText(merchantBrief.getString("merchCode"));
                    Uri imgurl = Uri.parse(data.getString("portrait"));
                    // 清除Fresco对这条验证码的缓存
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.evictFromMemoryCache(imgurl);
                    imagePipeline.evictFromDiskCache(imgurl);
                    // combines above two lines
                    imagePipeline.evictFromCache(imgurl);
                    me_head.setImageURI(imgurl);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
                if (pictureBean.isCut()) {
                    me_head.setImageBitmap(BitmapFactory.decodeFile(pictureBean.getPath()));
                } else {
                    me_head.setImageURI(pictureBean.getUri());
                }
                upload(pictureBean.getPath());
            }
        }
    }

    //上传图片
    private void upload(String file_url) {
        if (TextUtils.isEmpty(file_url)) {
            return;
        }
        if (cosxmlTask == null) {
            File file = new File(file_url);
            String cosPath;
            if (TextUtils.isEmpty(folderName)) {
                cosPath = file.getName();
            } else {
                cosPath = folderName + File.separator + file.getName();
            }
            cosxmlTask = transferManager.upload(getBucketName(), cosPath, file_url, null);
            Log.e("参数-------》", getBucketName() + "----" + cosPath + "---" + file_url);
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
                    setResult(RESULT_OK);
                    // 删除剪裁的图片
//                    FileUtils.deleteAllCacheImage(context);

                    // 上传服务器url
                    posHead(cOSXMLUploadTaskResult.accessUrl);
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;
                        Log.e("1111", "上传失败");
                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                }
            });

        }
    }

    //更换头像
    public void posHead(String url) {
        RequestParams params = new RequestParams();
        params.put("portraitUrl", url);
        HttpRequest.getUpdatePortrait(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                // 成功,通知我的，界面更新头像
                EventBus.getDefault().post(new MeFragment());
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }
}
