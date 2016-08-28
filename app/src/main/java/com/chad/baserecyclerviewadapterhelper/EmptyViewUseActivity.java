package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;

public class EmptyViewUseActivity extends Activity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;
    private boolean isNotData = true;
    private View errorView;
    private View notDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_view_use);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        Button btnSwuich = (Button) findViewById(R.id.btn_switch);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        btnSwuich.setOnClickListener(this);
        notDataView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerView.getParent(), false);
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(0);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        mQuickAdapter.setEmptyView(emptyView);
        View view = getLayoutInflater().inflate(R.layout.head_view, (ViewGroup) mRecyclerView.getParent(), false);
        mRecyclerView.setAdapter(mQuickAdapter);
    }

    @Override
    public void onClick(View v) {
        View view;
        if (!isNotData) {
            view = notDataView;
            isNotData = true;
        } else {
            view = errorView;
            isNotData = false;
        }
        mQuickAdapter.setEmptyView(view);
        mQuickAdapter.notifyItemChanged(0);
    }
}
