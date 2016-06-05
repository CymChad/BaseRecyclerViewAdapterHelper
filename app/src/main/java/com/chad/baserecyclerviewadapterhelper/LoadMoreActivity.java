package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class LoadMoreActivity extends Activity implements BaseQuickAdapter.RequestLoadMoreListener {

    public static final int PAGE_SIZE = 10;

    private RecyclerView mRecyclerView;

    private QuickAdapter mQuickAdapter;

    private Handler mHandler = new Handler();

    private int mCurrentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initAdapter();
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(LoadMoreActivity.this, PAGE_SIZE);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(LoadMoreActivity.this, "" + Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });

        mQuickAdapter.setOnLoadMoreListener(this);
        mQuickAdapter.openLoadMore(PAGE_SIZE, true);
    }

    @Override
    public void onLoadMoreRequested() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentPage++;
                mQuickAdapter.notifyDataChangedAfterLoadMore(DataServer.getSampleData(PAGE_SIZE), mCurrentPage < 3);
            }
        }, 3000);
    }
}
