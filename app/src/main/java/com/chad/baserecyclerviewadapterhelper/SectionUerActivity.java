package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.chad.baserecyclerviewadapterhelper.adapter.SectionAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.decoration.GridItemDecoration;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionUerActivity extends Activity {
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        SectionAdapter sectionAdapter = new SectionAdapter(this, R.layout.head_view, R.layout.def_section_head, DataServer.getSampleData());
        mRecyclerView.addItemDecoration(new GridItemDecoration(this,R.drawable.list_divider));
        mRecyclerView.setAdapter(sectionAdapter);
    }
}
