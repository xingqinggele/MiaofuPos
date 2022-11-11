package com.example.miaofupos.homefragment.homemerchants.homemerchantschange;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homemerchants.homemerchantschange.bean.MyEventBean;
import com.example.miaofupos.homefragment.homemerchants.homemerchantschange.fragment.MerchantsFragment;
import com.example.miaofupos.homefragment.homemerchants.homemerchantschange.fragment.MerchantsProgressFragment;
import com.example.miaofupos.homefragment.homemessage.fragment.SystemMessageFragment;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2020/12/19
 * 描述: 商户变更
 */
public class HomeMerchatsChangeActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private ViewPager my_viewpager;
    private RadioGroup radio;
    private List<Fragment> fragments;
    private LinearLayout iv_back;

    /**
     * TextView选择框
     */
    private TextView home_merchants_manager_tv;

    /**
     * popup窗口里的ListView
     */
    private ListView mTypeLv;

    /**
     * popup窗口
     */
    private PopupWindow typeSelectPopup;

    /**
     * 模拟的假数据
     */
    private List<String> testData;

    /**
     * 数据适配器
     */
    private ArrayAdapter<String> testDataAdapter;

    private int select = 0;
    private MerchantsProgressFragment mfragment;

    @Override
    protected int getLayoutId() {
        return R.layout.home_merchats_change_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        radio = findViewById(R.id.home_merchants_change_radio);
        my_viewpager = findViewById(R.id.home_merchants_change_viewpager);
        home_merchants_manager_tv = findViewById(R.id.home_merchants_manager_tv);
        radio.setOnCheckedChangeListener(this);
        mfragment = new MerchantsProgressFragment();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        home_merchants_manager_tv.setOnClickListener(this);
        my_viewpager.addOnPageChangeListener(this);
        init();
    }

    @Override
    protected void initData() {

    }

    private void init() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new MerchantsFragment());
        fragments.add(new MerchantsProgressFragment());
        my_viewpager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });
        radio.check(R.id.r1);
    }



    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.r1:
                my_viewpager.setCurrentItem(0,true);
                home_merchants_manager_tv.setVisibility(View.INVISIBLE);
                break;
            case R.id.r2:
                my_viewpager.setCurrentItem(1,true);
                home_merchants_manager_tv.setVisibility(View.VISIBLE);
                break;

        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.home_merchants_manager_tv:
                // 点击控件后显示popup窗口
                initSelectPopup();
                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    typeSelectPopup.showAsDropDown(home_merchants_manager_tv, 0, 0);
                }
                break;
        }
    }


    /**
     * 初始化popup窗口
     */
    private void initSelectPopup() {
        mTypeLv = new ListView(this);
        mTypeLv.setDivider(getResources().getDrawable(R.color.white));
        mTypeLv.setDividerHeight(0);
        TestData();
        // 设置适配器
        testDataAdapter = new ArrayAdapter<String>(this, R.layout.popup_text_item, testData);
        mTypeLv.setAdapter(testDataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select = position;
                // 在这里获取item数据
                String value = testData.get(position);
                // 把选择的数据展示对应的TextView上
                home_merchants_manager_tv.setText(value);
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
                //商户变更进度查询,条件查询
                EventBus.getDefault().post(new MyEventBean(select));
            }
        });


        typeSelectPopup = new PopupWindow(mTypeLv, home_merchants_manager_tv.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.popwindow);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
    }
    /**
     * 模拟假数据
     */
    private void TestData() {
        testData = new ArrayList<>();
        testData.add("全部");
        testData.add("审核通过");
        testData.add("审核中");
        testData.add("审核失败");
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
//        radio.check(i);
    }

    @Override
    public void onPageSelected(int i) {
        shouLog("页",i+"");
        switch (i){
            case 0:
                radio.check(R.id.r1);
                break;
            case 1:
                radio.check(R.id.r2);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
