package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.SectionAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.baserecyclerviewadapterhelper.decoration.GridItemDecoration;
import com.chad.baserecyclerviewadapterhelper.entity.MySection;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionUseActivity extends Activity implements BaseQuickAdapter.OnRecyclerViewItemClickListener {
    private RecyclerView mRecyclerView;
    private List<MySection> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_uer);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mData = DataServer.getSampleData();
        SectionAdapter sectionAdapter = new SectionAdapter(this, R.layout.item_section_content, R.layout.def_section_head, mData);
        sectionAdapter.setOnRecyclerViewItemClickListener(this);
        mRecyclerView.addItemDecoration(new GridItemDecoration(this,R.drawable.list_divider));
        mRecyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        MySection mySection = mData.get(position);
        if(mySection.isHeader)
            Toast.makeText(this,mySection.header ,Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,mySection.t.getName(),Toast.LENGTH_LONG).show();
    }
}
