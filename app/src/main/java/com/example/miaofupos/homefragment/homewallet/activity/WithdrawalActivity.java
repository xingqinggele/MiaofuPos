package com.example.miaofupos.homefragment.homewallet.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.bean.CallbackRecordBean;
import com.example.miaofupos.homefragment.homewallet.HomeWalletActivity;
import com.example.miaofupos.mefragment.setup.MePayPassActivity;
import com.example.miaofupos.mefragment.setup.MePayPassOptionsActivity;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.utils.DecimalDigitsInputFilter;
import com.example.miaofupos.utils.SPUtils;
import com.example.miaofupos.views.MyDialog;
import com.example.miaofupos.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jungly.gridpasswordview.GridPasswordView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.miaofupos.utils.DESHelperUtil.decrypt;
import static com.example.miaofupos.utils.DESHelperUtil.encrypt;
import static com.example.miaofupos.utils.Utility.format2Decimal;

/**
 * 作者: qgl
 * 创建日期：2020/12/26
 * 描述:提现
 */
public class WithdrawalActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    //输入金额框
    private EditText withdrawal_money_ed;
    //提现按钮
    private Button withdrawal_bt;
    //返回键
    private LinearLayout iv_back;
    //显示可提现金额框
    private LinearLayout withdrawal_line;
    //可提现金
    private TextView withdrawal_money_tv;
    //条件不满足提示语
    private TextView withdrawal_prompt_tv;
    //当前界面
    public static WithdrawalActivity instance = null;
    //钱包类型显示
    private String wallets = "";
    //钱包类型、1.结算账户 2.奖励账户
    private String walletsType = "1";
    //服务器制定的最低提现金额
    private String minimumAmount = "";
    //服务器制定的服务费
    private String serviceFee = "";
    //钱包金额
    private String walletAmount = "";
    //开户银行
    private TextView withdrawal_bank_name;
    //银行卡号
    private TextView my_bank_number;
    //金额解密Key
    private String secretKey;
    //银行卡尾号
    private String bank_tail;
    //费率
    private String rate;
    //全部
    private TextView all_tv;
    //xml界面
    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.withdrawal_activity;
    }

    //初始化控件
    @Override
    protected void initView() {
        instance = this;
        withdrawal_prompt_tv = findViewById(R.id.withdrawal_prompt_tv);
        withdrawal_line = findViewById(R.id.withdrawal_line);
        withdrawal_money_tv = findViewById(R.id.withdrawal_money_tv);
        withdrawal_money_ed = findViewById(R.id.withdrawal_money_ed);
        withdrawal_bt = findViewById(R.id.withdrawal_bt);
        iv_back = findViewById(R.id.iv_back);
        withdrawal_bank_name = findViewById(R.id.withdrawal_bank_name);
        my_bank_number = findViewById(R.id.my_bank_number);
        all_tv = findViewById(R.id.all_tv);
        //按钮不可点击
        withdrawal_bt.setEnabled(false);
        //设置输入类型
        withdrawal_money_ed.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //设置过滤器  1.设置点后面最大2位 2.设置数字最大9位
        withdrawal_money_ed.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2), new InputFilter.LengthFilter(9)});
    }

    //事件绑定
    @Override
    protected void initListener() {
        withdrawal_bt.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        all_tv.setOnClickListener(this);
        withdrawal_money_ed.addTextChangedListener(this);
    }

    //数据配置
    @Override
    protected void initData() {
        walletsType = getIntent().getStringExtra("walletType");
        // 接受前页传递的钱包类型，三元运算
        wallets = walletsType.equals("1") ? "结算款账户" : "奖励账户";
        //请求接口
        getPayData();
    }

    //点击触发
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //提现按钮
            case R.id.withdrawal_bt:
                showDialog(format2Decimal(withdrawal_money_ed.getText().toString().trim()), wallets);
                break;
            //返回键
            case R.id.iv_back:
                finish();
                break;
            case R.id.all_tv:
