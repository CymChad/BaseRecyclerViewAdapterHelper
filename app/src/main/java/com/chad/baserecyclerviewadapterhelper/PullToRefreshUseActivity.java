package com.chad.baserecyclerviewadapterhelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.baserecyclerviewadapterhelper.adapter.QuickAdapter;
import com.chad.baserecyclerviewadapterhelper.data.DataServer;
import com.chad.library.adapter.base.BaseQuickAdapter;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PullToRefreshUseActivity extends Activity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private QuickAdapter mQuickAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int TOTAL_COUNTER = 18;

    private static final int PAGE_SIZE = 6;

    private int delayMillis = 1000;

    private int mCurrentCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        addHeadView();
        mRecyclerView.setAdapter(mQuickAdapter);
    }

    private void addHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.head_view, null);
        headView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PullToRefreshUseActivity.this, "click HeadView", Toast.LENGTH_LONG).show();
            }
        });
        mQuickAdapter.addHeaderView(headView);
    }

    @Override
    public void onLoadMoreRequested() {
        if (mCurrentCounter >= TOTAL_COUNTER) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mQuickAdapter.notifyDataChangedAfterLoadMore(false);
                }
            });

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DataServer.addData(mQuickAdapter.getData(), PAGE_SIZE);
                    mCurrentCounter = mQuickAdapter.getItemCount();
                    mQuickAdapter.notifyDataChangedAfterLoadMore(true);

                }
            }, delayMillis);
        }
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initAdapter();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, delayMillis);
    }

    private void initAdapter() {
        mQuickAdapter = new QuickAdapter(PullToRefreshUseActivity.this, PAGE_SIZE);
        mQuickAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mQuickAdapter);
        mCurrentCounter = mQuickAdapter.getItemCount();
        mQuickAdapter.setOnLoadMoreListener(this);
        mQuickAdapter.openLoadMore(PAGE_SIZE,true);//or call mQuickAdapter.setPageSize(PAGE_SIZE);  mQuickAdapter.openLoadMore(true);
        addHeadView();
        mQuickAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(PullToRefreshUseActivity.this, Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });
    }
}
