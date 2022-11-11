package com.example.miaofupos.homefragment.shopping;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeintegral.adpter.IntegraMostListAdapter;
import com.example.miaofupos.homefragment.homeintegral.adpter.MenuAdapter;
import com.example.miaofupos.homefragment.homeintegral.bean.IntegralMostBean;
import com.example.miaofupos.net.HttpRequest;
import com.example.miaofupos.net.OkHttpException;
import com.example.miaofupos.net.RequestParams;
import com.example.miaofupos.net.ResponseCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/5/17
 * 描述:购物列表
 */
public class ShoppingListActivity extends BaseActivity implements View.OnClickListener {
    //返回键
    private LinearLayout iv_back;
    //当前Item项
    private List<Integer> showTitle;
    //左侧分类的数据
    private List<String> menuList = new ArrayList<>();
    //右侧商品数据
    private List<IntegralMostBean.PosTypeList> homeList = new ArrayList<>();
    //商品adapter
    private IntegraMostListAdapter adapter1;
    private MenuAdapter menuAdapter;
    private ListView lv_menu;
    private ListView lv_home;
    private List<IntegralMostBean> memberList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.shoppinglist_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        lv_menu = (ListView) findViewById(R.id.lv_menu);
        lv_home = (ListView) findViewById(R.id.lv_home);
        menuAdapter = new MenuAdapter(this, menuList);
        lv_menu.setAdapter(menuAdapter);
//        adapter1 = new IntegraMostListAdapter(this, homeList,this);
        lv_home.setAdapter(adapter1);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                homeList.clear();
                homeList.addAll(memberList.get(position).getPosTypeList());
                menuAdapter.setSelectItem(position);
                menuAdapter.notifyDataSetInvalidated();
                adapter1.notifyDataSetInvalidated();
            }
        });

        lv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Intent intent = new Intent(ShoppingListActivity.this, HomeIntegraExchangeActivity.class);
//                startActivity(intent);
            }
        });

        posData();
    }


    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void posData() {
        RequestParams params = new RequestParams();
        HttpRequest.getMostList(params, getToken(), new ResponseCallback() {
            @Override
            public void onSuccess(Object responseObj) {
                //需要转化为实体对象
                Gson gson = new GsonBuilder().serializeNulls().create();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    memberList = gson.fromJson(result.getJSONArray("data").toString(),
                            new TypeToken<List<IntegralMostBean>>() {
                            }.getType());
                    showTitle = new ArrayList<>();
                    for (int i = 0; i < memberList.size(); i++) {
                        menuList.add(memberList.get(i).getBrandName());
                        showTitle.add(i);
                    }
                    homeList.addAll(memberList.get(0).getPosTypeList());
                    menuAdapter.notifyDataSetChanged();
                    adapter1.notifyDataSetChanged();
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
}
