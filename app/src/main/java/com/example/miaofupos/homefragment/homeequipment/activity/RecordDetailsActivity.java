package com.example.miaofupos.homefragment.homeequipment.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.miaofupos.R;
import com.example.miaofupos.base.BaseActivity;
import com.example.miaofupos.homefragment.homeequipment.adapter.RecordDetailsAdapter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者: qgl
 * 创建日期：2021/1/22
 * 描述: 调拨记录详情
 */
public class RecordDetailsActivity extends BaseActivity implements View.OnClickListener {
    private List<String> list = new ArrayList<>();
    private RecyclerView record_details_list_view;
    private LinearLayout iv_back;
    private RecordDetailsAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        //设置状态栏颜色
        statusBarConfig(R.color.new_theme_color,false).init();
        return R.layout.record_details_activity;
    }

    @Override
    protected void initView() {
        record_details_list_view = findViewById(R.id.record_details_list_view);
        iv_back = findViewById(R.id.iv_back);
        Intent intent = this.getIntent();
        list = (List<String>) intent.getSerializableExtra("SnCode");
        initList();
    }

    @Override
    protected void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //snCode = getIntent().getStringExtra("SnCode");
        //shouLog("111",snCode);
//        List idList = Arrays.asList(snCode.split(","));//根据逗号分隔转化为list
//        for (int i = 0;i < idList.size();i++){
//            list.add(idList.get(i).toString());
//        }
    }


    private void initList() {
        mAdapter = new RecordDetailsAdapter(R.layout.record_details_item_layout, list);
        mAdapter.openLoadAnimation();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.list_empty, null));
        record_details_list_view.setLayoutManager(new LinearLayoutManager(this));
        record_details_list_view.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", list.get(position));
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                showToast(2,"复制成功");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
