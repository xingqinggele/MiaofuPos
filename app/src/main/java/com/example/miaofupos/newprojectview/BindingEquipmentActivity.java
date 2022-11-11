package com.example.miaofupos.newprojectview;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * 创建日期：2022/8/22
 * 描述:设备绑定
 */
public class BindingEquipmentActivity extends BaseActivity implements View.OnClickListener {
    private Button bindEquipment_btn;
    private LinearLayout iv_back;
    private EditText equipment_num;
    private String mcId;
    private String snCode;
    public static BindingEquipmentActivity activity  = null;
    @Override
    protected int getLayoutId() {
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.binding_equipment_activity;
    }

    @Override
    protected void initView() {
        activity = this;
        bindEquipment_btn = findViewById(R.id.bindEquipment_btn);
        iv_back = findViewById(R.id.iv_back);
        equipment_num = findViewById(R.id.equipment_num);
        equipment_num.setEnabled(false);
    }

    @Override
    protected void initListener() {
        bindEquipment_btn.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mcId = getIntent().getStringExtra("mcId");
        snCode = getIntent().getStringExtra("snCode");
        equipment_num.setText(snCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.bindEquipment_btn:
                bindTap(mcId,equipment_num.getText().toString().trim());
                break;
        }
    }


    //查询有压无压
    private void bindTap(String mcId,String snCode){
        loadDialog.show();
        RequestParams params = new RequestParams();
        params.put("merchantNo", mcId);
        params.put("posCode", snCode);
        HttpRequest.posJudgeCashPledge(params,getToken(),new ResponseCallback() {
            //成功回调
            @Override
            public void onSuccess(Object responseObj) {
                loadDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(responseObj.toString());
                    String type = object.getString("type");
                    if (type.equals("0")){
                        showToast(3,"绑定成功！");
                        finish();
                    }else {
                        Intent intent = new Intent(BindingEquipmentActivity.this, ProcessActivity.class);
                        intent.putExtra("mcId",mcId);
                        intent.putExtra("snCode",snCode);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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