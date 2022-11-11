package com.example.miaofupos.homefragment.homeintegral;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Iterator;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/5/18
 * 描述: 积分极具列表
 */
//, IntegraMostListAdapter.FoodActionCallback
public class IntegralMostActivity extends BaseActivity implements View.OnClickListener{
    //返回键
    private LinearLayout iv_back;
    //通用积分
    private String integral = "";
    //活动积分
    private String activityIntegral = "";
    //总积分
    private String totalintegral = "";
    //当前Item项
    private List<Integer> showTitle;
    //左侧分类的数据
    private List<String> menuList = new ArrayList<>();
    //右侧商品数据
    private List<IntegralMostBean.PosTypeList> homeList = new ArrayList<>();
    private List<IntegralMostBean.PosTypeList> selectList = new ArrayList<>();

    private List<IntegralMostBean.PosTypeList> HList = new ArrayList<>();
    //商品adapter
    private IntegraMostListAdapter adapter1;
    private MenuAdapter menuAdapter;
    private ListView lv_menu;
    private ListView lv_home;
    private List<IntegralMostBean> memberList = new ArrayList<>();
//    private TextView good_numtv; //商品数量

//    private int clikindex = 0;
//    private RelativeLayout rl_goods_fits_car;
    private TextView integral_most_car;  //购物车

    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color, false).init();
        return R.layout.integral_most_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        lv_menu = findViewById(R.id.lv_menu);
        lv_home = findViewById(R.id.lv_home);
        integral_most_car = findViewById(R.id.integral_most_car);
//        good_numtv = findViewById(R.id.good_numtv);
//        rl_goods_fits_car = findViewById(R.id.rl_goods_fits_car);
        menuAdapter = new MenuAdapter(this, menuList);
        lv_menu.setAdapter(menuAdapter);
        adapter1 = new IntegraMostListAdapter(this, homeList);
        lv_home.setAdapter(adapter1);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                clikindex = position;
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
                Intent intent = new Intent(IntegralMostActivity.this, HomeIntegraExchangeActivity.class);
                // 通用积分
                intent.putExtra("integral", integral);
                //活动积分
                intent.putExtra("activityIntegral", activityIntegral);
                //总积分
                intent.putExtra("totalintegral", totalintegral);
                //机器ID
                intent.putExtra("id", homeList.get(position).getId());
                //价格
                intent.putExtra("returnIntegral", homeList.get(position).getReturnIntegral());
                //名称
                intent.putExtra("typeName", homeList.get(position).getTypeName());
                //图片
                intent.putExtra("detailImg", homeList.get(position).getDetailImg());
                //小图
                intent.putExtra("smail", homeList.get(position).getImgPath());
                startActivity(intent);
            }
        });

        posData();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        integral_most_car.setOnClickListener(this);
//        rl_goods_fits_car.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        integral = getIntent().getStringExtra("integral");
        activityIntegral = getIntent().getStringExtra("activityIntegral");
        totalintegral = getIntent().getStringExtra("totalintegral");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.integral_most_car:
                startActivity(new Intent(this,ShoppingCartActivity.class));
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
                        HList.addAll(memberList.get(i).getPosTypeList());
                    }
                    shouLog("--------->",HList.toString());
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

//
//    @Override
//    public void addAction(View view, int item) {
//        shouLog("0---",item+"---->");
//        for (int i = 0; i < memberList.size(); i++) {
//            if (clikindex!=0 && i!=0){
//                item = memberList.get(i-1).getPosTypeList().size() + item;
//            }
//
//        }
//        shouLog("item---->",item+"");
//        ShoppingCartAnimationView shoppingCartAnimationView = new ShoppingCartAnimationView(this);
//        int position[] = new int[2];
//        view.getLocationInWindow(position);
//        shoppingCartAnimationView.setStartPosition(new Point(position[0], position[1]));
//        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
//        rootView.addView(shoppingCartAnimationView);
//        int endPosition[] = new int[2];
//        good_numtv.getLocationInWindow(endPosition);
//        shoppingCartAnimationView.setEndPosition(new Point(endPosition[0], endPosition[1]));
//        shoppingCartAnimationView.startBeizerAnimation();
//        IntegralMostBean.PosTypeList model = HList.get(item);
//        model.setNum(model.getNum() + 1);
//        adapter1.notifyDataSetChanged();
//        calculatePrice();
//    }
//
//    @Override
//    public void reduceGood(int position) {
//        for (int i = 0; i < memberList.size(); i++) {
//            if (clikindex!=0 && i!=0){
//                position = memberList.get(i-1).getPosTypeList().size() + position;
//            }
//
//        }
//        IntegralMostBean.PosTypeList model = HList.get(position);
//        model.setNum(model.getNum() - 1);
//        adapter1.notifyDataSetChanged();
//        calculatePrice();
//    }
//
//    //购物车份数+总价格计算
//    private void calculatePrice() {
//        selectList.clear();
//        int num = 0;
//        Iterator<IntegralMostBean.PosTypeList> iterator = HList.iterator();
//        while (iterator.hasNext()) {
//            IntegralMostBean.PosTypeList model = iterator.next();
//            if (model.getNum() != 0) {
//                selectList.add(model);
//                num += model.getNum();
//            }
//        }
//        good_numtv.setText(num + "");
//    }
}
