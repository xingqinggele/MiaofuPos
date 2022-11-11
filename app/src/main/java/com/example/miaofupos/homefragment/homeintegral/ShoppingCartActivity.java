package com.example.miaofupos.homefragment.homeintegral;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.datafragment.datapersonalresults.bean.DataPersonBean;
import com.example.miaofupos.homefragment.homeintegral.adpter.ShoppingCartAdapter;
import com.example.miaofupos.homefragment.homeintegral.bean.ShoppingCartBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.example.miaofupos.views.MyDialog1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.miaofupos.utils.DESHelperUtil.decrypt;

/**
 * 作者: qgl
 * 创建日期：2021/7/21
 * 描述:购物车
 */
public class ShoppingCartActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, ShoppingCartAdapter.FoodActionCallback, View.OnClickListener {
    private List<ShoppingCartBean> list = new ArrayList<>();
    private List<ShoppingCartBean> selectList = new ArrayList<>();
    //列表adapter
    private ShoppingCartAdapter adapter;
    //刷新控件
    private SwipeRefreshLayout shopping_cart_swipe;
    //listView
    private RecyclerView shopping_cart_list;
    //价格
    private TextView price_tv;
    //返回键
    private LinearLayout iv_back;
    //提交兑换按钮
    private Button submit_btn;
    //选择商品数量
    private int num = 0;
    //可用积分
    private String integral;
    //解密密钥
    private String key;

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.shoppingcart_activity;
    }

    @Override
    protected void initView() {
        shopping_cart_swipe = findViewById(R.id.shopping_cart_swipe);
        shopping_cart_list = findViewById(R.id.shopping_cart_list);
        iv_back = findViewById(R.id.iv_back);
        submit_btn = findViewById(R.id.submit_btn);
        price_tv = findViewById(R.id.price_tv);
        //初始化下拉刷新、上拉加载
        initList();
    }

    private void initList() {
        //下拉样式
        shopping_cart_swipe.setColorSchemeResources(R.color.new_theme_color, R.color.green, R.color.colorAccent);
        shopping_cart_swipe.setOnRefreshListener(this);
        adapter = new ShoppingCartAdapter(R.layout.shoppingcart_item, list, this);
        adapter.openLoadAnimation();
        adapter.setEnableLoadMore(false);
        adapter.setOnLoadMoreListener(this, shopping_cart_list);
        adapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        shopping_cart_list.setLayoutManager(new LinearLayoutManager(this));
        shopping_cart_list.setAdapter(adapter);
        postData();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {


    }

    /**
     * 请求购物车数据
     */
    private void postData() {
        //进行网络请求数据
        RequestParams params = new RequestParams();
        HttpRequest.getCarList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //关闭加载框
                shopping_cart_swipe.setRefreshing(false);
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    //String 转 JSONObject
                    JSONObject result = new JSONObject(responseObj.toString());
                    integral = result.getString("integral");
                    key = result.getString("key");
                    //List<实体类>list 填充数据
                    List<ShoppingCartBean> memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<ShoppingCartBean>>() {
                            }.getType());
                    //在adapter List 中添加 list
                    list.addAll(memberList);
                    //数据长度小于定义的长度,adapter 加载更多的结束
                    adapter.loadMoreEnd();
                    //更新adapter
                    adapter.notifyDataSetChanged();
                    calculatePrice();
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
    public void onRefresh() {
        list.clear();
        postData();
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void addAction(int position) {
        ShoppingCartBean model = list.get(position);
        model.setPosNum(model.getPosNum() + 1);
        adapter.notifyDataSetChanged();
        calculatePrice();
    }

    @Override
    public void reduceGood(int position) {
        ShoppingCartBean model = list.get(position);
        model.setPosNum(model.getPosNum() - 1);
        adapter.notifyDataSetChanged();
        calculatePrice();
    }

    @Override
    public void removeData(int position) {
        showDialog1(position);
    }

    //删除弹框
    private void showDialog1(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_out_login, null);
        RadioButton dialog_radio_cancel = view.findViewById(R.id.dialog_radio_cancel);
        TextView dialog_title = view.findViewById(R.id.dialog_title);
        RadioButton dialog_radio_confirm = view.findViewById(R.id.dialog_radio_confirm);
        dialog_title.setText("您是否删除此商品？");
        Dialog dialog = new MyDialog1(ShoppingCartActivity.this, true, true, (float) 0.9).setNewView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog_radio_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog_radio_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteData(list.get(position).getCommId(), position);
            }
        });
    }

    //删除接口
    private void deleteData(String id, int position) {
        //进行网络请求数据
        RequestParams params = new RequestParams();
        params.put("commId", id);
        HttpRequest.getDeleteList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                list.remove(position);
                //删除动画
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(OkHttpException failuer) {
                Failuer(failuer.getEcode(), failuer.getEmsg());
            }
        });


    }

    //购物车份数+总价格计算
    private void calculatePrice() {
        selectList.clear();
        int price = 0;
        num = 0;
        Iterator<ShoppingCartBean> iterator = list.iterator();
        while (iterator.hasNext()) {
            ShoppingCartBean model = iterator.next();
            if (model.getPosNum() != 0) {
                selectList.add(model);
                num += model.getPosNum();
                price += model.getPosNum() * Integer.parseInt(model.getReturnMoney());
            }
        }
        price_tv.setText(price + "");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.submit_btn:
                if (num >= 5) {
                    Intent intent = new Intent(this, HomeIntegralOrderActivity.class);
                    intent.putExtra("list", (Serializable)selectList);
                    intent.putExtra("integral", decrypt(key, integral));
                    startActivity(intent);
                } else {
                    showToast(2, "您必须兑换5台以上设备！！！");
                }


                break;
        }
    }
}
