package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.SectionAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.entity.MySection;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionUseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private List<MySection> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);
        setBackBtn();
        setTitle("Section Use");
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mData = DataServer.getSampleData();
        SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);
//        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
//
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//
//
//        });
        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MySection mySection = mData.get(position);
                if (mySection.isHeader)
                    Toast.makeText(SectionUseActivity.this, mySection.header, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SectionUseActivity.this, mySection.t.getName(), Toast.LENGTH_LONG).show();
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SectionUseActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show();
                return false;
            }
        });
        mRecyclerView.setAdapter(sectionAdapter);
    }


}
