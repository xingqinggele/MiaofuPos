package com.example.miaofupos.newprojectview.model;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.newprojectview.AddMerchantsActivity3;
import com.example.miaofupos.newprojectview.adapter.NewCityAdapter;
import com.example.miaofupos.newprojectview.adapter.NewProvinceAdapter;
import com.example.miaofupos.newprojectview.bean.NewCityBean;
import com.example.miaofupos.newprojectview.bean.NewProvinceBean;
import com.example.miaofupos.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2022/6/14
 * 描述: 三级联动
 */
public class ProvinceView {
    private NewProvinceAdapter mProvinceAdapter;
    private NewCityAdapter mCityAdapter;
    private ListView mCityListView;
    private TextView mProTv;
    private TextView mCityTv;
    private ImageView mCloseImg;
    private PopupWindow popWindow;
    private View mSelectedLine;
    private View popView;
    private int tabIndex = 0;
    private Context context;
    private List<NewProvinceBean> titleBeans = new ArrayList<>();
    private List<NewCityBean> twoLabelBeans = null;
    private String colorSelected = "#ff181c20";
    private String colorAlert = "#ffff4444";
    private AddMerchantsActivity3 activity = null;


    public ProvinceView(Context context, AddMerchantsActivity3 activity) {
        this.context = context;
        this.activity = activity;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                case 0:
                    titleBeans = (List) msg.obj;
                    mProvinceAdapter.notifyDataSetChanged();
                    mCityListView.setAdapter(mProvinceAdapter);
                    break;
                case 1:
                    twoLabelBeans = (List) msg.obj;
                    mCityAdapter.notifyDataSetChanged();
                    if (twoLabelBeans != null && !twoLabelBeans.isEmpty()) {
                        mCityListView.setAdapter(mCityAdapter);
                        tabIndex = 1;
                    }
                    break;
            }
            updateTabsStyle(tabIndex);
            updateIndicator();
            return true;
        }
    });

    public void showCityPicker() {
        initJDCityPickerPop();
        if (!isShow()) {
            popWindow.showAtLocation(popView, 80, 0, 0);
        }
    }

    private boolean isShow() {
        return popWindow.isShowing();
    }

    public void hidePop() {
        if (isShow()) {
            popWindow.dismiss();
        }
    }

    private void initJDCityPickerPop() {
        tabIndex = 0;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popView = layoutInflater.inflate(R.layout.pop_jdcitypicker, (ViewGroup) null);
        mCityListView = (ListView) popView.findViewById(R.id.city_listview);
        mProTv = (TextView) popView.findViewById(R.id.province_tv);
        mCityTv = (TextView) popView.findViewById(R.id.city_tv);
        mCloseImg = (ImageView) popView.findViewById(R.id.close_img);
        mSelectedLine = popView.findViewById(R.id.selected_line);
        popWindow = new PopupWindow(popView, -1, -2);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mCloseImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hidePop();
                Utils.setBackgroundAlpha(context, 1.0F);
            }
        });
        mProTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 0;
                if (mProvinceAdapter != null) {
                    mCityListView.setAdapter(mProvinceAdapter);
                    if (mProvinceAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mProvinceAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tabIndex = 1;
                if (mCityAdapter != null) {
                    mCityListView.setAdapter(mCityAdapter);
                    if (mCityAdapter.getSelectedPosition() != -1) {
                        mCityListView.setSelection(mCityAdapter.getSelectedPosition());
                    }
                }

                updateTabVisible();
                updateIndicator();
            }
        });
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedList(position);
            }
        });
        Utils.setBackgroundAlpha(context, 0.5F);
        updateIndicator();
        updateTabsStyle(-1);
        //请求一级类别
        activity.postProvince("1", "", 0);
    }

    @SuppressLint("WrongConstant")
    private void updateTabVisible() {
        mProTv.setVisibility(titleBeans != null && !titleBeans.isEmpty() ? 0 : 8);
        mCityTv.setVisibility(twoLabelBeans != null && !twoLabelBeans.isEmpty() ? 0 : 8);
    }

    private void updateIndicator() {
        popView.post(new Runnable() {
            public void run() {
                switch (tabIndex) {
                    case 0:
                        tabSelectedIndicatorAnimation(mProTv).start();
                        break;
                    case 1:
                        tabSelectedIndicatorAnimation(mCityTv).start();
                        break;
                }

            }
        });
    }

    private AnimatorSet tabSelectedIndicatorAnimation(TextView tab) {
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(mSelectedLine, "X", new float[]{mSelectedLine.getX(), tab.getX()});
        final ViewGroup.LayoutParams params = mSelectedLine.getLayoutParams();
        ValueAnimator widthAnimator = ValueAnimator.ofInt(new int[]{params.width, tab.getMeasuredWidth()});
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                params.width = (Integer) animation.getAnimatedValue();
                mSelectedLine.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.playTogether(new Animator[]{xAnimator, widthAnimator});
        return set;
    }

    private void selectedList(int position) {
        switch (tabIndex) {
            case 0:
                NewProvinceBean provinceBean = mProvinceAdapter.getItem(position);
                if (provinceBean != null) {
                    mProTv.setText("" + provinceBean.getAreaName());
                    mCityTv.setText("请选择");
                    mProvinceAdapter.updateSelectedPosition(position);
                    mProvinceAdapter.notifyDataSetChanged();
                    //请求二级接口
                    activity.postProvince("2", provinceBean.getAreaCode(), 1);
                }
                break;
            case 1:
                NewCityBean cityBean = mCityAdapter.getItem(position);
                if (cityBean != null) {
                    callback(cityBean);
                }

                break;
        }

    }

    private void callback(NewCityBean cityBean) {
        NewProvinceBean titleBean = titleBeans != null && !titleBeans.isEmpty() && mProvinceAdapter != null && mProvinceAdapter.getSelectedPosition() != -1 ? (NewProvinceBean) titleBeans.get(mProvinceAdapter.getSelectedPosition()) : null;
        activity.setSelectAdrCallback(titleBean + "-" + cityBean, titleBean.getAreaCode(), cityBean.getAreaCode());
        hidePop();
    }

    @SuppressLint("WrongConstant")
    private void updateTabsStyle(int tabIndex) {
        switch (tabIndex) {
            case -1:
            case 0:
                mProTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(8);
                break;
            case 1:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorAlert));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
                break;
            case 2:
                mProTv.setTextColor(Color.parseColor(colorSelected));
                mCityTv.setTextColor(Color.parseColor(colorSelected));
                mProTv.setVisibility(0);
                mCityTv.setVisibility(0);
        }

    }

    public void setTitleData(List<NewProvinceBean> titleList) {
        titleBeans = titleList;
        if (titleList != null && !titleList.isEmpty()) {
            mProvinceAdapter = new NewProvinceAdapter(context, titleList);
            mCityListView.setAdapter(mProvinceAdapter);
        } else {
            Log.e("MainActivity.tshi", "解析本地城市数据失败！");
        }
    }

    public void setTwoLabelData(List<NewCityBean> twoLabelList) {
        mCityAdapter = new NewCityAdapter(context, twoLabelList);
        mHandler.sendMessage(Message.obtain(mHandler, 1, twoLabelList));
    }

}