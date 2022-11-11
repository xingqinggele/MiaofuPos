package com.example.miaofupos.homefragment.homeequipment;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.activity.TerminalActivity;
import com.example.miaofupos.homefragment.homeequipment.activity.TerminalRecordActivity;
import com.example.miaofupos.homefragment.homeequipment.activity.TerminalTransferActivity;
import com.example.miaofupos.homefragment.homeequipment.activity.TransferCallbackActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: qgl
 * 创建日期：2020/12/21
 * 描述:终端管理
 */
public class HomeEquipmentActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //调拨记录
    private LinearLayout equipment_record;
    //总终端数(台)
    private TextView equipment_num;
    //总激活数(台)
    private TextView equipment_open_num;
    //未激活数(台)
    private TextView equipment_activation_num;
    //终端查询
    private ConstraintLayout home_equipment_terminal;
    //终端划拨
    private ConstraintLayout home_equipment_transfer;
    //终端回调
    private ConstraintLayout home_equipment_callback;
    private String userId; // 用户ID
    private String Token; //
    private String  totalNum; // 总数
    private String  has_activationNum; // 已激活数
    private String  inactiveNum; // 未激活

    @Override
    protected int getLayoutId() {
        // 设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.home_equipment_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        home_equipment_terminal = findViewById(R.id.home_equipment_terminal);
        home_equipment_transfer = findViewById(R.id.home_equipment_transfer);
        home_equipment_callback = findViewById(R.id.home_equipment_callback);
        equipment_record = findViewById(R.id.equipment_record);
        equipment_num = findViewById(R.id.equipment_num);
        equipment_open_num = findViewById(R.id.equipment_open_num);
        equipment_activation_num = findViewById(R.id.equipment_activation_num);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        home_equipment_terminal.setOnClickListener(this);
        home_equipment_transfer.setOnClickListener(this);
        home_equipment_callback.setOnClickListener(this);
        equipment_record.setOnClickListener(this);
    }
    @Override
    protected void initData() {
        userId = SPUtils.get(HomeEquipmentActivity.this, "userId", "").toString();
        Token = SPUtils.get(HomeEquipmentActivity.this, "Token", "").toString();
        if (userId.equals("2")){
            home_equipment_callback.setVisibility(View.VISIBLE);
        }else {
            home_equipment_callback.setVisibility(View.GONE);
        }
        posData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.home_equipment_terminal:
                //终端查询
                Intent intent = new Intent(HomeEquipmentActivity.this, TerminalActivity.class);
                intent.putExtra("totalNum",totalNum);
                startActivity(intent);
                break;
            case R.id.home_equipment_transfer:
                //终端划拨
                Intent intent1 = new Intent(HomeEquipmentActivity.this, TerminalTransferActivity.class);
                startActivity(intent1);
                break;
            case R.id.home_equipment_callback:
                //终端回调
                startActivity(new Intent(HomeEquipmentActivity.this, CallbackPersonActivity.class));
                break;
            case R.id.equipment_record:
                //调拨记录
                startActivity(new Intent(HomeEquipmentActivity.this, TerminalRecordActivity.class));
                break;
        }

    }

    /*******************请求终端管理数据*******************/
    public void posData(){
        RequestParams params = new RequestParams();
        params.put("userId",userId);
        HttpRequest.getEquipment(params, Token, new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    totalNum = result.getJSONObject("data").getString("terminalCounts");
                    has_activationNum = result.getJSONObject("data").getString("terminalActivatedCounts");
                    inactiveNum = result.getJSONObject("data").getString("terminalNoActivateCounts");
                    setText();
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

    private void setText(){
        equipment_num.setText(totalNum+"");
        equipment_open_num.setText(has_activationNum+"");
        equipment_activation_num.setText(inactiveNum+"");
    }

}
