package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.SectionMultipleItemAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.SectionMultipleItem;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionMultipleItemUseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<SectionMultipleItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);
        setBackBtn();
        setTitle("SectionMultiple Use");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mData = DataServer.getSectionMultiData();
        SectionMultipleItemAdapter sectionAdapter = new SectionMultipleItemAdapter(R.layout.def_section_head, mData);
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SectionMultipleItemUseActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(sectionAdapter);
    }
}
