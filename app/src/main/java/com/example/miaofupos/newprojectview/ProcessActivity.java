package com.example.miaofupos.newprojectview;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2022/8/25
 * 描述:报件资料流程
 */
public class ProcessActivity extends BaseActivity implements View.OnClickListener {
    private CheckBox signature_checkbox,nuclear_body_checkbox;
    private ImageView signature_im,nuclear_body_im;
    private LinearLayout iv_back;
    private String mcId="";
    private String snCode="";
    private Button nextTo;

    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.process_activity;
    }

    @Override
    protected void initView() {
        signature_checkbox = findViewById(R.id.signature_checkbox);
        nuclear_body_checkbox = findViewById(R.id.nuclear_body_checkbox);
        signature_im = findViewById(R.id.signature_im);
        nuclear_body_im = findViewById(R.id.nuclear_body_im);
        iv_back = findViewById(R.id.iv_back);
        nextTo = findViewById(R.id.nextTo);
        mcId = getIntent().getStringExtra("mcId");
        snCode = getIntent().getStringExtra("snCode");
        posFiles();
    }

    @Override
    protected void initListener() {
        signature_im.setOnClickListener(this);
        nuclear_body_im.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        nextTo.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signature_im:
                Intent intent = new Intent(this, BindingEquipmentSigningActivity.class);
                intent.putExtra("mcId",mcId);
                intent.putExtra("snCode",snCode);
                startActivityForResult(intent,220);
                break;
            case R.id.nuclear_body_im:
                Intent intent1 = new Intent(this, VideoNuclearActivity.class);
                intent1.putExtra("mcId",mcId);
                intent1.putExtra("snCode",snCode);
                startActivityForResult(intent1,222);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.nextTo:
                if (!signature_checkbox.isChecked()){
                    showToast(3,"请上传签名！");
                    return;
                }
                if (!nuclear_body_checkbox.isChecked()){
                    showToast(3,"请上传意愿核身！");
                    return;
                }
                posBind();
                break;
        }
    }


    //查询上传的文件
    private void posFiles(){
        RequestParams params = new RequestParams();
        params.put("merchantNo", mcId);
        HttpRequest.posWhetherPhotoVideo(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {

                try {
                    JSONObject object = new JSONObject(responseObj.toString());
                    String type = object.getString("type");
                    //图片视频未上传
                    if (type.equals("1")){
                        signature_checkbox.setChecked(false);
                        nuclear_body_checkbox.setChecked(false);
                        signature_im.setEnabled(true);
                        nuclear_body_im.setEnabled(true);
                    }else if (type.equals("2")){
                        signature_checkbox.setChecked(true);
                        nuclear_body_checkbox.setChecked(false);
                        signature_im.setEnabled(false);
                        nuclear_body_im.setEnabled(true);
                    }else if (type.equals("3")){
                        signature_checkbox.setChecked(false);
                        nuclear_body_checkbox.setChecked(true);
                        signature_im.setEnabled(true);
                        nuclear_body_im.setEnabled(false);
                    }else {
                        signature_checkbox.setChecked(true);
                        nuclear_body_checkbox.setChecked(true);
                        signature_im.setEnabled(false);
                        nuclear_body_im.setEnabled(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });

    }

    //查询上传的文件
    private void posBind(){
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("merchantNo", mcId);
        params.put("posCode", snCode);
        HttpRequest.posBindCode(params, getToken(), new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
               loadDialog.dismiss();
                BindingEquipmentActivity.activity.finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        posFiles();
    }
}