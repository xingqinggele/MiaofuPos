package com.example.miaofupos.mefragment.mereferees;

import android.app.Dialog;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.hometeam.HomeTeamDetailsActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.Utility;
import com.example.miaofupos.views.MyDialog1;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2021/3/5
 * 描述:我的推荐人
 */
public class MeRefereesActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout iv_back;
    //头像
    private SimpleDraweeView home_team_logo;
    //姓名
    private TextView home_team_name;
    //电话
    private TextView home_team_phone;
    //拨打电话按钮
    private ImageView home_team_phone_btn;
    //电话
    private String phone = "";
    //注册时间
    private String createTime = "";
    //注册时间显示控件
    private TextView home_team_createTime;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.mereferees_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        home_team_phone_btn = findViewById(R.id.home_team_phone_btn);
        home_team_logo = findViewById(R.id.home_team_logo);
        home_team_name = findViewById(R.id.home_team_name);
        home_team_phone = findViewById(R.id.home_team_phone);
        home_team_createTime = findViewById(R.id.home_team_createTime);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        home_team_phone_btn.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        posData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.home_team_phone_btn:
                showDialog(phone);
                break;
        }
    }

    public void posData(){
        RequestParams params = new RequestParams();
        HttpRequest.getObtainSuperior(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    Uri imgurl=Uri.parse(result.getJSONObject("data").getString("portrait"));
                    // 清除Fresco对这条验证码的缓存
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.evictFromMemoryCache(imgurl);
                    imagePipeline.evictFromDiskCache(imgurl);
                    // combines above two lines
                    imagePipeline.evictFromCache(imgurl);
                    home_team_logo.setImageURI(imgurl);
                    home_team_name.setText(result.getJSONObject("data").getString("nickName"));
                    phone = result.getJSONObject("data").getString("phonenumber");
                    createTime = result.getJSONObject("data").getString("createTime");
                    home_team_phone.setText(phone.substring(0,3) + "****" + phone.substring(phone.length() - 4));
                    home_team_createTime.setText("注册时间：" + createTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(),failuer.getEmsg());
            }
        });
    }

    // 拨打电话dialog
    private void showDialog(String value) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_content, null);
        TextView textView = view.findViewById(R.id.dialog_tv1);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        TextView dialog_determine = view.findViewById(R.id.dialog_determine);
        textView.setText("您是否拨打  " + value);
        Dialog dialog = new MyDialog1(MeRefereesActivity.this, true, true, (float) 0.7).setNewView(view);
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
                Utility.callPhone(MeRefereesActivity.this,value);
            }
        });
    }
}
