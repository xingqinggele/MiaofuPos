package com.example.miaofupos.homefragment.homeInvitepartners;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.cap.encode.CodeCreator;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.SPUtils;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;


import static com.example.miaofupos.utils.ImageConvertUtil.saveImageToGallery;

/**
 * 作者: qgl
 * 创建日期：2020/12/15
 * 描述: 邀请伙伴
 */
public class HomeInvitePartnersActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private TextView home_invitepar_tv_save;
    private TextView home_inviterpar_mercode;
    private ImageView home_invitepar_img;
    private SoundPool soundPool;
    private int soundID;
    //注册地址
    private String url;
    //邀请码
    private String merchCode;
    //用户名
    private TextView user_name_tv;
    //用户手机号
    private TextView user_phone_tv;
    //生成二维码
    private TextView generate_code_tv;
    //生成的政策ID
    private String id = "";
    //修改按钮
    private TextView edit_clink;
    private LinearLayout liner_btn;


    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.homeinvitepartnersactivity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        home_invitepar_tv_save = findViewById(R.id.home_invitepar_tv_save);
        home_inviterpar_mercode = findViewById(R.id.home_inviterpar_mercode);
        home_invitepar_img = findViewById(R.id.home_invitepar_img);
        user_name_tv = findViewById(R.id.user_name_tv);
        user_phone_tv = findViewById(R.id.user_phone_tv);
        generate_code_tv = findViewById(R.id.generate_code_tv);
        edit_clink = findViewById(R.id.edit_clink);
        liner_btn = findViewById(R.id.liner_btn);
        initSound();
    }

    //点击事件
    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        home_invitepar_tv_save.setOnClickListener(this);
        generate_code_tv.setOnClickListener(this);
        edit_clink.setOnClickListener(this);
        getData();
    }

    @Override
    protected void initData() {
        user_name_tv.setText(SPUtils.get(this, "nickName", "").toString());
        user_phone_tv.setText(getUserName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.home_invitepar_tv_save: // 截屏
                //隐藏不截屏的控件
                liner_btn.setVisibility(View.GONE);
                iv_back.setVisibility(View.GONE);
                //音效
                playSound();
                View dView = this.getWindow().getDecorView();
                dView.destroyDrawingCache();
                dView.setDrawingCacheEnabled(false);
                dView.buildDrawingCache();
                //截屏完显示不截屏的控件
                liner_btn.setVisibility(View.VISIBLE);
                iv_back.setVisibility(View.VISIBLE);
                Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
                if (bitmap != null) {
                    if (saveImageToGallery(this, bitmap)) {
                        showToast(3, "截图保存成功");
                    }
                }
                break;
            case R.id.generate_code_tv:
                Intent intent = new Intent(this,HomeFillActivity.class);
                intent.putExtra("type","1");
                startActivityForResult(intent,5);
                break;
            case R.id.edit_clink:
                Intent intent1 = new Intent(this,HomeFillActivity.class);
                intent1.putExtra("type","2");
                intent1.putExtra("accoundId",id);
                startActivityForResult(intent1,5);
                break;
        }
    }


    @SuppressLint("NewApi")
    private void initSound() {
        soundPool = new SoundPool.Builder().build();
        soundID = soundPool.load(this, R.raw.screen, 1);
    }


    private void playSound() {
        soundPool.play(
                soundID,
                0.1f,      //左耳道音量【0~1】
                0.5f,      //右耳道音量【0~1】
                0,         //播放优先级【0表示最低优先级】
                0,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1          //播放速度【1是正常，范围从0~2】
        );
    }

    public void getData() {
        RequestParams params = new RequestParams();
        HttpRequest.getInvitationPartner(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    merchCode = result.getJSONObject("data").getString("merchCode");
                    url = result.getJSONObject("data").getString("url");
                    home_inviterpar_mercode.setText("邀请码" + merchCode);

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

    /**
     * 生成二维码
     */
    public void getOrCode() {
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.aa);
        home_invitepar_img.setImageBitmap(CodeCreator.createQRCode(url + "?id=" + merchCode + "&accountId=" + id,  400, 400, logo));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shouLog("1","----------------");
        shouLog("11",resultCode+ "----------------");
        shouLog("111",requestCode+ "----------------");
        if (resultCode == 5) {
                shouLog("111111","----------------");
                id = data.getStringExtra("id");
                getOrCode();
                generate_code_tv.setVisibility(View.GONE);
                liner_btn.setVisibility(View.VISIBLE);
        }
    }
}
