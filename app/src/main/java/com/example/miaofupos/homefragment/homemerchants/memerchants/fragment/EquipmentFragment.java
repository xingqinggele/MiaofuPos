package com.example.miaofupos.homefragment.homemerchants.memerchants.fragment;

import android.view.View;
import android.widget.TextView;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseFragment;
import com.example.miaofupos.homefragment.homemerchants.memerchants.bean.EquipmentEvnBusBean;

import de.greenrobot.event.EventBus;

/**
 * 作者: qgl
 * 创建日期：2021/3/10
 * 描述:设备Fragment
 */
public class EquipmentFragment extends BaseFragment {
    //设备编号
    private TextView equipment_number;
    //设备激活时间
    private TextView equipment_time;
    @Override
    protected int getLayoutInflaterResId() {
        return R.layout.equipment_fragment;
    }

    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
        equipment_number = rootView.findViewById(R.id.equipment_number);
        equipment_time = rootView.findViewById(R.id.equipment_time);
    }



    public void onEventMainThread(EquipmentEvnBusBean busBean){
        shouLog("------","aaa");
        //显示设备编号
        equipment_number.setText(busBean.getSnCode());
        //显示设备激活时间
        equipment_time.setText(busBean.getTimer());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
