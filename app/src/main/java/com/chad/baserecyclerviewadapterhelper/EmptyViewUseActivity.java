package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;

public class EmptyViewUseActivity extends Activity {
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_view_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(this, 0);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        mQuickAdapter.setEmptyView(emptyView);
        View view = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        mRecyclerView.setAdapter(mQuickAdapter);
    }
}
