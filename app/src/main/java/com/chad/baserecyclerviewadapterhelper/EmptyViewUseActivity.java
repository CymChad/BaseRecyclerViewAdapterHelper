package com.chad.baserecyclerviewadapterhelper;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.baserecyclerviewadapterhelper.base.BaseActivity;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;

public class EmptyViewUseActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;
    private View notDataView;
    private View errorView;
    private boolean mError = true;
    private boolean mNoData = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackBtn();
        setTitle("EmptyView Use");
        setContentView(R.layout.activity_empty_view_use);
        findViewById(R.id.btn_reset).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        notDataView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        errorView = getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        initAdapter();
        onRefresh();
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(0);
        mRecyclerView.setAdapter(mQuickAdapter);
    }

    @Override
    public void onClick(View v) {
        mError = true;
        mNoData = true;
        mQuickAdapter.setNewData(null);
        onRefresh();
    }



    private void onRefresh() {
        mQuickAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRecyclerView.getParent());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mError) {
                    mQuickAdapter.setEmptyView(errorView);
                    mError = false;
                } else {
                    if (mNoData) {
                        mQuickAdapter.setEmptyView(notDataView);
                        mNoData = false;
                    } else {
                        mQuickAdapter.setNewData(DataServer.getSampleData(10));
                    }
                }
            }
        }, 1000);
    }
}