//                withdrawal_money_ed.setText("1");
                withdrawal_money_ed.setText(walletAmount);
                break;
        }
    }

    /**
     * 输入支付密码dialog
     *
     * @param money
     * @param type
     */
    public void showDialog(String money, String type) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pay_dialog, null);
        Dialog dialog = new MyDialog1(WithdrawalActivity.this, true, true, (float) 0.8).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        GridPasswordView pswView = view.findViewById(R.id.pswView);
        //提现钱包类型
        TextView pay_dialog_wallet_type_tv = view.findViewById(R.id.pay_dialog_wallet_type_tv);
        //提现金额
        TextView pay_dialog_price_tv = view.findViewById(R.id.pay_dialog_price_tv);
        //提现服务费
        TextView pay_dialog_minimum_price_tv = view.findViewById(R.id.pay_dialog_minimum_price_tv);
        //费率
        TextView pay_dialog_minimum_rate_tv = view.findViewById(R.id.pay_dialog_minimum_rate_tv);
        //dialog关闭按钮
        LinearLayout pay_dialog_kill = view.findViewById(R.id.pay_dialog_kill);

        //显示钱包类型
        pay_dialog_wallet_type_tv.setText(type);
        //显示提现金额
        pay_dialog_price_tv.setText("￥" + money);
        //显示服务费
        pay_dialog_minimum_price_tv.setText("￥" + serviceFee);
        //显示费率
        pay_dialog_minimum_rate_tv.setText(rate + "%");

        //设置输入密码监听
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            //正在输入密码时执行此方法
            public void onTextChanged(String psw) {
            }

            //输入密码完成时执行此方法
            public void onInputFinish(String psw) {
                dialog.dismiss();
                PayData(psw);
            }
        });
        //取消按钮
        pay_dialog_kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**************请求服务器，比对支付密码*****************/
    public void PayData(String psw) {
        //设置加载title
        loadDialog.setTitleStr("正在支付...");
        //开启加载框
        loadDialog.show();
        RequestParams params = new RequestParams();
        //支付密码
        params.put("paymentPassword", psw);
        HttpRequest.getPayPassWord(params, getToken(), new ResponseCallback() {
            //成功回调
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (("true").equals(result.getString("data"))) {
                        //加密提现金额
                        String secretBalance = encrypt(secretKey, withdrawal_money_ed.getText().toString().trim());
                        //发送提现金额
                        Withdrawal(secretBalance, walletsType);
                    } else {
                        //关闭加载框
                        loadDialog.dismiss();
                        //弹框
                        Is_dialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //失败回调
            @Override
            public void onFailure(OkHttpException failuer) {
                //关闭加载框
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /***************提交提现接口***********************/
    public void Withdrawal(String amount, String type) {
        RequestParams params = new RequestParams();
        params.put("cashOutAmount", amount);
        params.put("accountType", type);  //1 结算账户 2 激活奖励
        HttpRequest.getNewPayWithdrawal(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载框
                loadDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (("true").equals(result.getString("data"))) {
                        finish();
                        Intent intent = new Intent(WithdrawalActivity.this, WithdrawalSuccessActivity.class);
                        intent.putExtra("bank_name", withdrawal_bank_name.getText().toString().trim());
                        intent.putExtra("bank_num", bank_tail);
                        intent.putExtra("amount", withdrawal_money_ed.getText().toString().trim());
                        startActivity(intent);
                    } else {
                        showToast(3, result.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException failuer) {
                //关闭加载框
                loadDialog.dismiss();
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });
    }

    /**
     * 支付密码错误Dialog
     */
    public void Is_dialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pay_fail_dialog, null);
        Dialog dialog = new MyDialog1(WithdrawalActivity.this, true, true, (float) 0.8).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        TextView pay_fail_dialog_retry = view.findViewById(R.id.pay_fail_dialog_retry);
        TextView pay_fail_dialog_forget_pass = view.findViewById(R.id.pay_fail_dialog_forget_pass);
        pay_fail_dialog_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showDialog(format2Decimal(withdrawal_money_ed.getText().toString().trim()), wallets);
            }
        });
        pay_fail_dialog_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                //设置支付密码
                startActivity(new Intent(mContext, MePayPassActivity.class));
            }
        });

    }

    /**************************服务器请求提现界面数据*******************/
    //请求提现界面数据
    public void getPayData() {
        RequestParams params = new RequestParams();
        //提现钱包类型
        params.put("accountType", walletsType);
        //提现类型  传入1 即可  后续可以扩展
        params.put("cashoutType", "1");
        HttpRequest.getPayData(params, getToken(), new ResponseCallback() {
            //成功回调
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    //最小提现金额
                    minimumAmount = result.getString("minAmount");
                    //服务费
                    serviceFee = result.getString("serviceCharge");
                    //费率
                    rate = result.getString("rate");
                    //开户行
                    withdrawal_bank_name.setText(result.getJSONObject("data").getString("merchBank"));
                    //银行卡号
                    my_bank_number.setText("**** **** **** " + result.getJSONObject("data").getString("merchBankCardno"));
                    bank_tail = result.getJSONObject("data").getString("merchBankCardno");
                    //可提现金额
                    //解密Key
                    secretKey = result.getJSONObject("data").getString("secretkey");
                    //可提现金额
                    String profitAmount;
                    if (walletsType.equals("1")) {
                        //结算金额
                        profitAmount = result.getJSONObject("data").getString("profitAmount");
                    } else {
                        //奖励金额
                        profitAmount = result.getJSONObject("data").getString("rewardAmount");
                    }
                    walletAmount = decrypt(secretKey, profitAmount);
                    withdrawal_money_tv.setText(walletAmount);

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
    /******************************************金额输入框监听、EditText 重写*****************************/
    /**
     * 在文本改变之前
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    /**
     * 在文本改变时候
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // 输入框为空的时候按钮不可点击，颜色灰色
        if (charSequence.toString().equals("")) {
            withdrawal_bt.setEnabled(false);
            withdrawal_bt.setTextColor(Color.parseColor("#B2B1B1"));
            //输入框为空的时候隐藏提示正常显示
            withdrawal_line.setVisibility(View.VISIBLE);
            withdrawal_prompt_tv.setVisibility(View.GONE);
        } else {
            // 如果用户输入 . 默认变成0. 光标跟着到后边
            if (charSequence.toString().equals(".")) {
                withdrawal_money_ed.setText("0.");
                withdrawal_money_ed.setSelection(withdrawal_money_ed.length());
            }
            if (!withdrawal_money_ed.getText().toString().trim().equals("0.") && !withdrawal_money_ed.getText().toString().trim().equals("")) {
                // 判断输入的是否大于0
                BigDecimal ethLar = new BigDecimal(charSequence.toString().trim());
                BigDecimal trasNum1 = new BigDecimal("0");
                if (ethLar.compareTo(trasNum1) == 1) {
                    //判断用户输入的钱数是否大于自己的余额
                    BigDecimal trasNum = new BigDecimal(withdrawal_money_tv.getText().toString().trim());
                    if (ethLar.compareTo(trasNum) == 1) {
                        withdrawal_line.setVisibility(View.GONE);
                        withdrawal_prompt_tv.setText("输入金额超过您的余额");
                        withdrawal_prompt_tv.setVisibility(View.VISIBLE);
                        withdrawal_bt.setEnabled(false);
                        withdrawal_bt.setTextColor(Color.parseColor("#B2B1B1"));
                    } else {
                        //判断输入的金额是否大于等于服务器定的最低提现额
                        BigDecimal netNum = new BigDecimal(minimumAmount);
                        if (ethLar.compareTo(netNum) == -1) {
                            withdrawal_line.setVisibility(View.GONE);
                            withdrawal_prompt_tv.setText("可提现最小金额为：" + netNum + "元");
                            withdrawal_prompt_tv.setVisibility(View.VISIBLE);
                            withdrawal_bt.setEnabled(false);
                            withdrawal_bt.setTextColor(Color.parseColor("#B2B1B1"));
                        } else {
                            withdrawal_line.setVisibility(View.VISIBLE);
                            withdrawal_prompt_tv.setVisibility(View.GONE);
                            withdrawal_bt.setEnabled(true);
                            withdrawal_bt.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                }
            } else {
                withdrawal_bt.setEnabled(false);
                withdrawal_bt.setTextColor(Color.parseColor("#B2B1B1"));
            }

        }
    }

    /**
     * 在文本改变之后
     *
     * @param editable
     */
    @Override
    public void afterTextChanged(Editable editable) {

    }
}
