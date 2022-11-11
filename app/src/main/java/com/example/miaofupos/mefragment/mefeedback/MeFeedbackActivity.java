package com.example.miaofupos.mefragment.mefeedback;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.mefragment.mefeedback.adapter.ChekBoxAdapter;
import com.example.miaofupos.mefragment.mefeedback.bean.ChekBean;
import com.example.miaofupos.views.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2020/12/19
 * 描述:意见反馈
 */
public class MeFeedbackActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout iv_back;
    private MyListView checkbox_list;
    private ChekBoxAdapter adapter;
    List<ChekBean> datas = new ArrayList();//数据集合
    @Override
    protected int getLayoutId() {
        return R.layout.me_feedback_activity;
    }

    @Override
    protected void initView() {
        iv_back = findViewById(R.id.iv_back);
        checkbox_list = findViewById(R.id.checkbox_list);
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
        checkbox_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int currentNum = -1;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (ChekBean person : datas) {
                    person.setChecked(false);
                }
                if (currentNum == -1) { //选中
                    datas.get(i).setChecked(true);
                    currentNum = i;
                } else if (currentNum == i) { //同一个item选中变未选中
                    for (ChekBean person : datas) {
                        person.setChecked(false);
                    }
                    currentNum = -1;
                } else if (currentNum != i) { //不是同一个item选中当前的，去除上一个选中的
                    for (ChekBean person : datas) {
                        person.setChecked(false);
                    }
                    datas.get(i).setChecked(true);
                    currentNum = i;
                }
                Toast.makeText(adapterView.getContext(), datas.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        //创建数据
        datas.add(new ChekBean("功能问题"));
        datas.add(new ChekBean("体检问题"));
        datas.add(new ChekBean("新功能建议"));
        datas.add(new ChekBean("其他"));
        adapter = new ChekBoxAdapter(this);
        adapter.setDatas(datas);
        checkbox_list.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
